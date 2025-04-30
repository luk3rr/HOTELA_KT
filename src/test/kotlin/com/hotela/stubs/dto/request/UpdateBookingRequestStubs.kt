package com.hotela.stubs.dto.request

import com.hotela.model.dto.request.UpdateBookingRequest
import com.hotela.model.enum.BookingStatus
import java.time.LocalDateTime
import java.util.UUID

object UpdateBookingRequestStubs {
    fun create(): UpdateBookingRequest =
        UpdateBookingRequest(
            roomId = UUID.fromString("4aeaf41f-7aaa-4bbd-8c9b-2624752b6c4b"),
            checkin = LocalDateTime.now().plusDays(1),
            checkout = LocalDateTime.now().plusDays(3),
            guests = 1,
            status = BookingStatus.CONFIRMED,
            notes = "This is a test note",
        )
}
