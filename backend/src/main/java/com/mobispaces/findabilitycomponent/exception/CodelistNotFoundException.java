package com.mobispaces.findabilitycomponent.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CodelistNotFoundException extends RuntimeException {
    public CodelistNotFoundException() {
        super();
    }

    public CodelistNotFoundException(String message) {
        super(message);
    }

    public CodelistNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}