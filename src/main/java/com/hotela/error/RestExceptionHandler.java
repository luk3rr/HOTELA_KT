package com.hotela.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(HotelaException.class)
    public ResponseEntity<ErrorResponse> handleHotelaException(HotelaException e) {
        HttpStatus status = switch (e) {
            case HotelaException.ExampleNotFoundException __ -> HttpStatus.NOT_FOUND;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };

        return new ResponseEntity<>(new ErrorResponse(e.getCode(), e.getMessage()), status);
    }
}
