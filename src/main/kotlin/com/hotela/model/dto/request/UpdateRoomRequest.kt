package com.hotela.model.dto.request

import com.hotela.model.enum.RoomStatus
import java.math.BigDecimal

data class UpdateRoomRequest(
    val roomTypeId: String?,
    val roomId: String?,
    val floor: Int?,
    val pricePerNight: BigDecimal?,
    val capacity: Int?,
    val status: RoomStatus?,
    val description: String?,
)
