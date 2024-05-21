package com.lolmatch.chat.dto;

import com.lolmatch.chat.entity.User;

import java.util.List;


public record ContactListDTO(User user, List<ContactDTO> contacts) {
}


