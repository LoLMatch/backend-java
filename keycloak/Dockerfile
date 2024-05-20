FROM maven:3.8.4-openjdk-17 as build

WORKDIR /app

COPY ./pom.xml /app/pom.xml
COPY ./keycloak/pom.xml /app/keycloak/pom.xml
COPY ./keycloak/src /app/keycloak/src

RUN mvn -f /app/keycloak/pom.xml clean install

RUN ls -la /app/keycloak/target

FROM keycloak/keycloak:24.0.1

# to trzeba z pliku konfiguracyjnego wczytać, poki co na sztywno zostawiamy
ARG AMQP_USERNAME=admin
ARG AMQP_PASSWORD=password

# Enable health and metrics support
ENV KC_HEALTH_ENABLED=true
ENV KC_METRICS_ENABLED=true

# Configure database and message queue
ENV KC_DB=postgres
ENV KC_DB_PASSWORD=keycloak
ENV KC_DB_USERNAME=keycloak
ENV KC_DB_URL=jdbc:postgresql://database:5432/keycloak

ENV AMQP_HOSTNAME=rabbitmq
ENV AMQP_PORT=5672
ENV AMQP_USERNAME=$AMQP_USERNAME
ENV AMQP_PASSWORD=$AMQP_PASSWORD

# Logs
ENV KC_LOG=file,console

# Create admin
#ENV KEYCLOAK_ADMIN=admin
#ENV KEYCLOAK_ADMIN_PASSWORD=password

# Set port
ENV KC_HTTP_ENABLED=true
ENV KC_HTTP_PORT=8080

# Add providers
WORKDIR /opt/keycloak/providers

COPY --from=build --chown=keycloak:keycloak /app/keycloak/target/*.jar .
# Ponizsze jest do zmiany, bo .jar powinien byc wynikiem builda
COPY --chown=keycloak:keycloak /keycloak/*.jar  .

WORKDIR /

COPY ./keycloak/realm.json /opt/keycloak/data/import/realm.json
ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start-dev", "--import-realm", "--health-enabled=true"]
