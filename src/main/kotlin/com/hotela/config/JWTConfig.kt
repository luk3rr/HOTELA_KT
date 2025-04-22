package com.hotela.config

import com.nimbusds.jose.jwk.source.ImmutableSecret
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder
import javax.crypto.spec.SecretKeySpec

@Configuration
class JWTConfig(
    @Value("\${security.key}")
    private val jwtKey: String,
) {
    init {
        require(jwtKey.length >= 32) { "JWT secret key must be at least 32 characters long." }
    }

    private val secretKey = SecretKeySpec(jwtKey.toByteArray(), "HmacSHA256")

    @Bean
    fun jwtDecoder(): NimbusReactiveJwtDecoder = NimbusReactiveJwtDecoder.withSecretKey(secretKey).build()

    @Bean
    fun jwtEncoder(): NimbusJwtEncoder {
        val secret = ImmutableSecret<SecurityContext>(secretKey)
        return NimbusJwtEncoder(secret)
    }
}
