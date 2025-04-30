package com.hotela.error

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.UUID

sealed class HotelaException(
    val code: String,
    override val message: String,
) : RuntimeException(message) {
    companion object {
        // 1xx - Authentication
        const val EMAIL_ALREADY_REGISTERED = "100"
        const val INVALID_CREDENTIALS = "101"
        const val INVALID_OR_EXPIRED_TOKEN = "102"
        const val ACCESS_DENIED = "103"
        const val CUSTOMER_AUTH_ALREADY_EXISTS = "104"
        const val PARTNER_AUTH_ALREADY_EXISTS = "105"
        const val CUSTOMER_AUTH_NOT_FOUND = "106"
        const val PARTNER_AUTH_NOT_FOUND = "107"

        // 2xx - Customer
        const val CUSTOMER_NOT_FOUND = "200"
        const val CUSTOMER_ALREADY_EXISTS = "201"

        // 3xx - Partner
        const val PARTNER_NOT_FOUND = "300"
        const val PARTNER_ALREADY_EXISTS = "301"

        // 4xx - Hotel
        const val HOTEL_NOT_FOUND = "400"
        const val HOTEL_ALREADY_EXISTS = "401"

        // 5xx - Room
        const val ROOM_NOT_FOUND = "500"
        const val ROOM_ALREADY_EXISTS = "501"

        // 6xx - Booking
        const val BOOKING_NOT_FOUND = "600"
        const val BOOKING_ALREADY_CANCELLED = "601"

        // 7xx - Payment
        const val PAYMENT_NOT_FOUND = "700"

        // 8xx - Review
        const val REVIEW_NOT_FOUND = "800"

        // 9xx - General
        const val INVALID_DATA = "900"
        const val FIELD_IS_REQUIRED_BUT_WAS_NULL_OR_EMPTY = "901"
    }

    // 1xx - Authentication
    @ResponseStatus(HttpStatus.CONFLICT)
    class EmailAlreadyRegisteredException : HotelaException(EMAIL_ALREADY_REGISTERED, "Email already registered")

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    class InvalidCredentialsException : HotelaException(INVALID_CREDENTIALS, "Invalid credentials")

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    class InvalidOrExpiredTokenException : HotelaException(INVALID_OR_EXPIRED_TOKEN, "Token is invalid or expired")

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    class AccessDeniedException : HotelaException(ACCESS_DENIED, "Access denied")

    @ResponseStatus(HttpStatus.CONFLICT)
    class CustomerAuthAlreadyExistsException(
        id: UUID,
    ) : HotelaException(CUSTOMER_AUTH_ALREADY_EXISTS, "Customer auth with id $id already exists")

    @ResponseStatus(HttpStatus.CONFLICT)
    class PartnerAuthAlreadyExistsException(
        id: UUID,
    ) : HotelaException(PARTNER_AUTH_ALREADY_EXISTS, "Partner auth with id $id already exists")

    @ResponseStatus(HttpStatus.NOT_FOUND)
    class CustomerAuthNotFoundException(
        id: UUID,
    ) : HotelaException(CUSTOMER_AUTH_NOT_FOUND, "Customer auth with id $id not found")

    @ResponseStatus(HttpStatus.NOT_FOUND)
    class PartnerAuthNotFoundException(
        id: UUID,
    ) : HotelaException(PARTNER_AUTH_NOT_FOUND, "Partner auth with id $id not found")

    // 2xx - Customer
    @ResponseStatus(HttpStatus.NOT_FOUND)
    class CustomerNotFoundException(
        id: UUID,
    ) : HotelaException(CUSTOMER_NOT_FOUND, "Customer with id $id not found")

    @ResponseStatus(HttpStatus.CONFLICT)
    class CustomerAlreadyExistsException(
        id: UUID,
    ) : HotelaException(CUSTOMER_ALREADY_EXISTS, "Customer with id $id already exists")

    // 3xx - Partner
    @ResponseStatus(HttpStatus.NOT_FOUND)
    class PartnerNotFoundException(
        id: UUID,
    ) : HotelaException(PARTNER_NOT_FOUND, "Partner with id $id not found")

    @ResponseStatus(HttpStatus.CONFLICT)
    class PartnerAlreadyExistsException(
        id: UUID,
    ) : HotelaException(PARTNER_ALREADY_EXISTS, "Partner with id $id already exists")

    // 4xx - Hotel
    @ResponseStatus(HttpStatus.NOT_FOUND)
    class HotelNotFoundException(
        id: UUID,
    ) : HotelaException(HOTEL_NOT_FOUND, "Hotel with id $id not found")

    @ResponseStatus(HttpStatus.CONFLICT)
    class HotelAlreadyExistsException(
        id: UUID,
    ) : HotelaException(HOTEL_ALREADY_EXISTS, "Hotel with id $id already exists")

    // 5xx - Room
    @ResponseStatus(HttpStatus.NOT_FOUND)
    class RoomNotFoundException(
        id: UUID,
    ) : HotelaException(ROOM_NOT_FOUND, "Room with id $id not found")

    @ResponseStatus(HttpStatus.CONFLICT)
    class RoomAlreadyExistsException(
        id: UUID,
        msg: String = "Room with id $id already exists",
    ) : HotelaException(ROOM_ALREADY_EXISTS, msg)

    // 6xx - Booking
    @ResponseStatus(HttpStatus.NOT_FOUND)
    class BookingNotFoundException(
        id: UUID,
    ) : HotelaException(BOOKING_NOT_FOUND, "Booking with id $id not found")

    @ResponseStatus(HttpStatus.CONFLICT)
    class BookingAlreadyCancelledException(
        id: UUID,
    ) : HotelaException(BOOKING_ALREADY_CANCELLED, "Booking with id $id already cancelled")

    // 7xx - Payment
    @ResponseStatus(HttpStatus.NOT_FOUND)
    class PaymentNotFoundException(
        id: UUID,
    ) : HotelaException(PAYMENT_NOT_FOUND, "Payment with id $id not found")

    // 8xx - Review
    @ResponseStatus(HttpStatus.NOT_FOUND)
    class ReviewNotFoundException(
        id: UUID,
    ) : HotelaException(REVIEW_NOT_FOUND, "Review with id $id not found")

    // 9xx - General
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    class InvalidDataException(
        msg: String = "Invalid data provided",
    ) : HotelaException(INVALID_DATA, msg)

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    class FieldIsRequiredButWasNullOrEmptyException(
        field: String,
    ) : HotelaException(FIELD_IS_REQUIRED_BUT_WAS_NULL_OR_EMPTY, "Field $field is required, but was null or empty")
}
