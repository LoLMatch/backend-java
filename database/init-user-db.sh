#!/bin/bash
set -e

for var in keycloak calendar chat teams recommender
do
    psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
        CREATE USER $var WITH PASSWORD '$var';
        CREATE DATABASE $var;
        GRANT ALL PRIVILEGES ON DATABASE $var TO $var;
EOSQL

    psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$var" <<-EOSQL
        GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO $var;
        GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO $var;
        GRANT USAGE, CREATE ON SCHEMA public TO $var;
EOSQL
done
