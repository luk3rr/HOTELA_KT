package com.hotela.repository

import com.hotela.model.db.Review
import java.util.UUID

interface ReviewRepository {
    suspend fun findById(id: UUID): Review?

    suspend fun findByHotelId(hotelId: UUID): List<Review>

    suspend fun findByCustomerId(customerId: UUID): List<Review>

    suspend fun findByBookingId(bookingId: UUID): Review?

    suspend fun create(review: Review): Review

    suspend fun update(review: Review): Review
}
