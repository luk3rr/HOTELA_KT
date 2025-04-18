package com.hotela.error;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(HotelaException.class)
    public ResponseEntity<ErrorResponse> handleHotelaException(HotelaException e) {
        ResponseStatus responseStatus =
                AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class);

        HttpStatus status =
                responseStatus != null ? responseStatus.code() : HttpStatus.INTERNAL_SERVER_ERROR;

        return new ResponseEntity<>(new ErrorResponse(e.getCode(), e.getMessage()), status);
    }
}
