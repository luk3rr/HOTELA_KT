package com.hotela.repository

import com.hotela.model.db.Hotel
import java.util.UUID

interface HotelRepository {
    suspend fun findById(id: UUID): Hotel?

    suspend fun findByPartnerId(partnerId: UUID): List<Hotel>

    suspend fun create(hotel: Hotel): Hotel

    suspend fun update(hotel: Hotel): Hotel
}
