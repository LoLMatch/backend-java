package com.lolmatch.calendar.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AmqpUserRegisterRequest {
    private UUID id;
    private String username;
}
