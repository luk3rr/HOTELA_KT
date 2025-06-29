package com.hotela.service

import com.hotela.error.HotelaException
import com.hotela.model.enum.AuthClaimKey
import com.hotela.repository.RoomRepository
import com.hotela.stubs.db.HotelStubs
import com.hotela.stubs.db.PartnerStubs
import com.hotela.stubs.db.RoomStubs
import com.hotela.stubs.dto.request.CreateRoomRequestStubs
import com.hotela.stubs.dto.request.UpdateRoomRequestStubs
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import java.util.UUID

class RoomServiceTest :
    BehaviorSpec({
        val hotelService = mockk<HotelService>()
        val roomRepository = mockk<RoomRepository>()
        val roomService = RoomService(roomRepository, hotelService)

        val jwtToken = mockk<JwtAuthenticationToken>()
        val jwt = mockk<Jwt>()

        Given("a room service") {
            val partner = PartnerStubs.create()
            val anotherPartner = PartnerStubs.create(id = UUID.fromString("d97cd785-88c1-46b3-a172-22f697e9bea4"))
            val hotel = HotelStubs.create(partnerId = partner.id)
            val room = RoomStubs.create(hotelId = hotel.id)
            val anotherRoomWithSameNumber =
                RoomStubs.create(
                    id = UUID.fromString("385d040a-067a-47f4-aa66-fdd1bec4d61d"),
                    hotelId = hotel.id,
                )
            val anotherRoom = RoomStubs.createAnother(hotelId = hotel.id)

            every { jwtToken.token } returns jwt

            And("calling findById") {
                When("the room exists") {
                    coEvery { roomRepository.findById(room.id) } returns room

                    Then("it should return the room") {
                        val result = roomService.findById(room.id)

                        result shouldBe room
                    }
                }
            }

            And("calling findByHotelId") {
                When("the room exists") {
                    val hotelId = hotel.id

                    Then("it should return the rooms for the hotel") {
                        coEvery { roomRepository.findByHotelId(hotelId) } returns listOf(room)

                        val result = roomService.findByHotelId(hotelId)

                        result shouldBe listOf(room)
                    }
                }

                When("the room does not exist") {
                    val hotelId = hotel.id

                    Then("it should return an empty list") {
                        coEvery { roomRepository.findByHotelId(hotelId) } returns emptyList()

                        val result = roomService.findByHotelId(hotelId)

                        result shouldBe emptyList()
                    }
                }
            }

            And("calling createRoom") {
                val createRoomRequest = CreateRoomRequestStubs.create(hotelId = hotel.id)

                When("requester has permissions") {
                    every { jwt.claims } returns mapOf(AuthClaimKey.USERID.key to partner.id)

                    And("not already exists a room with the same number") {
                        Then("it should create a room") {
                            coEvery { hotelService.findById(hotel.id) } returns hotel
                            coEvery { roomRepository.findByHotelId(hotel.id) } returns listOf(anotherRoom)
                            coEvery { roomRepository.create(any()) } returns room

                            val result = roomService.createRoom(createRoomRequest, jwtToken)

                            result shouldBe room
                        }
                    }

                    And("requester does not have permissions") {
                        every { jwt.claims } returns mapOf(AuthClaimKey.USERID.key to anotherPartner.id)

                        Then("it should throw an exception") {
                            coEvery { hotelService.findById(hotel.id) } returns hotel

                            val exception =
                                shouldThrow<HotelaException.InvalidCredentialsException> {
                                    roomService.createRoom(createRoomRequest, jwtToken)
                                }

                            coVerify(exactly = 0) { roomRepository.create(any()) }

                            exception.code shouldBe HotelaException.INVALID_CREDENTIALS
                            exception.message shouldBe "Invalid credentials"
                        }
                    }

                    And("already exists a room with the same number") {
                        val createRoomRequestWithSameNumber =
                            CreateRoomRequestStubs.create(
                                hotelId = hotel.id,
                                roomCode = anotherRoomWithSameNumber.roomCode,
                                floor = anotherRoomWithSameNumber.floor,
                            )

                        Then("it should throw an exception") {
                            coEvery { hotelService.findById(hotel.id) } returns hotel
                            coEvery { roomRepository.findByHotelId(any()) } returns listOf(anotherRoom, room)

                            val exception =
                                shouldThrow<HotelaException.RoomAlreadyExistsException> {
                                    roomService.createRoom(createRoomRequestWithSameNumber, jwtToken)
                                }

                            coVerify(exactly = 0) { roomRepository.create(any()) }

                            exception.code shouldBe HotelaException.ROOM_ALREADY_EXISTS
                            exception.message shouldBe "Room with number ${room.roomCode} already exists"
                        }
                    }
                }
            }

            When("calling updateRoom") {
                And("requester has permissions") {
                    every { jwt.claims } returns mapOf(AuthClaimKey.USERID.key to partner.id)
                    coEvery { roomRepository.findById(any()) } returns room
                    coEvery { hotelService.findById(any()) } returns hotel

                    And("already exists a room with the same number") {
                        coEvery { roomRepository.findByHotelId(any()) } returns
                            listOf(
                                room,
                                anotherRoomWithSameNumber,
                                anotherRoom,
                            )

                        val updateRoomRequestWithSameNumber =
                            UpdateRoomRequestStubs.create(
                                roomCode = anotherRoomWithSameNumber.roomCode,
                                floor = anotherRoomWithSameNumber.floor,
                            )

                        Then("it should throw an exception") {
                            val exception =
                                shouldThrow<HotelaException.RoomAlreadyExistsException> {
                                    roomService.updateRoom(
                                        anotherRoomWithSameNumber.id,
                                        updateRoomRequestWithSameNumber,
                                        jwtToken,
                                    )
                                }

                            coVerify(exactly = 0) { roomRepository.update(any()) }

                            exception.code shouldBe HotelaException.ROOM_ALREADY_EXISTS
                            exception.message shouldBe "Room with number ${room.roomCode} already exists"
                        }
                    }

                    And("not already exists a room with the same number") {
                        coEvery { roomRepository.findByHotelId(hotel.id) } returns listOf(room, anotherRoom)
                        val updateRoomRequest = UpdateRoomRequestStubs.create()

                        Then("it should update the room") {
                            coEvery { roomRepository.update(any()) } returns room

                            val result = roomService.updateRoom(room.id, updateRoomRequest, jwtToken)

                            result shouldBe room
                        }
                    }
                }

                And("requester does not have permissions") {
                    every { jwt.claims } returns mapOf(AuthClaimKey.USERID.key to anotherPartner.id)
                    val updateRoomRequest = UpdateRoomRequestStubs.create()

                    Then("it should throw an exception") {
                        coEvery { hotelService.findById(hotel.id) } returns hotel
                        coEvery { roomRepository.findById(room.id) } returns room

                        val exception =
                            shouldThrow<HotelaException.InvalidCredentialsException> {
                                roomService.updateRoom(room.id, updateRoomRequest, jwtToken)
                            }

                        coVerify(exactly = 0) { roomRepository.update(any()) }

                        exception.code shouldBe HotelaException.INVALID_CREDENTIALS
                        exception.message shouldBe "Invalid credentials"
                    }
                }
            }
        }
    })
