package com.hotela.service

import com.hotela.error.HotelaException
import com.hotela.model.db.Booking
import com.hotela.model.db.Review
import com.hotela.model.dto.request.CreateReviewRequest
import com.hotela.model.dto.request.UpdateReviewRequest
import com.hotela.model.enum.BookingStatus
import com.hotela.repository.ReviewRepository
import com.hotela.util.TimeProvider
import com.hotela.util.getUserId
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class ReviewService(
    private val reviewRepository: ReviewRepository,
    private val bookingService: BookingService,
    private val timeProvider: TimeProvider<Instant>,
) {
    suspend fun findById(id: UUID): Review? = reviewRepository.findById(id)

    suspend fun findByHotelId(hotelId: UUID): List<Review> = reviewRepository.findByHotelId(hotelId)

    suspend fun findByBookingId(bookingId: UUID): Review? = reviewRepository.findByBookingId(bookingId)

    suspend fun findByCustomerId(customerId: UUID): List<Review> = reviewRepository.findByCustomerId(customerId)

    suspend fun createReview(
        payload: CreateReviewRequest,
        token: JwtAuthenticationToken,
    ): Review {
        val booking =
            bookingService.findById(payload.bookingId)
                ?: throw HotelaException.BookingNotFoundException(payload.bookingId)

        if (isBookingAlreadyReviewed(booking)) {
            throw HotelaException.InvalidDataException("Booking already reviewed")
        }

        val review =
            Review(
                id = UUID.randomUUID(),
                bookingId = booking.id,
                customerId = token.getUserId(),
                hotelId = booking.hotelId,
                rating = payload.rating,
                title = payload.title,
                comment = payload.comment,
                isAnonymous = payload.isAnonymous,
            )

        validateReview(booking, token)

        return reviewRepository.create(review)
    }

    suspend fun updateReview(
        id: UUID,
        payload: UpdateReviewRequest,
        token: JwtAuthenticationToken,
    ): Review {
        val review =
            reviewRepository.findById(id)
                ?: throw HotelaException.ReviewNotFoundException(id)

        val booking =
            bookingService.findById(review.bookingId)
                ?: throw HotelaException.BookingNotFoundException(review.bookingId)

        val updatedReview =
            review.copy(
                rating = payload.rating ?: review.rating,
                title = payload.title ?: review.title,
                comment = payload.comment ?: review.comment,
                updatedAt = timeProvider.now(),
                isAnonymous = payload.isAnonymous ?: review.isAnonymous,
            )

        validateReview(booking, token)

        return reviewRepository.update(updatedReview)
    }

    private suspend fun validateReview(
        booking: Booking,
        token: JwtAuthenticationToken,
    ) {
        val customerId = token.getUserId()

        if (!isBookingOwnedByCustomer(booking, customerId)) {
            throw HotelaException.InvalidCredentialsException()
        }

        if (!isBookingCompleted(booking)) {
            throw HotelaException.InvalidDataException("Booking is not completed")
        }
    }

    private fun isBookingOwnedByCustomer(
        booking: Booking,
        customerId: UUID,
    ): Boolean = booking.customerId == customerId

    private fun isBookingCompleted(booking: Booking): Boolean = booking.status == BookingStatus.CHECKED_OUT

    private suspend fun isBookingAlreadyReviewed(booking: Booking): Boolean = reviewRepository.findByBookingId(booking.id) != null
}
