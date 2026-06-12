package com.app.contactmanagementsystem.exceptions;

import com.app.contactmanagementsystem.exceptionhandling.model.ErrorResponse;

public class ContactNotFoundException extends InternalException {

    public ContactNotFoundException(String message) {
        super(message);
    }

    @Override
    public ErrorResponse getAsErrorResponse() {
        return new ErrorResponse(getMessage());
    }
}
