package com.lolmatch.calendar.errors;

public class NotificationError extends RuntimeException {
    public NotificationError(String message) {
        super(message);
    }
}
