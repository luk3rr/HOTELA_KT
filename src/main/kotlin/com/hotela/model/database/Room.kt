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
) {
    init {
        require(number.isNotBlank()) { "Room number cannot be blank" }
        require(type.isNotBlank()) { "Room type cannot be blank" }
        require(price > BigDecimal.ZERO) { "Price must be greater than zero" }
        require(capacity > 0) { "Capacity must be greater than zero" }
        require(floor >= 0) { "Floor cannot be negative" }
    }
}
