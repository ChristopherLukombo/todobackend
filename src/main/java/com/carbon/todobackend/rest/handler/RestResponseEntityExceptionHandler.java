package com.carbon.todobackend.rest.handler;

import com.carbon.todobackend.exception.AlreadyExistException;
import com.carbon.todobackend.exception.NotExistingTodoException;
import com.carbon.todobackend.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({AlreadyExistException.class})
    ResponseEntity<?> handleAlreadyExist() {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .build();
    }

    @ExceptionHandler({NotFoundException.class})
    ResponseEntity<?> handleNotFound() {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .build();
    }

    @ExceptionHandler({NotExistingTodoException.class})
    ResponseEntity<?> handleNotExistingTodo() {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .build();
    }
}