package com.hotela.service

import com.hotela.error.HotelaException
import com.hotela.model.enum.AuthClaimKey
import com.hotela.repository.PartnerAuthRepository
import com.hotela.repository.PartnerRepository
import com.hotela.stubs.database.PartnerAuthStubs
import com.hotela.stubs.database.PartnerStubs
import com.hotela.stubs.dto.request.UpdatePartnerRequestStubs
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken

class PartnerServiceTest :
    BehaviorSpec({
        val partnerRepository = mockk<PartnerRepository>()
        val partnerAuthRepository = mockk<PartnerAuthRepository>()
        val partnerService =
            PartnerService(
                partnerRepository = partnerRepository,
                partnerAuthRepository = partnerAuthRepository,
            )

        val jwtToken = mockk<JwtAuthenticationToken>()
        val jwt = mockk<Jwt>()

        Given("a partner service") {
            val partner = PartnerStubs.create()
            val partnerAuth = PartnerAuthStubs.create(partnerId = partner.id)
            val updatePartnerRequest = UpdatePartnerRequestStubs.create()

            require(
                partner.name != updatePartnerRequest.name &&
                    partner.cnpj != updatePartnerRequest.cnpj &&
                    partner.phone != updatePartnerRequest.phone &&
                    partner.address != updatePartnerRequest.address &&
                    partner.contactName != updatePartnerRequest.contactName &&
                    partner.contactPhone != updatePartnerRequest.contactPhone &&
                    partner.contactEmail != updatePartnerRequest.contactEmail &&
                    partner.contractSigned != updatePartnerRequest.contractSigned &&
                    partner.status != updatePartnerRequest.status &&
                    partner.notes != updatePartnerRequest.notes,
            ) {
                "Partner and UpdatePartnerRequest should have different values"
            }

            val partnerUpdated =
                partner.copy(
                    name = updatePartnerRequest.name ?: partner.name,
                    cnpj = updatePartnerRequest.cnpj ?: partner.cnpj,
                    phone = updatePartnerRequest.phone ?: partner.phone,
                    address = updatePartnerRequest.address ?: partner.address,
                    contactName = updatePartnerRequest.contactName ?: partner.contactName,
                    contactPhone = updatePartnerRequest.contactPhone ?: partner.contactPhone,
                    contactEmail = updatePartnerRequest.contactEmail ?: partner.contactEmail,
                    contractSigned =
                        updatePartnerRequest.contractSigned
                            ?: partner.contractSigned,
                    status = updatePartnerRequest.status ?: partner.status,
                    notes = updatePartnerRequest.notes ?: partner.notes,
                )

            And("calling findById") {
                When("the partner exists") {
                    Then("it should return the partner") {
                        coEvery { partnerRepository.findById(partner.id) } returns partner

                        val result = partnerService.findById(partner.id)

                        result shouldBe partner
                    }
                }

                When("the partner does not exist") {
                    Then("it should return null") {
                        coEvery { partnerRepository.findById(partner.id) } returns null

                        val result = partnerService.findById(partner.id)

                        result shouldBe null
                    }
                }
            }

            And("calling findByEmail") {
                When("the partner exists") {
                    Then("it should return the partner") {
                        coEvery { partnerRepository.findByEmail(partner.email) } returns partner

                        val result = partnerService.findByEmail(partner.email)

                        result shouldBe partner
                    }
                }

                When("the partner does not exist") {
                    Then("it should return null") {
                        coEvery { partnerRepository.findByEmail(partner.email) } returns null

                        val result = partnerService.findByEmail(partner.email)

                        result shouldBe null
                    }
                }
            }

            And("calling existsByEmail") {
                When("the partner exists") {
                    Then("it should return true") {
                        coEvery { partnerRepository.existsByEmail(partner.email) } returns true

                        val result = partnerService.existsByEmail(partner.email)

                        result shouldBe true
                    }
                }

                When("the partner does not exist") {
                    Then("it should return false") {
                        coEvery { partnerRepository.existsByEmail(partner.email) } returns false

                        val result = partnerService.existsByEmail(partner.email)

                        result shouldBe false
                    }
                }
            }

            And("calling createPartner") {
                When("the partner does not exist") {
                    coEvery { partnerRepository.existsByEmail(partner.email) } returns false

                    Then("it should create the partner") {
                        coEvery { partnerRepository.create(partner) } returns partner

                        val result = partnerService.createPartner(partner)

                        result shouldBe partner
                    }
                }

                When("the partner already exists") {
                    coEvery { partnerRepository.existsByEmail(partner.email) } returns true

                    Then("it should throw an exception") {
                        val exception =
                            shouldThrow<HotelaException.EmailAlreadyRegisteredException> {
                                partnerService.createPartner(partner)
                            }

                        exception.code shouldBe HotelaException.EMAIL_ALREADY_REGISTERED
                        exception.message shouldBe "Email already registered"
                    }
                }
            }

            And("calling updatePartner") {
                When("the partner exists") {
                    coEvery { partnerRepository.findById(partner.id) } returns partner

                    And("requester is the same as partner") {
                        every { jwtToken.token } returns jwt
                        every { jwt.claims } returns mapOf(AuthClaimKey.AUTHID.key to partnerAuth.id.toString())
                        coEvery { partnerAuthRepository.findById(partnerAuth.id) } returns partnerAuth

                        Then("it should update the partner") {
                            coEvery { partnerRepository.update(any()) } returns partnerUpdated

                            val result =
                                partnerService.updatePartner(
                                    payload = updatePartnerRequest,
                                    token = jwtToken,
                                )

                            result shouldBe partnerUpdated
                        }
                    }

                    And("partner id is not associated with partner auth id") {
                        every { jwtToken.token } returns jwt
                        every { jwt.claims } returns mapOf(AuthClaimKey.AUTHID.key to partnerAuth.id.toString())
                        coEvery { partnerAuthRepository.findById(partnerAuth.id) } returns null

                        Then("it should throw an exception") {
                            val exception =
                                shouldThrow<HotelaException.PartnerAuthNotFoundException> {
                                    partnerService.updatePartner(
                                        payload = updatePartnerRequest,
                                        token = jwtToken,
                                    )
                                }

                            exception.code shouldBe HotelaException.PARTNER_AUTH_NOT_FOUND
                            exception.message shouldBe "Partner auth with id ${partnerAuth.id} not found"
                        }
                    }

                    And("requester is not a partner") {
                        every { jwtToken.token } returns jwt
                        every { jwt.claims } returns mapOf("other_claim" to "some_value")

                        Then("it should throw an exception") {
                            val exception =
                                shouldThrow<HotelaException.InvalidCredentialsException> {
                                    partnerService.updatePartner(
                                        payload = updatePartnerRequest,
                                        token = jwtToken,
                                    )
                                }

                            exception.code shouldBe HotelaException.INVALID_CREDENTIALS
                            exception.message shouldBe "Invalid credentials"
                        }
                    }
                }
            }
        }
    })
