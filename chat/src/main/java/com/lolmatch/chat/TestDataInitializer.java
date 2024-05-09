package com.lolmatch.chat;

import com.lolmatch.chat.dao.ContactRepository;
import com.lolmatch.chat.dao.GroupRepository;
import com.lolmatch.chat.dao.MessageRepository;
import com.lolmatch.chat.dao.UserRepository;
import com.lolmatch.chat.entity.Contact;
import com.lolmatch.chat.entity.Group;
import com.lolmatch.chat.entity.Message;
import com.lolmatch.chat.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Profile({"local", "docker"})
public class TestDataInitializer {
	
	private final UserRepository userRepository;
	private final ContactRepository contactRepository;
	private final GroupRepository groupRepository;
	private final MessageRepository messageRepository;
	
	public void initUsers(){
		User user = new User();
		user.setId(UUID.fromString("fd0a67ca-1fe7-4759-854b-4ba0a1ac818e"));
		user.setUsername("bob");
		user.setProfilePictureId(2);
		User bob = userRepository.save(user);
		
		user.setId(UUID.fromString("c8973806-df29-4ae7-8bab-79a2c52b7193"));
		user.setUsername("ash");
		user.setProfilePictureId(0);
		User ash = userRepository.save(user);
		
		user.setId(UUID.fromString("21d8e0d9-2545-4404-925e-e245032ec5cc"));
		user.setUsername("rob");
		user.setProfilePictureId(0);
		User rob = userRepository.save(user);
		
		user.setId(UUID.fromString("b4b70f3a-7aa4-4d53-8020-e466d1e1a019"));
		user.setUsername("user");
		user.setProfilePictureId(45);
		User user1 = userRepository.save(user);
		
		createContact(bob, rob);
		createContact(bob, ash);
		createContact(bob, user1);
		createContact(rob, ash);
		
		createMessage(bob, rob, "message1");
		createMessage(rob, bob, "no content");
		createMessage(bob, rob, "message2");
		createMessage(rob, ash, "some message");
		createMessage(ash, bob, "message content");
		
		Group group = new Group();
		group.setName("best team");
		group.setId(UUID.randomUUID());
		groupRepository.save(group);
		
		bob.setGroup(group);
		rob.setGroup(group);
		userRepository.save(bob);
		userRepository.save(rob);
	}
	
	private void createContact(User first, User second) {
		Contact contact = new Contact();
		contact.setUser(first);
		contact.setContact(second);
		// contact.setContactUsername(second.getUsername());
		contactRepository.save(contact);
		
		Contact backContact = new Contact();
		backContact.setUser(second);
		backContact.setContact(first);
		//backContact.setContactUsername(first.getUsername());
		contactRepository.save(backContact);
	}
	
	private void createMessage(User sender, User recipient, String content){
		Message message = new Message();
		message.setSender(sender);
		message.setRecipient(recipient);
		message.setContent(content);
		message.setCreatedAt(Timestamp.from(Instant.now()));
		
		messageRepository.save(message);
	}
}
