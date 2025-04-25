package com.hotela.service

import com.hotela.error.HotelaException
import com.hotela.model.database.Customer
import com.hotela.model.database.CustomerAuth
import com.hotela.model.database.Partner
import com.hotela.model.database.PartnerAuth
import com.hotela.model.dto.request.AuthRequest
import com.hotela.model.dto.request.CustomerRegisterRequest
import com.hotela.model.dto.request.PartnerRegisterRequest
import com.hotela.model.dto.response.AuthResponse
import com.hotela.model.enum.AuthClaimKey
import com.hotela.model.enum.Role
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.security.oauth2.jwt.JwsHeader
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.UUID

@Service
class AuthService(
    private val jwtEncoder: JwtEncoder,
    private val jwtDecoder: NimbusReactiveJwtDecoder,
    private val customerService: CustomerService,
    private val partnerAuthService: PartnerAuthService,
    private val partnerService: PartnerService,
    private val customerAuthService: CustomerAuthService,
) {
    companion object {
        private val JWS_HEADER = JwsHeader.with { "HS256" }.build()
        private const val ISSUER = "hotela_backend"
        private const val EXPIRATION_DURATION = 60L
        private val EXPIRATION_DURATION_UNIT = ChronoUnit.MINUTES
        private const val SALT_ROUNDS = 10
    }

    suspend fun partnerLogin(payload: AuthRequest): AuthResponse {
        val partnerAuth =
            partnerAuthService.findByEmail(payload.email)
                ?: throw HotelaException.InvalidCredentialsException()

        if (!checkBCryptPassword(payload.password, partnerAuth.passwordHash)) {
            throw HotelaException.InvalidCredentialsException()
        }

        return AuthResponse(
            authId = partnerAuth.id,
            token = createPartnerToken(partnerAuth),
        )
    }

    @Transactional
    suspend fun partnerRegister(payload: PartnerRegisterRequest): AuthResponse {
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
                passwordHash = hashPassword(payload.password),
                createdAt = LocalDateTime.now(),
                lastLogin = LocalDateTime.now(),
                active = true,
            )

        partnerService.createPartner(partner)
        val savedPartnerAuth = partnerAuthService.createPartnerAuth(partnerAuth)

        return AuthResponse(
            authId = savedPartnerAuth.id,
            token = createPartnerToken(savedPartnerAuth),
        )
    }

    suspend fun customerLogin(payload: AuthRequest): AuthResponse {
        val customerAuth =
            customerAuthService.findByEmail(payload.email)
                ?: throw HotelaException.InvalidCredentialsException()

        if (!checkBCryptPassword(payload.password, customerAuth.passwordHash)) {
            throw HotelaException.InvalidCredentialsException()
        }

        return AuthResponse(
            authId = customerAuth.id,
            token = createCustomerToken(customerAuth),
        )
    }

    suspend fun customerRegister(payload: CustomerRegisterRequest): AuthResponse {
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
                passwordHash = hashPassword(payload.password),
                createdAt = LocalDateTime.now(),
                lastLogin = LocalDateTime.now(),
                active = true,
            )

        customerService.createCustomer(customer)
        val savedCustomerAuth = customerAuthService.createCustomerAuth(customerAuth)

        return AuthResponse(
            authId = savedCustomerAuth.id,
            token = createCustomerToken(savedCustomerAuth),
        )
    }

    private fun createCustomerToken(customer: CustomerAuth): String =
        createToken(subject = customer.email, claimKey = AuthClaimKey.CUSTOMER.key, claimValue = customer.id, role = Role.CUSTOMER)

    private fun createPartnerToken(partner: PartnerAuth): String =
        createToken(subject = partner.email, claimKey = AuthClaimKey.PARTNER.key, claimValue = partner.id, role = Role.PARTNER)

    private fun createToken(
        subject: String,
        claimKey: String,
        claimValue: Any,
        role: Role,
    ): String {
        val now = Instant.now()
        val claims =
            JwtClaimsSet
                .builder()
                .issuer(ISSUER)
                .issuedAt(now)
                .expiresAt(now.plus(EXPIRATION_DURATION, EXPIRATION_DURATION_UNIT))
                .subject(subject)
                .claim(claimKey, claimValue)
                .claim(AuthClaimKey.ROLE.key, role)
                .build()

        return jwtEncoder.encode(JwtEncoderParameters.from(JWS_HEADER, claims)).tokenValue
    }

    private fun checkBCryptPassword(
        password: String,
        hashedPassword: String,
    ): Boolean =
        try {
            BCrypt.checkpw(password, hashedPassword)
        } catch (e: Exception) {
            false
        }

    private fun hashPassword(password: String): String = BCrypt.hashpw(password, BCrypt.gensalt(SALT_ROUNDS))
}
