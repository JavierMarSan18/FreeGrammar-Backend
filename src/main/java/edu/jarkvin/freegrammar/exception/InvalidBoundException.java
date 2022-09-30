package edu.jarkvin.freegrammar.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InvalidBoundException extends RuntimeException {
    public InvalidBoundException() {
    }

    public InvalidBoundException(String message) {
        super(message);
    }
}
