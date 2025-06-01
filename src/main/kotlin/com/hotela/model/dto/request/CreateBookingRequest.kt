package com.hotela.model.dto.request

import java.time.Instant
import java.util.UUID

data class CreateBookingRequest(
    val customerId: UUID,
    val hotelId: UUID,
    val roomId: UUID,
    val checkin: Instant,
    val checkout: Instant,
    val numberOfGuests: Int,
    val specialRequests: String? = null,
)
