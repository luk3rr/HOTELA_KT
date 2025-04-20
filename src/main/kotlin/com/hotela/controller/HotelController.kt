package com.hotela.controller

import com.hotela.error.HotelaException
import com.hotela.model.database.Hotel
import com.hotela.model.dto.CreateHotelRequest
import com.hotela.model.dto.ResourceCreatedResponse
import com.hotela.service.HotelService
import com.hotela.service.PartnerAuthService
import org.springframework.http.HttpStatus
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/hotel")
class HotelController(
    private val hotelService: HotelService,
    private val partnerAuthService: PartnerAuthService,
) {
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    suspend fun getHotelById(
        @PathVariable id: UUID,
    ): Hotel? = hotelService.findById(id) ?: throw HotelaException.HotelNotFoundException(id)

    @GetMapping("/partner/{partnerId}")
    @ResponseStatus(HttpStatus.OK)
    suspend fun getHotelsByPartnerId(
        @PathVariable partnerId: UUID,
    ): List<Hotel> = hotelService.findByPartnerId(partnerId)

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun createHotel(
        @RequestBody payload: CreateHotelRequest,
        principal: JwtAuthenticationToken,
    ): ResourceCreatedResponse {
        val claims = principal.token.claims

        val partnerAuthIdFromToken =
            claims["partnerAuthId"]?.let {
                UUID.fromString(it.toString())
            } ?: throw HotelaException.InvalidCredentialsException()

        val partnerAuth =
            partnerAuthService.findById(partnerAuthIdFromToken)
                ?: throw HotelaException.InvalidCredentialsException()

        if (partnerAuth.id != payload.partnerAuthId) {
            throw HotelaException.AccessDeniedException()
        }

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

        val createdHotel = hotelService.save(hotel)

        return ResourceCreatedResponse(
            id = createdHotel.id,
            message = "Hotel created successfully",
        )
    }
}
