package com.hotela.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class RestExceptionHandler {
    private static final Map<Class<? extends HotelaException>, HttpStatus> STATUS_MAP = Map.ofEntries(
            Map.entry(HotelaException.CustomerNotFoundException.class, HttpStatus.NOT_FOUND),
            Map.entry(HotelaException.PartnerNotFoundException.class, HttpStatus.NOT_FOUND),
            Map.entry(HotelaException.HotelNotFoundException.class, HttpStatus.NOT_FOUND),
            Map.entry(HotelaException.RoomNotFoundException.class, HttpStatus.NOT_FOUND),
            Map.entry(HotelaException.BookingNotFoundException.class, HttpStatus.NOT_FOUND),
            Map.entry(HotelaException.PaymentNotFoundException.class, HttpStatus.NOT_FOUND),
            Map.entry(HotelaException.ReviewNotFoundException.class, HttpStatus.NOT_FOUND),
            Map.entry(HotelaException.ExampleNotFoundException.class, HttpStatus.NOT_FOUND),
            Map.entry(HotelaException.InvalidCredentialsException.class, HttpStatus.UNAUTHORIZED),
            Map.entry(HotelaException.InvalidOrExpiredTokenException.class, HttpStatus.UNAUTHORIZED),
            Map.entry(HotelaException.EmailAlreadyRegisteredException.class, HttpStatus.CONFLICT),
            Map.entry(HotelaException.BookingAlreadyCancelledException.class, HttpStatus.CONFLICT),
            Map.entry(HotelaException.InvalidDataException.class, HttpStatus.BAD_REQUEST)
    );

    @ExceptionHandler(HotelaException.class)
    public ResponseEntity<ErrorResponse> handleHotelaException(HotelaException e) {
        HttpStatus status = STATUS_MAP.getOrDefault(e.getClass(), HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(new ErrorResponse(e.getCode(), e.getMessage()), status);
    }
}
