package com.hotela.stubs.dto.request

import com.hotela.model.dto.request.UpdateRoomRequest
import com.hotela.model.enum.RoomStatus
import java.math.BigDecimal

object UpdateRoomRequestStubs {
    fun create(
        roomTypeId: String? = "suite-id-123",
        roomId: String? = "room-id-501",
        floor: Int? = 5,
        pricePerNight: BigDecimal? = BigDecimal("900.00"),
        capacity: Int? = 3,
        status: RoomStatus? = RoomStatus.BOOKED,
        description: String? = "A spacious suite with a king-size bed and a balcony",
    ): UpdateRoomRequest =
        UpdateRoomRequest(
            roomTypeId = roomTypeId,
            roomId = roomId,
            floor = floor,
            pricePerNight = pricePerNight,
            capacity = capacity,
            status = status,
            description = description
        )
}
