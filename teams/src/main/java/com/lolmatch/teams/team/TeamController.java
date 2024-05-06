package com.lolmatch.teams.team;

import com.lolmatch.teams.team.dto.AddTeamRequest;
import com.lolmatch.teams.team.dto.AddUserToTeamRequest;
import com.lolmatch.teams.team.dto.TeamDTO;
import com.lolmatch.teams.team.dto.TeamListDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamController {
	
	private final TeamService teamService;
	
	@GetMapping()
	public TeamListDTO getTeams(@ParameterObject Pageable pageable, @RequestParam Optional<String> country, @RequestParam Optional<Rank> minimalRank) {
		log.info("Get list of teams request: " + pageable + ";" + country + ";" + minimalRank);
		return teamService.getTeamsFilteredAndPaginated(pageable, country, minimalRank);
	}
	
	@PostMapping()
	@ResponseStatus(value = HttpStatus.CREATED)
	public TeamDTO addTeam(@Valid @RequestBody AddTeamRequest dto, Principal principal) {
		log.info("add team request: " + dto.toString() + ";" + principal);
		return teamService.saveTeam(dto, principal);
	}
	
	@GetMapping("/{criteria}")
	@Operation( summary = "Searches team either by ID if given criteria is proper UUID or else criteria is a team's name")
	public TeamDTO getTeamById(@PathVariable String criteria) {
		log.info("get team by criteria: " + criteria);
		return teamService.findTeamByCriteria(criteria);
	}
	
	@DeleteMapping("/{id}")
	public void deleteTeamById(@PathVariable UUID id, Principal principal) {
		log.info("delete team with id: " + id);
		teamService.deleteTeamById(id, principal);
	}
	
	@PatchMapping("/{id}")
	public TeamDTO editTeamById(@Valid @RequestBody AddTeamRequest dto, Principal principal, @PathVariable UUID id) {
		log.info("Edit team request: " + dto.toString() + "; principal: " + principal.getName() + "; team id: " + id);
		return teamService.updateTeam(id, dto, principal);
	}
	
	@PostMapping("/{teamId}/users")
	public TeamDTO addUserToTeam(@RequestBody AddUserToTeamRequest request, Principal principal, @PathVariable UUID teamId) {
		log.info("Add user to a team request: " + request.toString() + "; principal: " + principal.getName() + "; team id: " + teamId);
		return teamService.addUserToTeam(request.userId(), teamId, request.password(), principal);
	}
	
	@DeleteMapping("/{teamId}/users/{userId}")
	public void deleteUserFromTeam(@PathVariable UUID userId, Principal principal, @PathVariable UUID teamId) {
		log.info(("Delete user from a team userID: " + userId + "; teamId: " + teamId  + "; principal: " + principal.getName()));
		teamService.deleteUserFromTeam(teamId, userId, principal);
	}
}
