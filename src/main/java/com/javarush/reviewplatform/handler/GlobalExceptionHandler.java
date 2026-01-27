package com.javarush.reviewplatform.handler;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ValidationErrorResponse handleEntityNotFoundException(EntityNotFoundException e) {
        return ValidationErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .errors(Map.of("error", e.getMessage()))
                .build();
    }
}
