package com.hotela.model.database

import com.hotela.model.enum.RoomStatus
import java.math.BigDecimal
import java.util.UUID

data class Room(
    val id: UUID,
    val hotelId: UUID,
    val number: String,
    val floor: Int,
    val type: String,
    val price: BigDecimal,
    val capacity: Int,
    val status: RoomStatus = RoomStatus.AVAILABLE,
    val description: String? = null,
)
