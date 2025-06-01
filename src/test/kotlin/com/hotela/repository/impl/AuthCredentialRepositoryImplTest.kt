package com.hotela.repository.impl

import com.hotela.model.db.AuthCredential
import com.hotela.model.domain.Email
import com.hotela.model.enum.Role
import com.hotela.stubs.db.AuthCredentialStubs
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

class AuthCredentialRepositoryImplTest :
    ShouldSpec({
        val databaseClient = mockk<DatabaseClient>()
        val authRepositoryImpl = AuthCredentialRepositoryImpl(databaseClient)

        val authCredential = AuthCredentialStubs.create()
        val genericDatabaseSpec = mockk<DatabaseClient.GenericExecuteSpec>()
        val mockRow = mockk<Row>()
        val rowsFetchSpec = mockk<RowsFetchSpec<AuthCredential>>()

        fun setupMockForDatabaseClient() {
            every { databaseClient.sql(any<String>()) } returns genericDatabaseSpec
            every { genericDatabaseSpec.bind(any<String>(), any()) } returns genericDatabaseSpec
            every { genericDatabaseSpec.bindNull(any<String>(), any()) } returns genericDatabaseSpec
        }

        fun setupMockRowForAuthCredential() {
            every {
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, AuthCredential>>())
            } answers {
                val function = args[0] as BiFunction<Row, RowMetadata, AuthCredential>
                every { rowsFetchSpec.first() } returns Mono.just(function.apply(mockRow, mockk()))
                rowsFetchSpec
            }

            every { mockRow.get("id", UUID::class.java) } returns authCredential.id
            every { mockRow.get("login_email", Email::class.java) } returns authCredential.loginEmail
            every { mockRow.get("password", String::class.java) } returns authCredential.password
            every { mockRow.get("role", Role::class.java) } returns authCredential.role
            every { mockRow.get("is_active", Boolean::class.java) } returns authCredential.isActive
            every { mockRow.get("last_login_at", Instant::class.java) } returns authCredential.lastLoginAt
            every { mockRow.get("created_at", Instant::class.java) } returns authCredential.createdAt
            every { mockRow.get("updated_at", Instant::class.java) } returns authCredential.updatedAt
        }

        beforeTest {
            setupMockForDatabaseClient()
            setupMockRowForAuthCredential()
        }

        afterTest { clearAllMocks() }

        should("successfully create a auth") {
            authRepositoryImpl.create(authCredential) shouldBe authCredential

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("id", authCredential.id)
                genericDatabaseSpec.bind("loginEmail", authCredential.loginEmail)
                genericDatabaseSpec.bind("password", authCredential.password)
                genericDatabaseSpec.bind("role", authCredential.role)
                genericDatabaseSpec.bind("isActive", authCredential.isActive)
                genericDatabaseSpec.bind("lastLoginAt", authCredential.lastLoginAt)
                genericDatabaseSpec.bind("createdAt", authCredential.createdAt)
                genericDatabaseSpec.bind("updatedAt", authCredential.updatedAt)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, AuthCredential>>())
                rowsFetchSpec.first()
            }
        }

        should("successfully find a auth by email") {
            authRepositoryImpl.findByLoginEmail(authCredential.loginEmail) shouldBe authCredential

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("loginEmail", authCredential.loginEmail)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, AuthCredential>>())
                rowsFetchSpec.first()
            }
        }

        should("successfully find a auth by id") {
            authRepositoryImpl.findById(authCredential.id) shouldBe authCredential

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("id", authCredential.id)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, AuthCredential>>())
                rowsFetchSpec.first()
            }
        }

        should("successfully check if a auth exists by email") {
            every { mockRow.get("exists", Boolean::class.java) } returns true

            authRepositoryImpl.existsByLoginEmail(authCredential.loginEmail) shouldBe true

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("loginEmail", authCredential.loginEmail)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, Boolean>>())
            }
        }

        should("successfully check if a auth exists by id") {
            every { mockRow.get("exists", Boolean::class.java) } returns true

            authRepositoryImpl.existsById(authCredential.id) shouldBe true

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("id", authCredential.id)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, Boolean>>())
            }
        }
    })
