package com.lolmatch.calendar.model.notification;

import lombok.AllArgsConstructor;

import java.security.Principal;

@AllArgsConstructor
public class StompPrincipal implements Principal {

    private String name;

    @Override
    public String getName() {
        return this.name;
    }
}
