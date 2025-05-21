package com.leonardo.propostaapp.exception;

import java.io.Serial;

/**
 * Exception for errors related to message broker communication.
 */
public class MessagingServiceException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public MessagingServiceException(String message) {
        super(message);
    }

    public MessagingServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
