package com.hotela.service

import com.hotela.error.HotelaException
import com.hotela.model.enum.AuthClaimKey
import com.hotela.repository.HotelRepository
import com.hotela.stubs.database.HotelStubs
import com.hotela.stubs.database.PartnerAuthStubs
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
        val partnerAuthService = mockk<PartnerAuthService>()
        val hotelRepository = mockk<HotelRepository>()
        val hotelService = HotelService(partnerAuthService, hotelRepository)

        val jwtToken = mockk<JwtAuthenticationToken>()
        val jwt = mockk<Jwt>()

        Given("a hotel service") {
            val hotel = HotelStubs.create()
            val createHotelRequest = CreateHotelRequestStubs.create()
            val updateHotelRequest = UpdateHotelRequestStubs.create()
            val partnerAuth =
                PartnerAuthStubs.create(
                    partnerId = hotel.partnerId,
                )
            val anotherPartnerAuth =
                PartnerAuthStubs.create(
                    id = UUID.fromString("48bdb90b-9481-4278-b352-128c19d4e840"),
                    partnerId = UUID.fromString("a4e183ba-6c2d-416e-936e-20172e8fc219"),
                )

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

                When("calling updateHotel with a valid partner auth id") {
                    every { jwt.claims } returns mapOf(AuthClaimKey.AUTHID.key to partnerAuth.id.toString())

                    Then("it should update the hotel") {
                        coEvery { hotelRepository.findById(hotel.id) } returns hotel
                        coEvery { partnerAuthService.findById(partnerAuth.id) } returns partnerAuth
                        coEvery { hotelRepository.update(any()) } returns hotel

                        val result = hotelService.updateHotel(hotel.id, updateHotelRequest, jwtToken)

                        result shouldBe hotel
                    }
                }

                When("calling updateHotel with an invalid partner auth id") {
                    every { jwt.claims } returns mapOf(AuthClaimKey.AUTHID.key to anotherPartnerAuth.id)

                    Then("it should throw an exception") {
                        coEvery { hotelRepository.findById(hotel.id) } returns hotel
                        coEvery { partnerAuthService.findById(any()) } returns null

                        val exception =
                            shouldThrow<HotelaException.InvalidCredentialsException> {
                                hotelService.updateHotel(hotel.id, updateHotelRequest, jwtToken)
                            }

                        exception.code shouldBe HotelaException.INVALID_CREDENTIALS
                        exception.message shouldBe "Invalid credentials"
                    }
                }

                When("calling updateHotel with a partner auth id that does not match the partner auth id in the hotel") {
                    every { jwt.claims } returns mapOf(AuthClaimKey.AUTHID.key to anotherPartnerAuth.id)

                    Then("it should throw an exception") {
                        coEvery { hotelRepository.findById(hotel.id) } returns hotel
                        coEvery { partnerAuthService.findById(anotherPartnerAuth.id) } returns anotherPartnerAuth

                        val exception =
                            shouldThrow<HotelaException.AccessDeniedException> {
                                hotelService.updateHotel(hotel.id, updateHotelRequest, jwtToken)
                            }

                        exception.code shouldBe HotelaException.ACCESS_DENIED
                        exception.message shouldBe "Access denied"
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
            }

            And("a valid partner id") {
                When("calling findByPartnerId") {
                    val partnerId = hotel.partnerId

                    Then("it should return a list of hotels") {
                        coEvery { hotelRepository.findByPartnerId(partnerId) } returns listOf(hotel)

                        val result = hotelService.findByPartnerId(partnerId)

                        result.first() shouldBe hotel
                    }
                }
            }

            And("a valid partner auth id") {
                every { jwt.claims } returns mapOf(AuthClaimKey.AUTHID.key to partnerAuth.id)

                When("calling createHotel") {
                    Then("it should create a new hotel") {
                        coEvery { partnerAuthService.findById(partnerAuth.id) } returns partnerAuth
                        coEvery { hotelRepository.create(any()) } returns hotel

                        val result = hotelService.createHotel(createHotelRequest, jwtToken)

                        result shouldBe hotel
                    }
                }
            }

            And("an invalid partner auth id") {
                every { jwt.claims } returns mapOf(AuthClaimKey.AUTHID.key to anotherPartnerAuth.id)

                When("calling createHotel") {
                    Then("it should throw an exception") {
                        coEvery { partnerAuthService.findById(any()) } returns null

                        val exception =
                            shouldThrow<HotelaException.InvalidCredentialsException> {
                                hotelService.createHotel(createHotelRequest, jwtToken)
                            }

                        exception.code shouldBe HotelaException.INVALID_CREDENTIALS
                        exception.message shouldBe "Invalid credentials"
                    }
                }
            }
        }
    })
