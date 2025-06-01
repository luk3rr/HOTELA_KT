package com.hotela.repository.impl

import com.hotela.model.db.Review
import com.hotela.repository.ReviewRepository
import io.r2dbc.spi.Row
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.awaitSingle
import org.springframework.r2dbc.core.awaitSingleOrNull
import org.springframework.r2dbc.core.bind
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.UUID

@Component
class ReviewRepositoryImpl(
    private val databaseClient: DatabaseClient,
) : ReviewRepository {
    override suspend fun findById(id: UUID): Review? =
        databaseClient
            .sql(FIND_BY_ID)
            .bind("id", id)
            .map { row, _ ->
                mapper(row)
            }.awaitSingleOrNull()

    override suspend fun findByHotelId(hotelId: UUID): List<Review> =
        databaseClient
            .sql(FIND_BY_HOTEL_ID)
            .bind("hotelId", hotelId)
            .map { row, _ ->
                mapper(row)
            }.all()
            .collectList()
            .awaitSingleOrNull() ?: emptyList()

    override suspend fun findByCustomerId(customerId: UUID): List<Review> =
        databaseClient
            .sql(FIND_BY_CUSTOMER_ID)
            .bind("customerId", customerId)
            .map { row, _ ->
                mapper(row)
            }.all()
            .collectList()
            .awaitSingleOrNull() ?: emptyList()

    override suspend fun findByBookingId(bookingId: UUID): Review? =
        databaseClient
            .sql(FIND_BY_BOOKING_ID)
            .bind("bookingId", bookingId)
            .map { row, _ ->
                mapper(row)
            }.awaitSingleOrNull()

    override suspend fun create(review: Review): Review =
        databaseClient
            .sql(CREATE)
            .bind("id", review.id)
            .bind("bookingId", review.bookingId)
            .bind("customerId", review.customerId)
            .bind("hotelId", review.hotelId)
            .bind("rating", review.rating)
            .bind("title", review.title)
            .bind("comment", review.comment)
            .bind("isAnonymous", review.isAnonymous)
            .bind("reviewedAt", review.reviewedAt)
            .bind("updatedAt", review.updatedAt)
            .map { row, _ ->
                mapper(row)
            }.awaitSingle()

    override suspend fun update(review: Review): Review =
        databaseClient
            .sql(UPDATE)
            .bind("id", review.id)
            .bind("rating", review.rating)
            .bind("title", review.title)
            .bind("comment", review.comment)
            .bind("isAnonymous", review.isAnonymous)
            .bind("reviewedAt", review.reviewedAt)
            .bind("updatedAt", review.updatedAt)
            .map { row, _ ->
                mapper(row)
            }.awaitSingle()

    private fun mapper(row: Row): Review =
        Review(
            id = row.get("id", UUID::class.java)!!,
            bookingId = row.get("booking_id", UUID::class.java)!!,
            customerId = row.get("customer_id", UUID::class.java)!!,
            hotelId = row.get("hotel_id", UUID::class.java)!!,
            rating = row.get("rating", Int::class.java)!!,
            title = row.get("title", String::class.java),
            comment = row.get("comment", String::class.java),
            isAnonymous = row.get("is_anonymous", Boolean::class.java)!!,
            reviewedAt = row.get("reviewed_at", Instant::class.java)!!,
            updatedAt = row.get("updated_at", Instant::class.java),
        )

    companion object {
        private const val FIND_BY_ID = """
            SELECT * FROM review WHERE id = :id
        """

        private const val FIND_BY_HOTEL_ID = """
            SELECT * FROM review
            WHERE hotel_id = :hotelId
        """

        private const val FIND_BY_CUSTOMER_ID = """
            SELECT * FROM review
            WHERE customer_id = :customerId
        """

        private const val FIND_BY_BOOKING_ID = """
            SELECT * FROM review WHERE booking_id = :bookingId
        """

        private const val CREATE = """
            INSERT INTO review (id, booking_id, customer_id, hotel_id, rating, title, comment, is_anonymous, reviewed_at, updated_at)
            VALUES (:id, :bookingId, :customerId, :hotelId, :rating, :title, :comment, :isAnonymous, :reviewedAt, :updatedAt)
            RETURNING *
        """

        private const val UPDATE = """
            UPDATE review 
            SET rating = :rating, 
                title = :title, 
                comment = :comment, 
                is_anonymous = :isAnonymous, 
                reviewed_at = :reviewedAt, 
                updated_at = :updatedAt
            WHERE id = :id
            RETURNING *
        """
    }
}
