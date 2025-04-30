package com.hotela.model.dto.request

import com.hotela.model.enum.BookingStatus
import java.time.LocalDateTime
import java.util.UUID

data class UpdateBookingRequest(
    val roomId: UUID?,
    val checkin: LocalDateTime?,
    val checkout: LocalDateTime?,
    val guests: Int?,
    val status: BookingStatus?,
    val notes: String?,
)
