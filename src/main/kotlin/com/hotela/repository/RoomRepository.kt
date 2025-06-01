package com.hotela.repository

import com.hotela.model.db.Room
import java.util.UUID

interface RoomRepository {
    suspend fun findById(id: UUID): Room?

    suspend fun findByHotelId(hotelId: UUID): List<Room>

    suspend fun create(room: Room): Room

    suspend fun update(room: Room): Room
}
