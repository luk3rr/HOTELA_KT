package com.hotela.controller

import com.hotela.asGuest
import com.hotela.model.dto.response.AuthResponse
import com.hotela.service.AuthCredentialService
import com.hotela.stubs.dto.request.AuthRequestStubs
import com.hotela.stubs.dto.request.CustomerRegisterRequestStubs
import com.hotela.stubs.dto.request.PartnerRegisterRequestStubs
import com.hotela.stubs.dto.response.AuthResponseStubs
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

@WebFluxTest(controllers = [AuthController::class])
class AuthControllerTest(
    private val webTestClient: WebTestClient,
    private val authCredentialService: AuthCredentialService,
) : FunSpec() {
    override fun extensions() = listOf(SpringExtension)

    @TestConfiguration
    class MockBeans {
        @Bean
        fun authService(): AuthCredentialService = mockk<AuthCredentialService>()
    }

    init {
        val authResponse = AuthResponseStubs.create()

        context("POST /auth/partner/register") {
            context("when the request is valid") {
                test("should return 200 OK") {
                    coEvery {
                        authCredentialService.partnerRegister(any())
                    } returns authResponse

                    val requestBody = PartnerRegisterRequestStubs.create()

                    val response =
                        webTestClient
                            .asGuest()
                            .post()
                            .uri("/auth/partner/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(requestBody)
                            .exchange()
                            .expectStatus()
                            .isCreated
                            .expectHeader()
                            .contentType(MediaType.APPLICATION_JSON)
                            .expectBody(AuthResponse::class.java)
                            .returnResult()
                            .responseBody!!

                    response.token shouldBe response.token
                    response.type shouldBe response.type
                }
            }
        }

        context("POST /auth/partner/login") {
            context("when the request is valid") {
                test("should return 200 OK") {
                    coEvery {
                        authCredentialService.login(any())
                    } returns authResponse

                    val requestBody = AuthRequestStubs.create()

                    val response =
                        webTestClient
                            .asGuest()
                            .post()
                            .uri("/auth/partner/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(requestBody)
                            .exchange()
                            .expectStatus()
                            .isOk
                            .expectHeader()
                            .contentType(MediaType.APPLICATION_JSON)
                            .expectBody(AuthResponse::class.java)
                            .returnResult()
                            .responseBody!!

                    response.token shouldBe response.token
                    response.type shouldBe response.type
                }
            }
        }

        context("POST /auth/customer/login") {
            context("when the request is valid") {
                test("should return 200 OK") {
                    coEvery {
                        authCredentialService.login(any())
                    } returns authResponse

                    val requestBody = AuthRequestStubs.create()

                    val response =
                        webTestClient
                            .asGuest()
                            .post()
                            .uri("/auth/customer/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(requestBody)
                            .exchange()
                            .expectStatus()
                            .isOk
                            .expectHeader()
                            .contentType(MediaType.APPLICATION_JSON)
                            .expectBody(AuthResponse::class.java)
                            .returnResult()
                            .responseBody!!

                    response.token shouldBe response.token
                    response.type shouldBe response.type
                }
            }
        }

        context("POST /auth/customer/register") {
            context("when the request is valid") {
                test("should return 200 OK") {
                    coEvery {
                        authCredentialService.customerRegister(any())
                    } returns authResponse

                    val requestBody = CustomerRegisterRequestStubs.create()

                    val response =
                        webTestClient
                            .asGuest()
                            .post()
                            .uri("/auth/customer/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(requestBody)
                            .exchange()
                            .expectStatus()
                            .isCreated
                            .expectHeader()
                            .contentType(MediaType.APPLICATION_JSON)
                            .expectBody(AuthResponse::class.java)
                            .returnResult()
                            .responseBody!!

                    response.token shouldBe response.token
                    response.type shouldBe response.type
                }
            }
        }
    }
}
