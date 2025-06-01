package com.hotela.repository.impl

import com.hotela.model.db.Customer
import com.hotela.model.domain.Email
import com.hotela.model.domain.PhoneNumber
import com.hotela.model.enum.DocumentIdType
import com.hotela.stubs.db.CustomerStubs
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
import org.springframework.r2dbc.core.bind
import reactor.core.publisher.Mono
import java.time.Instant
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
            every { mockRow.get("auth_credential_id", UUID::class.java) } returns customer.authCredentialId
            every { mockRow.get("address_id", UUID::class.java) } returns customer.addressId
            every { mockRow.get("name", String::class.java) } returns customer.name
            every { mockRow.get("email", Email::class.java) } returns customer.contactInfo.email
            every { mockRow.get("phone", PhoneNumber::class.java) } returns customer.contactInfo.phone
            every { mockRow.get("document_id_type", DocumentIdType::class.java) } returns customer.documentId.type
            every { mockRow.get("document_id_value", String::class.java) } returns customer.documentId.value
            every { mockRow.get("birth_date", Instant::class.java) } returns customer.birthDate
        }

        beforeTest {
            setupMockForDatabaseClient()
            setupMockRowForCustomer()
        }

        afterTest { clearAllMocks() }

        should("successfully create a customer") {
            val result = customerRepositoryImpl.create(customer)

            result.id shouldBe customer.id
            result.authCredentialId shouldBe customer.authCredentialId
            result.addressId shouldBe customer.addressId

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("id", customer.id)
                genericDatabaseSpec.bind("authCredentialId", customer.authCredentialId)
                genericDatabaseSpec.bind("addressId", customer.addressId)
                genericDatabaseSpec.bind("name", customer.name)
                genericDatabaseSpec.bind("email", customer.contactInfo.email)
                genericDatabaseSpec.bind("phone", customer.contactInfo.phone)
                genericDatabaseSpec.bind("documentIdType", customer.documentId.type)
                genericDatabaseSpec.bind("documentIdValue", customer.documentId.value)
                genericDatabaseSpec.bind("birthDate", customer.birthDate)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, Customer>>())
                rowsFetchSpec.first()
            }
        }

        should("successfully find a customer by id") {
            val result = customerRepositoryImpl.findById(customer.id)

            result?.id shouldBe customer.id
            result?.authCredentialId shouldBe customer.authCredentialId
            result?.addressId shouldBe customer.addressId

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("id", customer.id)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, Customer>>())
                rowsFetchSpec.first()
            }
        }

        should("successfully find a customer by email") {
            val result = customerRepositoryImpl.findByEmail(customer.contactInfo.email)

            result?.id shouldBe customer.id
            result?.authCredentialId shouldBe customer.authCredentialId
            result?.addressId shouldBe customer.addressId
            result?.contactInfo?.email shouldBe customer.contactInfo.email

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("email", customer.contactInfo.email)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, Customer>>())
                rowsFetchSpec.first()
            }
        }

        should("successfully check if a customer exists by email") {
            every { mockRow.get("exists", Boolean::class.java) } returns true

            customerRepositoryImpl.existsByEmail(customer.contactInfo.email) shouldBe true

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("email", customer.contactInfo.email)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, Boolean>>())
            }
        }

        should("successfully update a customer") {
            val result = customerRepositoryImpl.update(customer)

            result.id shouldBe customer.id
            result.authCredentialId shouldBe customer.authCredentialId
            result.addressId shouldBe customer.addressId

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("id", customer.id)
                genericDatabaseSpec.bind("name", customer.name)
                genericDatabaseSpec.bind("email", customer.contactInfo.email)
                genericDatabaseSpec.bind("phone", customer.contactInfo.phone)
                genericDatabaseSpec.bind("documentIdType", customer.documentId.type)
                genericDatabaseSpec.bind("documentIdValue", customer.documentId.value)
                genericDatabaseSpec.bind("birthDate", customer.birthDate)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, Customer>>())
                rowsFetchSpec.first()
            }
        }
    })
