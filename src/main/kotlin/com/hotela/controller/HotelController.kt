package com.hotela.controller

import com.hotela.error.HotelaException
import com.hotela.model.db.Hotel
import com.hotela.model.dto.request.CreateHotelRequest
import com.hotela.model.dto.request.UpdateHotelRequest
import com.hotela.model.dto.response.ResourceCreatedResponse
import com.hotela.model.dto.response.ResourceUpdatedResponse
import com.hotela.service.HotelService
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/hotel")
@EnableReactiveMethodSecurity
class HotelController(
    private val hotelService: HotelService,
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
    @PreAuthorize("hasRole(T(com.hotela.model.enum.Role).PARTNER)")
    suspend fun createHotel(
        @RequestBody payload: CreateHotelRequest,
        principal: JwtAuthenticationToken,
    ): ResourceCreatedResponse {
        val createdHotel = hotelService.createHotel(payload, principal)

        return ResourceCreatedResponse(
            id = createdHotel.id,
            message = "Hotel created successfully",
        )
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole(T(com.hotela.model.enum.Role).PARTNER)")
    suspend fun updateHotel(
        @PathVariable id: UUID,
        @RequestBody payload: UpdateHotelRequest,
        principal: JwtAuthenticationToken,
    ): ResourceUpdatedResponse {
        hotelService.updateHotel(id, payload, principal)

        return ResourceUpdatedResponse(
            message = "Hotel updated successfully",
        )
    }
}
