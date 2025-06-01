package com.hotela.service

import com.hotela.error.HotelaException
import com.hotela.model.enum.AuthClaimKey
import com.hotela.model.enum.Role
import com.hotela.repository.AddressRepository
import com.hotela.repository.HotelRepository
import com.hotela.stubs.db.AddressStubs
import com.hotela.stubs.db.CustomerStubs
import com.hotela.stubs.db.HotelStubs
import com.hotela.stubs.db.PartnerStubs
import com.hotela.stubs.dto.request.CreateHotelRequestStubs
import com.hotela.stubs.dto.request.UpdateHotelRequestStubs
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import java.util.UUID

class HotelServiceTest :
    BehaviorSpec({
        val hotelRepository = mockk<HotelRepository>()
        val addressRepository = mockk<AddressRepository>()
        val hotelService = HotelService(hotelRepository, addressRepository)

        val jwtToken = mockk<JwtAuthenticationToken>()
        val jwt = mockk<Jwt>()

        Given("a hotel service") {
            val partner = PartnerStubs.create()
            val anotherPartner =
                PartnerStubs.create(
                    id = UUID.fromString("f40ccfb1-d579-4916-aa38-646cd897a799"),
                )
            val customer = CustomerStubs.create()

            val address = AddressStubs.create()
            val hotel = HotelStubs.create(partnerId = partner.id, addressId = address.id)
            val createHotelRequest = CreateHotelRequestStubs.create()
            val updateHotelRequest = UpdateHotelRequestStubs.create()

            every { jwtToken.token } returns jwt

            And("a valid hotel id") {
                When("calling findById") {
                    val hotelId = hotel.id

                    Then("it should return the hotel") {
                        coEvery { hotelRepository.findById(hotelId) } returns hotel

                        val result = hotelService.findById(hotelId)

                        result shouldBe hotel
                    }
                }

                When("calling updateHotel") {
                    And("partner that matches the hotel") {
                        every { jwt.claims } returns
                            mapOf(
                                AuthClaimKey.ROLE.key to Role.PARTNER,
                                AuthClaimKey.USERID.key to partner.id,
                            )

                        Then("it should update the hotel") {
                            coEvery { hotelRepository.findById(hotel.id) } returns hotel
                            coEvery { hotelRepository.update(any()) } returns hotel

                            val result = hotelService.updateHotel(hotel.id, updateHotelRequest, jwtToken)

                            result shouldBe hotel
                        }
                    }

                    And("partner that does not match the hotel") {
                        every { jwt.claims } returns
                            mapOf(
                                AuthClaimKey.ROLE.key to Role.PARTNER,
                                AuthClaimKey.USERID.key to anotherPartner.id,
                            )

                        Then("it should throw an exception") {
                            coEvery { hotelRepository.findById(hotel.id) } returns hotel

                            val exception =
                                shouldThrow<HotelaException.AccessDeniedException> {
                                    hotelService.updateHotel(hotel.id, updateHotelRequest, jwtToken)
                                }

                            exception.code shouldBe HotelaException.ACCESS_DENIED
                            exception.message shouldBe "Access denied"
                        }
                    }

                    And("token with customer role ") {
                        every { jwt.claims } returns
                            mapOf(
                                AuthClaimKey.ROLE.key to Role.CUSTOMER,
                                AuthClaimKey.USERID.key to customer.id,
                            )

                        Then("it should throw an exception") {
                            coEvery { hotelRepository.findById(hotel.id) } returns hotel

                            val exception =
                                shouldThrow<HotelaException.AccessDeniedException> {
                                    hotelService.updateHotel(hotel.id, updateHotelRequest, jwtToken)
                                }

                            exception.code shouldBe HotelaException.ACCESS_DENIED
                            exception.message shouldBe "Access denied"
                        }
                    }
                }
            }

            And("an invalid hotel id") {
                When("calling findById") {
                    val hotelId = hotel.id

                    Then("it should return null") {
                        coEvery { hotelRepository.findById(hotelId) } returns null

                        hotelService.findById(hotelId) shouldBe null
                    }
                }

                When("calling updateHotel") {
                    every { jwt.claims } returns
                        mapOf(
                            AuthClaimKey.ROLE.key to Role.PARTNER,
                            AuthClaimKey.USERID.key to partner.id,
                        )

                    Then("it should throw an exception") {
                        coEvery { hotelRepository.findById(hotel.id) } returns null

                        val exception =
                            shouldThrow<HotelaException.HotelNotFoundException> {
                                hotelService.updateHotel(hotel.id, updateHotelRequest, jwtToken)
                            }
                        exception.code shouldBe HotelaException.HOTEL_NOT_FOUND
                        exception.message shouldBe "Hotel with id ${hotel.id} not found"
                    }
                }
            }

            And("a valid partner token") {
                When("calling findByPartnerId") {
                    Then("it should return a list of hotels") {
                        coEvery { hotelRepository.findByPartnerId(partner.id) } returns listOf(hotel)

                        val result = hotelService.findByPartnerId(partner.id)

                        result.first() shouldBe hotel
                    }
                }

                When("calling createHotel") {
                    every { jwt.claims } returns
                        mapOf(
                            AuthClaimKey.ROLE.key to Role.PARTNER,
                            AuthClaimKey.USERID.key to partner.id,
                        )

                    Then("it should create a new hotel") {
                        coEvery { hotelRepository.create(any()) } returns hotel
                        coEvery { addressRepository.create(any()) } returns address

                        val result = hotelService.createHotel(createHotelRequest, jwtToken)

                        result shouldBe hotel
                    }
                }
            }
        }
    })
