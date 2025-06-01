package com.hotela.stubs.dto.request

import com.hotela.model.dto.request.CreateBookingRequest
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID

object CreateBookingRequestStubs {
    fun create(
        customerId: UUID = UUID.fromString("a0bbb974-7284-47b9-b4a8-0db84061b9a0"),
        hotelId: UUID = UUID.fromString("3eb23f17-98d4-41b9-a890-de8aa36013fd"),
        roomId: UUID = UUID.fromString("4aeaf41f-7aaa-4bbd-8c9b-2624752b6c4b"),
        checkin: Instant = Instant.now(),
        checkout: Instant = Instant.now().plus(1L, ChronoUnit.DAYS),
        numberOfGuests: Int = 2,
        specialRequests: String? = "This is a test note",
    ): CreateBookingRequest =
        CreateBookingRequest(
            customerId = customerId,
            hotelId = hotelId,
            roomId = roomId,
            checkin = checkin,
            checkout = checkout,
            numberOfGuests = numberOfGuests,
            specialRequests = specialRequests,
        )
}
