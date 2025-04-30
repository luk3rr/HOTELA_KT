package com.hotela.service

import com.hotela.error.HotelaException
import com.hotela.model.enum.AuthClaimKey
import com.hotela.repository.CustomerAuthRepository
import com.hotela.repository.CustomerRepository
import com.hotela.stubs.database.CustomerAuthStubs
import com.hotela.stubs.database.CustomerStubs
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
        val customerAuthRepository = mockk<CustomerAuthRepository>()
        val customerService =
            CustomerService(
                customerRepository = customerRepository,
                customerAuthRepository = customerAuthRepository,
            )

        val jwtToken = mockk<JwtAuthenticationToken>()
        val jwt = mockk<Jwt>()

        Given("a customer service") {
            val customer = CustomerStubs.create()
            val customerAuth = CustomerAuthStubs.create(customer.id)
            val updateCustomerRequest = UpdateCustomerRequestStubs.create()

            require(
                customer.name != updateCustomerRequest.name &&
                    customer.phone != updateCustomerRequest.phone &&
                    customer.idDocument != updateCustomerRequest.idDocument &&
                    customer.birthDate != updateCustomerRequest.birthDate &&
                    customer.address != updateCustomerRequest.address,
            ) {
                "Customer and UpdateCustomerRequest should have different values"
            }

            val customerUpdated =
                customer.copy(
                    name = updateCustomerRequest.name ?: customer.name,
                    phone = updateCustomerRequest.phone ?: customer.phone,
                    idDocument = updateCustomerRequest.idDocument ?: customer.idDocument,
                    birthDate = updateCustomerRequest.birthDate ?: customer.birthDate,
                    address = updateCustomerRequest.address ?: customer.address,
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
                    coEvery { customerRepository.findById(customer.id) } returns null

                    Then("it should create the customer") {
                        coEvery { customerRepository.create(customer) } returns customer

                        val result = customerService.createCustomer(customer)

                        result shouldBe customer
                    }
                }

                When("the customer already exists") {
                    coEvery { customerRepository.findById(customer.id) } returns customer

                    Then("it should throw an exception") {
                        val exception =
                            shouldThrow<HotelaException.CustomerAlreadyExistsException> {
                                customerService.createCustomer(customer)
                            }

                        exception.code shouldBe HotelaException.CUSTOMER_ALREADY_EXISTS
                        exception.message shouldBe "Customer with id ${customer.id} already exists"
                    }
                }
            }

            And("calling updateCustomer") {
                When("the customer exists") {
                    coEvery { customerRepository.findById(customer.id) } returns customer

                    And("requester is the same as customer") {
                        every { jwtToken.token } returns jwt
                        every { jwt.claims } returns mapOf(AuthClaimKey.AUTHID.key to customerAuth.id.toString())

                        Then("it should update the customer") {
                            coEvery { customerAuthRepository.findById(customerAuth.id) } returns customerAuth
                            coEvery { customerRepository.update(any()) } returns customerUpdated

                            val result =
                                customerService.updateCustomer(
                                    payload = updateCustomerRequest,
                                    token = jwtToken,
                                )

                            result shouldBe customerUpdated
                        }
                    }

                    And("customer id is not associated with customer auth id") {
                        coEvery { customerAuthRepository.findById(customerAuth.id) } returns null

                        Then("it should throw an exception") {
                            every { jwtToken.token } returns jwt
                            every { jwt.claims } returns mapOf(AuthClaimKey.AUTHID.key to customerAuth.id.toString())

                            val exception =
                                shouldThrow<HotelaException.CustomerAuthNotFoundException> {
                                    customerService.updateCustomer(
                                        payload = updateCustomerRequest,
                                        token = jwtToken,
                                    )
                                }

                            exception.code shouldBe HotelaException.CUSTOMER_AUTH_NOT_FOUND
                            exception.message shouldBe "Customer auth with id ${customerAuth.id} not found"
                        }
                    }

                    And("requester is not a customer") {
                        every { jwtToken.token } returns jwt
                        every { jwt.claims } returns mapOf("other_claim" to "some_value")

                        Then("it should throw an exception") {
                            coEvery { customerAuthRepository.findById(customerAuth.id) } returns customerAuth

                            coEvery { customerRepository.update(any()) } returns customerUpdated

                            val exception =
                                shouldThrow<HotelaException.InvalidCredentialsException> {
                                    customerService.updateCustomer(
                                        payload = updateCustomerRequest,
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
