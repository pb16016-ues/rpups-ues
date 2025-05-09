package com.ues.edu.sv.rpups_ues.exceptions;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ControllerExceptionHelper {

    @ExceptionHandler(value = { MethodArgumentTypeMismatchException.class, NoSuchElementException.class,
            EmptyResultDataAccessException.class, UniqueValidationException.class, IllegalArgumentException.class })
    ResponseEntity<ErrorResponse> HandleNotFoundExceptions(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(new ErrorResponse(new Date(), status.value(), status.name(), ex.getMessage(),
                request.getDescription(false)), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = { DuplicateEntityException.class })
    public ResponseEntity<ErrorResponse> handleDuplicateEntityException(DuplicateEntityException ex,
            WebRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        String errorMessage = ex.getMessage();
        return new ResponseEntity<>(new ErrorResponse(new Date(), status.value(), status.name(), errorMessage,
                request.getDescription(false)), status);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ErrorResponse> HandleValidationsExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> validations = new HashMap<>();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        for (FieldError fieldError : ex.getFieldErrors()) {
            validations.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return new ResponseEntity<>(new ErrorResponse(new Date(), status.value(), status.name(), ex.getMessage(),
                request.getDescription(false), validations), HttpStatus.BAD_REQUEST);
    }

}
