package edu.jarkvin.freegrammar.controller;

import edu.jarkvin.freegrammar.exception.BadRequestException;
import edu.jarkvin.freegrammar.exception.InvalidBoundException;
import edu.jarkvin.freegrammar.exception.TimeOutException;
import edu.jarkvin.freegrammar.model.Grammar;
import edu.jarkvin.freegrammar.model.Message;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.lang.reflect.Method;
import java.time.LocalDate;

@RunWith(MockitoJUnitRunner.class)
public class ControllerAdviceTest {

    private ControllerAdvice controller;
    private GrammarController grammarController;
    private Message message;

    @Before
    public void before(){
        controller = new ControllerAdvice();
        grammarController = new GrammarController();
        message = new Message();
    }

    @Test
    public void outOfMemoryError_Test(){
        message.setSubject("Máximo de memoria alcanzado.");
        message.setDate(LocalDate.now());
        ResponseEntity<Message> response = new ResponseEntity<>(message, HttpStatus.INSUFFICIENT_STORAGE);
        Assert.assertEquals(response, controller.outOfMemoryError(new OutOfMemoryError()));
    }

    @Test
    public void httpMessageNotReadableException_Test(){
        message.setSubject("El formato de la solicitud no es soportado.");
        message.setDate(LocalDate.now());
        ResponseEntity<Message> response = new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);

        Assert.assertEquals(response, controller.httpMessageNotReadableException(new HttpMessageNotReadableException("")));
    }

    @Test
    public void methodArgumentTypeMismatchException_Test() throws NoSuchMethodException {
        //Se obtiene el método getStrings desde grammarController
        Method method = grammarController.getClass().getMethod("getStrings", Integer.class, Grammar.class);
        //Se obtiene el parámetro del método.
        MethodParameter methodParameter = new MethodParameter(method, 0);
        //Se crea la excepción, dándole como dato una cadena, 'tipoDato'.
        MethodArgumentTypeMismatchException exception = new MethodArgumentTypeMismatchException("tipoDato", null, "", methodParameter, new Throwable());

        message.setSubject("El tipo de dato de '"+ "tipoDato" +"' es inválido.");
        message.setDate(LocalDate.now());
        ResponseEntity<Message> response = new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        Assert.assertEquals(response, controller.methodArgumentTypeMismatchException(exception));
    }

    @Test
    public void badRequestException_Test(){
        message.setSubject("Mala solicitud.");
        message.setDate(LocalDate.now());
        ResponseEntity<Message> response = new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        Assert.assertEquals(response, controller.badRequestException(new BadRequestException("Mala solicitud.")));
    }

    @Test
    public void timeOutException_Test(){
        message.setSubject("La petición ha tardado demasiado tiempo.");
        message.setDate(LocalDate.now());
        ResponseEntity<Message> response = new ResponseEntity<>(message, HttpStatus.REQUEST_TIMEOUT);
        Assert.assertEquals(response, controller.timeOutException(new TimeOutException("La petición ha tardado demasiado tiempo.")));
    }

    @Test
    public void invalidBoundException_Test(){
        message.setSubject("El rango debe ser positivo");
        message.setDate(LocalDate.now());

        InvalidBoundException exception = new InvalidBoundException("El rango debe ser positivo");

        ResponseEntity<Message> response = new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        Assert.assertEquals(response, controller.invalidBoundException(exception));
    }

}
