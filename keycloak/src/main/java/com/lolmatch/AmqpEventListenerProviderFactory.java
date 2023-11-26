package com.lolmatch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.ConnectionFactory;
import org.keycloak.Config;
import com.rabbitmq.client.Connection;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

public class AmqpEventListenerProviderFactory implements EventListenerProviderFactory {
	
	ConnectionFactory connectionFactory;
	Connection connection;
	

	
	@Override
	public EventListenerProvider create(KeycloakSession keycloakSession) {
		try {
			return new AmqpEventListenerProvider(connection);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void init(Config.Scope scope) {
		connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("172.25.0.3");
		connectionFactory.setPort(5672);
		try {
			connection = connectionFactory.newConnection();
		} catch (Exception e) {
			System.out.println("EE");
			System.out.println(e.getMessage());
			System.out.println("DD");
			System.out.println(Arrays.toString(e.getStackTrace()));
		}
	}
	
	@Override
	public void postInit(KeycloakSessionFactory keycloakSessionFactory) {
	
	}
	
	@Override
	public void close() {
		try {
			connection.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public String getId() {
		return "amqp_event_listener";
	}
}
