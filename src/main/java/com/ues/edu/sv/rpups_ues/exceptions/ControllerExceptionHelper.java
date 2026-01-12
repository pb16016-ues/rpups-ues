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

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.persistence.RollbackException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerExceptionHelper {

    @ExceptionHandler(value = { MethodArgumentTypeMismatchException.class, NoSuchElementException.class,
            EmptyResultDataAccessException.class, UniqueValidationException.class, IllegalArgumentException.class })
    ResponseEntity<ErrorResponse> HandleNotFoundExceptions(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(new ErrorResponse(new Date(), status.value(), status.name(), ex.getMessage(),
                request.getDescription(false)), status);
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

    @ExceptionHandler(value = ConstraintViolationException.class)
    ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        Map<String, String> validations = new HashMap<>();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String propertyPath = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            validations.put(propertyPath, message);
        }
        
        String errorMessage = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        
        return new ResponseEntity<>(new ErrorResponse(new Date(), status.value(), status.name(), errorMessage,
                request.getDescription(false), validations), status);
    }

    @ExceptionHandler(value = RollbackException.class)
    ResponseEntity<ErrorResponse> handleRollbackException(RollbackException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Map<String, String> validations = new HashMap<>();
        
        // Extraer ConstraintViolationException si existe en la causa
        Throwable cause = ex.getCause();
        if (cause instanceof ConstraintViolationException) {
            ConstraintViolationException cve = (ConstraintViolationException) cause;
            
            for (ConstraintViolation<?> violation : cve.getConstraintViolations()) {
                String propertyPath = violation.getPropertyPath().toString();
                String message = violation.getMessage();
                validations.put(propertyPath, message);
            }
            
            String errorMessage = cve.getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(". "));
            
            return new ResponseEntity<>(new ErrorResponse(new Date(), status.value(), status.name(), errorMessage,
                    request.getDescription(false), validations), status);
        }
        
        // Si no es una ConstraintViolationException, retornar mensaje genérico
        return new ResponseEntity<>(new ErrorResponse(new Date(), status.value(), status.name(), 
                "Error de validación al persistir datos",
                request.getDescription(false)), status);
    }

}
