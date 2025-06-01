package com.hotela.controller

import com.hotela.error.HotelaException
import com.hotela.model.dto.request.CreateRoomRequest
import com.hotela.model.dto.request.UpdateRoomRequest
import com.hotela.model.dto.response.ResourceCreatedResponse
import com.hotela.model.dto.response.ResourceUpdatedResponse
import com.hotela.service.RoomService
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
@RequestMapping("/room")
@EnableReactiveMethodSecurity
class RoomController(
    private val roomService: RoomService,
) {
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    suspend fun getRoomById(
        @PathVariable id: UUID,
    ) = roomService.findById(id) ?: HotelaException.RoomNotFoundException(id)

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole(T(com.hotela.model.enum.Role).PARTNER)")
    suspend fun createRoom(
        @RequestBody payload: CreateRoomRequest,
        principal: JwtAuthenticationToken,
    ): ResourceCreatedResponse {
        val createdRoom = roomService.createRoom(payload, principal)

        return ResourceCreatedResponse(
            id = createdRoom.id,
            message = "Room created successfully",
        )
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole(T(com.hotela.model.enum.Role).PARTNER)")
    suspend fun updateRoom(
        @PathVariable id: UUID,
        @RequestBody payload: UpdateRoomRequest,
        principal: JwtAuthenticationToken,
    ): ResourceUpdatedResponse {
        roomService.updateRoom(id, payload, principal)

        return ResourceUpdatedResponse(
            message = "Room updated successfully",
        )
    }
}
