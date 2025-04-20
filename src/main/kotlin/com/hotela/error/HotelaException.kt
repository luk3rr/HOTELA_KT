package com.hotela.error

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.UUID

sealed class HotelaException(
    val code: String,
    override val message: String,
) : RuntimeException(message) {
    // 0xx - Example
    @ResponseStatus(HttpStatus.NOT_FOUND)
    class ExampleNotFoundException(
        id: UUID,
    ) : HotelaException("000", "Example with id $id not found")

    @ResponseStatus(HttpStatus.CONFLICT)
    class ExampleAlreadyExistsException(
        id: UUID,
    ) : HotelaException("001", "Example with id $id already exists")

    // 1xx - Authentication
    @ResponseStatus(HttpStatus.CONFLICT)
    class EmailAlreadyRegisteredException : HotelaException("100", "Email already registered")

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    class InvalidCredentialsException : HotelaException("101", "Invalid credentials")

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    class InvalidOrExpiredTokenException : HotelaException("102", "Token is invalid or expired")

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    class AccessDeniedException : HotelaException("103", "Access denied")

    // 2xx - Customer
    @ResponseStatus(HttpStatus.NOT_FOUND)
    class CustomerNotFoundException(
        id: UUID,
    ) : HotelaException("200", "Customer with id $id not found")

    // 3xx - Partner
    @ResponseStatus(HttpStatus.NOT_FOUND)
    class PartnerNotFoundException(
        id: UUID,
    ) : HotelaException("300", "Partner with id $id not found")

    // 4xx - Hotel
    @ResponseStatus(HttpStatus.NOT_FOUND)
    class HotelNotFoundException(
        id: UUID,
    ) : HotelaException("400", "Hotel with id $id not found")

    // 5xx - Room
    @ResponseStatus(HttpStatus.NOT_FOUND)
    class RoomNotFoundException(
        id: UUID,
    ) : HotelaException("500", "Room with id $id not found")

    // 6xx - Booking
    @ResponseStatus(HttpStatus.NOT_FOUND)
    class BookingNotFoundException(
        id: UUID,
    ) : HotelaException("600", "Booking with id $id not found")

    @ResponseStatus(HttpStatus.CONFLICT)
    class BookingAlreadyCancelledException(
        id: UUID,
    ) : HotelaException("601", "Booking with id $id already cancelled")

    // 7xx - Payment
    @ResponseStatus(HttpStatus.NOT_FOUND)
    class PaymentNotFoundException(
        id: UUID,
    ) : HotelaException("700", "Payment with id $id not found")

    // 8xx - Review
    @ResponseStatus(HttpStatus.NOT_FOUND)
    class ReviewNotFoundException(
        id: UUID,
    ) : HotelaException("800", "Review with id $id not found")

    // 9xx - General
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    class InvalidDataException : HotelaException("900", "Invalid data provided")
}
