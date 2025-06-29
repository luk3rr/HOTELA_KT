package com.hotela.model.db

import com.hotela.model.enum.RoomStatus
import java.math.BigDecimal
import java.util.UUID

data class Room(
    val id: UUID,
    val hotelId: UUID,
    val roomTypeId: UUID,
    val roomCode: String,
    val floor: Int,
    val pricePerNight: BigDecimal,
    val capacity: Int,
    val status: RoomStatus,
    val description: String? = null,
) {
    companion object {
        const val MINIMUM_CAPACITY = 1
        val MINIMUM_PRICE: BigDecimal = BigDecimal.ZERO
        const val MAX_DESCRIPTION_LENGTH = 250
    }

    init {
        require(roomCode.isNotBlank()) { "Room identifier cannot be blank" }
        require(pricePerNight >= MINIMUM_PRICE) { "Price must be greater or equal to $MINIMUM_PRICE" }
        require(capacity >= MINIMUM_CAPACITY) { "Capacity must be greater or equal to $MINIMUM_CAPACITY" }
        description?.let {
            require(it.length <= MAX_DESCRIPTION_LENGTH) {
                "Room description cannot exceed $MAX_DESCRIPTION_LENGTH characters"
            }
        }
    }
}
