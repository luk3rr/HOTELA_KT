package com.hotela.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.web.cors.CorsConfiguration
import reactor.core.publisher.Mono

@Configuration
@EnableWebFluxSecurity
class SecurityConfig {
    @Bean
    fun filterChain(http: ServerHttpSecurity): SecurityWebFilterChain =
        http
            .cors {
                CorsConfiguration().apply {
                    allowedOrigins = listOf("http://localhost:8080")
                    allowedMethods = listOf("GET", "POST", "PUT", "DELETE")
                    allowedHeaders = listOf("authorization", "content-type")
                }
            }.csrf { it.disable() }
            .headers {
                it.frameOptions { frame -> frame.disable() }
                it.xssProtection { xss -> xss.disable() }
            }.authorizeExchange {
                it
                    .pathMatchers(HttpMethod.POST, "/auth/*/register")
                    .permitAll()
                    .pathMatchers(HttpMethod.POST, "/auth/*/login")
                    .permitAll()
                    .anyExchange()
                    .authenticated()
            }.oauth2ResourceServer {
                it.jwt { jwt ->
                    jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())
                }
            }.build()

    @Bean
    fun jwtAuthenticationConverter(): Converter<Jwt, Mono<AbstractAuthenticationToken>> {
        val converter = JwtGrantedAuthoritiesConverter()
        converter.setAuthorityPrefix("ROLE_")
        converter.setAuthoritiesClaimName("role")

        val jwtConverter = JwtAuthenticationConverter()
        jwtConverter.setJwtGrantedAuthoritiesConverter(converter)
        return ReactiveJwtAuthenticationConverterAdapter(jwtConverter)
    }
}
