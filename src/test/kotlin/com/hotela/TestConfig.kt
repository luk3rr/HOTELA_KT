package com.hotela

import com.hotela.model.enum.AuthClaimKey
import com.hotela.model.enum.Role
import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.spec.IsolationMode
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.test.web.reactive.server.WebTestClient
import java.util.UUID

/*
To activate the test configuration, add the following annotation to your test class:
@Import(TestConfig::class)
When enabled, this configuration will disable CSRF protection and allow all requests to pass through.
*/
@TestConfiguration
class TestConfig {
    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain =
        http
            .csrf { it.disable() }
            .authorizeExchange { it.anyExchange().permitAll() }
            .build()
}

class ProjectConfig : AbstractProjectConfig() {
    override val isolationMode: IsolationMode = IsolationMode.InstancePerTest
}

fun WebTestClient.asGuest(): WebTestClient = this.mutateWith(mockJwt()).mutateWith(csrf())

fun WebTestClient.asCustomer(
    userId: UUID,
    authId: UUID,
): WebTestClient =
    this
        .mutateWith(
            mockJwt()
                .authorities(SimpleGrantedAuthority("ROLE_${Role.CUSTOMER}"))
                .jwt { jwt ->
                    jwt.claim(AuthClaimKey.USERID.key, userId.toString())
                    jwt.claim(AuthClaimKey.AUTHID.key, authId.toString())
                },
        ).mutateWith(csrf())

fun WebTestClient.asPartner(
    userId: UUID,
    authId: UUID,
): WebTestClient =
    this
        .mutateWith(
            mockJwt()
                .authorities(SimpleGrantedAuthority("ROLE_${Role.PARTNER}"))
                .jwt { jwt ->
                    jwt.claim(AuthClaimKey.USERID.key, authId.toString())
                    jwt.claim(AuthClaimKey.AUTHID.key, userId.toString())
                },
        ).mutateWith(csrf())
