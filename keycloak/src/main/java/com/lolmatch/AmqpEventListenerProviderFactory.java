package com.lolmatch;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.jboss.logging.Logger;
import org.keycloak.Config;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

import java.io.IOException;
import java.util.Properties;

public class AmqpEventListenerProviderFactory implements EventListenerProviderFactory {
	
	private static final Logger logger = Logger.getLogger(AmqpEventListenerProviderFactory.class.getName());
	
	ConnectionFactory connectionFactory;
	Connection connection;
	Properties properties;
	
	@Override
	public EventListenerProvider create(KeycloakSession keycloakSession) {
		if( connection == null){
			try {
				connection = connectionFactory.newConnection();
			} catch ( Exception e){
				logger.error("Connection was null, error while trying to reconnect to amqp broker");
				logger.error("Connection properties: " + properties.toString());
				logger.error(e.getMessage());
			}
		}
		try {
			return new AmqpEventListenerProvider(connection);
		} catch (IOException e) {
			logger.error(e.getMessage());
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
		logger.info("Initializing EventListenerFactory - properties: " + properties.toString());
		try {
			connection = connectionFactory.newConnection();
			logger.info("Successfully connected to amqp broker with given properties");
		} catch (Exception e) {
			logger.error("Error while connecting to amqp broker");
			logger.error(e.getMessage());
		}
	}
	
	public void loadProperties() {
		properties = new Properties();
		properties.setProperty("amqp-hostname",System.getProperty("AMQP_HOSTNAME", "rabbitmq"));
		properties.setProperty("amqp-port", System.getProperty("AMQP_PORT", "5672"));
		properties.setProperty("amqp-username", System.getProperty("AMQP_USERNAME", "guest"));
		properties.setProperty("amqp-password", System.getProperty("AMQP_PASSWORD", "guest"));
	}
	
	@Override
	public void postInit(KeycloakSessionFactory keycloakSessionFactory) {
	}
	
	@Override
	public void close() {
		try {
			connection.close();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}
	
	@Override
	public String getId() {
		return "amqp_event_listener";
	}
}