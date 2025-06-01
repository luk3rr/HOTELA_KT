package com.hotela.service

import com.hotela.error.HotelaException
import com.hotela.model.enum.AuthClaimKey
import com.hotela.repository.CustomerRepository
import com.hotela.stubs.db.CustomerStubs
import com.hotela.stubs.dto.request.UpdateCustomerRequestStubs
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken

class CustomerServiceTest :
    BehaviorSpec({
        val customerRepository = mockk<CustomerRepository>()
        val customerService =
            CustomerService(
                customerRepository = customerRepository,
            )

        val jwtToken = mockk<JwtAuthenticationToken>()
        val jwt = mockk<Jwt>()

        Given("a customer service") {
            val customer = CustomerStubs.create()
            val updateCustomerRequest = UpdateCustomerRequestStubs.create()

            val customerUpdated =
                customer.copy(
                    name = updateCustomerRequest.name ?: customer.name,
                    contactInfo = updateCustomerRequest.contactInfo ?: customer.contactInfo,
                    documentId = updateCustomerRequest.documentId ?: customer.documentId,
                    birthDate = updateCustomerRequest.birthDate ?: customer.birthDate,
                )

            And("calling findById") {
                When("the customer exists") {
                    Then("it should return the customer") {
                        coEvery { customerRepository.findById(customer.id) } returns customer

                        val result = customerService.findById(customer.id)

                        result shouldBe customer
                    }
                }

                When("the customer does not exist") {
                    Then("it should return null") {
                        coEvery { customerRepository.findById(customer.id) } returns null

                        val result = customerService.findById(customer.id)

                        result shouldBe null
                    }
                }
            }

            And("calling createCustomer") {
                When("the customer does not exist") {
                    coEvery { customerRepository.existsByEmail(customer.contactInfo.email) } returns false

                    Then("it should create the customer") {
                        coEvery { customerRepository.create(customer) } returns customer

                        val result = customerService.create(customer)

                        result shouldBe customer
                    }
                }

                When("the customer already exists") {
                    coEvery { customerRepository.existsByEmail(customer.contactInfo.email) } returns true

                    Then("it should throw an exception") {
                        val exception =
                            shouldThrow<HotelaException.EmailAlreadyRegisteredException> {
                                customerService.create(customer)
                            }

                        exception.code shouldBe HotelaException.EMAIL_ALREADY_REGISTERED
                        exception.message shouldBe "Email already registered"
                    }
                }
            }

            And("calling updateCustomer") {
                When("the customer exists") {
                    coEvery { customerRepository.findById(customer.id) } returns customer

                    And("requester is the same as customer") {
                        every { jwtToken.token } returns jwt
                        every { jwt.claims } returns mapOf(AuthClaimKey.USERID.key to customer.id.toString())

                        Then("it should update the customer") {
                            coEvery { customerRepository.update(any()) } returns customerUpdated

                            val result =
                                customerService.updateCustomer(
                                    payload = updateCustomerRequest,
                                    token = jwtToken,
                                )

                            result shouldBe customerUpdated
                        }
                    }
                }

                When("the customer does not exist") {
                    coEvery { customerRepository.findById(customer.id) } returns null
                    every { jwtToken.token } returns jwt
                    every { jwt.claims } returns mapOf(AuthClaimKey.USERID.key to customer.id.toString())

                    Then("it should throw an exception") {
                        val exception =
                            shouldThrow<HotelaException.CustomerNotFoundException> {
                                customerService.updateCustomer(
                                    payload = updateCustomerRequest,
                                    token = jwtToken,
                                )
                            }

                        exception.code shouldBe HotelaException.CUSTOMER_NOT_FOUND
                        exception.message shouldBe "Customer with id ${customer.id} not found"
                    }
                }
            }
        }
    })
