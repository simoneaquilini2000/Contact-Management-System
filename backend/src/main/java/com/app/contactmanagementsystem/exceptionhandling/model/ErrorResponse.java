package com.app.contactmanagementsystem.exceptionhandling.model;

import java.util.UUID;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ErrorResponse {
    private final String message;

    private final UUID traceId = UUID.randomUUID();
}
