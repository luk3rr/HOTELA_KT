package com.hotela.service

import com.hotela.error.HotelaException
import com.hotela.model.enum.AuthClaimKey
import com.hotela.repository.PartnerRepository
import com.hotela.stubs.db.CustomerStubs
import com.hotela.stubs.db.PartnerStubs
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
        val partnerService =
            PartnerService(
                partnerRepository = partnerRepository,
            )

        val jwtToken = mockk<JwtAuthenticationToken>()
        val jwt = mockk<Jwt>()

        Given("a partner service") {
            val partner = PartnerStubs.create()
            val customer = CustomerStubs.create()
            val updatePartnerRequest = UpdatePartnerRequestStubs.create()

            every { jwtToken.token } returns jwt

            val partnerUpdated =
                partner.copy(
                    companyName = updatePartnerRequest.companyName ?: partner.companyName,
                    legalName = updatePartnerRequest.legalName ?: partner.legalName,
                    contactInfo = updatePartnerRequest.contactInfo ?: partner.contactInfo,
                    documentId = updatePartnerRequest.documentId ?: partner.documentId,
                    contractSignedAt =
                        updatePartnerRequest.contractSignedAt
                            ?: partner.contractSignedAt,
                    status = updatePartnerRequest.status ?: partner.status,
                    notes = updatePartnerRequest.notes ?: partner.notes,
                )

            When("calling findById") {
                And("the partner exists") {
                    Then("it should return the partner") {
                        coEvery { partnerRepository.findById(partner.id) } returns partner

                        val result = partnerService.findById(partner.id)

                        result shouldBe partner
                    }
                }

                And("the partner does not exist") {
                    Then("it should return null") {
                        coEvery { partnerRepository.findById(partner.id) } returns null

                        val result = partnerService.findById(partner.id)

                        result shouldBe null
                    }
                }
            }

            When("calling findByEmail") {
                And("the partner exists") {
                    Then("it should return the partner") {
                        coEvery { partnerRepository.findByEmail(partner.contactInfo.email) } returns partner

                        val result = partnerService.findByEmail(partner.contactInfo.email)

                        result shouldBe partner
                    }
                }

                And("the partner does not exist") {
                    Then("it should return null") {
                        coEvery { partnerRepository.findByEmail(partner.contactInfo.email) } returns null

                        val result = partnerService.findByEmail(partner.contactInfo.email)

                        result shouldBe null
                    }
                }
            }

            When("calling existsByEmail") {
                And("the partner exists") {
                    Then("it should return true") {
                        coEvery { partnerRepository.existsByEmail(partner.contactInfo.email) } returns true

                        val result = partnerService.existsByEmail(partner.contactInfo.email)

                        result shouldBe true
                    }
                }

                When("the partner does not exist") {
                    Then("it should return false") {
                        coEvery { partnerRepository.existsByEmail(partner.contactInfo.email) } returns false

                        val result = partnerService.existsByEmail(partner.contactInfo.email)

                        result shouldBe false
                    }
                }
            }

            When("calling createPartner") {
                And("the partner does not exist") {
                    coEvery { partnerRepository.existsByEmail(partner.contactInfo.email) } returns false

                    Then("it should create the partner") {
                        coEvery { partnerRepository.create(partner) } returns partner

                        val result = partnerService.create(partner)

                        result shouldBe partner
                    }
                }

                And("the partner already exists") {
                    coEvery { partnerRepository.existsByEmail(partner.contactInfo.email) } returns true

                    Then("it should throw an exception") {
                        val exception =
                            shouldThrow<HotelaException.EmailAlreadyRegisteredException> {
                                partnerService.create(partner)
                            }

                        exception.code shouldBe HotelaException.EMAIL_ALREADY_REGISTERED
                        exception.message shouldBe "Email already registered"
                    }
                }
            }

            When("calling updatePartner") {
                And("the partner exists") {
                    coEvery { partnerRepository.findById(partner.id) } returns partner

                    every { jwt.claims } returns mapOf(AuthClaimKey.USERID.key to partner.id)

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

                And("the partner does not exist") {
                    every { jwt.claims } returns
                        mapOf(
                            AuthClaimKey.USERID.key to customer.id,
                        )

                    coEvery { partnerRepository.findById(customer.id) } returns null

                    Then("it should throw an exception") {
                        val exception =
                            shouldThrow<HotelaException.PartnerNotFoundException> {
                                partnerService.updatePartner(
                                    payload = updatePartnerRequest,
                                    token = jwtToken,
                                )
                            }

                        exception.code shouldBe HotelaException.PARTNER_NOT_FOUND
                        exception.message shouldBe "Partner with id ${customer.id} not found"
                    }
                }
            }
        }
    })
