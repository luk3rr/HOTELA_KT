package com.hotela.service

import com.hotela.error.HotelaException
import com.hotela.model.db.Address
import com.hotela.model.db.Hotel
import com.hotela.model.dto.request.CreateHotelRequest
import com.hotela.model.dto.request.UpdateHotelRequest
import com.hotela.model.enum.Role
import com.hotela.repository.AddressRepository
import com.hotela.repository.HotelRepository
import com.hotela.util.getRole
import com.hotela.util.getUserId
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class HotelService(
    private val hotelRepository: HotelRepository,
    private val addressRepository: AddressRepository,
) {
    suspend fun findById(id: UUID) = hotelRepository.findById(id)

    suspend fun findByPartnerId(partnerId: UUID) = hotelRepository.findByPartnerId(partnerId)

    suspend fun createHotel(
        payload: CreateHotelRequest,
        token: JwtAuthenticationToken,
    ): Hotel {
        val userRole = token.getRole()

        if (userRole != Role.PARTNER) {
            throw HotelaException.AccessDeniedException()
        }

        val address =
            Address(
                id = UUID.randomUUID(),
                streetAddress = payload.address.streetAddress,
                number = payload.address.number,
                complement = payload.address.complement,
                neighborhood = payload.address.neighborhood,
                city = payload.address.city,
                stateProvince = payload.address.stateProvince,
                postalCode = payload.address.postalCode,
                countryCode = payload.address.countryCode,
                latitude = payload.address.latitude,
                longitude = payload.address.longitude,
            )

        val hotel =
            Hotel(
                id = UUID.randomUUID(),
                partnerId = token.getUserId(),
                addressId = address.id,
                name = payload.name,
                contactInfo = payload.contactInfo,
                website = payload.website,
                description = payload.description,
            )

        addressRepository.create(address)

        return hotelRepository.create(hotel)
    }

    suspend fun updateHotel(
        id: UUID,
        payload: UpdateHotelRequest,
        token: JwtAuthenticationToken,
    ): Hotel {
        val partnerId = token.getUserId()

        val userRole = token.getRole()

        if (userRole != Role.PARTNER) {
            throw HotelaException.AccessDeniedException()
        }

        val hotel = hotelRepository.findById(id) ?: throw HotelaException.HotelNotFoundException(id)

        if (partnerId != hotel.partnerId) {
            throw HotelaException.AccessDeniedException()
        }

        val updatedHotel =
            hotel.copy(
                name = payload.name ?: hotel.name,
                contactInfo = payload.contactInfo ?: hotel.contactInfo,
                website = payload.website ?: hotel.website,
                description = payload.description ?: hotel.description,
            )

        return hotelRepository.update(updatedHotel)
    }
}
