FROM postgres:latest

ARG POSTGRES_USER
ARG POSTGRES_PASSWORD
ARG KEYCLOAK_PASSWORD

ENV KEYCLOAK_DB='keycloak_db'
ENV POSTGRES_USER=${POSTGRES_USER}
ENV POSTGRES_PASSWORD=${POSTGRES_PASSWORD}

RUN echo " #!/bin/bash \
    psql -v ON_ERROR_STOP=1 -U "${POSTGRES_USER}" <<-EOSQL \
    CREATE USER keycloak WITH PASSWORD '${KEYCLOAK_PASSWORD}'; \
    CREATE DATABASE ${KEYCLOAK_DB}; \
    GRANT ALL PRIVILEGES ON DATABASE ${KEYCLOAK_DB} to keycloak; \ 
    EOSQL" > /docker-entrypoint-initdb.d/init-user-db.sh
    
RUN chmod +x /docker-entrypoint-initdb.d/init-user-db.sh