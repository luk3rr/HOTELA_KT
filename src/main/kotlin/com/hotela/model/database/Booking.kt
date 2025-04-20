package com.hotela.model.database

import com.hotela.model.enum.BookingStatus
import java.time.LocalDateTime
import java.util.UUID

data class Booking(
    val id: UUID,
    val customerId: UUID,
    val hotelId: UUID,
    val roomId: UUID,
    val checkin: LocalDateTime,
    val checkout: LocalDateTime,
    val guests: Int,
    val status: BookingStatus = BookingStatus.CONFIRMED,
    val notes: String? = null,
)
