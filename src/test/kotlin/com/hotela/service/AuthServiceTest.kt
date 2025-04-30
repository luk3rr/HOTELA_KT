package com.hotela.service

import com.hotela.error.HotelaException
import com.hotela.stubs.database.CustomerAuthStubs
import com.hotela.stubs.database.CustomerStubs
import com.hotela.stubs.database.PartnerAuthStubs
import com.hotela.stubs.database.PartnerStubs
import com.hotela.stubs.dto.request.AuthRequestStubs
import com.hotela.stubs.dto.request.CustomerRegisterRequestStubs
import com.hotela.stubs.dto.request.PartnerRegisterRequestStubs
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldNotBe
import io.mockk.coEvery
import io.mockk.mockk
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder

class AuthServiceTest :
    BehaviorSpec({
        val jwtEncoder = mockk<JwtEncoder>()
        val jwtDecoder = mockk<NimbusReactiveJwtDecoder>()
        val customerService = mockk<CustomerService>()
        val partnerAuthService = mockk<PartnerAuthService>()
        val partnerService = mockk<PartnerService>()
        val customerAuthService = mockk<CustomerAuthService>()

        val authService =
            AuthService(
                jwtEncoder = jwtEncoder,
                jwtDecoder = jwtDecoder,
                customerService = customerService,
                partnerAuthService = partnerAuthService,
                partnerService = partnerService,
                customerAuthService = customerAuthService,
            )

        val authRequest = AuthRequestStubs.create()
        val partnerAuth =
            PartnerAuthStubs.create().copy(passwordHash = BCrypt.hashpw(authRequest.password, BCrypt.gensalt()))
        val partnerAuthWithInvalidPassword = partnerAuth.copy(passwordHash = "invalid_password")
        val partnerRegisterRequest = PartnerRegisterRequestStubs.create()
        val partner = PartnerStubs.create()

        val customerAuth =
            CustomerAuthStubs.create().copy(passwordHash = BCrypt.hashpw(authRequest.password, BCrypt.gensalt()))
        val customerAuthWithInvalidPassword = customerAuth.copy(passwordHash = "invalid_password")
        val customerRegisterRequest = CustomerRegisterRequestStubs.create()
        val customer = CustomerStubs.create()

        Given("a valid partner login request") {
            When("the credentials are correct") {
                Then("it should return an AuthResponse with a valid token") {
                    coEvery { partnerAuthService.findByEmail(authRequest.email) } returns partnerAuth
                    coEvery { jwtEncoder.encode(any()) } returns mockk(relaxed = true)

                    val result = authService.partnerLogin(authRequest)

                    result.token shouldNotBe null
                }
            }

            When("the email does not exist") {
                Then("it should throw InvalidCredentialsException") {
                    coEvery { partnerAuthService.findByEmail(authRequest.email) } returns null

                    shouldThrow<HotelaException.InvalidCredentialsException> {
                        authService.partnerLogin(authRequest)
                    }
                }
            }

            When("the password is incorrect") {
                Then("it should throw InvalidCredentialsException") {
                    coEvery { partnerAuthService.findByEmail(authRequest.email) } returns partnerAuthWithInvalidPassword
                    coEvery { jwtEncoder.encode(any()) } returns mockk(relaxed = true)

                    shouldThrow<HotelaException.InvalidCredentialsException> {
                        authService.partnerLogin(authRequest)
                    }
                }
            }
        }

        Given("a valid partner registration request") {
            When("the email is not already registered") {
                Then("it should create a new partner and return an AuthResponse") {
                    coEvery { partnerAuthService.existsByEmail(partnerRegisterRequest.email) } returns false
                    coEvery { partnerService.createPartner(any()) } returns partner
                    coEvery { partnerAuthService.createPartnerAuth(any()) } returns partnerAuth
                    coEvery { jwtEncoder.encode(any()) } returns mockk(relaxed = true)

                    val result = authService.partnerRegister(partnerRegisterRequest)

                    result.token shouldNotBe null
                }
            }

            When("the email is already registered") {
                Then("it should throw EmailAlreadyRegisteredException") {
                    coEvery { partnerAuthService.existsByEmail(partnerRegisterRequest.email) } returns true

                    shouldThrow<HotelaException.EmailAlreadyRegisteredException> {
                        authService.partnerRegister(partnerRegisterRequest)
                    }
                }
            }
        }

        Given("a valid customer login request") {
            When("the credentials are correct") {
                Then("it should return an AuthResponse with a valid token") {
                    coEvery { customerAuthService.findByEmail(authRequest.email) } returns customerAuth
                    coEvery { jwtEncoder.encode(any()) } returns mockk(relaxed = true)

                    val result = authService.customerLogin(authRequest)

                    result.token shouldNotBe null
                }
            }

            When("the email does not exist") {
                Then("it should throw InvalidCredentialsException") {
                    coEvery { customerAuthService.findByEmail(authRequest.email) } returns null

                    shouldThrow<HotelaException.InvalidCredentialsException> {
                        authService.customerLogin(authRequest)
                    }
                }
            }

            When("the password is incorrect") {
                Then("it should throw InvalidCredentialsException") {
                    coEvery { customerAuthService.findByEmail(authRequest.email) } returns customerAuthWithInvalidPassword
                    coEvery { jwtEncoder.encode(any()) } returns mockk(relaxed = true)

                    shouldThrow<HotelaException.InvalidCredentialsException> {
                        authService.customerLogin(authRequest)
                    }
                }
            }
        }

        Given("a valid customer registration request") {
            When("the email is not already registered") {
                Then("it should create a new customer and return an AuthResponse") {
                    coEvery { customerAuthService.existsByEmail(customerRegisterRequest.email) } returns false
                    coEvery { customerService.createCustomer(any()) } returns customer
                    coEvery { customerAuthService.createCustomerAuth(any()) } returns customerAuth
                    coEvery { jwtEncoder.encode(any()) } returns mockk(relaxed = true)

                    val result = authService.customerRegister(customerRegisterRequest)

                    result.token shouldNotBe null
                }
            }

            When("the email is already registered") {
                Then("it should throw EmailAlreadyRegisteredException") {
                    coEvery { customerAuthService.existsByEmail(customerRegisterRequest.email) } returns true

                    shouldThrow<HotelaException.EmailAlreadyRegisteredException> {
                        authService.customerRegister(customerRegisterRequest)
                    }
                }
            }
        }
    })
