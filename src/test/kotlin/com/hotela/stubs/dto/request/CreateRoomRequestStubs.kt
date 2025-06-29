package com.hotela.stubs.dto.request

import com.hotela.model.dto.request.CreateRoomRequest
import com.hotela.model.enum.RoomStatus
import java.math.BigDecimal
import java.util.UUID

object CreateRoomRequestStubs {
    fun create(
        hotelId: UUID = UUID.fromString("14a1dac5-f928-4225-ac89-1519f40cce0f"),
        roomTypeId: UUID = UUID.fromString("f981f6b3-184e-419f-8671-dc856a91d23d"),
        roomCode: String = "601",
        floor: Int = 6,
        pricePerNight: BigDecimal = BigDecimal(550.00),
        capacity: Int = 2,
        status: RoomStatus = RoomStatus.AVAILABLE,
        description: String? = "A luxurious room with a sea view",
    ): CreateRoomRequest =
        CreateRoomRequest(
            hotelId = hotelId,
            roomTypeId = roomTypeId,
            number = roomCode,
            floor = floor,
            pricePerNight = pricePerNight,
            capacity = capacity,
            status = status,
            description = description,
        )
}
