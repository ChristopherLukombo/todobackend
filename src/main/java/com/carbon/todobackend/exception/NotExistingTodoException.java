package com.carbon.todobackend.exception;

import java.io.Serial;

public class NotExistingTodoException extends Exception {
    @Serial
    private static final long serialVersionUID = 1L;

    public NotExistingTodoException(final String message) {
        super(message);
    }
}
