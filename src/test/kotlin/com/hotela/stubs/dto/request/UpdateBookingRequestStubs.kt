package com.hotela.stubs.dto.request

import com.hotela.model.dto.request.UpdateBookingRequest
import com.hotela.model.enum.BookingStatus
import java.time.Instant
import java.util.UUID

object UpdateBookingRequestStubs {
    fun create(
        roomId: UUID? = UUID.fromString("4aeaf41f-7aaa-4bbd-8c9b-2624752b6c4b"),
        checkin: Instant? = Instant.parse("2025-01-02T10:00:00Z"),
        checkout: Instant? = Instant.parse("2025-01-04T10:00:00Z"),
        numberOfGuests: Int? = 1,
        status: BookingStatus? = BookingStatus.CONFIRMED,
        specialRequests: String? = "This is a test note",
    ): UpdateBookingRequest =
        UpdateBookingRequest(
            roomId = roomId,
            checkin = checkin,
            checkout = checkout,
            numberOfGuests = numberOfGuests,
            status = status,
            specialRequests = specialRequests,
        )
}
