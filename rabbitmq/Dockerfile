FROM rabbitmq:3-management

ENV RABBIT_DEFINITIONS_PATH /etc/rabbitmq/definitions.json

COPY rabbitmq/definitions.json ${RABBIT_DEFINITIONS_PATH}
COPY rabbitmq/rabbitmq.conf /etc/rabbitmq/conf.d/rabbitmq.conf