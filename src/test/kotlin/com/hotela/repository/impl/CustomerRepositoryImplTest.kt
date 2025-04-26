package com.hotela.repository.impl

import com.hotela.model.database.Customer
import com.hotela.stubs.CustomerStubs
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.r2dbc.spi.Row
import io.r2dbc.spi.RowMetadata
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.RowsFetchSpec
import reactor.core.publisher.Mono
import java.time.LocalDate
import java.util.UUID
import java.util.function.BiFunction

class CustomerRepositoryImplTest :
    ShouldSpec({
        val databaseClient = mockk<DatabaseClient>()
        val customerRepositoryImpl = CustomerRepositoryImpl(databaseClient)

        val customer = CustomerStubs.create()
        val genericDatabaseSpec = mockk<DatabaseClient.GenericExecuteSpec>()
        val mockRow = mockk<Row>()
        val rowsFetchSpec = mockk<RowsFetchSpec<Customer>>()

        fun setupMockForDatabaseClient() {
            every { databaseClient.sql(any<String>()) } returns genericDatabaseSpec
            every { genericDatabaseSpec.bind(any<String>(), any()) } returns genericDatabaseSpec
            every { genericDatabaseSpec.bindNull(any<String>(), any()) } returns genericDatabaseSpec
        }

        fun setupMockRowForCustomer() {
            every {
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, Customer>>())
            } answers {
                val function = args[0] as BiFunction<Row, RowMetadata, Customer>
                every { rowsFetchSpec.first() } returns Mono.just(function.apply(mockRow, mockk()))
                rowsFetchSpec
            }

            every { mockRow.get("id", UUID::class.java) } returns customer.id
            every { mockRow.get("name", String::class.java) } returns customer.name
            every { mockRow.get("email", String::class.java) } returns customer.email
            every { mockRow.get("phone", String::class.java) } returns customer.phone
            every { mockRow.get("id_document", String::class.java) } returns customer.idDocument
            every { mockRow.get("birth_date", LocalDate::class.java) } returns customer.birthDate
            every { mockRow.get("address", String::class.java) } returns customer.address
        }

        beforeTest {
            setupMockForDatabaseClient()
            setupMockRowForCustomer()
        }

        afterTest { clearAllMocks() }

        should("successfully create a customer") {
            customerRepositoryImpl.create(customer) shouldBe customer

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("id", customer.id)
                genericDatabaseSpec.bind("name", customer.name)
                genericDatabaseSpec.bind("email", customer.email)
                genericDatabaseSpec.bind("phone", customer.phone)
                genericDatabaseSpec.bind("idDocument", customer.idDocument)
                genericDatabaseSpec.bind("birthDate", customer.birthDate)
                genericDatabaseSpec.bind("address", customer.address)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, Customer>>())
                rowsFetchSpec.first()
            }
        }

        should("successfully find a customer by id") {
            customerRepositoryImpl.findById(customer.id) shouldBe customer

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("id", customer.id)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, Customer>>())
                rowsFetchSpec.first()
            }
        }

        should("successfully find a customer by email") {
            customerRepositoryImpl.findByEmail(customer.email) shouldBe customer

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("email", customer.email)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, Customer>>())
                rowsFetchSpec.first()
            }
        }

        should("successfully check if a customer exists by email") {
            every { mockRow.get("exists", Boolean::class.java) } returns true

            customerRepositoryImpl.existsByEmail(customer.email) shouldBe true

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("email", customer.email)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, Boolean>>())
            }
        }

        should("successfully update a customer") {
            customerRepositoryImpl.update(customer) shouldBe customer

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("id", customer.id)
                genericDatabaseSpec.bind("name", customer.name)
                genericDatabaseSpec.bind("email", customer.email)
                genericDatabaseSpec.bind("phone", customer.phone)
                genericDatabaseSpec.bind("idDocument", customer.idDocument)
                genericDatabaseSpec.bind("birthDate", customer.birthDate)
                genericDatabaseSpec.bind("address", customer.address)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, Customer>>())
                rowsFetchSpec.first()
            }
        }
    })
