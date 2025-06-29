package com.hotela.service

import com.hotela.error.HotelaException
import com.hotela.model.enum.AuthClaimKey
import com.hotela.model.enum.BookingStatus
import com.hotela.repository.BookingRepository
import com.hotela.service.BookingService.Companion.CHECKIN_ALLOWED_TIME_WINDOW_MINUTES
import com.hotela.stubs.db.BookingStubs
import com.hotela.stubs.db.CustomerStubs
import com.hotela.stubs.db.HotelStubs
import com.hotela.stubs.db.RoomStubs
import com.hotela.stubs.dto.request.CreateBookingRequestStubs
import com.hotela.stubs.dto.request.UpdateBookingRequestStubs
import com.hotela.util.TimeProvider
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID

class BookingServiceTest :
    BehaviorSpec({
        val bookingRepository = mockk<BookingRepository>()
        val hotelService = mockk<HotelService>()
        val roomService = mockk<RoomService>()
        val timeProvider = mockk<TimeProvider<Instant>>()
        val bookingService = BookingService(bookingRepository, hotelService, roomService, timeProvider)

        val jwtToken = mockk<JwtAuthenticationToken>()
        val jwt = mockk<Jwt>()

        Given("a booking service") {
            val customer = CustomerStubs.create()
            val hotel = HotelStubs.create()
            val room = RoomStubs.create(hotelId = hotel.id)
            val booking = BookingStubs.create(hotelId = hotel.id, roomId = room.id, customerId = customer.id)

            val roomInAnotherHotel = RoomStubs.create(UUID.fromString("00ba37ad-10fe-47ba-b375-463f566da178"))
            val anotherCustomerId = UUID.fromString("b6299563-9fd6-4030-8a3b-2c90ee1a0042")
            val bookingInProgress = booking.copy(status = BookingStatus.CHECKED_IN)
            val bookingCompleted = booking.copy(status = BookingStatus.CHECKED_OUT)
            val anotherBookingInProgress =
                booking.copy(
                    id = UUID.fromString("b2c08ccd-a0b7-4793-aa09-4f6f56f3a4af"),
                    status = BookingStatus.CHECKED_IN,
                )

            every { jwtToken.token } returns jwt
            every { jwt.claims } returns
                mapOf(
                    AuthClaimKey.USERID.key to customer.id.toString(),
                )

            every { timeProvider.now() } returns Instant.now()

            And("calling findById") {
                When("it should return the booking") {
                    coEvery { bookingRepository.findById(booking.id) } returns booking

                    Then("it should return the booking") {
                        val result = bookingService.findById(booking.id)

                        result shouldBe booking
                    }
                }
            }

            And("calling findByHotelId") {
                When("it should return the booking") {
                    coEvery { bookingRepository.findByHotelId(hotel.id) } returns listOf(booking)

                    Then("it should return the booking") {
                        val result = bookingService.findByHotelId(hotel.id)

                        result shouldBe listOf(booking)
                    }
                }
            }

            And("calling findInProgressBookingsByHotelId") {
                When("it should return the booking") {
                    coEvery { bookingRepository.findCheckedInBookingsByHotelId(hotel.id) } returns listOf(booking)

                    Then("it should return the booking") {
                        val result = bookingService.findInProgressBookingsByHotelId(hotel.id)

                        result shouldBe listOf(booking)
                    }
                }
            }

            And("calling findRunningBookingsByHotelId") {
                When("it should return the booking") {
                    coEvery { bookingRepository.findRunningBookingsByHotelId(hotel.id) } returns listOf(booking)

                    Then("it should return the booking") {
                        val result = bookingService.findRunningBookingsByHotelId(hotel.id)

                        result shouldBe listOf(booking)
                    }
                }
            }

            And("calling findFinishedBookingsByHotelId") {
                When("it should return the booking") {
                    coEvery { bookingRepository.findFinishedBookingsByHotelId(hotel.id) } returns listOf(booking)

                    Then("it should return the booking") {
                        val result = bookingService.findFinishedBookingsByHotelId(hotel.id)

                        result shouldBe listOf(booking)
                    }
                }
            }

            And("calling findByRoomId") {
                When("it should return the booking") {
                    coEvery { bookingRepository.findByRoomId(room.id) } returns listOf(booking)

                    Then("it should return the booking") {
                        val result = bookingService.findByRoomId(room.id)

                        result shouldBe listOf(booking)
                    }
                }
            }

            And("calling createBooking") {
                val createBookingRequest = CreateBookingRequestStubs.create()

                When("it should return the booking") {
                    coEvery { hotelService.findById(any()) } returns hotel
                    coEvery { roomService.findById(any()) } returns room
                    coEvery { bookingRepository.create(any()) } returns booking
                    coEvery { bookingRepository.findRunningBookingsByHotelId(any()) } returns emptyList()

                    Then("it should return the booking") {
                        val result = bookingService.createBooking(createBookingRequest, jwtToken)

                        result shouldBe booking
                    }
                }

                When("hotel not found") {
                    coEvery { hotelService.findById(any()) } returns null

                    Then("it should throw HotelNotFoundException") {
                        val exception =
                            shouldThrow<HotelaException.HotelNotFoundException> {
                                bookingService.createBooking(createBookingRequest, jwtToken)
                            }

                        exception.code shouldBe HotelaException.HOTEL_NOT_FOUND
                        exception.message shouldBe "Hotel with id ${createBookingRequest.hotelId} not found"
                    }
                }

                When("room not found") {
                    coEvery { hotelService.findById(any()) } returns hotel
                    coEvery { roomService.findById(any()) } returns null

                    Then("it should throw RoomNotFoundException") {
                        val exception =
                            shouldThrow<HotelaException.RoomNotFoundException> {
                                bookingService.createBooking(createBookingRequest, jwtToken)
                            }

                        exception.code shouldBe HotelaException.ROOM_NOT_FOUND
                        exception.message shouldBe "Room with id ${createBookingRequest.roomId} not found"
                    }
                }

                When("room does not belong to hotel") {
                    coEvery { hotelService.findById(any()) } returns hotel
                    coEvery { roomService.findById(any()) } returns roomInAnotherHotel

                    Then("it should throw InvalidDataException") {
                        val exception =
                            shouldThrow<HotelaException.InvalidDataException> {
                                bookingService.createBooking(createBookingRequest, jwtToken)
                            }

                        exception.code shouldBe HotelaException.INVALID_DATA
                        exception.message shouldBe "Room ${roomInAnotherHotel.id} does not belong to hotel ${hotel.id}"
                    }
                }

                When("checkin is after now") {
                    val invalidCreateBookingRequest =
                        createBookingRequest.copy(
                            checkin =
                                timeProvider.now().plus(
                                    BookingService.CHECKIN_ALLOWED_TIME_WINDOW_MINUTES + 1,
                                    ChronoUnit.MINUTES,
                                ),
                        )

                    coEvery { hotelService.findById(any()) } returns hotel
                    coEvery { roomService.findById(any()) } returns room

                    Then("it should throw IllegalArgumentException") {
                        shouldThrow<IllegalArgumentException> {
                            bookingService.createBooking(invalidCreateBookingRequest, jwtToken)
                        }
                    }
                }

                When("room is not available for the selected dates") {
                    coEvery { bookingRepository.findRunningBookingsByHotelId(any()) } returns
                        listOf(
                            booking,
                            anotherBookingInProgress,
                        )

                    val createBookingRequestInvalid =
                        createBookingRequest.copy(
                            checkin = booking.checkin,
                            checkout = booking.checkout,
                        )

                    coEvery { hotelService.findById(any()) } returns hotel
                    coEvery { roomService.findById(any()) } returns room
                    every { timeProvider.now() } returns booking.checkin

                    Then("it should throw InvalidDataException") {
                        val exception =
                            shouldThrow<HotelaException.InvalidDataException> {
                                bookingService.createBooking(createBookingRequestInvalid, jwtToken)
                            }

                        exception.code shouldBe HotelaException.INVALID_DATA
                        exception.message shouldBe "Room ${room.id} is not available for the selected dates"
                    }
                }

                When("room cannot accommodate the number of guests") {
                    val invalidCreateBookingRequest = createBookingRequest.copy(numberOfGuests = room.capacity + 1)
                    coEvery { bookingRepository.findRunningBookingsByHotelId(any()) } returns emptyList()

                    coEvery { hotelService.findById(any()) } returns hotel
                    coEvery { roomService.findById(any()) } returns room

                    Then("it should throw InvalidDataException") {
                        val exception =
                            shouldThrow<HotelaException.InvalidDataException> {
                                bookingService.createBooking(invalidCreateBookingRequest, jwtToken)
                            }

                        exception.code shouldBe HotelaException.INVALID_DATA
                        exception.message shouldBe "Room ${room.id} cannot accommodate ${invalidCreateBookingRequest.numberOfGuests} guests"
                    }
                }
            }

            And("calling updateBooking") {
                val updateBookingRequest = UpdateBookingRequestStubs.create()

                When("request is valid") {
                    coEvery { bookingRepository.findById(any()) } returns booking
                    coEvery { bookingRepository.findRunningBookingsByHotelId(any()) } returns listOf(booking)
                    coEvery { hotelService.findById(any()) } returns hotel
                    coEvery { roomService.findById(any()) } returns room
                    coEvery { bookingRepository.update(any()) } returns booking

                    every { timeProvider.now() } returns booking.checkin

                    Then("it should return the booking") {
                        val result = bookingService.updateBooking(booking.id, updateBookingRequest, jwtToken)

                        result shouldBe booking
                    }
                }

                When("booking not found") {
                    coEvery { bookingRepository.findById(any()) } returns null

                    Then("it should throw BookingNotFoundException") {
                        val exception =
                            shouldThrow<HotelaException.BookingNotFoundException> {
                                bookingService.updateBooking(booking.id, updateBookingRequest, jwtToken)
                            }

                        exception.code shouldBe HotelaException.BOOKING_NOT_FOUND
                        exception.message shouldBe "Booking with id ${booking.id} not found"
                    }
                }

                When("customer id does not match") {
                    every { jwt.claims } returns mapOf(AuthClaimKey.USERID.key to anotherCustomerId.toString())
                    coEvery { bookingRepository.findById(any()) } returns booking
                    coEvery { hotelService.findById(any()) } returns hotel

                    Then("it should throw InvalidCredentialsException") {
                        val exception =
                            shouldThrow<HotelaException.InvalidCredentialsException> {
                                bookingService.updateBooking(booking.id, updateBookingRequest, jwtToken)
                            }

                        exception.code shouldBe HotelaException.INVALID_CREDENTIALS
                        exception.message shouldBe "Invalid credentials"
                    }
                }

                When("hotel not found") {
                    coEvery { bookingRepository.findById(any()) } returns booking
                    coEvery { hotelService.findById(any()) } returns null

                    Then("it should throw HotelNotFoundException") {
                        val exception =
                            shouldThrow<HotelaException.HotelNotFoundException> {
                                bookingService.updateBooking(booking.id, updateBookingRequest, jwtToken)
                            }

                        exception.code shouldBe HotelaException.HOTEL_NOT_FOUND
                        exception.message shouldBe "Hotel with id ${hotel.id} not found"
                    }
                }

                When("room not found") {
                    coEvery { bookingRepository.findById(any()) } returns booking
                    coEvery { hotelService.findById(any()) } returns hotel
                    coEvery { roomService.findById(any()) } returns null

                    Then("it should throw RoomNotFoundException") {
                        val exception =
                            shouldThrow<HotelaException.RoomNotFoundException> {
                                bookingService.updateBooking(booking.id, updateBookingRequest, jwtToken)
                            }

                        exception.code shouldBe HotelaException.ROOM_NOT_FOUND
                        exception.message shouldBe "Room with id ${updateBookingRequest.roomId} not found"
                    }
                }

                When("room does not belong to hotel") {
                    coEvery { bookingRepository.findById(any()) } returns booking
                    coEvery { hotelService.findById(any()) } returns hotel
                    coEvery { roomService.findById(any()) } returns roomInAnotherHotel

                    Then("it should throw InvalidDataException") {
                        val exception =
                            shouldThrow<HotelaException.InvalidDataException> {
                                bookingService.updateBooking(booking.id, updateBookingRequest, jwtToken)
                            }

                        exception.code shouldBe HotelaException.INVALID_DATA
                        exception.message shouldBe "Room ${roomInAnotherHotel.id} does not belong to hotel ${hotel.id}"
                    }
                }

                When("checkin is before now") {
                    val invalidUpdateBookingRequest =
                        updateBookingRequest.copy(
                            checkin =
                                Instant.now().minus(
                                    CHECKIN_ALLOWED_TIME_WINDOW_MINUTES + 1,
                                    ChronoUnit.MINUTES,
                                ),
                            checkout = Instant.now().plus(2L, ChronoUnit.DAYS),
                        )

                    coEvery { bookingRepository.findRunningBookingsByHotelId(any()) } returns listOf(booking)
                    coEvery { bookingRepository.findById(any()) } returns booking
                    coEvery { hotelService.findById(any()) } returns hotel
                    coEvery { roomService.findById(any()) } returns room
                    coEvery { timeProvider.now() } returns Instant.now()

                    Then("it should throw InvalidDataException") {
                        val exception =
                            shouldThrow<HotelaException.InvalidDataException> {
                                bookingService.updateBooking(booking.id, invalidUpdateBookingRequest, jwtToken)
                            }

                        exception.message shouldBe "Check-in must be after now and check-out must be after check-in"
                    }
                }

                When("room is not available for the selected dates") {
                    coEvery { bookingRepository.findRunningBookingsByHotelId(any()) } returns
                        listOf(
                            booking,
                            anotherBookingInProgress,
                        )

                    val updateBookingRequestInvalid =
                        updateBookingRequest.copy(
                            checkin = booking.checkin,
                            checkout = booking.checkout,
                        )

                    coEvery { bookingRepository.findById(any()) } returns booking
                    coEvery { hotelService.findById(any()) } returns hotel
                    coEvery { roomService.findById(any()) } returns room
                    every { timeProvider.now() } returns booking.checkin

                    Then("it should throw InvalidDataException") {
                        val exception =
                            shouldThrow<HotelaException.InvalidDataException> {
                                bookingService.updateBooking(booking.id, updateBookingRequestInvalid, jwtToken)
                            }

                        exception.code shouldBe HotelaException.INVALID_DATA
                        exception.message shouldBe "Room ${room.id} is not available for the selected dates"
                    }
                }

                When("room cannot accommodate the number of guests") {
                    val invalidUpdateBookingRequest = updateBookingRequest.copy(numberOfGuests = room.capacity + 1)

                    coEvery { bookingRepository.findRunningBookingsByHotelId(any()) } returns listOf(booking)

                    coEvery { bookingRepository.findById(any()) } returns booking
                    coEvery { hotelService.findById(any()) } returns hotel
                    coEvery { roomService.findById(any()) } returns room
                    every { timeProvider.now() } returns booking.checkin

                    Then("it should throw InvalidDataException") {
                        val exception =
                            shouldThrow<HotelaException.InvalidDataException> {
                                bookingService.updateBooking(booking.id, invalidUpdateBookingRequest, jwtToken)
                            }

                        exception.code shouldBe HotelaException.INVALID_DATA
                        exception.message shouldBe "Room ${room.id} cannot accommodate ${invalidUpdateBookingRequest.numberOfGuests} guests"
                    }
                }
            }

            And("calling checkIn") {
                require(booking.status == BookingStatus.CONFIRMED) {
                    "Booking status must be CONFIRMED to check in"
                }

                When("requester is booking owner") {
                    coEvery { bookingRepository.findById(any()) } returns booking
                    coEvery { hotelService.findById(any()) } returns hotel
                    coEvery { roomService.findById(any()) } returns room
                    coEvery { bookingRepository.update(any()) } returns bookingInProgress

                    every { timeProvider.now() } returns booking.checkin

                    Then("it should check in the booking") {
                        val response = bookingService.checkIn(booking.id, jwtToken)

                        response.status shouldBe BookingStatus.CHECKED_IN
                    }
                }

                When("requester is not booking owner") {
                    every { jwt.claims } returns mapOf(AuthClaimKey.USERID.key to anotherCustomerId.toString())
                    coEvery { bookingRepository.findById(any()) } returns booking
                    coEvery { hotelService.findById(any()) } returns hotel

                    Then("it should throw InvalidCredentialsException") {
                        val exception =
                            shouldThrow<HotelaException.InvalidCredentialsException> {
                                bookingService.checkIn(booking.id, jwtToken)
                            }

                        exception.code shouldBe HotelaException.INVALID_CREDENTIALS
                        exception.message shouldBe "Invalid credentials"
                    }
                }

                When("booking not found") {
                    coEvery { bookingRepository.findById(any()) } returns null

                    Then("it should throw BookingNotFoundException") {
                        val exception =
                            shouldThrow<HotelaException.BookingNotFoundException> {
                                bookingService.checkIn(booking.id, jwtToken)
                            }

                        exception.code shouldBe HotelaException.BOOKING_NOT_FOUND
                        exception.message shouldBe "Booking with id ${booking.id} not found"
                    }
                }

                When("scheduled check-in time has not arrived yet") {
                    coEvery { bookingRepository.findById(any()) } returns booking
                    coEvery { hotelService.findById(any()) } returns hotel

                    every {
                        timeProvider.now()
                    } returns
                        booking.checkin.minus(
                            BookingService.CHECKIN_ALLOWED_TIME_WINDOW_MINUTES + 1,
                            ChronoUnit.MINUTES,
                        )

                    Then("it should throw InvalidDataException") {
                        val exception =
                            shouldThrow<HotelaException.InvalidDataException> {
                                bookingService.checkIn(booking.id, jwtToken)
                            }

                        exception.code shouldBe HotelaException.INVALID_DATA
                        exception.message shouldBe "Check-in is not allowed at this time"
                    }
                }
            }

            And("calling checkOut") {
                require(bookingInProgress.status == BookingStatus.CHECKED_IN) {
                    "Booking status must be CHECKED_IN to check out"
                }

                When("requester is booking owner") {
                    coEvery { bookingRepository.findById(any()) } returns bookingInProgress
                    coEvery { hotelService.findById(any()) } returns hotel
                    coEvery { bookingRepository.update(any()) } returns bookingCompleted

                    Then("it should check out the booking") {
                        val response = bookingService.checkOut(bookingInProgress.id, jwtToken)

                        response.status shouldBe BookingStatus.CHECKED_OUT
                    }
                }

                When("requester is not booking owner") {
                    every { jwt.claims } returns mapOf(AuthClaimKey.USERID.key to anotherCustomerId.toString())
                    coEvery { bookingRepository.findById(any()) } returns bookingInProgress
                    coEvery { hotelService.findById(any()) } returns hotel

                    Then("it should throw InvalidCredentialsException") {
                        val exception =
                            shouldThrow<HotelaException.InvalidCredentialsException> {
                                bookingService.checkOut(bookingInProgress.id, jwtToken)
                            }

                        exception.code shouldBe HotelaException.INVALID_CREDENTIALS
                        exception.message shouldBe "Invalid credentials"
                    }
                }

                When("booking not found") {
                    coEvery { bookingRepository.findById(any()) } returns null

                    Then("it should throw BookingNotFoundException") {
                        val exception =
                            shouldThrow<HotelaException.BookingNotFoundException> {
                                bookingService.checkOut(bookingInProgress.id, jwtToken)
                            }

                        exception.code shouldBe HotelaException.BOOKING_NOT_FOUND
                        exception.message shouldBe "Booking with id ${bookingInProgress.id} not found"
                    }
                }

                When("booking is not in progress") {
                    coEvery { bookingRepository.findById(any()) } returns bookingCompleted
                    coEvery { hotelService.findById(any()) } returns hotel

                    Then("it should throw InvalidDataException") {
                        val exception =
                            shouldThrow<HotelaException.InvalidDataException> {
                                bookingService.checkOut(bookingCompleted.id, jwtToken)
                            }

                        exception.code shouldBe HotelaException.INVALID_DATA
                        exception.message shouldBe "Booking is not in progress"
                    }
                }
            }
        }
    })
