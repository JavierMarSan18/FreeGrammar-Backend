package edu.jarkvin.freegrammar.controller;

import edu.jarkvin.freegrammar.exception.BadRequestException;
import edu.jarkvin.freegrammar.exception.InvalidBoundException;
import edu.jarkvin.freegrammar.exception.TimeOutException;
import edu.jarkvin.freegrammar.model.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDate;

@RestControllerAdvice
public class ControllerAdvice {
    private final Message message = new Message();

    @ExceptionHandler(OutOfMemoryError.class)
    public ResponseEntity<Message> outOfMemoryError(OutOfMemoryError e){
        message.setSubject("Máximo de memoria alcanzado.");
        message.setDate(LocalDate.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Message> httpMessageNotReadableException(HttpMessageNotReadableException e){
        message.setSubject("El formato de la solicitud no es soportado.");
        message.setDate(LocalDate.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Message> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e){
        message.setSubject("El tipo de dato de '"+ e.getValue() +"' es inválido.");
        message.setDate(LocalDate.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Message> badRequestException(BadRequestException e){
        message.setSubject(e.getMessage());
        message.setDate(LocalDate.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(TimeOutException.class)
    public ResponseEntity<Message> timeOutException(TimeOutException e){
        message.setSubject(e.getMessage());
        message.setDate(LocalDate.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(InvalidBoundException.class)
    public ResponseEntity<Message> invalidBoundException(InvalidBoundException e){
        message.setSubject(e.getMessage());
        message.setDate(LocalDate.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
    }
}
