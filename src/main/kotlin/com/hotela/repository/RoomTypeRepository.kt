package com.hotela.repository

import com.hotela.model.db.RoomType
import java.util.UUID

interface RoomTypeRepository {
    suspend fun findById(id: UUID): RoomType?

    suspend fun findAll(): List<RoomType>
}
