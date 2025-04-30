package com.hotela.model.database

import com.hotela.model.enum.BookingStatus
import java.time.LocalDateTime
import java.util.UUID

data class Booking(
    val id: UUID,
    val customerId: UUID,
    val hotelId: UUID,
    val roomId: UUID,
    val bookedAt: LocalDateTime = LocalDateTime.now(),
    val checkin: LocalDateTime,
    val checkout: LocalDateTime,
    val guests: Int,
    val status: BookingStatus = BookingStatus.CONFIRMED,
    val notes: String? = null,
) {
    companion object {
        const val MINIMUM_GUESTS = 1
        const val MINIMUM_NIGHTS = 1
    }

    init {
        require(checkout.isAfter(checkin)) { "Checkout must be after checkin" }
        require(checkout.isAfter(checkin.plusDays(1))) {
            "Checkout must be at least $MINIMUM_NIGHTS nights after checkin"
        }
        require(guests >= MINIMUM_GUESTS) { "Number of guests must be at least $MINIMUM_GUESTS" }
        notes?.let { require(it.isNotBlank()) { "Booking notes cannot be blank" } }
    }
}
