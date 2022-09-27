package comedu.jarkvin.freegrammar.controller;

import comedu.jarkvin.freegrammar.exception.TimeOutException;
import comedu.jarkvin.freegrammar.model.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;

@RestControllerAdvice
public class ControllerAdvice {
    private final Message message = new Message();

    @ExceptionHandler(TimeOutException.class)
    public ResponseEntity<Message> timeOutException(TimeOutException e){
        message.setSubject(e.getMessage());
        message.setDate(LocalDate.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }
}
