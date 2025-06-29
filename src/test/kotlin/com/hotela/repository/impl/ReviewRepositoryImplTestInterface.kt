package com.hotela.repository.impl

import com.hotela.clearAllTables
import com.hotela.model.db.Review
import com.hotela.repository.AddressRepository
import com.hotela.repository.AuthCredentialRepository
import com.hotela.repository.BookingRepository
import com.hotela.repository.CustomerRepository
import com.hotela.repository.HotelRepository
import com.hotela.repository.PartnerRepository
import com.hotela.repository.ReviewRepository
import com.hotela.repository.RoomRepository
import com.hotela.stubs.db.AddressStubs
import com.hotela.stubs.db.AuthCredentialStubs
import com.hotela.stubs.db.BookingStubs
import com.hotela.stubs.db.CustomerStubs
import com.hotela.stubs.db.HotelStubs
import com.hotela.stubs.db.PartnerStubs
import com.hotela.stubs.db.ReviewStubs
import com.hotela.stubs.db.RoomStubs
import com.hotela.stubs.db.RoomTypeStubs
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.r2dbc.core.DatabaseClient
import java.time.temporal.ChronoUnit
import java.util.UUID

class ReviewRepositoryImplTestInterface :
    ShouldSpec(),
    DatabaseIntegrationTestInterface {
    @Autowired
    private lateinit var reviewRepository: ReviewRepository

    @Autowired
    private lateinit var bookingRepository: BookingRepository

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var hotelRepository: HotelRepository

    @Autowired
    private lateinit var addressRepository: AddressRepository

    @Autowired
    private lateinit var authCredentialRepository: AuthCredentialRepository

    @Autowired
    private lateinit var partnerRepository: PartnerRepository

    @Autowired
    private lateinit var roomRepository: RoomRepository

    @Autowired
    private lateinit var databaseClient: DatabaseClient

    override fun extensions() = listOf(SpringExtension)

    init {
        lateinit var review: Review

        beforeSpec {
            databaseClient.clearAllTables()

            val address = addressRepository.create(AddressStubs.create())
            val credential = authCredentialRepository.create(AuthCredentialStubs.create())
            val partner = partnerRepository.create(PartnerStubs.create(authCredentialId = credential.id))
            val hotel = hotelRepository.create(HotelStubs.create(partnerId = partner.id, addressId = address.id))
            val room = roomRepository.create(RoomStubs.create(hotelId = hotel.id, roomTypeId = RoomTypeStubs.createStandardSolteiro().id))
            val customer = customerRepository.create(CustomerStubs.create())
            val booking = bookingRepository.create(BookingStubs.create(customerId = customer.id, hotelId = hotel.id, roomId = room.id))

            review =
                ReviewStubs.create(
                    bookingId = booking.id,
                    customerId = customer.id,
                    hotelId = hotel.id,
                )
        }

        beforeTest {
            databaseClient
                .sql("DELETE FROM review")
                .fetch()
                .rowsUpdated()
                .awaitSingle()
        }

        should("create review successfully") {
            val saved = reviewRepository.create(review)

            saved.id shouldBe review.id
            saved.bookingId shouldBe review.bookingId
            saved.customerId shouldBe review.customerId
            saved.hotelId shouldBe review.hotelId
            saved.rating shouldBe review.rating
            saved.title shouldBe review.title
            saved.comment shouldBe review.comment
            saved.isAnonymous shouldBe review.isAnonymous
            saved.reviewedAt.truncatedTo(ChronoUnit.MILLIS) shouldBe review.reviewedAt.truncatedTo(ChronoUnit.MILLIS)
            saved.updatedAt shouldBe review.updatedAt
        }

        should("update review successfully") {
            reviewRepository.create(review)
            val updatedReview = review.copy(comment = "Updated Comment", rating = 1)
            val updated = reviewRepository.update(updatedReview)

            updated.comment shouldBe "Updated Comment"
            updated.rating shouldBe 1
        }

        should("find review by id") {
            reviewRepository.create(review)

            val found = reviewRepository.findById(review.id)

            found.shouldNotBeNull()
            found.id shouldBe review.id
        }

        should("return null when id not found") {
            val found = reviewRepository.findById(UUID.randomUUID())
            found shouldBe null
        }

        should("find reviews by hotel id") {
            reviewRepository.create(review)

            val found = reviewRepository.findByHotelId(review.hotelId)

            found shouldHaveSize 1
            found.first().id shouldBe review.id
        }

        should("find reviews by customer id") {
            reviewRepository.create(review)

            val found = reviewRepository.findByCustomerId(review.customerId)

            found shouldHaveSize 1
            found.first().id shouldBe review.id
        }

        should("find review by booking id") {
            reviewRepository.create(review)

            val found = reviewRepository.findByBookingId(review.bookingId)

            found.shouldNotBeNull()
            found.id shouldBe review.id
        }

        should("return null when booking id not found") {
            val found = reviewRepository.findByBookingId(UUID.randomUUID())
            found shouldBe null
        }
    }
}
