package edu.jarkvin.freegrammar.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TimeOutException extends RuntimeException{
    public TimeOutException() {
    }

    public TimeOutException(String message) {
        super(message);
    }
}
