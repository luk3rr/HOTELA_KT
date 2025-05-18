package com.hotela.model.db

import java.util.UUID

data class RoomType(
    val id: UUID,
    val name: String,
    val description: String? = null,
) {
    init {
        require(name.isNotBlank()) { "Room type name cannot be blank" }
    }
}
