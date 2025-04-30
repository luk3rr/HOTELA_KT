package com.hotela.stubs.dto.request

import com.hotela.model.dto.request.UpdateRoomRequest
import com.hotela.model.enum.RoomStatus
import java.math.BigDecimal

object UpdateRoomRequestStubs {
    fun create(
        number: String = "501",
        floor: Int = 5,
    ): UpdateRoomRequest =
        UpdateRoomRequest(
            number = number,
            floor = floor,
            type = "Suite",
            price = BigDecimal(900.00),
            capacity = 3,
            status = RoomStatus.BOOKED,
            description = "A spacious suite with a king-size bed and a balcony",
        )
}
