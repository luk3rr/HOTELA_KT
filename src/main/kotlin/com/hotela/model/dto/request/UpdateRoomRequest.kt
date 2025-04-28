package com.hotela.model.dto.request

import com.hotela.model.enum.RoomStatus
import java.math.BigDecimal

data class UpdateRoomRequest(
    val number: String?,
    val floor: Int?,
    val type: String?,
    val price: BigDecimal?,
    val capacity: Int?,
    val status: RoomStatus?,
    val description: String?,
)
