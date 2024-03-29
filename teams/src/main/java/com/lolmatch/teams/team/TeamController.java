package com.lolmatch.teams.team;

import com.lolmatch.teams.team.dto.AddTeamRequest;
import com.lolmatch.teams.team.dto.AddUserToTeamRequest;
import com.lolmatch.teams.team.dto.TeamDTO;
import com.lolmatch.teams.team.dto.TeamListReponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
	
	@GetMapping
	public TeamListReponse getTeams(@RequestParam Optional<Integer> size, @RequestParam Optional<Integer> page, @RequestParam Optional<String> country, @RequestParam Optional<Rank> minimalRank) {
		log.info("Get list of teams request: " + size + ";" + page + ";" + country + ";" + minimalRank);
		return teamService.getTeamsFilteredAndPaginated(size, page, country, minimalRank);
	}
	
	@PostMapping()
	@ResponseStatus(value = HttpStatus.CREATED)
	public TeamDTO addTeam(@Valid @RequestBody AddTeamRequest dto, Principal principal) {
		log.info("add team request: " + dto.toString() + ";" + principal);
		return teamService.saveTeam(dto, principal);
	}
	
	@GetMapping("/{id}")
	public TeamDTO getTeamById(@PathVariable UUID id) {
		return teamService.findTeamById(id);
	}
	
	@DeleteMapping("/{id}")
	public void deleteTeamById(@PathVariable UUID id, Principal principal) {
		teamService.deleteTeamById(id, principal);
	}
	
	@PatchMapping("/{id}")
	public TeamDTO editTeamById(@Valid @RequestBody AddTeamRequest dto, Principal principal, @PathVariable UUID id) {
		return teamService.updateTeam(id, dto, principal);
	}
	
	@PostMapping("/users")
	public TeamDTO addUserToTeam(@RequestBody AddUserToTeamRequest request, Principal principal) {
		return teamService.addUserToTeam(request, principal);
	}
	
	@DeleteMapping("/{teamId}/users/{userId}")
	public void deleteUserFromTeam(@PathVariable UUID userId, Principal principal, @PathVariable UUID teamId) {
		teamService.deleteUserFromTeam(teamId, userId, principal);
	}
}
