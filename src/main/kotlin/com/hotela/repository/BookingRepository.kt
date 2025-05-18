package com.hotela.repository

import com.hotela.model.db.Booking
import java.util.UUID

interface BookingRepository {
    suspend fun findById(id: UUID): Booking?

    suspend fun findByRoomId(roomId: UUID): List<Booking>

    suspend fun findByHotelId(hotelId: UUID): List<Booking>

    suspend fun findCheckedInBookingsByHotelId(hotelId: UUID): List<Booking>

    suspend fun findRunningBookingsByHotelId(hotelId: UUID): List<Booking>

    suspend fun findFinishedBookingsByHotelId(hotelId: UUID): List<Booking>

    suspend fun create(booking: Booking): Booking

    suspend fun update(booking: Booking): Booking
}
