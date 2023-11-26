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
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

public class AmqpEventListenerProviderFactory implements EventListenerProviderFactory {
	
	ConnectionFactory connectionFactory;
	Connection connection;
	
	Properties properties;
	
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
		loadProperties();

		connectionFactory = new ConnectionFactory();
		connectionFactory.setHost(properties.getProperty("amqp-hostname"));
		connectionFactory.setPort(Integer.parseInt(properties.getProperty("amqp-port")));
		connectionFactory.setUsername(properties.getProperty("amqp-username"));
		connectionFactory.setPassword(properties.getProperty("amqp-password"));
		try {
			connection = connectionFactory.newConnection();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println(Arrays.toString(e.getStackTrace()));
		}
	}
	
	public void loadProperties() {
		File file = new File("opt/keycloak/providers/application.properties");
		properties = new Properties();
		try (InputStream resourceStream = new FileInputStream(file)) {
			properties.load(resourceStream);
		} catch (IOException e) {
			e.printStackTrace();
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
