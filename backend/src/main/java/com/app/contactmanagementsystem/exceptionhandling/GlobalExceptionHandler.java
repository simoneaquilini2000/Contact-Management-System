package com.app.contactmanagementsystem.exceptionhandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.app.contactmanagementsystem.exceptionhandling.model.ErrorResponse;
import com.app.contactmanagementsystem.exceptions.AuthenticationNotFoundException;
import com.app.contactmanagementsystem.exceptions.ContactNotFoundException;
import com.app.contactmanagementsystem.exceptions.InternalException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ContactNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleContactNotFoundException(ContactNotFoundException exception) {
        return logAndReturn(exception);
    }

    @ExceptionHandler(AuthenticationNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorResponse handleAuthenticationNotFoundException(AuthenticationNotFoundException exception) {
        return logAndReturn(exception);
    }

    private ErrorResponse logAndReturn(InternalException ex) {
        // Log the exception (you can use a logging framework like SLF4J)
        log.warn("An error occurred: " + ex.getMessage(), ex);
        return ex.getAsErrorResponse();
    }
}
