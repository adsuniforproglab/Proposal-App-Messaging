package com.leonardo.propostaapp.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * Global exception handler for centralizing API error responses.
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handles validation errors for request payloads.
     * 
     * @param ex The validation exception
     * @return A response entity with detailed validation errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ApiError apiError = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("Validation error")
                .timestamp(LocalDateTime.now())
                .details(errors)
                .build();

        log.warn("Validation error: {}", errors);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles resource not found exceptions.
     * 
     * @param ex The not found exception
     * @return A response entity with error details
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.NOT_FOUND)
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        log.warn("Resource not found: {}", ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles messaging service exceptions.
     * 
     * @param ex The messaging service exception
     * @return A response entity with error details
     */
    @ExceptionHandler(MessagingServiceException.class)
    public ResponseEntity<Object> handleMessagingServiceException(MessagingServiceException ex) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .message("Messaging service error")
                .timestamp(LocalDateTime.now())
                .details(ex.getMessage())
                .build();

        log.error("Messaging service error: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(apiError, HttpStatus.SERVICE_UNAVAILABLE);
    }

    /**
     * Handles all other uncaught exceptions.
     * 
     * @param ex The exception
     * @return A response entity with error details
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message("An unexpected error occurred")
                .timestamp(LocalDateTime.now())
                .build();

        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
