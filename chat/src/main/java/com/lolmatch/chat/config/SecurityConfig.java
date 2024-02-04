package com.lolmatch.chat.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests((requests) -> requests
						.requestMatchers("/websocket").permitAll()
						.anyRequest().authenticated()
				)
				.oauth2ResourceServer((oauth2) -> oauth2
						.jwt(Customizer.withDefaults())
				)
				.csrf(AbstractHttpConfigurer::disable)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
			
		// TODO - tutaj trzeba poprawnie skonfigurować security - dopiąć oauth z keycloaka oraz ustawić odpowiednie dostępy
		return http.build();
	}
	
	/*@Bean
	public UserDetailsService userDetailsService() {
		UserDetails user =
				User.withDefaultPasswordEncoder()
						.username("user")
						.password("password")
						.roles("USER")
						.build();
		UserDetails secondUser =
				User.withDefaultPasswordEncoder()
						.username("user1")
						.password("password1")
						.roles("USER")
						.build();
		*//* FIXME -
		    to jest chwilowa implementacja żeby można było sobie testować
			finalnie będziemy korzystać z OIDC do zdobywania info o userach.
		 *//*
		return new InMemoryUserDetailsManager(user, secondUser);
	}*/
}

