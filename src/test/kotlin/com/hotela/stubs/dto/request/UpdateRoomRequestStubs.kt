package com.hotela.stubs.dto.request

import com.hotela.model.dto.request.UpdateRoomRequest
import com.hotela.model.enum.RoomStatus
import java.math.BigDecimal
import java.util.UUID

object UpdateRoomRequestStubs {
    fun create(
        roomTypeId: UUID? = UUID.fromString("decb09b1-bd71-4976-8292-f95f367ef5c7"),
        roomCode: String? = "room-id-501",
        floor: Int? = 5,
        pricePerNight: BigDecimal? = BigDecimal("900.00"),
        capacity: Int? = 3,
        status: RoomStatus? = RoomStatus.BOOKED,
        description: String? = "A spacious suite with a king-size bed and a balcony",
    ): UpdateRoomRequest =
        UpdateRoomRequest(
            roomTypeId = roomTypeId,
            number = roomCode,
            floor = floor,
            pricePerNight = pricePerNight,
            capacity = capacity,
            status = status,
            description = description,
        )
}
