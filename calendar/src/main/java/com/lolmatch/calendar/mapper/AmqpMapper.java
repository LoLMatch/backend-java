package com.lolmatch.calendar.mapper;

import com.lolmatch.calendar.dto.request.AmqpUserRegisterRequest;
import com.lolmatch.calendar.model.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class AmqpMapper {

    public UserEntity map(AmqpUserRegisterRequest registerRequest) {
        return UserEntity.builder()
                .id(registerRequest.getId())
                .username(registerRequest.getUsername())
                .build();
    }
}
