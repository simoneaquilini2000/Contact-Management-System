package com.app.contactmanagementsystem.exceptions;

import com.app.contactmanagementsystem.exceptionhandling.model.ErrorResponse;

public abstract class InternalException extends RuntimeException {

    public InternalException(String message) {
        super(message);
    }

    abstract public ErrorResponse getAsErrorResponse();
}
