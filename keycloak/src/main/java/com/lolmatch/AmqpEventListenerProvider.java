package com.lolmatch;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.events.admin.OperationType;
import org.keycloak.events.admin.ResourceType;

import java.io.IOException;
import java.util.Objects;

public class AmqpEventListenerProvider implements EventListenerProvider {
	
	Connection connection;
	Channel channel;
	
	static final String queueName = "user-register-queue";
	
	public AmqpEventListenerProvider(Connection connection) throws IOException {
		this.connection = connection;
		channel = connection.createChannel();
		channel.queueDeclare(queueName, false, false, false, null);
	}
	
	@Override
	public void onEvent(Event event) {
		try {
			if (event.getType() == EventType.REGISTER) {
				String id = event.getUserId();
				String json = "{ \"id\":\"" + id + "\"}";
				channel.basicPublish("", queueName, null, json.getBytes());
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Override
	public void onEvent(AdminEvent adminEvent, boolean b) {
		if (Objects.equals(adminEvent.getResourceType(), ResourceType.USER) && Objects.equals(adminEvent.getOperationType(), OperationType.CREATE)) {
			String id = adminEvent.getResourcePath().split("/")[1];
			if (Objects.nonNull(id)) {
				try {
					String json = "{ \"id\":\"" + id + "\"}";
					channel.basicPublish("", queueName, null, json.getBytes());
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			} else {
				throw new RuntimeException("Failed to retrieve id of created user");
			}
		}
	}
	
	@Override
	public void close() {
		try {
			channel.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
