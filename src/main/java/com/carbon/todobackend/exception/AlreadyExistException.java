package com.carbon.todobackend.exception;

import java.io.Serial;

public class AlreadyExistException extends Exception {
    @Serial
    private static final long serialVersionUID = 1L;

    public AlreadyExistException(final String message) {
        super(message);
    }
}
