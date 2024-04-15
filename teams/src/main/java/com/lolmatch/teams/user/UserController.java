package com.lolmatch.teams.user;

import com.lolmatch.teams.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
	private final UserService userService;
	
	@GetMapping("/{id}")
	public UserDTO getUserById(@PathVariable UUID id) {
		log.info("Get user info with id: " + id);
		return userService.getUserById(id);
	}
}
