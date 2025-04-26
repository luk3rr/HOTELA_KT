package com.hotela.repository.impl

import com.hotela.model.database.CustomerAuth
import com.hotela.stubs.database.CustomerAuthStubs
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
import java.time.LocalDateTime
import java.util.UUID
import java.util.function.BiFunction

class CustomerAuthRepositoryImplTest :
    ShouldSpec({
        val databaseClient = mockk<DatabaseClient>()
        val customerAuthRepositoryImpl = CustomerAuthRepositoryImpl(databaseClient)

        val customerAuth = CustomerAuthStubs.create()
        val genericDatabaseSpec = mockk<DatabaseClient.GenericExecuteSpec>()
        val mockRow = mockk<Row>()
        val rowsFetchSpec = mockk<RowsFetchSpec<CustomerAuth>>()

        fun setupMockForDatabaseClient() {
            every { databaseClient.sql(any<String>()) } returns genericDatabaseSpec
            every { genericDatabaseSpec.bind(any<String>(), any()) } returns genericDatabaseSpec
            every { genericDatabaseSpec.bindNull(any<String>(), any()) } returns genericDatabaseSpec
        }

        fun setupMockRowForCustomerAuth() {
            every {
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, CustomerAuth>>())
            } answers {
                val function = args[0] as BiFunction<Row, RowMetadata, CustomerAuth>
                every { rowsFetchSpec.first() } returns Mono.just(function.apply(mockRow, mockk()))
                rowsFetchSpec
            }

            every { mockRow.get("id", UUID::class.java) } returns customerAuth.id
            every { mockRow.get("customer_id", UUID::class.java) } returns customerAuth.customerId
            every { mockRow.get("email", String::class.java) } returns customerAuth.email
            every { mockRow.get("password_hash", String::class.java) } returns customerAuth.passwordHash
            every { mockRow.get("created_at", LocalDateTime::class.java) } returns customerAuth.createdAt
            every { mockRow.get("last_login", LocalDateTime::class.java) } returns customerAuth.lastLogin
            every { mockRow.get("active", Boolean::class.java) } returns customerAuth.active
        }

        beforeTest {
            setupMockForDatabaseClient()
            setupMockRowForCustomerAuth()
        }

        afterTest { clearAllMocks() }

        should("successfully create a customer auth") {
            customerAuthRepositoryImpl.create(customerAuth) shouldBe customerAuth

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("id", customerAuth.id)
                genericDatabaseSpec.bind("customerId", customerAuth.customerId)
                genericDatabaseSpec.bind("email", customerAuth.email)
                genericDatabaseSpec.bind("passwordHash", customerAuth.passwordHash)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, CustomerAuth>>())
                rowsFetchSpec.first()
            }
        }

        should("successfully find a customer auth by email") {
            customerAuthRepositoryImpl.findByEmail(customerAuth.email) shouldBe customerAuth

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("email", customerAuth.email)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, CustomerAuth>>())
                rowsFetchSpec.first()
            }
        }

        should("successfully find a customer auth by id") {
            customerAuthRepositoryImpl.findById(customerAuth.id) shouldBe customerAuth

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("id", customerAuth.id)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, CustomerAuth>>())
                rowsFetchSpec.first()
            }
        }

        should("successfully find a customer auth by customer id") {
            customerAuthRepositoryImpl.findByCustomerId(customerAuth.customerId) shouldBe customerAuth

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("customerId", customerAuth.customerId)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, CustomerAuth>>())
                rowsFetchSpec.first()
            }
        }

        should("successfully check if a customer auth exists by email") {
            every { mockRow.get("exists", Boolean::class.java) } returns true

            customerAuthRepositoryImpl.existsByEmail(customerAuth.email) shouldBe true

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("email", customerAuth.email)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, Boolean>>())
            }
        }

        should("successfully check if a customer auth exists by id") {
            every { mockRow.get("exists", Boolean::class.java) } returns true

            customerAuthRepositoryImpl.existsById(customerAuth.id) shouldBe true

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("id", customerAuth.id)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, Boolean>>())
            }
        }
    })
