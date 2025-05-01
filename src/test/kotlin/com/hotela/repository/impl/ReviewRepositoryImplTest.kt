package com.hotela.repository.impl

import com.hotela.model.database.Review
import com.hotela.stubs.database.BookingStubs
import com.hotela.stubs.database.ReviewStubs
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.r2dbc.spi.Row
import io.r2dbc.spi.RowMetadata
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.RowsFetchSpec
import org.springframework.r2dbc.core.bind
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.util.UUID
import java.util.function.BiFunction

class ReviewRepositoryImplTest :
    ShouldSpec({
        val databaseClient = mockk<DatabaseClient>()
        val reviewRepositoryImpl = ReviewRepositoryImpl(databaseClient)

        val booking = BookingStubs.create()
        val review = ReviewStubs.create(bookingId = booking.id)
        val genericDatabaseSpec = mockk<DatabaseClient.GenericExecuteSpec>()
        val mockRow = mockk<Row>()
        val rowsFetchSpec = mockk<RowsFetchSpec<Review>>()

        fun setupMockForDatabaseClient() {
            every { databaseClient.sql(any<String>()) } returns genericDatabaseSpec
            every { genericDatabaseSpec.bind(any<String>(), any()) } returns genericDatabaseSpec
            every { genericDatabaseSpec.bindNull(any<String>(), any()) } returns genericDatabaseSpec
        }

        fun setupMockRowForReview() {
            every {
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, Review>>())
            } answers {
                val function = args[0] as BiFunction<Row, RowMetadata, Review>
                every { rowsFetchSpec.first() } returns Mono.just(function.apply(mockRow, mockk()))
                every { rowsFetchSpec.all().collectList() } returns Mono.just(listOf(function.apply(mockRow, mockk())))
                rowsFetchSpec
            }

            every { mockRow.get("id", UUID::class.java) } returns review.id
            every { mockRow.get("booking_id", UUID::class.java) } returns review.bookingId
            every { mockRow.get("rating", Int::class.java) } returns review.rating
            every { mockRow.get("comment", String::class.java) } returns review.comment
            every { mockRow.get("reviewed_at", LocalDateTime::class.java) } returns review.reviewedAt
            every { mockRow.get("updated_at", LocalDateTime::class.java) } returns review.updatedAt
        }

        beforeTest {
            setupMockForDatabaseClient()
            setupMockRowForReview()
        }

        afterTest { clearAllMocks() }

        should("successfully create a review") {
            reviewRepositoryImpl.create(review) shouldBe review

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("id", review.id)
                genericDatabaseSpec.bind("bookingId", review.bookingId)
                genericDatabaseSpec.bind("rating", review.rating)
                genericDatabaseSpec.bind("comment", review.comment)
                genericDatabaseSpec.bind("reviewedAt", review.reviewedAt)
                genericDatabaseSpec.bind("updatedAt", review.updatedAt)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, Review>>())
                rowsFetchSpec.first()
            }
        }

        should("successfully update a review") {
            reviewRepositoryImpl.update(review) shouldBe review

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("id", review.id)
                genericDatabaseSpec.bind("rating", review.rating)
                genericDatabaseSpec.bind("comment", review.comment)
                genericDatabaseSpec.bind("updatedAt", review.updatedAt)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, Review>>())
                rowsFetchSpec.first()
            }
        }

        should("successfully find a review by ID") {
            reviewRepositoryImpl.findById(review.id) shouldBe review

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("id", review.id)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, Review>>())
                rowsFetchSpec.first()
            }
        }

        should("successfully find reviews by hotel ID") {
            reviewRepositoryImpl.findByHotelId(review.bookingId) shouldBe listOf(review)

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("hotelId", review.bookingId)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, Review>>())
                rowsFetchSpec.all().collectList()
            }
        }

        should("successfully find reviews by customer ID") {
            reviewRepositoryImpl.findByCustomerId(review.bookingId) shouldBe listOf(review)

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("customerId", review.bookingId)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, Review>>())
                rowsFetchSpec.all().collectList()
            }
        }
    })
