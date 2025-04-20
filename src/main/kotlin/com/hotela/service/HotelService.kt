package com.hotela.service

import com.hotela.model.database.Hotel
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
}
