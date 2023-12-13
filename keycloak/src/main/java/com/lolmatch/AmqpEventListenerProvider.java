package com.lolmatch;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.jboss.logging.Logger;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.events.admin.OperationType;
import org.keycloak.events.admin.ResourceType;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;

import java.io.IOException;
import java.util.Objects;

public class AmqpEventListenerProvider implements EventListenerProvider {

	private static final Logger logger = Logger.getLogger(AmqpEventListenerProvider.class.getName());
	
	Connection connection;
	Channel channel;
	
	KeycloakSession session;
	
	Gson gson;
	
	static final String queueName = "user-register-queue";
	
	public AmqpEventListenerProvider(Connection connection, KeycloakSession session) throws IOException {
		this.connection = connection;
		this.session = session;
		gson = new Gson();
		channel = connection.createChannel();
		channel.queueDeclare(queueName, false, false, false, null);
	}
	
	@Override
	public void onEvent(Event event) {
		try {
			if (event.getType() == EventType.REGISTER) {
				String id = event.getUserId();
				publishNewUserId(id, event.getRealmId());
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	@Override
	public void onEvent(AdminEvent adminEvent, boolean b) {
		if (Objects.equals(adminEvent.getResourceType(), ResourceType.USER) && Objects.equals(adminEvent.getOperationType(), OperationType.CREATE)) {
			String id = adminEvent.getResourcePath().split("/")[1];
			if (Objects.nonNull(id)) {
				publishNewUserId(id, adminEvent.getRealmId());
			} else {
				logger.error("Failed to retrieve id of created user on adminEvent: " + adminEvent.getId());
			}
		}
	}
	
	public void publishNewUserId(String id, String realmId){
		RealmModel realm = session.realms().getRealm(realmId);
		String username = session.users().getUserById(realm, id).getUsername();
		//String json = "{ \"id\":\"" + id + "\"}";
		UserRegisterMessage message = new UserRegisterMessage(id, username);
		String json = gson.toJson(message);
		try {
			channel.basicPublish("", queueName, null, json.getBytes());
			logger.info( "Register event message sent: " + json);
		} catch ( IOException exception){
			logger.error(exception.getMessage());
		}
	}
	
	@Override
	public void close() {
		try {
			channel.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
}
