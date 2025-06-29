package com.hotela.repository.impl

import com.hotela.clearAllTables
import com.hotela.model.domain.Email
import com.hotela.repository.AddressRepository
import com.hotela.repository.AuthCredentialRepository
import com.hotela.repository.CustomerRepository
import com.hotela.stubs.db.AddressStubs
import com.hotela.stubs.db.AuthCredentialStubs
import com.hotela.stubs.db.CustomerStubs
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.r2dbc.core.DatabaseClient

class CustomerRepositoryImplTestInterface :
    ShouldSpec(),
    DatabaseIntegrationTestInterface {
    @Autowired
    private lateinit var addressRepository: AddressRepository

    @Autowired
    private lateinit var authCredentialRepository: AuthCredentialRepository

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var databaseClient: DatabaseClient

    override fun extensions() = listOf(SpringExtension)

    init {
        val address = AddressStubs.create()
        val authCustomer = AuthCredentialStubs.create()
        val customer = CustomerStubs.create(authCredentialId = authCustomer.id, addressId = address.id)

        beforeSpec {
            databaseClient.clearAllTables()

            addressRepository.create(address)
            authCredentialRepository.create(authCustomer)
        }

        beforeTest {
            databaseClient
                .sql("DELETE FROM customer")
                .fetch()
                .rowsUpdated()
                .awaitSingle()
        }

        should("successfully create a customer") {
            val savedCustomer = customerRepository.create(customer)

            savedCustomer.id shouldBe customer.id
            savedCustomer.name shouldBe customer.name
            savedCustomer.contactInfo.email shouldBe customer.contactInfo.email

            val foundInDb = customerRepository.findById(customer.id)
            foundInDb shouldNotBe null
            foundInDb?.name shouldBe customer.name
        }

        should("successfully find a customer by id") {
            customerRepository.create(customer)

            val result = customerRepository.findById(customer.id)

            result shouldNotBe null
            result?.id shouldBe customer.id
            result?.authCredentialId shouldBe customer.authCredentialId
            result?.addressId shouldBe customer.addressId
        }

        should("successfully find a customer by auth id") {
            customerRepository.create(customer)

            val result = customerRepository.findByAuthId(customer.authCredentialId)

            result shouldNotBe null
            result?.authCredentialId shouldBe customer.authCredentialId
            result?.addressId shouldBe customer.addressId
        }

        should("successfully find a customer by email") {
            customerRepository.create(customer)

            val result = customerRepository.findByEmail(customer.contactInfo.email)

            result shouldNotBe null
            result?.id shouldBe customer.id
            result?.contactInfo?.email shouldBe customer.contactInfo.email
        }

        should("successfully check if a customer exists by email") {
            customerRepository.create(customer)
            val nonExistentEmail = Email("nonexisting@email.com")

            customerRepository.existsByEmail(customer.contactInfo.email) shouldBe true
            customerRepository.existsByEmail(nonExistentEmail) shouldBe false
        }

        should("successfully update a customer") {
            customerRepository.create(customer)
            val updatedCustomer = customer.copy(name = "João da Silva Santos")

            customerRepository.update(updatedCustomer)

            val foundAfterUpdate = customerRepository.findById(customer.id)
            foundAfterUpdate shouldNotBe null
            foundAfterUpdate?.name shouldBe "João da Silva Santos"
        }
    }
}
