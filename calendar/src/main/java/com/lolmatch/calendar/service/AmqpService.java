package com.lolmatch.calendar.service;

import com.lolmatch.calendar.dto.request.AmqpUserRegisterRequest;
import com.lolmatch.calendar.mapper.AmqpMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AmqpService {

    private final UserService userService;
    private final AmqpMapper amqpMapper;

    public void saveRegisteredUser(AmqpUserRegisterRequest registerRequest) {
        userService.save(amqpMapper.map(registerRequest));
    }
}
