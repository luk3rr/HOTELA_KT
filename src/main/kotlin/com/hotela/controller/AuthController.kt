package com.hotela.controller

import com.hotela.error.HotelaException
import com.hotela.model.database.Customer
import com.hotela.model.database.CustomerAuth
import com.hotela.model.database.Partner
import com.hotela.model.database.PartnerAuth
import com.hotela.model.dto.request.AuthRequest
import com.hotela.model.dto.request.CustomerRegisterRequest
import com.hotela.model.dto.request.PartnerRegisterRequest
import com.hotela.model.dto.response.AuthResponse
import com.hotela.service.AuthService
import com.hotela.service.CustomerAuthService
import com.hotela.service.CustomerService
import com.hotela.service.PartnerAuthService
import com.hotela.service.PartnerService
import org.springframework.http.HttpStatus
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.util.UUID

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService,
    private val customerAuthService: CustomerAuthService,
    private val partnerAuthService: PartnerAuthService,
    private val partnerService: PartnerService,
    private val customerService: CustomerService,
) {
    @PostMapping("/partner/login")
    @ResponseStatus(HttpStatus.OK)
    suspend fun partnerLogin(
        @RequestBody payload: AuthRequest,
    ): AuthResponse {
        val partnerAuth = partnerAuthService.findByEmail(payload.email) ?: throw HotelaException.InvalidCredentialsException()

        if (!authService.checkBCryptPassword(payload.password, partnerAuth.passwordHash)) {
            throw HotelaException.InvalidCredentialsException()
        }

        return AuthResponse(
            authId = partnerAuth.id,
            token = authService.createPartnerToken(partnerAuth),
        )
    }

    @PostMapping("/partner/register")
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun partnerRegister(
        @RequestBody payload: PartnerRegisterRequest,
    ): AuthResponse {
        if (partnerAuthService.existsByEmail(payload.email)) {
            throw HotelaException.EmailAlreadyRegisteredException()
        }

        val partner =
            Partner(
                id = UUID.randomUUID(),
                name = payload.name,
                cnpj = payload.cnpj,
                email = payload.email,
                phone = payload.phone,
                address = payload.address,
                contactName = payload.contactName,
                contactEmail = payload.contactEmail,
                contactPhone = payload.contactPhone,
                contractSigned = payload.contractSigned,
                status = payload.status,
                createdAt = LocalDateTime.now(),
                notes = payload.notes,
            )

        val partnerAuth =
            PartnerAuth(
                id = UUID.randomUUID(),
                partnerId = partner.id,
                email = payload.email,
                passwordHash = authService.hashPassword(payload.password),
                createdAt = LocalDateTime.now(),
                lastLogin = LocalDateTime.now(),
                active = true,
            )

        partnerService.save(partner)
        val savedPartnerAuth = partnerAuthService.save(partnerAuth)

        return AuthResponse(
            authId = savedPartnerAuth.id,
            token = authService.createPartnerToken(savedPartnerAuth),
        )
    }

    @PostMapping("/customer/login")
    @ResponseStatus(HttpStatus.OK)
    suspend fun customerLogin(
        @RequestBody payload: AuthRequest,
    ): AuthResponse {
        val customerAuth = customerAuthService.findByEmail(payload.email) ?: throw HotelaException.InvalidCredentialsException()

        if (!authService.checkBCryptPassword(payload.password, customerAuth.passwordHash)) {
            throw HotelaException.InvalidCredentialsException()
        }

        return AuthResponse(
            authId = customerAuth.id,
            token = authService.createCustomerToken(customerAuth),
        )
    }

    @PostMapping("/customer/register")
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun customerRegister(
        @RequestBody payload: CustomerRegisterRequest,
    ): AuthResponse {
        if (customerAuthService.existsByEmail(payload.email)) {
            throw HotelaException.EmailAlreadyRegisteredException()
        }

        val customer =
            Customer(
                id = UUID.randomUUID(),
                name = payload.name,
                email = payload.email,
                phone = payload.phone,
                idDocument = payload.idDocument,
                birthDate = payload.birthDate,
                address = payload.address,
            )

        val customerAuth =
            CustomerAuth(
                id = UUID.randomUUID(),
                customerId = customer.id,
                email = payload.email,
                passwordHash = authService.hashPassword(payload.password),
                createdAt = LocalDateTime.now(),
                lastLogin = LocalDateTime.now(),
                active = true,
            )

        customerService.save(customer)
        val savedCustomerAuth = customerAuthService.save(customerAuth)

        return AuthResponse(
            authId = savedCustomerAuth.id,
            token = authService.createCustomerToken(savedCustomerAuth),
        )
    }
}
