package com.leonardo.propostaapp.exception;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Data;

/**
 * Standardized API error response object.
 */
@Data
@Builder
public class ApiError {

    private HttpStatus status;
    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    private Object details;
}
