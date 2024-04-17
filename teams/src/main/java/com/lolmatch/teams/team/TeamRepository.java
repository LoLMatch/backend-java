package com.lolmatch.teams.team;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface TeamRepository extends JpaRepository<Team, UUID> {
	
	Page<Team> findTeamsByMinimalRankLessThanEqual(Pageable pageable, Rank rank);
	
	Page<Team> findTeamsByMinimalRankLessThanEqualAndTeamCountryEquals(Pageable pageable, Rank rank, String teamCountry);
	
	@Query(value = "SELECT t FROM Team t LEFT JOIN FETCH User u on t = u.team WHERE t.name = ?1")
	Optional<Team> findTeamByName(String name);
	
	@Query(value = "SELECT t FROM Team t LEFT JOIN FETCH User u on t = u.team WHERE t.id = ?1")
	Optional<Team> findTeamById(UUID id);
	
	@Query(value = "SELECT t FROM Team t INNER JOIN User u on t = u.team WHERE u.id = ?1")
	Optional<Team> findTeamByUser(UUID id);
}
