package com.hotela.stubs.database

import com.hotela.model.database.Room
import com.hotela.model.enum.RoomStatus
import java.math.BigDecimal
import java.util.UUID

object RoomStubs {
    fun create(): Room =
        Room(
            id = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
            hotelId = UUID.fromString("d6382730-8f96-49ec-8b74-43c5489c8647"),
            number = "101",
            floor = 1,
            type = "Deluxe",
            price = BigDecimal("150.00"),
            capacity = 2,
            status = RoomStatus.AVAILABLE,
            description = "A spacious deluxe room with a great view.",
        )
}
