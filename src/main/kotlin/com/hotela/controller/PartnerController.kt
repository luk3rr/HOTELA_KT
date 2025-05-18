package com.hotela.controller

import com.hotela.error.HotelaException
import com.hotela.model.db.Partner
import com.hotela.model.dto.request.UpdatePartnerRequest
import com.hotela.model.dto.response.ResourceUpdatedResponse
import com.hotela.service.PartnerService
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/partner")
@EnableReactiveMethodSecurity
class PartnerController(
    private val partnerService: PartnerService,
) {
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    suspend fun getPartnerById(
        @PathVariable id: UUID,
    ): Partner? = partnerService.findById(id) ?: throw HotelaException.PartnerNotFoundException(id)

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole(T(com.hotela.model.enum.Role).PARTNER)")
    suspend fun updatePartner(
        @RequestBody payload: UpdatePartnerRequest,
        principal: JwtAuthenticationToken,
    ): ResourceUpdatedResponse {
        partnerService.updatePartner(payload, principal)

        return ResourceUpdatedResponse(
            message = "Partner updated successfully",
        )
    }
}
