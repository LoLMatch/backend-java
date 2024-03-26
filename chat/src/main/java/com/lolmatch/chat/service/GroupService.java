package com.lolmatch.chat.service;

import com.lolmatch.chat.dao.GroupRepository;
import com.lolmatch.chat.dao.UserRepository;
import com.lolmatch.chat.dto.ContactChangeDTO;
import com.lolmatch.chat.entity.Group;
import com.lolmatch.chat.entity.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GroupService {
	
	private final GroupRepository groupRepository;
	
	private final UserRepository userRepository;
	
	private final SimpMessagingTemplate messagingTemplate;
	
	@Transactional
	public void saveGroup(UUID groupId, UUID leaderId, String groupName) {
		User groupLeader = getUser(leaderId);
		Group group = Group.builder()
				.id(groupId)
				.name(groupName)
				.users(Set.of(groupLeader))
				.build();
		groupLeader.setGroup(groupRepository.save(group));
		userRepository.save(groupLeader);
		ContactChangeDTO dto = new ContactChangeDTO(ContactChangeDTO.ActionType.CONTACT_ADDED, ContactChangeDTO.ContactType.GROUP, groupId, groupName);
		messagingTemplate.convertAndSend("/topic/chat/" + groupLeader.getId().toString(), dto);
	}
	
	@Transactional
	public void editGroup(UUID groupId, String groupName) {
		Group group = getGroup(groupId);
		group.setName(groupName);
		groupRepository.save(group);
	}
	
	@Transactional
	public void deleteGroup(UUID groupId) {
		Group group = getGroup(groupId);
		group.getUsers().forEach(user -> {
			user.setGroup(null);
			userRepository.save(user);
			ContactChangeDTO dto = new ContactChangeDTO(ContactChangeDTO.ActionType.CONTACT_DELETED, ContactChangeDTO.ContactType.GROUP, group.getId(), group.getName());
			messagingTemplate.convertAndSend("/topic/chat/" + user.getId().toString(), dto);
		});
		groupRepository.delete(group);
	}
	
	@Transactional
	public void addUserToGroup(UUID groupId, UUID userId) {
		User user = getUser(userId);
		Group group = getGroup(groupId);
		user.setGroup(group);
		userRepository.save(user);
		
		ContactChangeDTO dto = new ContactChangeDTO(ContactChangeDTO.ActionType.CONTACT_ADDED, ContactChangeDTO.ContactType.GROUP, groupId, group.getName());
		messagingTemplate.convertAndSend("/topic/chat/" + user.getId().toString(), dto);
	}
	
	public void deleteUserFromGroup(UUID userId) {
		User user = getUser(userId);
		Group group = user.getGroup();
		user.setGroup(null);
		userRepository.save(user);
		
		ContactChangeDTO dto = new ContactChangeDTO(ContactChangeDTO.ActionType.CONTACT_DELETED, ContactChangeDTO.ContactType.GROUP, group.getId(), group.getName());
		messagingTemplate.convertAndSend("/topic/chat/" + user.getId().toString(), dto);
	}
	
	private User getUser(UUID id) {
		return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No user found: " + id));
	}
	
	private Group getGroup(UUID id) {
		return groupRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No group found: " + id));
	}
}
