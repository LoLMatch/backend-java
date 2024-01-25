FROM maven:3.8.4-openjdk-17 as build

WORKDIR /app

COPY ./pom.xml ./pom.xml
COPY ./src ./src

RUN mvn -f ./pom.xml clean package

FROM keycloak/keycloak:23.0.4
COPY --from=build /app/target/*.jar /opt/keycloak/providers/
# Enable health and metrics support
ENV KC_HEALTH_ENABLED=true
ENV KC_METRICS_ENABLED=true

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

# Set port
ENV KC_HTTP_PORT=8443

# Add providers
WORKDIR /opt/keycloak/providers
ADD --chown=keycloak:keycloak *.jar .
ADD --chown=keycloak:keycloak target/*.jar .

WORKDIR /opt/keycloak
RUN keytool -genkeypair -storepass password -storetype PKCS12 -keyalg RSA -keysize 2048 -dname "CN=server" -alias server -ext "SAN:c=DNS:localhost,IP:127.0.0.1" -keystore conf/server.keystore

WORKDIR /
COPY /realm.json /opt/keycloak/data/import/realm.json

ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start-dev", "--import-realm"]