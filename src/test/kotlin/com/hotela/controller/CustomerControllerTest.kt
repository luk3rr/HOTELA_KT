package com.hotela.controller

import com.hotela.asCustomer
import com.hotela.asGuest
import com.hotela.asPartner
import com.hotela.error.ErrorResponse
import com.hotela.error.HotelaException
import com.hotela.model.database.Customer
import com.hotela.model.dto.response.ResourceUpdatedResponse
import com.hotela.service.CustomerService
import com.hotela.stubs.database.CustomerAuthStubs
import com.hotela.stubs.database.CustomerStubs
import com.hotela.stubs.request.UpdateCustomerRequestStubs
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

@WebFluxTest(controllers = [CustomerController::class])
class CustomerControllerTest(
    private val webTestClient: WebTestClient,
    private val customerService: CustomerService,
) : FunSpec() {
    override fun extensions() = listOf(SpringExtension)

    @TestConfiguration
    class MockBeans {
        @Bean
        fun customerService(): CustomerService = mockk<CustomerService>()
    }

    init {
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

        context("GET /customer/{id}") {
            context("when the customer exists") {
                test("should return 200 OK") {
                    coEvery {
                        customerService.findById(any())
                    } returns customer

                    val response =
                        webTestClient
                            .asCustomer(customer.id, customerAuth.id)
                            .get()
                            .uri("/customer/${customer.id}")
                            .exchange()
                            .expectStatus()
                            .isOk
                            .expectHeader()
                            .contentType(MediaType.APPLICATION_JSON)
                            .expectBody(Customer::class.java)
                            .returnResult()
                            .responseBody!!

                    response.id shouldBe customer.id
                }
            }

            context("when the customer does not exist") {
                test("should return 404 NOT FOUND") {
                    coEvery {
                        customerService.findById(any())
                    } returns null

                    val response =
                        webTestClient
                            .asCustomer(customer.id, customerAuth.id)
                            .get()
                            .uri("/customer/${customer.id}")
                            .exchange()
                            .expectStatus()
                            .isNotFound
                            .expectBody(ErrorResponse::class.java)
                            .returnResult()
                            .responseBody!!

                    response.code shouldBe HotelaException.CUSTOMER_NOT_FOUND
                    response.message shouldBe "Customer with id ${customer.id} not found"
                }
            }
        }

        context("PUT /customer/update/{id}") {
            val customerUpdated =
                customer.copy(
                    name = updateCustomerRequest.name ?: customer.name,
                    phone = updateCustomerRequest.phone ?: customer.phone,
                    idDocument = updateCustomerRequest.idDocument ?: customer.idDocument,
                    birthDate = updateCustomerRequest.birthDate ?: customer.birthDate,
                    address = updateCustomerRequest.address ?: customer.address,
                )

            context("when the customer exists") {
                test("should return 200 OK") {
                    coEvery {
                        customerService.updateCustomer(any(), any())
                    } returns customerUpdated

                    val response =
                        webTestClient
                            .asCustomer(customer.id, customerAuth.id)
                            .put()
                            .uri("/customer/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(updateCustomerRequest)
                            .exchange()
                            .expectStatus()
                            .isOk
                            .expectHeader()
                            .contentType(MediaType.APPLICATION_JSON)
                            .expectBody(ResourceUpdatedResponse::class.java)
                            .returnResult()
                            .responseBody!!

                    response.message shouldBe "Customer updated successfully"
                }
            }

            context("when the requester is a customer but not the owner") {
                test("should return 401 UNAUTHORIZED") {
                    coEvery {
                        customerService.updateCustomer(any(), any())
                    } throws HotelaException.InvalidCredentialsException()

                    val response =
                        webTestClient
                            .asCustomer(customer.id, customerAuth.id)
                            .put()
                            .uri("/customer/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(updateCustomerRequest)
                            .exchange()
                            .expectStatus()
                            .isUnauthorized
                            .expectBody(ErrorResponse::class.java)
                            .returnResult()
                            .responseBody!!

                    response.code shouldBe HotelaException.INVALID_CREDENTIALS
                    response.message shouldBe "Invalid credentials"
                }
            }

            context("when the requester is not a customer") {
                test("should return 403 FORBIDDEN when the requester is a partner") {
                    webTestClient
                        .asPartner(customer.id, customerAuth.id)
                        .put()
                        .uri("/customer/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(updateCustomerRequest)
                        .exchange()
                        .expectStatus()
                        .isForbidden
                }

                test("should return 403 FORBIDDEN when the requester is a guest") {
                    webTestClient
                        .asGuest()
                        .put()
                        .uri("/customer/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(updateCustomerRequest)
                        .exchange()
                        .expectStatus()
                }
            }

            context("when the customer does not exist") {
                test("should return 404 NOT FOUND") {
                    coEvery {
                        customerService.updateCustomer(any(), any())
                    } throws HotelaException.CustomerNotFoundException(customer.id)

                    val response =
                        webTestClient
                            .asCustomer(customer.id, customerAuth.id)
                            .put()
                            .uri("/customer/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(updateCustomerRequest)
                            .exchange()
                            .expectStatus()
                            .isNotFound
                            .expectBody(ErrorResponse::class.java)
                            .returnResult()
                            .responseBody!!

                    response.code shouldBe HotelaException.CUSTOMER_NOT_FOUND
                    response.message shouldBe "Customer with id ${customer.id} not found"
                }
            }
        }
    }
}
