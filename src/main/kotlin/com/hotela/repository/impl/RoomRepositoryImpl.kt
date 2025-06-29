package com.hotela.repository.impl

import com.hotela.model.db.Room
import com.hotela.model.enum.RoomStatus
import com.hotela.repository.RoomRepository
import io.r2dbc.spi.Row
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.awaitSingleOrNull
import org.springframework.r2dbc.core.bind
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.util.UUID

@Component
class RoomRepositoryImpl(
    private val databaseClient: DatabaseClient,
) : RoomRepository {
    override suspend fun findById(id: UUID): Room? =
        databaseClient
            .sql(FIND_BY_ID)
            .bind("id", id)
            .map { row, _ ->
                mapper(row)
            }.awaitSingleOrNull()

    override suspend fun findByHotelId(hotelId: UUID): List<Room> =
        databaseClient
            .sql(FIND_BY_HOTEL_ID)
            .bind("hotelId", hotelId)
            .map { row, _ ->
                mapper(row)
            }.all()
            .collectList()
            .awaitSingleOrNull() ?: emptyList()

    override suspend fun create(room: Room): Room =
        databaseClient
            .sql(SAVE)
            .bind("id", room.id)
            .bind("hotelId", room.hotelId)
            .bind("roomTypeId", room.roomTypeId)
            .bind("roomCode", room.roomCode)
            .bind("floor", room.floor)
            .bind("pricePerNight", room.pricePerNight)
            .bind("capacity", room.capacity)
            .bind("status", room.status)
            .bind("description", room.description)
            .map { row, _ ->
                mapper(row)
            }.awaitSingleOrNull()!!

    override suspend fun update(room: Room): Room =
        databaseClient
            .sql(UPDATE)
            .bind("id", room.id)
            .bind("roomTypeId", room.roomTypeId)
            .bind("roomCode", room.roomCode)
            .bind("floor", room.floor)
            .bind("pricePerNight", room.pricePerNight)
            .bind("capacity", room.capacity)
            .bind("status", room.status)
            .bind("description", room.description)
            .map { row, _ ->
                mapper(row)
            }.awaitSingleOrNull()!!

    private fun mapper(row: Row): Room =
        Room(
            id = row.get("id", UUID::class.java)!!,
            hotelId = row.get("hotel_id", UUID::class.java)!!,
            roomTypeId = row.get("room_type_id", UUID::class.java)!!,
            roomCode = row.get("room_code", String::class.java)!!,
            floor = row.get("floor", Int::class.java)!!,
            pricePerNight = row.get("price_per_night", BigDecimal::class.java)!!,
            capacity = row.get("capacity", Int::class.java)!!,
            status = row.get("status", RoomStatus::class.java)!!,
            description = row.get("description", String::class.java),
        )

    companion object {
        private const val FIND_BY_ID = """
            SELECT * FROM room WHERE id = :id
        """

        private const val FIND_BY_HOTEL_ID = """
           SELECT * FROM room WHERE hotel_id = :hotelId 
        """

        private const val SAVE = """
            INSERT INTO room (id, hotel_id, room_type_id, room_code, floor, price_per_night, capacity, status, description)
            VALUES (:id, :hotelId, :roomTypeId, :roomCode, :floor, :pricePerNight, :capacity, :status, :description)
            RETURNING *
        """

        private const val UPDATE = """
            UPDATE room 
            SET room_type_id = :roomTypeId,
                room_code = :roomCode,
                floor = :floor,
                price_per_night = :pricePerNight,
                capacity = :capacity,
                status = :status,
                description = :description
            WHERE id = :id
            RETURNING *
        """
    }
}
