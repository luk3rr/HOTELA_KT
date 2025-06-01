package com.hotela.repository.impl

import com.hotela.model.db.Booking
import com.hotela.model.enum.BookingStatus
import com.hotela.stubs.db.BookingStubs
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
import java.time.Instant
import java.util.UUID
import java.util.function.BiFunction

class BookingRepositoryImplTest :
    ShouldSpec({
        val databaseClient = mockk<DatabaseClient>()
        val bookingRepositoryImpl = BookingRepositoryImpl(databaseClient)

        val booking = BookingStubs.create()
        val genericDatabaseSpec = mockk<DatabaseClient.GenericExecuteSpec>()
        val mockRow = mockk<Row>()
        val rowsFetchSpec = mockk<RowsFetchSpec<Booking>>()

        fun setupMockForDatabaseClient() {
            every { databaseClient.sql(any<String>()) } returns genericDatabaseSpec
            every { genericDatabaseSpec.bind(any<String>(), any()) } returns genericDatabaseSpec
            every { genericDatabaseSpec.bindNull(any<String>(), any()) } returns genericDatabaseSpec
        }

        fun setupMockRowForBooking() {
            every {
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, Booking>>())
            } answers {
                val function = args[0] as BiFunction<Row, RowMetadata, Booking>
                every { rowsFetchSpec.first() } returns Mono.just(function.apply(mockRow, mockk()))
                every { rowsFetchSpec.all().collectList() } returns Mono.just(listOf(function.apply(mockRow, mockk())))
                rowsFetchSpec
            }

            every { mockRow.get("id", UUID::class.java) } returns booking.id
            every { mockRow.get("customer_id", UUID::class.java) } returns booking.customerId
            every { mockRow.get("hotel_id", UUID::class.java) } returns booking.hotelId
            every { mockRow.get("room_id", UUID::class.java) } returns booking.roomId
            every { mockRow.get("booked_at", Instant::class.java) } returns booking.bookedAt
            every { mockRow.get("checkin", Instant::class.java) } returns booking.checkin
            every { mockRow.get("checkout", Instant::class.java) } returns booking.checkout
            every { mockRow.get("number_of_guests", Int::class.java) } returns booking.numberOfGuests
            every { mockRow.get("status", BookingStatus::class.java) } returns booking.status
            every { mockRow.get("special_requests", String::class.java) } returns booking.specialRequests
        }

        beforeTest {
            setupMockForDatabaseClient()
            setupMockRowForBooking()
        }

        afterTest { clearAllMocks() }

        should("successfully create a booking") {
            bookingRepositoryImpl.create(booking) shouldBe booking

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("id", booking.id)
                genericDatabaseSpec.bind("customerId", booking.customerId)
                genericDatabaseSpec.bind("hotelId", booking.hotelId)
                genericDatabaseSpec.bind("roomId", booking.roomId)
                genericDatabaseSpec.bind("checkin", booking.checkin)
                genericDatabaseSpec.bind("checkout", booking.checkout)
                genericDatabaseSpec.bind("numberOfGuests", booking.numberOfGuests)
                genericDatabaseSpec.bind("status", booking.status)
                genericDatabaseSpec.bind("specialRequests", booking.specialRequests)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, Booking>>())
                rowsFetchSpec.first()
            }
        }

        should("successfully update a booking") {
            bookingRepositoryImpl.update(booking) shouldBe booking

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("id", booking.id)
                genericDatabaseSpec.bind("roomId", booking.roomId)
                genericDatabaseSpec.bind("checkin", booking.checkin)
                genericDatabaseSpec.bind("checkout", booking.checkout)
                genericDatabaseSpec.bind("numberOfGuests", booking.numberOfGuests)
                genericDatabaseSpec.bind("status", booking.status)
                genericDatabaseSpec.bind("specialRequests", booking.specialRequests)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, Booking>>())
                rowsFetchSpec.first()
            }
        }

        should("successfully find a booking by id") {
            bookingRepositoryImpl.findById(booking.id) shouldBe booking

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("id", booking.id)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, Booking>>())
                rowsFetchSpec.first()
            }
        }

        should("successfully find bookings by room id") {
            bookingRepositoryImpl.findByRoomId(booking.roomId) shouldBe listOf(booking)

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("roomId", booking.roomId)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, Booking>>())
                rowsFetchSpec.all().collectList()
            }
        }

        should("successfully find bookings by hotel id") {
            bookingRepositoryImpl.findByHotelId(booking.hotelId) shouldBe listOf(booking)

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("hotelId", booking.hotelId)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, Booking>>())
                rowsFetchSpec.all().collectList()
            }
        }

        should("successfully find checked-in bookings by hotel id") {
            bookingRepositoryImpl.findCheckedInBookingsByHotelId(booking.hotelId) shouldBe listOf(booking)

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("hotelId", booking.hotelId)
                genericDatabaseSpec.bind("status", setOf(BookingStatus.CHECKED_IN))
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, Booking>>())
                rowsFetchSpec.all().collectList()
            }
        }

        should("successfully find running bookings by hotel id") {
            bookingRepositoryImpl.findRunningBookingsByHotelId(booking.hotelId) shouldBe listOf(booking)

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("hotelId", booking.hotelId)
                genericDatabaseSpec.bind("status", Booking.RUNNING_BOOKINGS_STATUS)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, Booking>>())
                rowsFetchSpec.all().collectList()
            }
        }

        should("successfully find finished bookings by hotel id") {
            bookingRepositoryImpl.findFinishedBookingsByHotelId(booking.hotelId) shouldBe listOf(booking)

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("hotelId", booking.hotelId)
                genericDatabaseSpec.bind("status", Booking.FINISHED_BOOKINGS_STATUS)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, Booking>>())
                rowsFetchSpec.all().collectList()
            }
        }
    })
