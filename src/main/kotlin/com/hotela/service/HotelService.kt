package com.hotela.service

import com.hotela.error.HotelaException
import com.hotela.model.database.Hotel
import com.hotela.model.dto.request.UpdateHotelRequest
import com.hotela.repository.HotelRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class HotelService(
    private val hotelRepository: HotelRepository,
) {
    suspend fun findById(id: UUID) = hotelRepository.findById(id)

    suspend fun findByPartnerId(partnerId: UUID) = hotelRepository.findByPartnerId(partnerId)

    suspend fun save(hotel: Hotel) = hotelRepository.save(hotel)

    suspend fun update(
        id: UUID,
        payload: UpdateHotelRequest,
    ): Hotel {
        val hotel = hotelRepository.findById(id) ?: throw HotelaException.HotelNotFoundException(id)

        val updatedHotel =
            hotel.copy(
                name = payload.name ?: hotel.name,
                address = payload.address ?: hotel.address,
                city = payload.city ?: hotel.city,
                state = payload.state ?: hotel.state,
                zipCode = payload.zipCode ?: hotel.zipCode,
                phone = payload.phone ?: hotel.phone,
                rating = payload.rating ?: hotel.rating,
                description = payload.description ?: hotel.description,
                website = payload.website ?: hotel.website,
                latitude = payload.latitude ?: hotel.latitude,
                longitude = payload.longitude ?: hotel.longitude,
            )

        return hotelRepository.update(updatedHotel)
    }
}
