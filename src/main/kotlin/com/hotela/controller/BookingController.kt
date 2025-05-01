package com.hotela.controller

import com.hotela.error.HotelaException
import com.hotela.model.dto.request.CreateBookingRequest
import com.hotela.model.dto.request.UpdateBookingRequest
import com.hotela.model.dto.response.ResourceCreatedResponse
import com.hotela.model.dto.response.ResourceUpdatedResponse
import com.hotela.service.BookingService
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
@RequestMapping("/booking")
@EnableReactiveMethodSecurity
class BookingController(
    private val bookingService: BookingService,
) {
    @GetMapping("/{id}")
    suspend fun getBookingById(
        @PathVariable id: UUID,
    ) = bookingService.findById(id) ?: throw HotelaException.BookingNotFoundException(id)

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole(T(com.hotela.model.enum.Role).CUSTOMER) or hasRole(T(com.hotela.model.enum.Role).PARTNER)")
    suspend fun createBooking(
        @RequestBody payload: CreateBookingRequest,
        principal: JwtAuthenticationToken,
    ): ResourceCreatedResponse {
        val createdBooking = bookingService.createBooking(payload, principal)

        return ResourceCreatedResponse(
            id = createdBooking.id,
            message = "Booking created successfully",
        )
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole(T(com.hotela.model.enum.Role).CUSTOMER) or hasRole(T(com.hotela.model.enum.Role).PARTNER)")
    suspend fun updateBooking(
        @PathVariable id: UUID,
        @RequestBody payload: UpdateBookingRequest,
        principal: JwtAuthenticationToken,
    ): ResourceUpdatedResponse {
        bookingService.updateBooking(id, payload, principal)

        return ResourceUpdatedResponse(
            message = "Booking updated successfully",
        )
    }

    @PutMapping("/checkin/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole(T(com.hotela.model.enum.Role).CUSTOMER) or hasRole(T(com.hotela.model.enum.Role).PARTNER)")
    suspend fun checkIn(
        @PathVariable id: UUID,
        principal: JwtAuthenticationToken,
    ): ResourceUpdatedResponse {
        bookingService.checkIn(id, principal)

        return ResourceUpdatedResponse(
            message = "Checked in successfully",
        )
    }

    @PutMapping("/checkout/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole(T(com.hotela.model.enum.Role).CUSTOMER) or hasRole(T(com.hotela.model.enum.Role).PARTNER)")
    suspend fun checkOut(
        @PathVariable id: UUID,
        principal: JwtAuthenticationToken,
    ): ResourceUpdatedResponse {
        bookingService.checkOut(id, principal)

        return ResourceUpdatedResponse(
            message = "Checked out successfully",
        )
    }
}
