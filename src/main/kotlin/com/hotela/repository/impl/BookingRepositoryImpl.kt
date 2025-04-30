package com.hotela.repository.impl

import com.hotela.model.database.Booking
import com.hotela.model.enum.BookingStatus
import com.hotela.repository.BookingRepository
import io.r2dbc.spi.Row
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.awaitSingleOrNull
import org.springframework.r2dbc.core.bind
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.UUID

@Component
class BookingRepositoryImpl(
    private val databaseClient: DatabaseClient,
) : BookingRepository {
    override suspend fun findById(id: UUID): Booking? =
        databaseClient
            .sql(FIND_BY_ID)
            .bind("id", id)
            .map { row, _ ->
                mapper(row)
            }.awaitSingleOrNull()

    override suspend fun findByRoomId(roomId: UUID): List<Booking> =
        databaseClient
            .sql(FIND_BY_ROOM_ID)
            .bind("roomId", roomId)
            .map { row, _ ->
                mapper(row)
            }.all()
            .collectList()
            .awaitSingleOrNull() ?: emptyList()

    override suspend fun findByHotelId(hotelId: UUID): List<Booking> =
        databaseClient
            .sql(FIND_BY_HOTEL_ID)
            .bind("hotelId", hotelId)
            .map { row, _ ->
                mapper(row)
            }.all()
            .collectList()
            .awaitSingleOrNull() ?: emptyList()

    override suspend fun findInProgressBookingsByHotelId(hotelId: UUID): List<Booking> =
        databaseClient
            .sql(FIND_IN_PROGRESS_BOOKINGS_BY_HOTEL_ID)
            .bind("hotelId", hotelId)
            .map { row, _ ->
                mapper(row)
            }.all()
            .collectList()
            .awaitSingleOrNull() ?: emptyList()

    override suspend fun create(booking: Booking): Booking =
        databaseClient
            .sql(SAVE)
            .bind("id", booking.id)
            .bind("customerId", booking.customerId)
            .bind("hotelId", booking.hotelId)
            .bind("roomId", booking.roomId)
            .bind("checkin", booking.checkin)
            .bind("checkout", booking.checkout)
            .bind("guests", booking.guests)
            .bind("status", booking.status)
            .bind("notes", booking.notes)
            .map { row, _ ->
                mapper(row)
            }.awaitSingleOrNull()!!

    override suspend fun update(booking: Booking): Booking =
        databaseClient
            .sql(UPDATE)
            .bind("id", booking.id)
            .bind("customerId", booking.customerId)
            .bind("hotelId", booking.hotelId)
            .bind("roomId", booking.roomId)
            .bind("checkin", booking.checkin)
            .bind("checkout", booking.checkout)
            .bind("guests", booking.guests)
            .bind("status", booking.status)
            .bind("notes", booking.notes)
            .map { row, _ ->
                mapper(row)
            }.awaitSingleOrNull()!!

    private fun mapper(row: Row): Booking =
        Booking(
            id = row.get("id", UUID::class.java)!!,
            customerId = row.get("customer_id", UUID::class.java)!!,
            hotelId = row.get("hotel_id", UUID::class.java)!!,
            roomId = row.get("room_id", UUID::class.java)!!,
            checkin = row.get("checkin", LocalDateTime::class.java)!!,
            checkout = row.get("checkout", LocalDateTime::class.java)!!,
            guests = row.get("guests", Int::class.java)!!,
            status = row.get("status", BookingStatus::class.java)!!,
            notes = row.get("notes", String::class.java),
        )

    companion object {
        private const val FIND_BY_ID = """
            SELECT * FROM booking
            WHERE id = :id
        """

        private const val FIND_BY_ROOM_ID = """
            SELECT * FROM booking
            WHERE room_id = :roomId
        """

        private const val FIND_BY_HOTEL_ID = """
            SELECT * FROM booking
            WHERE hotel_id = :hotelId
        """

        private const val SAVE = """
            INSERT INTO booking (id, customer_id, hotel_id, room_id, checkin, checkout, guests, status, notes)
            VALUES (:id, :customerId, :hotelId, :roomId, :checkin, :checkout, :guests, :status, :notes)
            RETURNING *
        """

        private const val UPDATE = """
            UPDATE booking
            SET customer_id = :customerId, hotel_id = :hotelId, room_id = :roomId, checkin = :checkin, checkout = :checkout, guests = :guests, status = :status, notes = :notes
            WHERE id = :id
            RETURNING *
        """

        private const val FIND_IN_PROGRESS_BOOKINGS_BY_HOTEL_ID = """
            SELECT * FROM booking
            WHERE hotel_id = :hotelId AND status = 'IN_PROGRESS'
        """
    }
}
