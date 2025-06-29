package com.hotela.stubs.db

import com.hotela.model.db.Room
import com.hotela.model.enum.RoomStatus
import java.math.BigDecimal
import java.util.UUID

object RoomStubs {
    fun create(
        id: UUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
        hotelId: UUID = UUID.fromString("d6382730-8f96-49ec-8b74-43c5489c8647"),
        roomTypeId: UUID = UUID.fromString("a1b2c3d4-e5f6-7890-abcd-ef1234567890"),
        roomCode: String = "101",
        floor: Int = 1,
        pricePerNight: BigDecimal = BigDecimal("150.00"),
        capacity: Int = 2,
        status: RoomStatus = RoomStatus.AVAILABLE,
        description: String? = "A spacious deluxe room with a great view.",
    ): Room =
        Room(
            id = id,
            hotelId = hotelId,
            roomTypeId = roomTypeId,
            roomCode = roomCode,
            floor = floor,
            pricePerNight = pricePerNight,
            capacity = capacity,
            status = status,
            description = description,
        )

    fun createAnother(
        id: UUID = UUID.fromString("63c30ec6-abab-4a51-a3bc-a53d325dfcaa"),
        hotelId: UUID = UUID.fromString("d6382730-8f96-49ec-8b74-43c5489c8647"),
        roomTypeId: UUID = UUID.fromString("b2c3d4e5-f678-90ab-cdef-1234567890ab"),
        roomCode: String = "102",
        floor: Int = 1,
        pricePerNight: BigDecimal = BigDecimal("200.00"),
        capacity: Int = 2,
        status: RoomStatus = RoomStatus.BOOKED,
        description: String? = "A cozy standard room.",
    ): Room =
        Room(
            id = id,
            hotelId = hotelId,
            roomTypeId = roomTypeId,
            roomCode = roomCode,
            floor = floor,
            pricePerNight = pricePerNight,
            capacity = capacity,
            status = status,
            description = description,
        )
}
