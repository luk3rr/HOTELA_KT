package com.hotela.model.db

import com.hotela.model.enum.BookingStatus
import java.time.Instant
import java.util.*

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
        const val MINIMUM_NIGHTS_IN_SECONDS = 60 * 60 * 24 * MINIMUM_NIGHTS
    }

    init {
        require(checkout.isAfter(checkin)) { "Checkout must be after checkin" }
        require(checkout.isAfter(checkin.plusSeconds(MINIMUM_NIGHTS_IN_SECONDS.toLong()))) {
            "Checkout must be at least $MINIMUM_NIGHTS nights after checkin"
        }
        require(numberOfGuests >= MINIMUM_GUESTS) { "Number of guests must be at least $MINIMUM_GUESTS" }
        specialRequests?.let { require(it.isNotBlank()) { "Special requests cannot be blank" } }
    }
}
