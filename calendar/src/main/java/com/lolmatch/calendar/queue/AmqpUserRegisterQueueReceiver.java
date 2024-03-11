package com.lolmatch.calendar.queue;

import com.lolmatch.calendar.dto.request.AmqpUserRegisterRequest;
import com.lolmatch.calendar.service.AmqpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmqpUserRegisterQueueReceiver {

    private final AmqpService amqpService;
    private static final String REGISTER_QUEUE_NAME = "user-register-queue-calendar";

    @Bean
    public Queue myQueue() {
        return new Queue(REGISTER_QUEUE_NAME, true);
    }

    @RabbitListener(queues = REGISTER_QUEUE_NAME)
    public void listenOnUserRegistration(AmqpUserRegisterRequest registerRequest) {
        log.info("Message from user-register-queue-calendar: " + registerRequest.toString());
        amqpService.saveRegisteredUser(registerRequest);
    }
}
