package com.hotela.error;

import java.util.UUID;
import lombok.Getter;

@Getter
public sealed class HotelaException extends RuntimeException {
    private final String code;

    public HotelaException(String code, String message) {
        super(message);
        this.code = code;
    }

    public static final class ExampleNotFoundException extends HotelaException {
        public ExampleNotFoundException(UUID id) {
            super("000", "Example with id " + id + " not found");
        }
    }

    // 1xx - Authentication errors
    public static final class EmailAlreadyRegisteredException extends HotelaException {
        public EmailAlreadyRegisteredException() {
            super("100", "Email already registered");
        }
    }

    public static final class InvalidCredentialsException extends HotelaException {
        public InvalidCredentialsException() {
            super("101", "Invalid credentials");
        }
    }

    public static final class InvalidOrExpiredTokenException extends HotelaException {
        public InvalidOrExpiredTokenException() {
            super("102", "Token is invalid or expired");
        }
    }

    // 2xx - Customer errors
    public static final class CustomerNotFoundException extends HotelaException {
        public CustomerNotFoundException(UUID id) {
            super("200", "Customer with id " + id + " not found");
        }
    }

    // 3xx - Partner errors
    public static final class PartnerNotFoundException extends HotelaException {
        public PartnerNotFoundException(UUID id) {
            super("300", "Partner with id " + id + " not found");
        }
    }

    // 4xx - Hotel errors
    public static final class HotelNotFoundException extends HotelaException {
        public HotelNotFoundException(UUID id) {
            super("400", "Hotel with id " + id + " not found");
        }
    }

    // 5xx - Room errors
    public static final class RoomNotFoundException extends HotelaException {
        public RoomNotFoundException(UUID id) {
            super("500", "Room with id " + id + " not found");
        }
    }

    // 6xx - Booking errors
    public static final class BookingNotFoundException extends HotelaException {
        public BookingNotFoundException(UUID id) {
            super("600", "Booking with id " + id + " not found");
        }
    }

    public static final class BookingAlreadyCancelledException extends HotelaException {
        public BookingAlreadyCancelledException(UUID id) {
            super("601", "Booking with id " + id + " already cancelled");
        }
    }

    // 7xx - Payment errors
    public static final class PaymentNotFoundException extends HotelaException {
        public PaymentNotFoundException(UUID id) {
            super("700", "Payment with id " + id + " not found");
        }
    }

    // 8xx - Review errors
    public static final class ReviewNotFoundException extends HotelaException {
        public ReviewNotFoundException(UUID id) {
            super("800", "Review with id " + id + " not found");
        }
    }

    // 9xx - General errors
    public static final class InvalidDataException extends HotelaException {
        public InvalidDataException() {
            super("900", "Invalid data provided");
        }
    }
}
