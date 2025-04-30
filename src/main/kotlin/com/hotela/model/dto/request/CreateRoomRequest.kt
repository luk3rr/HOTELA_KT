package com.hotela.model.dto.request

import com.hotela.model.enum.RoomStatus
import java.math.BigDecimal
import java.util.UUID

data class CreateRoomRequest(
    val hotelId: UUID,
    val number: String,
    val floor: Int,
    val type: String,
    val price: BigDecimal,
    val capacity: Int,
    val status: RoomStatus,
    val description: String? = null,
)
