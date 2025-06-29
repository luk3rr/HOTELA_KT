package com.hotela.repository.impl

import com.hotela.clearAllTables
import com.hotela.repository.AddressRepository
import com.hotela.stubs.db.AddressStubs
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.r2dbc.core.DatabaseClient
import java.util.UUID

class AddressRepositoryImplTestInterface :
    ShouldSpec(),
    DatabaseIntegrationTestInterface {
    @Autowired
    private lateinit var addressRepository: AddressRepository

    @Autowired
    private lateinit var databaseClient: DatabaseClient

    override fun extensions() = listOf(SpringExtension)

    init {
        val address = AddressStubs.create()

        beforeSpec {
            databaseClient.clearAllTables()
        }

        beforeEach {
            databaseClient
                .sql("DELETE FROM address")
                .fetch()
                .rowsUpdated()
                .awaitSingle()
        }

        should("successfully create an address") {
            val savedAddress = addressRepository.create(address)

            savedAddress.id shouldBe address.id
            savedAddress.streetAddress shouldBe address.streetAddress
            savedAddress.city shouldBe address.city
            savedAddress.postalCode shouldBe address.postalCode

            val foundInDb = addressRepository.findById(address.id)
            foundInDb shouldNotBe null
            foundInDb?.id shouldBe address.id
        }

        should("successfully find an address by id") {
            addressRepository.create(address)

            val result = addressRepository.findById(address.id)

            result shouldNotBe null
            result?.id shouldBe address.id
            result?.streetAddress shouldBe address.streetAddress
        }

        should("return null when finding by a non-existent id") {
            val nonExistentId = UUID.fromString("c14cceb8-88e1-4ecb-9e55-a5d766abc730")

            val result = addressRepository.findById(nonExistentId)

            result shouldBe null
        }
    }
}
