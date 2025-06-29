package com.hotela.repository.impl

import com.hotela.model.db.Booking
import com.hotela.model.enum.BookingStatus
import com.hotela.repository.BookingRepository
import io.r2dbc.spi.Row
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.awaitSingleOrNull
import org.springframework.r2dbc.core.bind
import org.springframework.stereotype.Component
import java.time.Instant
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

    override suspend fun findCheckedInBookingsByHotelId(hotelId: UUID): List<Booking> =
        databaseClient
            .sql(FIND_BOOKINGS_BY_HOTEL_ID_AND_STATUS)
            .bind("hotelId", hotelId)
            .bind("status", setOf(BookingStatus.CHECKED_IN))
            .map { row, _ ->
                mapper(row)
            }.all()
            .collectList()
            .awaitSingleOrNull() ?: emptyList()

    override suspend fun findRunningBookingsByHotelId(hotelId: UUID): List<Booking> =
        databaseClient
            .sql(FIND_BOOKINGS_BY_HOTEL_ID_AND_STATUS)
            .bind("hotelId", hotelId)
            .bind("status", Booking.RUNNING_BOOKINGS_STATUS)
            .map { row, _ ->
                mapper(row)
            }.all()
            .collectList()
            .awaitSingleOrNull() ?: emptyList()

    override suspend fun findFinishedBookingsByHotelId(hotelId: UUID): List<Booking> =
        databaseClient
            .sql(FIND_BOOKINGS_BY_HOTEL_ID_AND_STATUS)
            .bind("hotelId", hotelId)
            .bind("status", Booking.FINISHED_BOOKINGS_STATUS)
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
            .bind("bookedAt", booking.bookedAt)
            .bind("checkinDate", booking.checkin)
            .bind("checkoutDate", booking.checkout)
            .bind("numberOfGuests", booking.numberOfGuests)
            .bind("status", booking.status)
            .bind("specialRequests", booking.specialRequests)
            .map { row, _ ->
                mapper(row)
            }.awaitSingleOrNull()!!

    override suspend fun update(booking: Booking): Booking =
        databaseClient
            .sql(UPDATE)
            .bind("id", booking.id)
            .bind("roomId", booking.roomId)
            .bind("bookedAt", booking.bookedAt)
            .bind("checkinDate", booking.checkin)
            .bind("checkoutDate", booking.checkout)
            .bind("numberOfGuests", booking.numberOfGuests)
            .bind("status", booking.status)
            .bind("specialRequests", booking.specialRequests)
            .map { row, _ ->
                mapper(row)
            }.awaitSingleOrNull()!!

    private fun mapper(row: Row): Booking =
        Booking(
            id = row.get("id", UUID::class.java)!!,
            customerId = row.get("customer_id", UUID::class.java)!!,
            hotelId = row.get("hotel_id", UUID::class.java)!!,
            roomId = row.get("room_id", UUID::class.java)!!,
            bookedAt = row.get("booked_at", Instant::class.java)!!,
            checkin = row.get("checkin_date", Instant::class.java)!!,
            checkout = row.get("checkout_date", Instant::class.java)!!,
            numberOfGuests = row.get("number_of_guests", Int::class.java)!!,
            status = row.get("status", BookingStatus::class.java)!!,
            specialRequests = row.get("special_requests", String::class.java),
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
            INSERT INTO booking (id, customer_id, hotel_id, room_id, booked_at, checkin_date, checkout_date, number_of_guests, status, special_requests)
            VALUES (:id, :customerId, :hotelId, :roomId, :bookedAt, :checkinDate, :checkoutDate, :numberOfGuests, :status, :specialRequests)
            RETURNING *
        """

        private const val UPDATE = """
            UPDATE booking
            SET room_id = :roomId, booked_at = :bookedAt, checkin_date = :checkinDate, checkout_date = :checkoutDate, number_of_guests = :numberOfGuests, status = :status, special_requests = :specialRequests
            WHERE id = :id
            RETURNING *
        """

        private const val FIND_BOOKINGS_BY_HOTEL_ID_AND_STATUS = """
            SELECT * FROM booking
            WHERE hotel_id = :hotelId AND status IN (:status)
        """
    }
}
