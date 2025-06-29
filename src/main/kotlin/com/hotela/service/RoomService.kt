package com.hotela.service

import com.hotela.error.HotelaException
import com.hotela.model.db.Room
import com.hotela.model.dto.request.CreateRoomRequest
import com.hotela.model.dto.request.UpdateRoomRequest
import com.hotela.repository.RoomRepository
import com.hotela.util.getUserId
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class RoomService(
    private val roomRepository: RoomRepository,
    private val hotelService: HotelService,
) {
    suspend fun findById(id: UUID) = roomRepository.findById(id)

    suspend fun findByHotelId(hotelId: UUID) = roomRepository.findByHotelId(hotelId)

    suspend fun createRoom(
        payload: CreateRoomRequest,
        token: JwtAuthenticationToken,
    ): Room {
        validateRequesterPermissions(token, payload.hotelId)

        val room =
            Room(
                id = UUID.randomUUID(),
                hotelId = payload.hotelId,
                roomTypeId = payload.roomTypeId,
                roomCode = payload.number,
                floor = payload.floor,
                pricePerNight = payload.pricePerNight,
                capacity = payload.capacity,
                status = payload.status,
                description = payload.description,
            )

        validateRoom(room)

        return roomRepository.create(room)
    }

    suspend fun updateRoom(
        id: UUID,
        payload: UpdateRoomRequest,
        token: JwtAuthenticationToken,
    ): Room {
        val existingRoom =
            roomRepository.findById(id)
                ?: throw HotelaException.RoomNotFoundException(id)

        validateRequesterPermissions(token, existingRoom.hotelId)

        val room =
            existingRoom.copy(
                roomTypeId = payload.roomTypeId ?: existingRoom.roomTypeId,
                roomCode = payload.number ?: existingRoom.roomCode,
                floor = payload.floor ?: existingRoom.floor,
                pricePerNight = payload.pricePerNight ?: existingRoom.pricePerNight,
                capacity = payload.capacity ?: existingRoom.capacity,
                status = payload.status ?: existingRoom.status,
                description = payload.description ?: existingRoom.description,
            )

        validateRoom(room)

        return roomRepository.update(room)
    }

    private suspend fun validateRequesterPermissions(
        token: JwtAuthenticationToken,
        hotelId: UUID,
    ) {
        val hotel =
            hotelService.findById(hotelId)
                ?: throw HotelaException.HotelNotFoundException(hotelId)

        val partnerUserId = token.getUserId()

        if (hotel.partnerId != partnerUserId) {
            throw HotelaException.InvalidCredentialsException()
        }
    }

    private suspend fun validateRoom(room: Room) {
        if (!isRoomNumberAvailable(room)) {
            throw HotelaException.RoomAlreadyExistsException(
                id = room.id,
                msg = "Room with number ${room.roomCode} already exists",
            )
        }
    }

    private suspend fun isRoomNumberAvailable(room: Room): Boolean {
        val rooms = roomRepository.findByHotelId(room.hotelId)

        return rooms.none {
            val isSameRoom = it.id == room.id
            val isSameNumber = it.roomCode == room.roomCode

            !isSameRoom && isSameNumber
        }
    }
}
