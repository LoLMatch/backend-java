package com.lolmatch.chat.dto;

import com.lolmatch.chat.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ContactListDTO {
	
	private User user;
	
	private List<ContactDTO> contacts;
}


