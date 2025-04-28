package com.hotela.stubs.dto.request

import com.hotela.model.dto.request.CreateRoomRequest
import com.hotela.model.enum.RoomStatus
import java.math.BigDecimal
import java.util.UUID

object CreateRoomRequestStubs {
    fun create(
        hotelId: UUID = UUID.fromString("14a1dac5-f928-4225-ac89-1519f40cce0f"),
        number: String = "601",
        floor: Int = 6,
    ): CreateRoomRequest =
        CreateRoomRequest(
            hotelId = hotelId,
            number = number,
            floor = floor,
            type = "Deluxe",
            price = BigDecimal(550.00),
            capacity = 2,
            status = RoomStatus.AVAILABLE,
            description = "A luxurious room with a sea view",
        )
}
