package com.hotela.service

import com.hotela.error.HotelaException
import com.hotela.model.database.Hotel
import com.hotela.model.dto.request.CreateHotelRequest
import com.hotela.model.dto.request.UpdateHotelRequest
import com.hotela.repository.HotelRepository
import com.hotela.util.getAuthId
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class HotelService(
    private val partnerAuthService: PartnerAuthService,
    private val hotelRepository: HotelRepository,
) {
    suspend fun findById(id: UUID) = hotelRepository.findById(id)

    suspend fun findByPartnerId(partnerId: UUID) = hotelRepository.findByPartnerId(partnerId)

    suspend fun createHotel(
        payload: CreateHotelRequest,
        token: JwtAuthenticationToken,
    ): Hotel {
        val partnerAuthId = token.getAuthId()

        val partnerAuth =
            partnerAuthService.findById(partnerAuthId)
                ?: throw HotelaException.InvalidCredentialsException()

        val hotel =
            Hotel(
                id = UUID.randomUUID(),
                partnerId = partnerAuth.partnerId,
                name = payload.name,
                address = payload.address,
                city = payload.city,
                state = payload.state,
                zipCode = payload.zipCode,
                phone = payload.phone,
                rating = payload.rating,
                description = payload.description,
                website = payload.website,
                latitude = payload.latitude,
                longitude = payload.longitude,
            )

        return hotelRepository.create(hotel)
    }

    suspend fun updateHotel(
        id: UUID,
        payload: UpdateHotelRequest,
        token: JwtAuthenticationToken,
    ): Hotel {
        val partnerAuthId = token.getAuthId()

        val hotel = hotelRepository.findById(id) ?: throw HotelaException.HotelNotFoundException(id)

        val partnerAuth =
            partnerAuthService.findById(partnerAuthId)
                ?: throw HotelaException.InvalidCredentialsException()

        if (partnerAuth.partnerId != hotel.partnerId) {
            throw HotelaException.AccessDeniedException()
        }

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
