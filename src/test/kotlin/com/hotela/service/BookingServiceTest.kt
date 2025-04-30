package com.hotela.service

import com.hotela.error.HotelaException
import com.hotela.model.enum.AuthClaimKey
import com.hotela.repository.BookingRepository
import com.hotela.stubs.database.BookingStubs
import com.hotela.stubs.database.CustomerStubs
import com.hotela.stubs.database.HotelStubs
import com.hotela.stubs.database.RoomStubs
import com.hotela.stubs.dto.request.CreateBookingRequestStubs
import com.hotela.stubs.dto.request.UpdateBookingRequestStubs
import com.hotela.util.getUserId
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import java.time.LocalDateTime
import java.util.UUID

class BookingServiceTest :
    BehaviorSpec({
        val bookingRepository = mockk<BookingRepository>()
        val hotelService = mockk<HotelService>()
        val roomService = mockk<RoomService>()
        val bookingService = BookingService(bookingRepository, hotelService, roomService)

        val jwtToken = mockk<JwtAuthenticationToken>()
        val jwt = mockk<Jwt>()

        Given("a booking service") {
            val hotel = HotelStubs.create()
            val room = RoomStubs.create(hotelId = hotel.id)
            val roomInAnotherHotel = RoomStubs.create(UUID.fromString("00ba37ad-10fe-47ba-b375-463f566da178"))
            val customer = CustomerStubs.create()
            val anotherCustomerId = UUID.fromString("b6299563-9fd6-4030-8a3b-2c90ee1a0042")
            val booking = BookingStubs.create(hotelId = hotel.id, roomId = room.id, customerId = customer.id)
            val anotherBookingInProgress =
                BookingStubs.create(
                    id = UUID.fromString("b2c08ccd-a0b7-4793-aa09-4f6f56f3a4af"),
                    hotelId = hotel.id,
                    roomId = room.id,
                    customerId = customer.id,
                )

            every { jwtToken.token } returns jwt
            every { jwt.claims } returns mapOf(AuthClaimKey.USERID.key to customer.id.toString())

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
                    coEvery { bookingRepository.findInProgressBookingsByHotelId(hotel.id) } returns listOf(booking)

                    Then("it should return the booking") {
                        val result = bookingService.findInProgressBookingsByHotelId(hotel.id)

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
                    coEvery { bookingRepository.findInProgressBookingsByHotelId(any()) } returns emptyList()

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

                When("checkin is before now") {
                    val invalidCreateBookingRequest =
                        createBookingRequest.copy(
                            checkin =
                                LocalDateTime.now().minusMinutes(
                                    BookingService.CHECKIN_ALLOWED_TIME_WINDOW_MINUTES + 1,
                                ),
                        )

                    coEvery { hotelService.findById(any()) } returns hotel
                    coEvery { roomService.findById(any()) } returns room

                    Then("it should throw InvalidDataException") {
                        val exception =
                            shouldThrow<HotelaException.InvalidDataException> {
                                bookingService.createBooking(invalidCreateBookingRequest, jwtToken)
                            }

                        exception.code shouldBe HotelaException.INVALID_DATA
                        exception.message shouldBe "Check-in must be after now and check-out must be after check-in"
                    }
                }

                When("room is not available for the selected dates") {
                    coEvery { bookingRepository.findInProgressBookingsByHotelId(any()) } returns
                        listOf(
                            booking,
                            anotherBookingInProgress,
                        )

                    coEvery { hotelService.findById(any()) } returns hotel
                    coEvery { roomService.findById(any()) } returns room

                    Then("it should throw InvalidDataException") {
                        val exception =
                            shouldThrow<HotelaException.InvalidDataException> {
                                bookingService.createBooking(createBookingRequest, jwtToken)
                            }

                        exception.code shouldBe HotelaException.INVALID_DATA
                        exception.message shouldBe "Room ${room.id} is not available for the selected dates"
                    }
                }

                When("room cannot accommodate the number of guests") {
                    val invalidCreateBookingRequest = createBookingRequest.copy(guests = room.capacity + 1)
                    coEvery { bookingRepository.findInProgressBookingsByHotelId(any()) } returns emptyList()

                    coEvery { hotelService.findById(any()) } returns hotel
                    coEvery { roomService.findById(any()) } returns room

                    Then("it should throw InvalidDataException") {
                        val exception =
                            shouldThrow<HotelaException.InvalidDataException> {
                                bookingService.createBooking(invalidCreateBookingRequest, jwtToken)
                            }

                        exception.code shouldBe HotelaException.INVALID_DATA
                        exception.message shouldBe "Room ${room.id} cannot accommodate ${invalidCreateBookingRequest.guests} guests"
                    }
                }
            }

            And("calling updateBooking") {
                val updateBookingRequest = UpdateBookingRequestStubs.create()

                When("it should return the booking") {
                    coEvery { bookingRepository.findById(any()) } returns booking
                    coEvery { bookingRepository.findInProgressBookingsByHotelId(any()) } returns listOf(booking)
                    coEvery { hotelService.findById(any()) } returns hotel
                    coEvery { roomService.findById(any()) } returns room
                    coEvery { bookingRepository.update(any()) } returns booking

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

                    Then("it should throw InvalidDataException") {
                        val exception =
                            shouldThrow<HotelaException.InvalidDataException> {
                                bookingService.updateBooking(booking.id, updateBookingRequest, jwtToken)
                            }

                        exception.code shouldBe HotelaException.INVALID_DATA
                        exception.message shouldBe "Booking ${booking.id} does not belong to customer ${jwtToken.getUserId()}"
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
                                LocalDateTime.now().minusMinutes(
                                    BookingService.CHECKIN_ALLOWED_TIME_WINDOW_MINUTES + 1,
                                ),
                        )

                    coEvery { bookingRepository.findById(any()) } returns booking
                    coEvery { hotelService.findById(any()) } returns hotel
                    coEvery { roomService.findById(any()) } returns room

                    Then("it should throw InvalidDataException") {
                        val exception =
                            shouldThrow<HotelaException.InvalidDataException> {
                                bookingService.updateBooking(booking.id, invalidUpdateBookingRequest, jwtToken)
                            }

                        exception.code shouldBe HotelaException.INVALID_DATA
                        exception.message shouldBe "Check-in must be after now and check-out must be after check-in"
                    }
                }

                When("room is not available for the selected dates") {
                    coEvery { bookingRepository.findInProgressBookingsByHotelId(any()) } returns
                        listOf(
                            booking,
                            anotherBookingInProgress,
                        )

                    coEvery { bookingRepository.findById(any()) } returns booking
                    coEvery { hotelService.findById(any()) } returns hotel
                    coEvery { roomService.findById(any()) } returns room

                    Then("it should throw InvalidDataException") {
                        val exception =
                            shouldThrow<HotelaException.InvalidDataException> {
                                bookingService.updateBooking(booking.id, updateBookingRequest, jwtToken)
                            }

                        exception.code shouldBe HotelaException.INVALID_DATA
                        exception.message shouldBe "Room ${room.id} is not available for the selected dates"
                    }
                }

                When("room cannot accommodate the number of guests") {
                    val invalidUpdateBookingRequest = updateBookingRequest.copy(guests = room.capacity + 1)

                    coEvery { bookingRepository.findInProgressBookingsByHotelId(any()) } returns listOf(booking)

                    coEvery { bookingRepository.findById(any()) } returns booking
                    coEvery { hotelService.findById(any()) } returns hotel
                    coEvery { roomService.findById(any()) } returns room

                    Then("it should throw InvalidDataException") {
                        val exception =
                            shouldThrow<HotelaException.InvalidDataException> {
                                bookingService.updateBooking(booking.id, invalidUpdateBookingRequest, jwtToken)
                            }

                        exception.code shouldBe HotelaException.INVALID_DATA
                        exception.message shouldBe "Room ${room.id} cannot accommodate ${invalidUpdateBookingRequest.guests} guests"
                    }
                }
            }
        }
    })
