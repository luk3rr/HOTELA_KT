package com.hotela.service

import com.hotela.error.HotelaException
import com.hotela.model.enum.Role
import com.hotela.repository.AddressRepository
import com.hotela.repository.AuthCredentialRepository
import com.hotela.stubs.db.AddressStubs
import com.hotela.stubs.db.AuthCredentialStubs
import com.hotela.stubs.db.CustomerStubs
import com.hotela.stubs.db.PartnerStubs
import com.hotela.stubs.dto.request.AuthRequestStubs
import com.hotela.stubs.dto.request.CustomerRegisterRequestStubs
import com.hotela.stubs.dto.request.PartnerRegisterRequestStubs
import com.hotela.util.TimeProvider
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldNotBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder
import java.time.Instant

class AuthCredentialServiceTest :
    BehaviorSpec({
        val jwtEncoder = mockk<JwtEncoder>()
        val jwtDecoder = mockk<NimbusReactiveJwtDecoder>()
        val authCredentialRepository = mockk<AuthCredentialRepository>()
        val customerService = mockk<CustomerService>()
        val partnerService = mockk<PartnerService>()
        val addressRepository = mockk<AddressRepository>()
        val timeProvider = mockk<TimeProvider<Instant>>()

        val authCredentialService =
            AuthCredentialService(
                jwtEncoder = jwtEncoder,
                jwtDecoder = jwtDecoder,
                authCredentialRepository = authCredentialRepository,
                customerService = customerService,
                partnerService = partnerService,
                addressRepository = addressRepository,
                timeProvider = timeProvider,
            )

        val authRequest = AuthRequestStubs.create()

        val authCredentialPartner =
            AuthCredentialStubs.create().copy(password = BCrypt.hashpw(authRequest.password, BCrypt.gensalt()), role = Role.PARTNER)
        val partnerAuthWithInvalidPassword = authCredentialPartner.copy(password = "invalid_password")
        val partnerRegisterRequest = PartnerRegisterRequestStubs.create()
        val partner = PartnerStubs.create()

        val customerAuth =
            AuthCredentialStubs.create().copy(password = BCrypt.hashpw(authRequest.password, BCrypt.gensalt()))
        val authCredentialCustomer =
            AuthCredentialStubs.create().copy(password = BCrypt.hashpw(authRequest.password, BCrypt.gensalt()), role = Role.CUSTOMER)
        val customerAuthWithInvalidPassword = customerAuth.copy(password = "invalid_password")
        val customerRegisterRequest = CustomerRegisterRequestStubs.create()
        val customer = CustomerStubs.create()

        val address = AddressStubs.create()

        every { timeProvider.now() } returns Instant.now()

        Given("a valid partner login request") {
            When("the credentials are correct") {
                Then("it should return an AuthResponse with a valid token") {
                    coEvery { partnerService.findByAuthId(authCredentialPartner.id) } returns partner
                    coEvery { authCredentialRepository.findByLoginEmail(authRequest.email) } returns authCredentialPartner
                    coEvery { jwtEncoder.encode(any()) } returns mockk(relaxed = true)

                    val result = authCredentialService.login(authRequest)

                    result.token shouldNotBe null
                }
            }

            When("the email does not exist") {
                Then("it should throw InvalidCredentialsException") {
                    coEvery { authCredentialRepository.findByLoginEmail(authRequest.email) } returns null

                    shouldThrow<HotelaException.InvalidCredentialsException> {
                        authCredentialService.login(authRequest)
                    }
                }
            }

            When("the password is incorrect") {
                Then("it should throw InvalidCredentialsException") {
                    coEvery { authCredentialRepository.findByLoginEmail(authRequest.email) } returns partnerAuthWithInvalidPassword
                    coEvery { jwtEncoder.encode(any()) } returns mockk(relaxed = true)

                    shouldThrow<HotelaException.InvalidCredentialsException> {
                        authCredentialService.login(authRequest)
                    }
                }
            }
        }

        Given("a valid partner registration request") {
            When("the email is not already registered") {
                Then("it should create a new partner and return an AuthResponse") {
                    coEvery { authCredentialRepository.existsByLoginEmail(partnerRegisterRequest.contactInfo.email) } returns false
                    coEvery { authCredentialRepository.create(any()) } returns authCredentialPartner
                    coEvery { partnerService.create(any()) } returns partner
                    coEvery { partnerService.findByAuthId(authCredentialPartner.id) } returns partner
                    coEvery { jwtEncoder.encode(any()) } returns mockk(relaxed = true)

                    val result = authCredentialService.partnerRegister(partnerRegisterRequest)

                    result.token shouldNotBe null
                }
            }

            When("the email is already registered") {
                Then("it should throw EmailAlreadyRegisteredException") {
                    coEvery { authCredentialRepository.existsByLoginEmail(partnerRegisterRequest.contactInfo.email) } returns true

                    shouldThrow<HotelaException.EmailAlreadyRegisteredException> {
                        authCredentialService.partnerRegister(partnerRegisterRequest)
                    }
                }
            }
        }

        Given("a valid customer login request") {
            When("the credentials are correct") {
                Then("it should return an AuthResponse with a valid token") {
                    coEvery { authCredentialRepository.findByLoginEmail(authRequest.email) } returns customerAuth
                    coEvery { customerService.findByAuthId(customerAuth.id) } returns customer
                    coEvery { jwtEncoder.encode(any()) } returns mockk(relaxed = true)

                    val result = authCredentialService.login(authRequest)

                    result.token shouldNotBe null
                }
            }

            When("the email does not exist") {
                Then("it should throw InvalidCredentialsException") {
                    coEvery { authCredentialRepository.findByLoginEmail(authRequest.email) } returns null

                    shouldThrow<HotelaException.InvalidCredentialsException> {
                        authCredentialService.login(authRequest)
                    }
                }
            }

            When("the password is incorrect") {
                Then("it should throw InvalidCredentialsException") {
                    coEvery { authCredentialRepository.findByLoginEmail(authRequest.email) } returns customerAuthWithInvalidPassword
                    coEvery { jwtEncoder.encode(any()) } returns mockk(relaxed = true)

                    shouldThrow<HotelaException.InvalidCredentialsException> {
                        authCredentialService.login(authRequest)
                    }
                }
            }
        }

        Given("a valid customer registration request") {
            When("the email is not already registered") {
                Then("it should create a new customer and return an AuthResponse") {
                    coEvery { authCredentialRepository.existsByLoginEmail(customerRegisterRequest.contactInfo.email) } returns false
                    coEvery { customerService.create(any()) } returns customer
                    coEvery { authCredentialRepository.create(any()) } returns customerAuth
                    coEvery { customerService.findByAuthId(customerAuth.id) } returns customer
                    coEvery { addressRepository.create(any()) } returns address
                    coEvery { jwtEncoder.encode(any()) } returns mockk(relaxed = true)

                    val result = authCredentialService.customerRegister(customerRegisterRequest)

                    result.token shouldNotBe null
                }
            }

            When("the email is already registered") {
                Then("it should throw EmailAlreadyRegisteredException") {
                    coEvery { authCredentialRepository.existsByLoginEmail(customerRegisterRequest.contactInfo.email) } returns true

                    shouldThrow<HotelaException.EmailAlreadyRegisteredException> {
                        authCredentialService.customerRegister(customerRegisterRequest)
                    }
                }
            }
        }
    })
