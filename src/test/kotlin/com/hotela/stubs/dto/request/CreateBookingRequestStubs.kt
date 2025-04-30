package com.hotela.stubs.dto.request

import com.hotela.model.dto.request.CreateBookingRequest
import com.hotela.model.enum.BookingStatus
import java.time.LocalDateTime
import java.util.UUID

object CreateBookingRequestStubs {
    fun create(): CreateBookingRequest =
        CreateBookingRequest(
            customerId = UUID.fromString("a0bbb974-7284-47b9-b4a8-0db84061b9a0"),
            hotelId = UUID.fromString("3eb23f17-98d4-41b9-a890-de8aa36013fd"),
            roomId = UUID.fromString("4aeaf41f-7aaa-4bbd-8c9b-2624752b6c4b"),
            checkin = LocalDateTime.now(),
            checkout = LocalDateTime.now().plusDays(2),
            guests = 2,
            status = BookingStatus.CONFIRMED,
            notes = "This is a test note",
        )
}
