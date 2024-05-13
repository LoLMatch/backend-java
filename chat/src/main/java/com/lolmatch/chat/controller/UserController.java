package com.lolmatch.chat.controller;

import com.lolmatch.chat.dto.UserDTO;
import com.lolmatch.chat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
	
	private final UserService userService;
	
	@PatchMapping("/{id}")
	public void updateUser(@PathVariable UUID id, @RequestBody UserDTO dto){
		//userService.updateUser(id, dto);
	}
}
