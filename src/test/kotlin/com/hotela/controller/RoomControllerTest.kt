package com.hotela.controller

import com.hotela.asCustomer
import com.hotela.asGuest
import com.hotela.asPartner
import com.hotela.model.db.Room
import com.hotela.model.dto.response.ResourceCreatedResponse
import com.hotela.model.dto.response.ResourceUpdatedResponse
import com.hotela.service.RoomService
import com.hotela.stubs.db.AuthCredentialStubs
import com.hotela.stubs.db.CustomerStubs
import com.hotela.stubs.db.PartnerStubs
import com.hotela.stubs.db.RoomStubs
import com.hotela.stubs.dto.request.CreateRoomRequestStubs
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
import kotlin.jvm.java

@WebFluxTest(controllers = [RoomController::class])
class RoomControllerTest(
    private val webTestClient: WebTestClient,
    private val roomService: RoomService,
) : FunSpec() {
    override fun extensions() = listOf(SpringExtension)

    @TestConfiguration
    class MockBeans {
        @Bean
        fun roomService(): RoomService = mockk<RoomService>()
    }

    init {
        val room = RoomStubs.create()
        val partnerAuth = AuthCredentialStubs.create()
        val partner = PartnerStubs.create(authCredentialId = partnerAuth.id)
        val customerAuth = AuthCredentialStubs.create()
        val customer = CustomerStubs.create(authCredentialId = customerAuth.id)

        context("POST /room/create") {
            context("when the request is valid") {
                test("should return 201 Created") {
                    coEvery {
                        roomService.createRoom(any(), any())
                    } returns room

                    val requestBody = CreateRoomRequestStubs.create(hotelId = partner.id)

                    val response =
                        webTestClient
                            .asPartner(partner.id, partnerAuth.id)
                            .post()
                            .uri("/room/create")
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

                    response.id shouldBe room.id
                    response.message shouldBe "Room created successfully"
                }
            }

            context("when an customer tries to create a room") {
                test("should return 403 Forbidden") {
                    val requestBody = CreateRoomRequestStubs.create(hotelId = partner.id)

                    webTestClient
                        .asCustomer(customer.id, customerAuth.id)
                        .post()
                        .uri("/room/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(requestBody)
                        .exchange()
                        .expectStatus()
                        .isForbidden
                }
            }

            context("when a guest tries to create a room") {
                test("should return 403 Forbidden") {
                    val requestBody = CreateRoomRequestStubs.create(hotelId = partner.id)

                    webTestClient
                        .asGuest()
                        .post()
                        .uri("/room/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(requestBody)
                        .exchange()
                        .expectStatus()
                        .isForbidden
                }
            }
        }

        context("GET /room/{id}") {
            context("when the request is valid") {
                test("should return 200 OK to partner") {
                    coEvery {
                        roomService.findById(any())
                    } returns room

                    val response =
                        webTestClient
                            .asPartner(partner.id, partnerAuth.id)
                            .get()
                            .uri("/room/${room.id}")
                            .exchange()
                            .expectStatus()
                            .isOk
                            .expectHeader()
                            .contentType(MediaType.APPLICATION_JSON)
                            .expectBody(Room::class.java)
                            .returnResult()
                            .responseBody!!

                    response.id shouldBe room.id
                }

                test("should return 200 OK to customer") {
                    coEvery {
                        roomService.findById(any())
                    } returns room

                    val response =
                        webTestClient
                            .asCustomer(customer.id, customerAuth.id)
                            .get()
                            .uri("/room/${room.id}")
                            .exchange()
                            .expectStatus()
                            .isOk
                            .expectHeader()
                            .contentType(MediaType.APPLICATION_JSON)
                            .expectBody(Room::class.java)
                            .returnResult()
                            .responseBody!!

                    response.id shouldBe room.id
                }
            }
        }

        context("PUT /room/update/{id}") {
            context("when the request is valid") {
                test("should return 200 OK") {
                    coEvery {
                        roomService.updateRoom(any(), any(), any())
                    } returns room

                    val requestBody = CreateRoomRequestStubs.create(hotelId = partner.id)

                    val response =
                        webTestClient
                            .asPartner(partner.id, partnerAuth.id)
                            .put()
                            .uri("/room/update/${room.id}")
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

                    response.message shouldBe "Room updated successfully"
                }

                test("should return 403 Forbidden to customer") {
                    val requestBody = CreateRoomRequestStubs.create(hotelId = partner.id)

                    webTestClient
                        .asCustomer(customer.id, customerAuth.id)
                        .put()
                        .uri("/room/update/${room.id}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(requestBody)
                        .exchange()
                        .expectStatus()
                        .isForbidden
                }

                test("should return 403 Forbidden to guest") {
                    val requestBody = CreateRoomRequestStubs.create(hotelId = partner.id)

                    webTestClient
                        .asGuest()
                        .put()
                        .uri("/room/update/${room.id}")
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
