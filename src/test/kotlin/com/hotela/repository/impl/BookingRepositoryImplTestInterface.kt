package com.hotela.repository.impl

import com.hotela.clearAllTables
import com.hotela.model.domain.Email
import com.hotela.model.enum.BookingStatus
import com.hotela.repository.AddressRepository
import com.hotela.repository.AuthCredentialRepository
import com.hotela.repository.BookingRepository
import com.hotela.repository.CustomerRepository
import com.hotela.repository.HotelRepository
import com.hotela.repository.PartnerRepository
import com.hotela.repository.RoomRepository
import com.hotela.stubs.db.AddressStubs
import com.hotela.stubs.db.AuthCredentialStubs
import com.hotela.stubs.db.BookingStubs
import com.hotela.stubs.db.CustomerStubs
import com.hotela.stubs.db.HotelStubs
import com.hotela.stubs.db.PartnerStubs
import com.hotela.stubs.db.RoomStubs
import com.hotela.stubs.db.RoomTypeStubs
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.r2dbc.core.DatabaseClient
import java.util.UUID

class BookingRepositoryImplTestInterface :
    ShouldSpec(),
    DatabaseIntegrationTestInterface {
    @Autowired
    private lateinit var bookingRepository: BookingRepository

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var partnerRepository: PartnerRepository

    @Autowired
    private lateinit var addressRepository: AddressRepository

    @Autowired
    private lateinit var authCredentialRepository: AuthCredentialRepository

    @Autowired
    private lateinit var hotelRepository: HotelRepository

    @Autowired
    private lateinit var roomRepository: RoomRepository

    @Autowired
    private lateinit var databaseClient: DatabaseClient

    override fun extensions() = listOf(SpringExtension)

    init {
        val address = AddressStubs.create()
        val customerAuthCredential = AuthCredentialStubs.create()
        val partnerAuthCredential =
            AuthCredentialStubs.create(
                id = UUID.fromString("98aade90-35c6-452b-9fbb-e089ff6751d8"),
                loginEmail = Email("partner@partner.com"),
            )
        val partner = PartnerStubs.create(authCredentialId = partnerAuthCredential.id)
        val customer = CustomerStubs.create(authCredentialId = customerAuthCredential.id, addressId = address.id)
        val hotel = HotelStubs.create(partnerId = partner.id, addressId = address.id)
        val roomType = RoomTypeStubs.createStandardSolteiro()
        val room = RoomStubs.create(hotelId = hotel.id, roomTypeId = roomType.id)
        val booking =
            BookingStubs.create(
                customerId = customer.id,
                hotelId = hotel.id,
                roomId = room.id,
            )

        beforeSpec {
            databaseClient.clearAllTables()

            addressRepository.create(address)
            authCredentialRepository.create(customerAuthCredential)
            authCredentialRepository.create(partnerAuthCredential)
            customerRepository.create(customer)
            partnerRepository.create(partner)
            hotelRepository.create(hotel)
            roomRepository.create(room)
        }

        beforeTest {
            databaseClient
                .sql("DELETE FROM booking")
                .fetch()
                .rowsUpdated()
                .awaitSingle()
        }

        should("successfully create a booking") {
            val savedBooking = bookingRepository.create(booking)

            savedBooking.id shouldBe booking.id
            savedBooking.customerId shouldBe customer.id
            savedBooking.roomId shouldBe room.id
            savedBooking.status shouldBe BookingStatus.CONFIRMED

            val foundInDb = bookingRepository.findById(booking.id)
            foundInDb shouldNotBe null
            foundInDb?.id shouldBe booking.id
        }

        should("successfully find a booking by id") {
            bookingRepository.create(booking)

            val result = bookingRepository.findById(booking.id)

            result shouldNotBe null
            result?.id shouldBe booking.id
            result?.customerId shouldBe customer.id
        }

        should("return null when finding by a non-existent id") {
            val nonExistentId = UUID.randomUUID()
            val result = bookingRepository.findById(nonExistentId)
            result shouldBe null
        }

        should("successfully find bookings by room id") {
            bookingRepository.create(booking)

            val results = bookingRepository.findByRoomId(room.id)

            results shouldHaveSize 1
            results.first().id shouldBe booking.id
        }

        should("successfully find bookings by hotel id") {
            bookingRepository.create(booking)

            val results = bookingRepository.findByHotelId(hotel.id)

            results shouldHaveSize 1
            results.first().id shouldBe booking.id
        }

        should("successfully find checked-in bookings by hotel id") {
            val checkedInBooking = booking.copy(status = BookingStatus.CHECKED_IN)
            bookingRepository.create(checkedInBooking)

            val results = bookingRepository.findCheckedInBookingsByHotelId(hotel.id)

            results shouldHaveSize 1
            results.first().id shouldBe checkedInBooking.id
        }

        should("successfully update a booking") {
            val bookingModified = booking.copy(status = BookingStatus.CONFIRMED, numberOfGuests = 2)
            bookingRepository.create(bookingModified)

            val updatedBooking = booking.copy(status = BookingStatus.NO_SHOW, numberOfGuests = 1)
            val result = bookingRepository.update(updatedBooking)

            result.status shouldBe BookingStatus.NO_SHOW
            result.numberOfGuests shouldBe 1

            val foundInDb = bookingRepository.findById(booking.id)
            foundInDb shouldNotBe null
            foundInDb?.status shouldBe BookingStatus.NO_SHOW
        }
    }
}
