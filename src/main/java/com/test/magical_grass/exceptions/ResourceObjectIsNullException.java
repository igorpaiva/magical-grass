package com.test.magical_grass.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ResourceObjectIsNullException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public ResourceObjectIsNullException(String exception) {
        super(exception);
    }

    public ResourceObjectIsNullException() {
        super("It is not allowed to persist a null object.");
    }
}
