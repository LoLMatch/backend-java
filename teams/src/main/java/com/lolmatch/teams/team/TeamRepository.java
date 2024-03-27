package com.lolmatch.teams.team;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TeamRepository extends JpaRepository<Team, UUID> {
	
	Page<Team> findTeamsByMinimalRankGreaterThan(Pageable pageable, Rank rank);
	
	Page<Team> findTeamsByMinimalRankGreaterThanAndTeamCountryEquals(Pageable pageable, Rank rank, String teamCountry);
}
