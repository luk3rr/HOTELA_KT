package com.hotela.service

import com.hotela.model.database.Customer
import com.hotela.model.database.CustomerAuth
import com.hotela.model.database.PartnerAuth
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.security.oauth2.jwt.JwsHeader
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID

@Service
class AuthService(
    private val jwtDecoder: JwtDecoder,
    private val jwtEncoder: JwtEncoder,
    private val customerService: CustomerService,
    private val partnerAuthService: PartnerAuthService,
) {
    companion object {
        private val JWS_HEADER = JwsHeader.with { "HS256" }.build()
        private const val ISSUER = "hotela_backend"
        private const val EXPIRATION_DURATION_MINUTES = 60L
        private val EXPIRATION_DURATION_UNIT = ChronoUnit.MINUTES
    }

    fun createCustomerToken(customer: CustomerAuth): String =
        createToken(subject = customer.email, claimKey = "customerAuthId", claimValue = customer.id)

    fun createPartnerToken(partner: PartnerAuth): String =
        createToken(subject = partner.email, claimKey = "partnerAuthId", claimValue = partner.id)

    private fun createToken(
        subject: String,
        claimKey: String,
        claimValue: Any,
    ): String {
        val now = Instant.now()
        val claims =
            JwtClaimsSet
                .builder()
                .issuer(ISSUER)
                .issuedAt(now)
                .expiresAt(now.plus(EXPIRATION_DURATION_MINUTES, EXPIRATION_DURATION_UNIT))
                .subject(subject)
                .claim(claimKey, claimValue)
                .build()

        return jwtEncoder.encode(JwtEncoderParameters.from(JWS_HEADER, claims)).tokenValue
    }

    suspend fun parseCustomerToken(token: String): Customer? =
        try {
            val jwt = jwtDecoder.decode(token)
            val userId = jwt.claims["customerAuthId"] as UUID
            customerService.findById(userId)
        } catch (e: Exception) {
            null
        }

    suspend fun parsePartnerToken(token: String): PartnerAuth? =
        try {
            val jwt = jwtDecoder.decode(token)
            val partnerId = jwt.claims["partnerAuthId"] as UUID
            partnerAuthService.findById(partnerId)
        } catch (e: Exception) {
            null
        }

    fun checkBCryptPassword(
        password: String,
        hashedPassword: String,
    ): Boolean =
        try {
            BCrypt.checkpw(password, hashedPassword)
        } catch (e: Exception) {
            false
        }

    fun hashPassword(password: String): String = BCrypt.hashpw(password, BCrypt.gensalt(10))
}
