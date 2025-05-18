package com.hotela.controller

import com.hotela.asCustomer
import com.hotela.asGuest
import com.hotela.model.db.Review
import com.hotela.model.dto.response.ResourceCreatedResponse
import com.hotela.model.dto.response.ResourceUpdatedResponse
import com.hotela.service.ReviewService
import com.hotela.stubs.db.AuthCredentialStubs
import com.hotela.stubs.db.CustomerStubs
import com.hotela.stubs.db.ReviewStubs
import com.hotela.stubs.dto.request.CreateReviewRequestStubs
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.coEvery
import io.mockk.mockk
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

@WebFluxTest(controllers = [ReviewController::class])
class ReviewControllerTest(
    private val webTestClient: WebTestClient,
    private val reviewService: ReviewService,
) : FunSpec() {
    override fun extensions() = listOf(SpringExtension)

    @TestConfiguration
    class MockBeans {
        @Bean
        fun reviewService(): ReviewService = mockk<ReviewService>()
    }

    init {
        val review = ReviewStubs.create()
        val customer = CustomerStubs.create()
        val customerAuth = AuthCredentialStubs.create()

        context("POST /review/create") {
            context("when the request is valid") {
                test("should return 201 Created") {
                    coEvery { reviewService.createReview(any(), any()) } returns review

                    val requestBody = CreateReviewRequestStubs.create()

                    webTestClient
                        .asCustomer(userId = customer.id, authId = customerAuth.id)
                        .post()
                        .uri("/review/create")
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
                            responseBody shouldNotBe null
                            responseBody?.id shouldBe review.id
                            responseBody?.message shouldBe "Review created successfully"
                        }
                }
            }

            context("when a guest tries to create a review") {
                test("should return 403 Forbidden") {
                    val requestBody = CreateReviewRequestStubs.create()

                    webTestClient
                        .asGuest()
                        .post()
                        .uri("/review/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(requestBody)
                        .exchange()
                        .expectStatus()
                        .isForbidden
                }
            }
        }

        context("PUT /review/update/{id}") {
            context("when the request is valid") {
                test("should return 200 OK") {
                    coEvery { reviewService.updateReview(any(), any(), any()) } returns review

                    val requestBody = CreateReviewRequestStubs.create()

                    webTestClient
                        .asCustomer(userId = customer.id, authId = customerAuth.id)
                        .put()
                        .uri("/review/update/${review.id}")
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
                            responseBody shouldNotBe null
                            responseBody?.message shouldBe "Review updated successfully"
                        }
                }
            }

            context("when a guest tries to update a review") {
                test("should return 403 Forbidden") {
                    val requestBody = CreateReviewRequestStubs.create()

                    webTestClient
                        .asGuest()
                        .put()
                        .uri("/review/update/${review.id}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(requestBody)
                        .exchange()
                        .expectStatus()
                        .isForbidden
                }
            }
        }

        context("GET /review/{id}") {
            context("when the review exists") {
                test("should return 200 OK") {
                    coEvery { reviewService.findById(any()) } returns review

                    webTestClient
                        .asCustomer(userId = customer.id, authId = customerAuth.id)
                        .get()
                        .uri("/review/${review.id}")
                        .exchange()
                        .expectStatus()
                        .isOk
                        .expectHeader()
                        .contentType(MediaType.APPLICATION_JSON)
                        .expectBody(Review::class.java)
                        .consumeWith { response ->
                            val responseBody = response.responseBody
                            responseBody shouldNotBe null
                            responseBody?.id shouldBe review.id
                            responseBody?.rating shouldBe review.rating
                            responseBody?.comment shouldBe review.comment
                        }
                }
            }
        }
    }
}
