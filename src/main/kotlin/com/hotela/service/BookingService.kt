package com.hotela.service

import com.hotela.error.HotelaException
import com.hotela.model.database.Booking
import com.hotela.model.database.Hotel
import com.hotela.model.database.Room
import com.hotela.model.dto.request.CreateBookingRequest
import com.hotela.model.dto.request.UpdateBookingRequest
import com.hotela.model.enum.BookingStatus
import com.hotela.repository.BookingRepository
import com.hotela.util.getUserId
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
class BookingService(
    private val bookingRepository: BookingRepository,
    private val hotelService: HotelService,
    private val roomService: RoomService,
) {
    suspend fun findById(id: UUID) = bookingRepository.findById(id)

    suspend fun findByHotelId(hotelId: UUID) = bookingRepository.findByHotelId(hotelId)

    suspend fun findInProgressBookingsByHotelId(hotelId: UUID) = bookingRepository.findInProgressBookingsByHotelId(hotelId)

    suspend fun findByRoomId(roomId: UUID) = bookingRepository.findByRoomId(roomId)

    suspend fun createBooking(
        payload: CreateBookingRequest,
        token: JwtAuthenticationToken,
    ): Booking {
        val customerId = token.getUserId()

        val hotel =
            hotelService.findById(payload.hotelId)
                ?: throw HotelaException.HotelNotFoundException(payload.hotelId)

        val room =
            roomService.findById(payload.roomId)
                ?: throw HotelaException.RoomNotFoundException(payload.roomId)

        val booking =
            Booking(
                id = UUID.randomUUID(),
                customerId = customerId,
                hotelId = hotel.id,
                roomId = room.id,
                checkin = payload.checkin,
                checkout = payload.checkout,
                guests = payload.guests,
                status = BookingStatus.CONFIRMED,
                notes = payload.notes,
            )

        validateBooking(hotel, room, booking)

        return bookingRepository.create(booking)
    }

    suspend fun updateBooking(
        id: UUID,
        payload: UpdateBookingRequest,
        token: JwtAuthenticationToken,
    ): Booking {
        val customerId = token.getUserId()

        val booking =
            bookingRepository.findById(id)
                ?: throw HotelaException.BookingNotFoundException(id)

        if (booking.customerId != customerId) {
            throw HotelaException.InvalidDataException(
                "Booking $id does not belong to customer $customerId",
            )
        }

        val hotel =
            hotelService.findById(booking.hotelId)
                ?: throw HotelaException.HotelNotFoundException(booking.hotelId)

        val room =
            roomService.findById(payload.roomId ?: booking.roomId)
                ?: throw HotelaException.RoomNotFoundException(payload.roomId ?: booking.roomId)

        val updatedBooking =
            booking.copy(
                roomId = payload.roomId ?: booking.roomId,
                checkin = payload.checkin ?: booking.checkin,
                checkout = payload.checkout ?: booking.checkout,
                guests = payload.guests ?: booking.guests,
                notes = payload.notes ?: booking.notes,
            )

        validateBooking(hotel, room, updatedBooking)

        return bookingRepository.update(updatedBooking)
    }

    private suspend fun validateBooking(
        hotel: Hotel,
        room: Room,
        booking: Booking,
    ) {
        if (!isRoomInSameHotel(hotel, room)) {
            throw HotelaException.InvalidDataException(
                "Room ${room.id} does not belong to hotel ${hotel.id}",
            )
        }

        if (!isCheckinCheckoutValid(booking.checkin, booking.checkout)) {
            throw HotelaException.InvalidDataException(
                "Check-in must be after now and check-out must be after check-in",
            )
        }

        if (!isRoomAvailability(hotel, room, booking)) {
            throw HotelaException.InvalidDataException(
                "Room ${room.id} is not available for the selected dates",
            )
        }

        if (!isNumberOfGuestsValid(room, booking.guests)) {
            throw HotelaException.InvalidDataException(
                "Room ${room.id} cannot accommodate ${booking.guests} guests",
            )
        }
    }

    private fun isRoomInSameHotel(
        hotel: Hotel,
        room: Room,
    ): Boolean = hotel.id == room.hotelId

    private fun isCheckinCheckoutValid(
        checkin: LocalDateTime,
        checkout: LocalDateTime,
    ): Boolean =
        checkin.isBefore(checkout) &&
            checkin.isAfter(
                LocalDateTime.now().minusMinutes(CHECKIN_ALLOWED_TIME_WINDOW_MINUTES),
            )

    private suspend fun isRoomAvailability(
        hotel: Hotel,
        room: Room,
        request: Booking,
    ): Boolean {
        val bookings = bookingRepository.findInProgressBookingsByHotelId(hotel.id)

        return bookings.none { booking ->
            if (booking.id == request.id) return@none false

            val isSameRoom = booking.roomId == room.id
            val isOverlapping =
                (request.checkin.isBefore(booking.checkout) && request.checkout.isAfter(booking.checkin))
            isSameRoom && isOverlapping
        }
    }

    private fun isNumberOfGuestsValid(
        room: Room,
        numberOfGuests: Int,
    ): Boolean = room.capacity >= numberOfGuests

    companion object {
        const val CHECKIN_ALLOWED_TIME_WINDOW_MINUTES = 10L
    }
}
