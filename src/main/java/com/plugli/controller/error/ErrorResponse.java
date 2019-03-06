package com.plugli.controller.error;

import org.springframework.http.HttpStatus;

import java.time.Instant;

public class ErrorResponse {
    private HttpStatus status;
    private String message;
    private Instant timestamp;

    public ErrorResponse(String message, HttpStatus status) {
        this.status = status;
        this.message = message;
        this.timestamp = Instant.now();
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
