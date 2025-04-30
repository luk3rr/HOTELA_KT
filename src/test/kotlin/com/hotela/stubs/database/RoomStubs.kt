package com.hotela.stubs.database

import com.hotela.model.database.Room
import com.hotela.model.enum.RoomStatus
import java.math.BigDecimal
import java.util.UUID

object RoomStubs {
    fun create(
        id: UUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
        hotelId: UUID = UUID.fromString("d6382730-8f96-49ec-8b74-43c5489c8647"),
    ): Room =
        Room(
            id = id,
            hotelId = hotelId,
            number = "101",
            floor = 1,
            type = "Deluxe",
            price = BigDecimal("150.00"),
            capacity = 2,
            status = RoomStatus.AVAILABLE,
            description = "A spacious deluxe room with a great view.",
        )

    fun createAnother(hotelId: UUID = UUID.fromString("d6382730-8f96-49ec-8b74-43c5489c8647")): Room =
        Room(
            id = UUID.fromString("63c30ec6-abab-4a51-a3bc-a53d325dfcaa"),
            hotelId = hotelId,
            number = "102",
            floor = 1,
            type = "Standard",
            price = BigDecimal("200.00"),
            capacity = 2,
            status = RoomStatus.BOOKED,
            description = "A cozy standard room.",
        )
}
