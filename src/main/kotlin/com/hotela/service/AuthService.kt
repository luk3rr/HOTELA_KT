package com.hotela.service

import com.hotela.model.database.Customer
import com.hotela.model.database.CustomerAuth
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
) {
    fun createCustomerToken(customer: CustomerAuth): String {
        val jwsHeader = JwsHeader.with { "HS256" }.build()
        val claims =
            JwtClaimsSet
                .builder()
                .issuer("hotela_backend")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(1L, ChronoUnit.HOURS))
                .subject(customer.email)
                .claim("customerId", customer.id)
                .build()
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).tokenValue
    }

    suspend fun parseToken(token: String): Customer? =
        try {
            val jwt = jwtDecoder.decode(token)
            val userId = jwt.claims["customerId"] as UUID
            customerService.findById(userId)
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
