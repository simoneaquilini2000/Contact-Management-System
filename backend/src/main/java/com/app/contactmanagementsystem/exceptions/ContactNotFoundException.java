package com.app.contactmanagementsystem.exceptions;

public class ContactNotFoundException extends InternalException {

    public ContactNotFoundException(String message) {
        super(message);
    }
}
