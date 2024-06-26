package com.lolmatch.teams.team;

import com.lolmatch.teams.team.dto.AddTeamRequest;
import com.lolmatch.teams.team.dto.TeamDTO;
import com.lolmatch.teams.team.dto.TeamListDTO;
import com.lolmatch.teams.user.User;
import com.lolmatch.teams.user.UserRepository;
import com.lolmatch.teams.util.AmqpTeamChangesTransmitter;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamService {
	// TODO - jakoś liczyć win rate drużyny
	private final TeamRepository teamRepository;
	
	private final UserRepository userRepository;
	
	private final PasswordEncoder passwordEncoder;
	
	private final AmqpTeamChangesTransmitter transmitter;
	
	@Transactional(readOnly = true)
	TeamListDTO getTeamsFilteredAndPaginated(Pageable pageable, Optional<String> country, Optional<Rank> rank) {
		Rank minimalRank = rank.orElse(Rank.DIAMOND_I);
		Page<Team> teams;
		if (country.isEmpty()) {
			teams = teamRepository.findTeamsByMinimalRankLessThanEqual(pageable, minimalRank);
		} else {
			teams = teamRepository.findTeamsByMinimalRankLessThanEqualAndTeamCountryEquals(pageable, minimalRank, country.get());
		}
		return new TeamListDTO(mapTeamListToTeamDTOList(teams.getContent()), teams.getSize(), teams.getNumber(), teams.getTotalElements());
	}
	
	@Transactional
	TeamDTO saveTeam(AddTeamRequest dto, Principal principal) {
		if (!principal.getName().equals(dto.leaderId().toString())) {
			throw new AccessDeniedException("Cannot create a team, not being a team's leader");
		}
		String password = dto.password() != null ? passwordEncoder.encode(dto.password()) : "";
		User leader = getUserById(dto.leaderId());
		if (leader.getTeam() != null) {
			throw new EntityNotFoundException("There is already a team associated with this leader");
		}
		Team team = Team.builder()
				.name(dto.name())
				.leaderId(dto.leaderId())
				.teamCountry(dto.teamCountry())
				.description(dto.description())
				.isPublic(dto.isPublic())
				.minimalRank(dto.minimalRank())
				.teamCountry(dto.teamCountry())
				.members(Set.of(leader))
				.password(password)
				.build();
		leader.setTeam(team);
		Team savedTeam = teamRepository.save(team);
		userRepository.save(leader);
		transmitter.transmitTeamChange(team, AmqpTeamChangesTransmitter.TeamChangeEnum.CREATE);
		return savedTeam.toDto();
	}
	
	TeamDTO findTeamByCriteria(String criteria) {
		try{
			UUID id = UUID.fromString(criteria);
			return teamRepository.findTeamById(id).orElseThrow(() -> new EntityNotFoundException("No team with id: " + criteria + ", has been found.")).toDto();
		} catch (IllegalArgumentException e){
			return teamRepository.findTeamByName(criteria).orElseThrow(() -> new EntityNotFoundException("No team with name: " + criteria + ", has been found.")).toDto();
		}
	}
	
	@Transactional
	TeamDTO updateTeam(UUID id, AddTeamRequest dto, Principal principal) {
		Team team = getTeamById(id);
		if (!Objects.equals(team.getLeaderId().toString(), principal.getName())) {
			throw new AccessDeniedException("Team can only be edited by team's leader");
		}
		String password = dto.password() != null ? passwordEncoder.encode(dto.password()) : "";
		team.setName(dto.name());
		team.setDescription(dto.description());
		team.setTeamCountry(dto.teamCountry());
		team.setPublic(dto.isPublic());
		team.setPassword(password);
		team.setMinimalRank(dto.minimalRank());
		team.setLeaderId(dto.leaderId());
		
		Team savedTeam = teamRepository.save(team);
		
		return savedTeam.toDto();
	}
	
	private Team getTeamById(UUID id) {
		return teamRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No team with id: " + id + ", has been found!"));
	}
	
	@Transactional
	void deleteTeamById(UUID id, Principal principal) {
		Team team = getTeamById(id);
		if (!Objects.equals(team.getLeaderId().toString(), principal.getName())) {
			throw new AccessDeniedException("Team must be deleted by team's leader");
		}
		for (User member : team.getMembers()) {
			member.setTeam(null);
		}
		teamRepository.delete(team);
		transmitter.transmitTeamChange(team, AmqpTeamChangesTransmitter.TeamChangeEnum.DELETE);
	}
	
	private User getUserById(UUID id) {
		return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No user with id: " + id + ", has been found."));
	}
	
	@Transactional
	TeamDTO addUserToTeam(UUID userId, UUID teamId, Optional<String> password, Principal principal) {
		if (!Objects.equals(userId.toString(), principal.getName())) {
			throw new AccessDeniedException("Cannot add other user to a team");
		}
		Team team = getTeamById(teamId);
		if (!team.isPublic()) {
			if (password.isEmpty()) {
				log.info("Tried to access closed team without password: " + userId + ";" + teamId + ';' + principal.getName());
				throw new AccessDeniedException("Cannot access closed team without password");
			}
			if (!passwordEncoder.matches(password.get(), team.getPassword())) {
				log.info("Wrong password to access a team: "  + userId + ";" + teamId + ";" + principal.getName());
				throw new AccessDeniedException("Wrong password");
			}
		}
		if (team.getMembers().size() >= 10) {
			log.info("Tried to add more than 10 members to a team: "  + userId + ";" + teamId);
			throw new IllegalArgumentException("Team cannot have more than 10 members");
		}
		User member = userRepository.getReferenceById(userId);
		team.addMember(member);
		transmitter.transmitUserChange(team.getId(), userId, AmqpTeamChangesTransmitter.UserChangeEnum.JOIN);
		
		return teamRepository.save(team).toDto();
	}
	
	@Transactional
	void deleteUserFromTeam(UUID teamId, UUID userId, Principal principal) {
		Team team = teamRepository.findTeamById(teamId).orElseThrow(EntityNotFoundException::new);
		if (!principal.getName().equals(userId.toString()) && !principal.getName().equals(team.getLeaderId().toString())) {
			log.info("Tried to kick another member from a team: teamId - " + team + " principal: " + principal);
			throw new AccessDeniedException("Member may leave team or can be kicked out only by team's leader");
		}
		User member = userRepository.getReferenceById(userId);
		team.removeMember(member);
		if (team.getLeaderId().equals(userId)){
			team.getMembers().forEach(teamMember -> teamMember.setTeam(null));
			teamRepository.delete(team);
		}
		if (team.getMembers().isEmpty()){
			teamRepository.delete(team);
		}
		transmitter.transmitUserChange(team.getId(), userId, AmqpTeamChangesTransmitter.UserChangeEnum.LEAVE);
	}
	
	List<TeamDTO> mapTeamListToTeamDTOList(List<Team> teams) {
		return teams
				.stream()
				.map(Team::toDto)
				.toList();
	}
}
