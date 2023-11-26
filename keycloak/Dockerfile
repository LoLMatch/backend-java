FROM keycloak/keycloak:22.0.5

# Enable health and metrics support
#ENV KC_HEALTH_ENABLED=true
#ENV KC_METRICS_ENABLED=true

# Configure database
#ENV KC_DB=postgres
#ENV KC_DB_PASSWORD=postgres
#ENV KC_DB_USERNAME=postgres
#ENV KC_DB_URL=jdbc:postgresql://localhost:5432/keycloak

# Logs
#ENV KC_LO=file

# Create admin
ENV KEYCLOAK_ADMIN=admin
ENV KEYCLOAK_ADMIN_PASSWORD=password

# Copy configs
COPY keycloak.conf /opt/keycloak/conf

# Copy .jars with external providers
WORKDIR /opt/keycloak/providers
ADD --chown=keycloak:keycloak *.jar .
ADD --chown=keycloak:keycloak /target/*.jar .
ADD --chown=keycloak:keycloak /src/main/resources/application.properties .
WORKDIR /

EXPOSE 8080

ENTRYPOINT ["/opt/keycloak/bin/kc.sh"]