package com.hotela.controller

import com.hotela.model.dto.request.AuthRequest
import com.hotela.model.dto.request.CustomerRegisterRequest
import com.hotela.model.dto.request.PartnerRegisterRequest
import com.hotela.model.dto.response.AuthResponse
import com.hotela.service.AuthCredentialService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authCredentialService: AuthCredentialService,
) {
    @PostMapping("/partner/login")
    @ResponseStatus(HttpStatus.OK)
    suspend fun partnerLogin(
        @RequestBody payload: AuthRequest,
    ): AuthResponse = authCredentialService.login(payload)

    @PostMapping("/partner/register")
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun partnerRegister(
        @RequestBody payload: PartnerRegisterRequest,
    ): AuthResponse = authCredentialService.partnerRegister(payload)

    @PostMapping("/customer/login")
    @ResponseStatus(HttpStatus.OK)
    suspend fun customerLogin(
        @RequestBody payload: AuthRequest,
    ): AuthResponse = authCredentialService.login(payload)

    @PostMapping("/customer/register")
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun customerRegister(
        @RequestBody payload: CustomerRegisterRequest,
    ): AuthResponse = authCredentialService.customerRegister(payload)
}
