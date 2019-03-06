package com.plugli.controller.error;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class RestResponseExceptionHandler {

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    private ResponseEntity<Object> handleMessageNotReadableException(HttpMessageNotReadableException e) {
        if (e.getCause() instanceof InvalidFormatException) {
            InvalidFormatException invalidFormatException = (InvalidFormatException) e.getCause();
            return new ResponseEntity<>(new ErrorResponse("The value: " + invalidFormatException.getValue() + " is not valid for the supplied field", BAD_REQUEST), BAD_REQUEST);
        }
        return new ResponseEntity<>( new ErrorResponse("The supplied request is not valid", BAD_REQUEST), BAD_REQUEST);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    private ResponseEntity<Object> handleArgumentNotValidException(MethodArgumentNotValidException e) {
        String fieldMessage = e.getBindingResult().getFieldErrors().stream().map(f -> f.getField() + " " + f.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return new ResponseEntity<>( new ErrorResponse(fieldMessage, BAD_REQUEST), BAD_REQUEST);
    }
}
