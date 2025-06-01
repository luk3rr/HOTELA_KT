package com.hotela.controller

import com.hotela.asCustomer
import com.hotela.asGuest
import com.hotela.asPartner
import com.hotela.error.ErrorResponse
import com.hotela.error.HotelaException
import com.hotela.model.db.Partner
import com.hotela.model.dto.response.ResourceUpdatedResponse
import com.hotela.service.PartnerService
import com.hotela.stubs.db.AuthCredentialStubs
import com.hotela.stubs.db.PartnerStubs
import com.hotela.stubs.dto.request.UpdatePartnerRequestStubs
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

@WebFluxTest(controllers = [PartnerController::class])
class PartnerControllerTest(
    private val webTestClient: WebTestClient,
    private val partnerService: PartnerService,
) : FunSpec() {
    override fun extensions() = listOf(SpringExtension)

    @TestConfiguration
    class MockBeans {
        @Bean
        fun partnerService(): PartnerService = mockk<PartnerService>()
    }

    init {
        val partner = PartnerStubs.create()
        val partnerAuth = AuthCredentialStubs.create(partner.id)
        val updatePartnerRequest = UpdatePartnerRequestStubs.create()

        context("GET /partner/{id}") {
            context("when the partner exists") {
                test("should return 200 OK") {
                    coEvery {
                        partnerService.findById(any())
                    } returns partner

                    val response =
                        webTestClient
                            .asPartner(partner.id, partnerAuth.id)
                            .get()
                            .uri("/partner/${partner.id}")
                            .exchange()
                            .expectStatus()
                            .isOk
                            .expectHeader()
                            .contentType(MediaType.APPLICATION_JSON)
                            .expectBody(Partner::class.java)
                            .returnResult()
                            .responseBody!!

                    response.id shouldBe partner.id
                }
            }

            context("when the partner does not exist") {
                test("should return 404 NOT FOUND") {
                    coEvery {
                        partnerService.findById(any())
                    } returns null

                    val response =
                        webTestClient
                            .asPartner(partner.id, partnerAuth.id)
                            .get()
                            .uri("/partner/${partner.id}")
                            .exchange()
                            .expectStatus()
                            .isNotFound
                            .expectBody(ErrorResponse::class.java)
                            .returnResult()
                            .responseBody!!

                    response.code shouldBe HotelaException.PARTNER_NOT_FOUND
                    response.message shouldBe "Partner with id ${partner.id} not found"
                }
            }
        }

        context("PUT /partner/update/{id}") {
            val partnerUpdated =
                partner.copy(
                    companyName = updatePartnerRequest.companyName ?: partner.companyName,
                    legalName = updatePartnerRequest.legalName ?: partner.legalName,
                    contactInfo = updatePartnerRequest.contactInfo ?: partner.contactInfo,
                    documentId = updatePartnerRequest.documentId ?: partner.documentId,
                    contractSignedAt = updatePartnerRequest.contractSignedAt ?: partner.contractSignedAt,
                    status = updatePartnerRequest.status ?: partner.status,
                    notes = updatePartnerRequest.notes ?: partner.notes,
                )

            context("when the partner exists") {
                test("should return 200 OK") {
                    coEvery {
                        partnerService.updatePartner(any(), any())
                    } returns partnerUpdated

                    val response =
                        webTestClient
                            .asPartner(partner.id, partnerAuth.id)
                            .put()
                            .uri("/partner/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(updatePartnerRequest)
                            .exchange()
                            .expectStatus()
                            .isOk
                            .expectHeader()
                            .contentType(MediaType.APPLICATION_JSON)
                            .expectBody(ResourceUpdatedResponse::class.java)
                            .returnResult()
                            .responseBody!!

                    response.message shouldBe "Partner updated successfully"
                }
            }

            context("when the requester is a partner but not the owner") {
                test("should return 401 UNAUTHORIZED") {
                    coEvery {
                        partnerService.updatePartner(any(), any())
                    } throws HotelaException.InvalidCredentialsException()

                    val response =
                        webTestClient
                            .asPartner(partner.id, partnerAuth.id)
                            .put()
                            .uri("/partner/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(updatePartnerRequest)
                            .exchange()
                            .expectStatus()
                            .isUnauthorized
                            .expectBody(ErrorResponse::class.java)
                            .returnResult()
                            .responseBody!!

                    response.code shouldBe HotelaException.INVALID_CREDENTIALS
                    response.message shouldBe "Invalid credentials"
                }
            }

            context("when the requester is not a partner") {
                test("should return 403 FORBIDDEN when the requester is a customer") {
                    webTestClient
                        .asCustomer(partner.id, partnerAuth.id)
                        .put()
                        .uri("/partner/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(updatePartnerRequest)
                        .exchange()
                        .expectStatus()
                        .isForbidden
                }

                test("should return 403 FORBIDDEN when the requester is a guest") {
                    webTestClient
                        .asGuest()
                        .put()
                        .uri("/partner/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(updatePartnerRequest)
                        .exchange()
                        .expectStatus()
                }
            }

            context("when the partner does not exist") {
                test("should return 404 NOT FOUND") {
                    coEvery {
                        partnerService.updatePartner(any(), any())
                    } throws HotelaException.PartnerNotFoundException(partner.id)

                    val response =
                        webTestClient
                            .asPartner(partner.id, partnerAuth.id)
                            .put()
                            .uri("/partner/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(updatePartnerRequest)
                            .exchange()
                            .expectStatus()
                            .isNotFound
                            .expectBody(ErrorResponse::class.java)
                            .returnResult()
                            .responseBody!!

                    response.code shouldBe HotelaException.PARTNER_NOT_FOUND
                    response.message shouldBe "Partner with id ${partner.id} not found"
                }
            }
        }
    }
}
