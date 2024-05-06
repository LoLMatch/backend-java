package com.lolmatch.teams.team;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface TeamRepository extends JpaRepository<Team, UUID> {
	
	@Query(value = "SELECT t.is_public, minimal_rank, t.password, t.id, leader_id, t.team_win_rate, description, name, team_country, u.id as userId, u.username FROM teams t LEFT JOIN users u ON t.id = u.team_id WHERE t.minimal_rank <= ?1 AND t.is_public IS TRUE GROUP BY t.is_public, minimal_rank, t.password, t.id, leader_id, t.team_win_rate, description, name, team_country, u.id, u.username HAVING COUNT(u.id) < 10",
			countQuery = "SELECT COUNT(t) FROM teams t LEFT JOIN users u ON t.id = u.team_id WHERE t.minimal_rank <= ?1 AND t.is_public IS TRUE GROUP BY t HAVING COUNT(u.id) < 10",
			nativeQuery = true)
	Page<Team> findTeamsByMinimalRankLessThanEqual(Pageable pageable, Rank rank);
	
	@Query(value = "SELECT t.is_public, minimal_rank, t.password, t.id, leader_id, t.team_win_rate, description, name, team_country, u.id as userId, u.username FROM teams t LEFT JOIN users u ON t.id = u.team_id WHERE t.minimal_rank <= ?1 AND t.is_public IS TRUE AND t.team_country LIKE ?2 GROUP BY t.is_public, minimal_rank, t.password, t.id, leader_id, t.team_win_rate, description, name, team_country, u.id, u.username HAVING COUNT(u.id) < 1",
			countQuery = "SELECT COUNT(t) FROM teams t LEFT JOIN users u ON t.id = u.team_id WHERE t.minimal_rank <= ?1 AND t.is_public IS TRUE AND t.team_country LIKE ?2 GROUP BY t HAVING COUNT(u.id) < 1",
			nativeQuery = true)
	Page<Team> findTeamsByMinimalRankLessThanEqualAndTeamCountryEquals(Pageable pageable, Rank rank, String teamCountry);
	
	@Query(value = "SELECT t FROM Team t LEFT JOIN FETCH User u on t = u.team WHERE t.name = ?1")
	Optional<Team> findTeamByName(String name);
	
	@Query(value = "SELECT t FROM Team t LEFT JOIN FETCH User u on t = u.team WHERE t.id = ?1")
	Optional<Team> findTeamById(UUID id);
	
	@Query(value = "SELECT t FROM Team t INNER JOIN User u on t = u.team WHERE u.id = ?1")
	Optional<Team> findTeamByUser(UUID id);
}
