package com.hotela.stubs.db

import com.hotela.model.db.Booking
import com.hotela.model.enum.BookingStatus
import java.time.Instant
import java.util.UUID

object BookingStubs {
    fun create(
        id: UUID = UUID.fromString("080e6191-65d1-4f87-a1e6-509a2f73c4eb"),
        customerId: UUID = UUID.fromString("d167ab17-e593-46f5-b8ca-73a9c9726d3c"),
        hotelId: UUID = UUID.fromString("f8f0424b-76ff-4351-b7d0-37e66e79db17"),
        roomId: UUID = UUID.fromString("034a102b-8fc4-447d-9232-82013bf2f438"),
        bookedAt: Instant = Instant.parse("2023-10-01T14:00:00Z"),
        checkin: Instant = Instant.parse("2023-10-05T14:00:00Z"),
        checkout: Instant = Instant.parse("2023-10-07T14:00:00Z"),
        numberOfGuests: Int = 2,
        status: BookingStatus = BookingStatus.CONFIRMED,
        specialRequests: String? = "Test booking",
    ): Booking =
        Booking(
            id = id,
            customerId = customerId,
            hotelId = hotelId,
            roomId = roomId,
            bookedAt = bookedAt,
            checkin = checkin,
            checkout = checkout,
            numberOfGuests = numberOfGuests,
            status = status,
            specialRequests = specialRequests,
        )
}
