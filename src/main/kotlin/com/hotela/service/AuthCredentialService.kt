package com.hotela.service

import com.hotela.error.HotelaException
import com.hotela.model.db.Address
import com.hotela.model.db.AuthCredential
import com.hotela.model.db.Customer
import com.hotela.model.db.Partner
import com.hotela.model.dto.request.AuthRequest
import com.hotela.model.dto.request.CustomerRegisterRequest
import com.hotela.model.dto.request.PartnerRegisterRequest
import com.hotela.model.dto.response.AuthResponse
import com.hotela.model.enum.AuthClaimKey
import com.hotela.model.enum.Role
import com.hotela.repository.AddressRepository
import com.hotela.repository.AuthCredentialRepository
import com.hotela.util.TimeProvider
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.security.oauth2.jwt.JwsHeader
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID

@Service
class AuthCredentialService(
    private val jwtEncoder: JwtEncoder,
    private val jwtDecoder: NimbusReactiveJwtDecoder,
    private val authCredentialRepository: AuthCredentialRepository,
    private val customerService: CustomerService,
    private val partnerService: PartnerService,
    // TODO: Refactor to use AddressService
    private val addressRepository: AddressRepository,
    private val timeProvider: TimeProvider<Instant>,
) {
    companion object {
        private val JWS_HEADER = JwsHeader.with { "HS256" }.build()
        private const val ISSUER = "hotela_backend"
        private const val EXPIRATION_DURATION = 60L
        private val EXPIRATION_DURATION_UNIT = ChronoUnit.MINUTES
        private const val SALT_ROUNDS = 10
    }

    @Transactional
    suspend fun partnerRegister(payload: PartnerRegisterRequest): AuthResponse {
        if (authCredentialRepository.existsByLoginEmail(payload.loginEmail)) {
            throw HotelaException.EmailAlreadyRegisteredException()
        }

        val partnerAuth =
            AuthCredential(
                id = UUID.randomUUID(),
                loginEmail = payload.loginEmail,
                password = hashPassword(payload.password),
                role = Role.PARTNER,
                isActive = true,
                lastLoginAt = timeProvider.now(),
            )

        val partner =
            Partner(
                id = UUID.randomUUID(),
                authCredentialId = partnerAuth.id,
                companyName = payload.companyName,
                legalName = payload.legalName,
                contactInfo = payload.contactInfo,
                documentId = payload.documentId,
                contractSignedAt = payload.contractSignedAt,
                status = payload.status,
                notes = payload.notes,
            )

        val savedPartnerAuth = authCredentialRepository.create(partnerAuth)
        partnerService.create(partner)

        return AuthResponse(
            token = createToken(savedPartnerAuth),
        )
    }

    suspend fun customerRegister(payload: CustomerRegisterRequest): AuthResponse {
        if (authCredentialRepository.existsByLoginEmail(payload.loginEmail)) {
            throw HotelaException.EmailAlreadyRegisteredException()
        }

        val customerAuth =
            AuthCredential(
                id = UUID.randomUUID(),
                loginEmail = payload.loginEmail,
                password = hashPassword(payload.password),
                role = Role.CUSTOMER,
                isActive = true,
                lastLoginAt = timeProvider.now(),
            )

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

        val customer =
            Customer(
                id = UUID.randomUUID(),
                authCredentialId = customerAuth.id,
                addressId = address.id,
                name = payload.name,
                contactInfo = payload.contactInfo,
                documentId = payload.documentId,
                birthDate = payload.birthDate,
            )

        val savedCustomerAuth = authCredentialRepository.create(customerAuth)
        addressRepository.create(address)
        customerService.create(customer)

        return AuthResponse(
            token = createToken(savedCustomerAuth),
        )
    }

    suspend fun login(payload: AuthRequest): AuthResponse {
        val userAuth =
            authCredentialRepository.findByLoginEmail(payload.email)
                ?: throw HotelaException.InvalidCredentialsException()

        if (!matchPassword(payload.password, userAuth.password)) {
            throw HotelaException.InvalidCredentialsException()
        }

        return AuthResponse(
            token = createToken(userAuth),
        )
    }

    private suspend fun createToken(authCredential: AuthCredential): String {
        val userId =
            authCredential.role.let {
                when (it) {
                    Role.CUSTOMER -> customerService.findByAuthId(authCredential.id)?.id
                    Role.PARTNER -> partnerService.findByAuthId(authCredential.id)?.id
                }
            }
        val now = timeProvider.now()
        val claims =
            JwtClaimsSet
                .builder()
                .issuer(ISSUER)
                .issuedAt(now)
                .expiresAt(now.plus(EXPIRATION_DURATION, EXPIRATION_DURATION_UNIT))
                .subject(authCredential.loginEmail.toString())
                .claim(AuthClaimKey.USERID.key, userId)
                .claim(AuthClaimKey.AUTHID.key, authCredential.id)
                .claim(AuthClaimKey.ROLE.key, authCredential.role)
                .build()

        return jwtEncoder.encode(JwtEncoderParameters.from(JWS_HEADER, claims)).tokenValue
    }

    private fun matchPassword(
        password: String,
        hashedPassword: String,
    ): Boolean =
        try {
            BCrypt.checkpw(password, hashedPassword)
        } catch (_: Exception) {
            false
        }

    private fun hashPassword(password: String): String = BCrypt.hashpw(password, BCrypt.gensalt(SALT_ROUNDS))
}
