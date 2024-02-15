#!/bin/bash
set -e

wait_for_rabbitmq() {
    echo "Waiting for RabbitMQ to start..."
    while ! rabbitmqctl status; do
        sleep 1
    done
}

rabbitmq-server -detached
wait_for_rabbitmq
sleep 3
rabbitmqctl add_user $RABBITMQ_DEFAULT_USER $RABBITMQ_DEFAULT_PASS
rabbitmqctl set_user_tags $RABBITMQ_DEFAULT_USER administrator
rabbitmqctl set_permissions -p / $RABBITMQ_DEFAULT_USER ".*" ".*" ".*"
rabbitmqctl stop

exec docker-entrypoint.sh rabbitmq-server
