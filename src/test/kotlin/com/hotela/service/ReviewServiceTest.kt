package com.hotela.service

import com.hotela.error.HotelaException
import com.hotela.model.enum.AuthClaimKey
import com.hotela.model.enum.BookingStatus
import com.hotela.repository.ReviewRepository
import com.hotela.stubs.database.BookingStubs
import com.hotela.stubs.database.CustomerStubs
import com.hotela.stubs.database.HotelStubs
import com.hotela.stubs.database.ReviewStubs
import com.hotela.stubs.dto.request.CreateReviewRequestStubs
import com.hotela.stubs.dto.request.UpdateReviewRequestStubs
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import java.util.UUID

class ReviewServiceTest :
    BehaviorSpec({
        val reviewRepository = mockk<ReviewRepository>()
        val bookingService = mockk<BookingService>()
        val reviewService =
            ReviewService(
                reviewRepository = reviewRepository,
                bookingService = bookingService,
            )

        val jwtToken = mockk<JwtAuthenticationToken>()
        val jwt = mockk<Jwt>()

        Given("a review service") {
            val customer = CustomerStubs.create()
            val anotherCustomerId = UUID.fromString("7a6398d1-4dbb-4e4b-892c-3d0b76756972")
            val hotel = HotelStubs.create()
            val bookingInProgress = BookingStubs.create(customerId = customer.id, hotelId = hotel.id, status = BookingStatus.IN_PROGRESS)
            val bookingCompleted = BookingStubs.create(customerId = customer.id, hotelId = hotel.id, status = BookingStatus.COMPLETED)

            val review = ReviewStubs.create(bookingId = bookingCompleted.id)

            every { jwtToken.token } returns jwt
            every { jwt.claims } returns mapOf(AuthClaimKey.USERID.key to customer.id.toString())

            And("calling findById") {
                coEvery { reviewRepository.findById(review.id) } returns review

                When("findById is called") {
                    val result = reviewService.findById(review.id)

                    Then("it should return the review") {
                        result shouldBe review
                    }
                }
            }

            And("calling findByHotelId") {
                coEvery { reviewRepository.findByHotelId(hotel.id) } returns listOf(review)

                When("findByHotelId is called") {
                    val result = reviewService.findByHotelId(hotel.id)

                    Then("it should return the reviews") {
                        result shouldBe listOf(review)
                    }
                }
            }

            And("calling findByBookingId") {
                coEvery { reviewRepository.findByBookingId(bookingCompleted.id) } returns review

                When("findByBookingId is called") {
                    val result = reviewService.findByBookingId(bookingCompleted.id)

                    Then("it should return the review") {
                        result shouldBe review
                    }
                }
            }

            And("calling findByCustomerId") {
                coEvery { reviewRepository.findByCustomerId(customer.id) } returns listOf(review)

                When("findByCustomerId is called") {
                    val result = reviewService.findByCustomerId(customer.id)

                    Then("it should return the reviews") {
                        result shouldBe listOf(review)
                    }
                }
            }

            And("calling createReview") {
                val createReviewRequest =
                    CreateReviewRequestStubs.create(bookingId = bookingCompleted.id, rating = 5, comment = "Great stay!")

                When("the request is valid") {
                    coEvery { bookingService.findById(createReviewRequest.bookingId) } returns bookingCompleted
                    coEvery { reviewRepository.findByBookingId(bookingCompleted.id) } returns null
                    coEvery { reviewRepository.create(any()) } returns review

                    Then("it should return the created review") {
                        val result =
                            reviewService.createReview(
                                payload = createReviewRequest,
                                token = jwtToken,
                            )

                        result shouldBe review
                    }
                }

                When("booking not found") {
                    coEvery { bookingService.findById(createReviewRequest.bookingId) } returns null

                    Then("it should throw BookingNotFoundException") {
                        val exception =
                            shouldThrow<HotelaException.BookingNotFoundException> {
                                reviewService.createReview(
                                    payload = createReviewRequest,
                                    token = jwtToken,
                                )
                            }

                        exception.code shouldBe HotelaException.BOOKING_NOT_FOUND
                        exception.message shouldBe "Booking with id ${createReviewRequest.bookingId} not found"
                    }
                }

                When("the requester customer is not the owner of the booking") {
                    coEvery { bookingService.findById(createReviewRequest.bookingId) } returns bookingCompleted
                    coEvery { reviewRepository.findByBookingId(bookingCompleted.id) } returns null
                    every { jwt.claims } returns mapOf(AuthClaimKey.USERID.key to anotherCustomerId.toString())

                    Then("it should throw InvalidCredentialsException") {
                        val exception =
                            shouldThrow<HotelaException.InvalidCredentialsException> {
                                reviewService.createReview(
                                    payload = createReviewRequest,
                                    token = jwtToken,
                                )
                            }

                        exception.code shouldBe HotelaException.INVALID_CREDENTIALS
                        exception.message shouldBe "Invalid credentials"
                    }
                }

                When("booking not already completed") {
                    coEvery { bookingService.findById(createReviewRequest.bookingId) } returns bookingInProgress
                    coEvery { reviewRepository.findByBookingId(bookingInProgress.id) } returns null
                    every { jwt.claims } returns mapOf(AuthClaimKey.USERID.key to customer.id.toString())

                    Then("it should throw BookingNotCompletedException") {
                        val exception =
                            shouldThrow<HotelaException.InvalidDataException> {
                                reviewService.createReview(
                                    payload = createReviewRequest,
                                    token = jwtToken,
                                )
                            }

                        exception.code shouldBe HotelaException.INVALID_DATA
                        exception.message shouldBe "Booking is not completed"
                    }
                }

                When("booking already reviewed") {
                    coEvery { bookingService.findById(createReviewRequest.bookingId) } returns bookingCompleted
                    coEvery { reviewRepository.findByBookingId(createReviewRequest.bookingId) } returns review

                    Then("it should throw BookingAlreadyReviewedException") {
                        val exception =
                            shouldThrow<HotelaException.InvalidDataException> {
                                reviewService.createReview(
                                    payload = createReviewRequest,
                                    token = jwtToken,
                                )
                            }

                        exception.code shouldBe HotelaException.INVALID_DATA
                        exception.message shouldBe "Booking already reviewed"
                    }
                }
            }

            And("calling updateReview") {
                val updateReviewRequest =
                    UpdateReviewRequestStubs.create(rating = 1, comment = "Bad stay!")

                When("the request is valid") {
                    coEvery { reviewRepository.findById(review.id) } returns review
                    coEvery { bookingService.findById(review.bookingId) } returns bookingCompleted
                    coEvery { reviewRepository.update(any()) } returns review

                    Then("it should return the updated review") {
                        val result =
                            reviewService.updateReview(
                                id = review.id,
                                payload = updateReviewRequest,
                                token = jwtToken,
                            )

                        result shouldBe review
                    }
                }

                When("review not found") {
                    coEvery { reviewRepository.findById(review.id) } returns null

                    Then("it should throw ReviewNotFoundException") {
                        val exception =
                            shouldThrow<HotelaException.ReviewNotFoundException> {
                                reviewService.updateReview(
                                    id = review.id,
                                    payload = updateReviewRequest,
                                    token = jwtToken,
                                )
                            }

                        exception.code shouldBe HotelaException.REVIEW_NOT_FOUND
                        exception.message shouldBe "Review with id ${review.id} not found"
                    }
                }

                When("the requester customer is not the owner of the booking") {
                    coEvery { reviewRepository.findById(review.id) } returns review
                    coEvery { bookingService.findById(review.bookingId) } returns bookingCompleted
                    every { jwt.claims } returns mapOf(AuthClaimKey.USERID.key to anotherCustomerId.toString())

                    Then("it should throw InvalidCredentialsException") {
                        val exception =
                            shouldThrow<HotelaException.InvalidCredentialsException> {
                                reviewService.updateReview(
                                    id = review.id,
                                    payload = updateReviewRequest,
                                    token = jwtToken,
                                )
                            }

                        exception.code shouldBe HotelaException.INVALID_CREDENTIALS
                        exception.message shouldBe "Invalid credentials"
                    }
                }
            }
        }
    })
