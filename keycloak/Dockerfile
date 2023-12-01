FROM keycloak/keycloak:22.0.3

# Enable health and metrics support
#ENV KC_HEALTH_ENABLED=true
#ENV KC_METRICS_ENABLED=true

# Configure database and message queue
ENV KC_DB=postgres
ENV KC_DB_PASSWORD=postgres
ENV KC_DB_USERNAME=postgres
ENV KC_DB_URL=jdbc:postgresql://database:5432/keycloak
ENV AMQP_HOSTNAME=rabbitmq
ENV AMQP_PORT=5672
ENV AMQP_USERNAME=guest
ENV AMQP_PASSWORD=guest

# Logs
ENV KC_LOG=file,console

# Create admin
ENV KEYCLOAK_ADMIN=admin
ENV KEYCLOAK_ADMIN_PASSWORD=password

# Enable metrics
ENV KC_METRICS_ENABLED=true
ENV KC_HEALTH_ENABLED=true

# Copy configs - for now config will be set with dockerfile
# COPY keycloak.conf /opt/keycloak/conf

# Copy .jars with external providers
WORKDIR /opt/keycloak/providers
ADD --chown=keycloak:keycloak *.jar .
ADD --chown=keycloak:keycloak /target/*.jar .
WORKDIR /

EXPOSE 8080

ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start-dev"]