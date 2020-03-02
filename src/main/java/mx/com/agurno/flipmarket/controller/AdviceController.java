package mx.com.agurno.flipmarket.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class AdviceController {

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handle(Exception ex) {
        return "redirect:/DomainVerification.html";
    }
}