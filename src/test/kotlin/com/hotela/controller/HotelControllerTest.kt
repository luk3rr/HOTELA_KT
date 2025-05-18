package com.hotela.controller

import com.hotela.asCustomer
import com.hotela.asPartner
import com.hotela.error.ErrorResponse
import com.hotela.error.HotelaException
import com.hotela.model.db.Hotel
import com.hotela.model.dto.response.ResourceCreatedResponse
import com.hotela.model.dto.response.ResourceUpdatedResponse
import com.hotela.model.enum.Role
import com.hotela.service.HotelService
import com.hotela.stubs.db.AuthCredentialStubs
import com.hotela.stubs.db.CustomerStubs
import com.hotela.stubs.db.HotelStubs
import com.hotela.stubs.db.PartnerStubs
import com.hotela.stubs.dto.request.CreateHotelRequestStubs
import com.hotela.stubs.dto.request.UpdateHotelRequestStubs
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

@WebFluxTest(controllers = [HotelController::class])
class HotelControllerTest(
    private val webTestClient: WebTestClient,
    private val hotelService: HotelService,
) : FunSpec() {
    override fun extensions() = listOf(SpringExtension)

    @TestConfiguration
    class MockBeans {
        @Bean
        fun hotelService(): HotelService = mockk<HotelService>()
    }

    init {
        val partner = PartnerStubs.create()
        val customer = CustomerStubs.create()
        val hotel = HotelStubs.create(partnerId = partner.id)
        val customerAuth = AuthCredentialStubs.create(role = Role.CUSTOMER)
        val partnerAuth = AuthCredentialStubs.create(role = Role.PARTNER)

        context("POST /hotel/create") {
            context("when the request is valid") {
                test("should return 200 OK") {
                    coEvery {
                        hotelService.createHotel(any(), any())
                    } returns hotel

                    val requestBody = CreateHotelRequestStubs.create()

                    val response =
                        webTestClient
                            .asPartner(partner.id, partnerAuth.id)
                            .post()
                            .uri("/hotel/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(requestBody)
                            .exchange()
                            .expectStatus()
                            .isCreated
                            .expectHeader()
                            .contentType(MediaType.APPLICATION_JSON)
                            .expectBody(ResourceCreatedResponse::class.java)
                            .returnResult()
                            .responseBody!!

                    response.id shouldBe hotel.id
                    response.message shouldBe "Hotel created successfully"
                }
            }

            context("when an customer tries to create a hotel") {
                test("should return 403 FORBIDDEN") {
                    val requestBody = CreateHotelRequestStubs.create()

                    webTestClient
                        .asCustomer(customer.id, customerAuth.id)
                        .post()
                        .uri("/hotel/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(requestBody)
                        .exchange()
                        .expectStatus()
                        .isForbidden
                }
            }
        }

        context("GET /hotel/{id}") {
            context("when the request is valid") {
                test("should return 200 OK to partner") {
                    coEvery {
                        hotelService.findById(any())
                    } returns hotel

                    val response =
                        webTestClient
                            .asPartner(partner.id, partnerAuth.id)
                            .get()
                            .uri("/hotel/${hotel.id}")
                            .exchange()
                            .expectStatus()
                            .isOk
                            .expectHeader()
                            .contentType(MediaType.APPLICATION_JSON)
                            .expectBody(Hotel::class.java)
                            .returnResult()
                            .responseBody!!

                    response.id shouldBe hotel.id
                }

                test("should return 200 OK to customer") {
                    coEvery {
                        hotelService.findById(any())
                    } returns hotel

                    val response =
                        webTestClient
                            .asCustomer(customer.id, customerAuth.id)
                            .get()
                            .uri("/hotel/${hotel.id}")
                            .exchange()
                            .expectStatus()
                            .isOk
                            .expectHeader()
                            .contentType(MediaType.APPLICATION_JSON)
                            .expectBody(Hotel::class.java)
                            .returnResult()
                            .responseBody!!

                    response.id shouldBe hotel.id
                }
            }

            context("when the hotel does not exist") {
                test("should return 404 NOT FOUND") {
                    coEvery {
                        hotelService.findById(any())
                    } returns null

                    val response =
                        webTestClient
                            .asPartner(partner.id, partnerAuth.id)
                            .get()
                            .uri("/hotel/${hotel.id}")
                            .exchange()
                            .expectStatus()
                            .isNotFound
                            .expectBody(ErrorResponse::class.java)
                            .returnResult()
                            .responseBody!!

                    response.code shouldBe HotelaException.HOTEL_NOT_FOUND
                    response.message shouldBe "Hotel with id ${hotel.id} not found"
                }
            }
        }

        context("GET /hotel/partner/{partnerId}") {
            context("when the request is valid") {
                test("should return 200 OK") {
                    coEvery {
                        hotelService.findByPartnerId(any())
                    } returns listOf(hotel)

                    val response =
                        webTestClient
                            .asPartner(partner.id, partnerAuth.id)
                            .get()
                            .uri("/hotel/partner/${partner.id}")
                            .exchange()
                            .expectStatus()
                            .isOk
                            .expectHeader()
                            .contentType(MediaType.APPLICATION_JSON)
                            .expectBodyList(Hotel::class.java)
                            .returnResult()
                            .responseBody!!

                    response.size shouldBe 1
                    response[0].id shouldBe hotel.id
                }
            }
        }

        context("PUT /hotel/update/{id}") {
            context("when the request is valid") {
                test("should return 200 OK") {
                    coEvery {
                        hotelService.updateHotel(any(), any(), any())
                    } returns hotel

                    val requestBody = UpdateHotelRequestStubs.create()

                    val response =
                        webTestClient
                            .asPartner(partner.id, partnerAuth.id)
                            .put()
                            .uri("/hotel/update/${hotel.id}")
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(requestBody)
                            .exchange()
                            .expectStatus()
                            .isOk
                            .expectHeader()
                            .contentType(MediaType.APPLICATION_JSON)
                            .expectBody(ResourceUpdatedResponse::class.java)
                            .returnResult()
                            .responseBody!!

                    response.message shouldBe "Hotel updated successfully"
                }
            }

            context("when the hotel does not exist") {
                test("should return 404 NOT FOUND") {
                    coEvery {
                        hotelService.updateHotel(any(), any(), any())
                    } throws HotelaException.HotelNotFoundException(hotel.id)

                    val requestBody = UpdateHotelRequestStubs.create()

                    val response =
                        webTestClient
                            .asPartner(partner.id, partnerAuth.id)
                            .put()
                            .uri("/hotel/update/${hotel.id}")
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(requestBody)
                            .exchange()
                            .expectStatus()
                            .isNotFound
                            .expectBody(ErrorResponse::class.java)
                            .returnResult()
                            .responseBody!!

                    response.code shouldBe HotelaException.HOTEL_NOT_FOUND
                    response.message shouldBe "Hotel with id ${hotel.id} not found"
                }
            }

            context("when a customer tries to update a hotel") {
                test("should return 403 FORBIDDEN") {
                    val requestBody = UpdateHotelRequestStubs.create()

                    webTestClient
                        .asCustomer(customer.id, customerAuth.id)
                        .put()
                        .uri("/hotel/update/${hotel.id}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(requestBody)
                        .exchange()
                        .expectStatus()
                        .isForbidden
                }
            }
        }
    }
}
