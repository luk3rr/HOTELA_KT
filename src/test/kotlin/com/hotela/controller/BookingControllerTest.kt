package com.hotela.controller

import com.hotela.asCustomer
import com.hotela.asGuest
import com.hotela.model.db.Booking
import com.hotela.model.dto.response.ResourceCreatedResponse
import com.hotela.model.dto.response.ResourceUpdatedResponse
import com.hotela.service.BookingService
import com.hotela.stubs.db.AuthCredentialStubs
import com.hotela.stubs.db.BookingStubs
import com.hotela.stubs.db.CustomerStubs
import com.hotela.stubs.db.HotelStubs
import com.hotela.stubs.db.RoomStubs
import com.hotela.stubs.dto.request.CreateBookingRequestStubs
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

@WebFluxTest(controllers = [BookingController::class])
class BookingControllerTest(
    private val webTestClient: WebTestClient,
    private val bookingService: BookingService,
) : FunSpec() {
    override fun extensions() = listOf(SpringExtension)

    @TestConfiguration
    class MockBeans {
        @Bean
        fun bookingService(): BookingService = mockk<BookingService>()
    }

    init {
        val customerAuth = AuthCredentialStubs.create()
        val customer = CustomerStubs.create(authCredentialId = customerAuth.id)
        val hotel = HotelStubs.create()
        val room = RoomStubs.create(hotel.id)
        val booking = BookingStubs.create(customerId = customer.id, hotelId = hotel.id, roomId = room.id)

        context("POST /booking/create") {
            context("when the request is valid") {
                test("should return 201 Created") {

                    coEvery { bookingService.createBooking(any(), any()) } returns booking

                    val requestBody = CreateBookingRequestStubs.create()

                    webTestClient
                        .asCustomer(customer.id, customerAuth.id)
                        .post()
                        .uri("/booking/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(requestBody)
                        .exchange()
                        .expectStatus()
                        .isCreated
                        .expectHeader()
                        .contentType(MediaType.APPLICATION_JSON)
                        .expectBody(ResourceCreatedResponse::class.java)
                        .consumeWith { response ->
                            val responseBody = response.responseBody
                            responseBody?.id shouldBe booking.id
                            responseBody?.message shouldBe "Booking created successfully"
                        }
                }
            }

            context("when a guest tries to create a booking") {
                test("should return 403 Forbidden") {
                    val requestBody = CreateBookingRequestStubs.create()

                    webTestClient
                        .asGuest()
                        .post()
                        .uri("/booking/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(requestBody)
                        .exchange()
                        .expectStatus()
                        .isForbidden
                }
            }
        }

        context("PUT /booking/update/{id}") {
            context("when the request is valid") {
                test("should return 200 OK") {

                    coEvery { bookingService.updateBooking(any(), any(), any()) } returns booking

                    val requestBody = CreateBookingRequestStubs.create()

                    webTestClient
                        .asCustomer(customer.id, customerAuth.id)
                        .put()
                        .uri("/booking/update/${booking.id}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(requestBody)
                        .exchange()
                        .expectStatus()
                        .isOk
                        .expectHeader()
                        .contentType(MediaType.APPLICATION_JSON)
                        .expectBody(ResourceUpdatedResponse::class.java)
                        .consumeWith { response ->
                            val responseBody = response.responseBody
                            responseBody?.message shouldBe "Booking updated successfully"
                        }
                }
            }

            context("when a guest tries to update a booking") {
                test("should return 403 Forbidden") {
                    val requestBody = CreateBookingRequestStubs.create()

                    webTestClient
                        .asGuest()
                        .put()
                        .uri("/booking/update/${booking.id}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(requestBody)
                        .exchange()
                        .expectStatus()
                        .isForbidden
                }
            }
        }

        context("GET /booking/{id}") {
            context("when the booking exists") {
                test("should return 200 OK") {

                    coEvery { bookingService.findById(any()) } returns booking

                    webTestClient
                        .asCustomer(customer.id, customerAuth.id)
                        .get()
                        .uri("/booking/${booking.id}")
                        .exchange()
                        .expectStatus()
                        .isOk
                        .expectHeader()
                        .contentType(MediaType.APPLICATION_JSON)
                        .expectBody(Booking::class.java)
                        .consumeWith { response ->
                            val responseBody = response.responseBody
                            responseBody?.id shouldBe booking.id
                            responseBody?.customerId shouldBe booking.customerId
                            responseBody?.hotelId shouldBe booking.hotelId
                            responseBody?.roomId shouldBe booking.roomId
                            responseBody?.status shouldBe booking.status
                        }
                }
            }

            context("when the booking does not exist") {
                test("should return 404 Not Found") {

                    coEvery { bookingService.findById(any()) } returns null

                    webTestClient
                        .asCustomer(customer.id, customerAuth.id)
                        .get()
                        .uri("/booking/${booking.id}")
                        .exchange()
                        .expectStatus()
                        .isNotFound
                }
            }
        }

        context("PUT /booking/checkin/{id}") {
            context("when the booking exists") {
                test("should return 200 OK") {
                    coEvery { bookingService.checkIn(any(), any()) } returns booking

                    webTestClient
                        .asCustomer(customer.id, customerAuth.id)
                        .put()
                        .uri("/booking/checkin/${booking.id}")
                        .exchange()
                        .expectStatus()
                        .isOk
                        .expectHeader()
                        .contentType(MediaType.APPLICATION_JSON)
                        .expectBody(ResourceUpdatedResponse::class.java)
                        .consumeWith { response ->
                            val responseBody = response.responseBody
                            responseBody?.message shouldBe "Checked in successfully"
                        }
                }

                test("should return 403 Forbidden when a guest tries to check in") {
                    webTestClient
                        .asGuest()
                        .put()
                        .uri("/booking/checkin/${booking.id}")
                        .exchange()
                        .expectStatus()
                        .isForbidden
                }
            }
        }

        context("PUT /booking/checkout/{id}") {
            context("when the booking exists") {
                test("should return 200 OK") {
                    coEvery { bookingService.checkOut(any(), any()) } returns booking

                    webTestClient
                        .asCustomer(customer.id, customerAuth.id)
                        .put()
                        .uri("/booking/checkout/${booking.id}")
                        .exchange()
                        .expectStatus()
                        .isOk
                        .expectHeader()
                        .contentType(MediaType.APPLICATION_JSON)
                        .expectBody(ResourceUpdatedResponse::class.java)
                        .consumeWith { response ->
                            val responseBody = response.responseBody
                            responseBody?.message shouldBe "Checked out successfully"
                        }
                }

                test("should return 403 Forbidden when a guest tries to check out") {
                    webTestClient
                        .asGuest()
                        .put()
                        .uri("/booking/checkout/${booking.id}")
                        .exchange()
                        .expectStatus()
                        .isForbidden
                }
            }
        }
    }
}
