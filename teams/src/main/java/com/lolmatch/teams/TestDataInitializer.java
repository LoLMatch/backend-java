package com.lolmatch.teams;

import com.lolmatch.teams.team.Rank;
import com.lolmatch.teams.team.Team;
import com.lolmatch.teams.team.TeamRepository;
import com.lolmatch.teams.user.User;
import com.lolmatch.teams.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Profile({"local","dev"})
public class TestDataInitializer {
	
	private final UserRepository userRepository;
	private final TeamRepository teamRepository;
	private final PasswordEncoder passwordEncoder;
	
	public void initUsers() {
		Team team = new Team();
		team.setName("Best team");
		team.setDescription("Team description ----");
		team.setPublic(true);
		team.setMinimalRank(Rank.EMERALD_I);
		team.setTeamCountry("Poland");
		team.setLeaderId(UUID.fromString("fd0a67ca-1fe7-4759-854b-4ba0a1ac818e"));
		team.setTeamWinRate(BigDecimal.valueOf(69.69));
		Team team1 = teamRepository.save(team);
		
		team.setName("ESSA TEAM");
		team.setDescription("Team description ----1234");
		team.setPublic(true);
		team.setMinimalRank(Rank.IRON_IV);
		team.setTeamCountry("Greece");
		team.setLeaderId(UUID.fromString("21d8e0d9-2545-4404-925e-e245032ec5cc"));
		team.setTeamWinRate(BigDecimal.valueOf(1.15));
		Team team2 = teamRepository.save(team);
		
		team.setName("PDW");
		team.setDescription("POZDRO DLA KUMATYCH");
		team.setPublic(false);
		team.setMinimalRank(Rank.IRON_IV);
		team.setTeamCountry("Poland");
		team.setPassword(passwordEncoder.encode("1234"));
		team.setLeaderId(UUID.fromString("b4b70f3a-7aa4-4d53-8020-e466d1e1a019"));
		team.setTeamWinRate(BigDecimal.valueOf(50.0));
		Team team3 = teamRepository.save(team);
		
		User user = new User();
		user.setId(UUID.fromString("fd0a67ca-1fe7-4759-854b-4ba0a1ac818e"));
		user.setUsername("bob");
		user.setWinRate(BigDecimal.valueOf(51.00));
		user.setTeam(team1);
		User bob = userRepository.save(user);
		
		
		user.setId(UUID.fromString("c8973806-df29-4ae7-8bab-79a2c52b7193"));
		user.setUsername("ash");
		user.setWinRate(BigDecimal.valueOf(48.50));
		user.setTeam(team1);
		User ash = userRepository.save(user);
		
		user.setId(UUID.fromString("21d8e0d9-2545-4404-925e-e245032ec5cc"));
		user.setUsername("rob");
		user.setWinRate(BigDecimal.valueOf(52.69));
		user.setTeam(team2);
		User rob = userRepository.save(user);
		
		user.setId(UUID.fromString("b4b70f3a-7aa4-4d53-8020-e466d1e1a019"));
		user.setUsername("user");
		user.setWinRate(BigDecimal.valueOf(46.70));
		user.setTeam(team3);
		User user1 = userRepository.save(user);
	}
}
