package com.hotela.model.dto.request

import com.hotela.model.enum.BookingStatus
import java.time.Instant
import java.util.UUID

data class UpdateBookingRequest(
    val roomId: UUID?,
    val checkin: Instant?,
    val checkout: Instant?,
    val numberOfGuests: Int?,
    val status: BookingStatus?,
    val specialRequests: String?,
)
