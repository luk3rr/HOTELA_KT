package com.hotela.controller

import com.hotela.error.HotelaException
import com.hotela.model.auth.AuthRequest
import com.hotela.model.auth.AuthResponse
import com.hotela.model.auth.CustomerRegisterRequest
import com.hotela.model.database.Customer
import com.hotela.model.database.CustomerAuth
import com.hotela.service.AuthService
import com.hotela.service.CustomerAuthService
import com.hotela.service.CustomerService
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
    private val customerService: CustomerService,
) {
    @PostMapping("/customer/login")
    @ResponseStatus(HttpStatus.OK)
    suspend fun customerLogin(
        @RequestBody payload: AuthRequest,
    ): AuthResponse {
        val customer = customerAuthService.findByEmail(payload.email) ?: throw HotelaException.InvalidCredentialsException()

        if (!authService.checkBCryptPassword(payload.password, customer.passwordHash)) {
            throw HotelaException.InvalidCredentialsException()
        }

        return AuthResponse(
            token = authService.createCustomerToken(customer),
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
            token = authService.createCustomerToken(savedCustomerAuth),
        )
    }
}
