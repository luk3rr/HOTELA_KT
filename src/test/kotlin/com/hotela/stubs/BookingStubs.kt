package com.hotela.stubs

import com.hotela.model.database.Booking
import com.hotela.model.enum.BookingStatus
import java.time.LocalDateTime
import java.util.UUID

object BookingStubs {
    fun create(): Booking {
        return Booking(
            id = UUID.fromString("080e6191-65d1-4f87-a1e6-509a2f73c4eb"),
            customerId = UUID.fromString("d167ab17-e593-46f5-b8ca-73a9c9726d3c"),
            hotelId = UUID.fromString("f8f0424b-76ff-4351-b7d0-37e66e79db17"),
            roomId = UUID.fromString("034a102b-8fc4-447d-9232-82013bf2f438"),
            checkin = LocalDateTime.of(2021, 10, 1, 14, 0),
            checkout = LocalDateTime.of(2021, 10, 3, 14, 0),
            guests = 2,
            status = BookingStatus.CONFIRMED,
            notes = "Test booking",
        )
    }
}