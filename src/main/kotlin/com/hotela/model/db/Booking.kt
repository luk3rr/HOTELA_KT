package com.hotela.model.db

import com.hotela.model.enum.BookingStatus
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID

data class Booking(
    val id: UUID,
    val customerId: UUID,
    val hotelId: UUID,
    val roomId: UUID,
    val bookedAt: Instant = Instant.now(),
    val checkin: Instant,
    val checkout: Instant,
    val numberOfGuests: Int,
    val status: BookingStatus = BookingStatus.PENDING_CONFIRMATION,
    val specialRequests: String? = null,
) {
    companion object {
        const val MINIMUM_GUESTS = 1
        const val MINIMUM_NIGHTS = 1

        val RUNNING_BOOKINGS_STATUS =
            setOf(
                BookingStatus.PENDING_CONFIRMATION,
                BookingStatus.CONFIRMED,
                BookingStatus.CHECKED_IN,
            )

        val FINISHED_BOOKINGS_STATUS =
            setOf(
                BookingStatus.CANCELLED_BY_CUSTOMER,
                BookingStatus.CANCELLED_BY_HOTEL,
                BookingStatus.CHECKED_OUT,
                BookingStatus.NO_SHOW,
            )
    }

    init {
        require(checkout.isAfter(checkin)) { "Checkout must be after checkin" }
        require(checkout.isAfter(checkin.plus(MINIMUM_NIGHTS.toLong(), ChronoUnit.DAYS))) {
            "Checkout must be at least $MINIMUM_NIGHTS nights after checkin"
        }
        require(numberOfGuests >= MINIMUM_GUESTS) { "Number of guests must be at least $MINIMUM_GUESTS" }
        specialRequests?.let { require(it.isNotBlank()) { "Special requests cannot be blank" } }
    }

    fun isInProgress(): Boolean = this.status in RUNNING_BOOKINGS_STATUS
}
