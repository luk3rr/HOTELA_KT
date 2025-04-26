package com.hotela.repository.impl

import com.hotela.model.database.PartnerAuth
import com.hotela.stubs.database.PartnerAuthStubs
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

class PartnerAuthRepositoryImplTest :
    ShouldSpec({
        val databaseClient = mockk<DatabaseClient>()
        val partnerAuthRepositoryImpl = PartnerAuthRepositoryImpl(databaseClient)

        val partnerAuth = PartnerAuthStubs.create()
        val genericDatabaseSpec = mockk<DatabaseClient.GenericExecuteSpec>()
        val mockRow = mockk<Row>()
        val rowsFetchSpec = mockk<RowsFetchSpec<PartnerAuth>>()
        val booleanRowsFetchSpec = mockk<RowsFetchSpec<Boolean>>()

        fun setupMockForDatabaseClient() {
            every { databaseClient.sql(any<String>()) } returns genericDatabaseSpec
            every { genericDatabaseSpec.bind(any<String>(), any()) } returns genericDatabaseSpec
            every { genericDatabaseSpec.bindNull(any<String>(), any()) } returns genericDatabaseSpec
        }

        fun setupMockRowForPartnerAuth() {
            every {
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, PartnerAuth>>())
            } answers {
                val function = args[0] as BiFunction<Row, RowMetadata, PartnerAuth>
                every { rowsFetchSpec.first() } returns Mono.just(function.apply(mockRow, mockk()))
                rowsFetchSpec
            }

            every { mockRow.get("id", UUID::class.java) } returns partnerAuth.id
            every { mockRow.get("partner_id", UUID::class.java) } returns partnerAuth.partnerId
            every { mockRow.get("email", String::class.java) } returns partnerAuth.email
            every { mockRow.get("password_hash", String::class.java) } returns partnerAuth.passwordHash
            every { mockRow.get("created_at", LocalDateTime::class.java) } returns partnerAuth.createdAt
            every { mockRow.get("last_login", LocalDateTime::class.java) } returns partnerAuth.lastLogin
            every { mockRow.get("active", Boolean::class.java) } returns partnerAuth.active
        }

        beforeTest {
            setupMockForDatabaseClient()
            setupMockRowForPartnerAuth()
        }

        afterTest { clearAllMocks() }

        should("successfully find a partner auth by id") {
            partnerAuthRepositoryImpl.findById(partnerAuth.id) shouldBe partnerAuth

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("id", partnerAuth.id)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, PartnerAuth>>())
                rowsFetchSpec.first()
            }
        }

        should("successfully find a partner auth by email") {
            partnerAuthRepositoryImpl.findByEmail(partnerAuth.email) shouldBe partnerAuth

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("email", partnerAuth.email)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, PartnerAuth>>())
                rowsFetchSpec.first()
            }
        }

        should("successfully check if a partner auth exists by email") {
            every { mockRow.get("exists", Boolean::class.java) } returns true

            partnerAuthRepositoryImpl.existsByEmail(partnerAuth.email) shouldBe true

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("email", partnerAuth.email)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, Boolean>>())
            }
        }

        should("successfully create a partner auth") {
            partnerAuthRepositoryImpl.create(partnerAuth) shouldBe partnerAuth

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("id", partnerAuth.id)
                genericDatabaseSpec.bind("partnerId", partnerAuth.partnerId)
                genericDatabaseSpec.bind("email", partnerAuth.email)
                genericDatabaseSpec.bind("passwordHash", partnerAuth.passwordHash)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, PartnerAuth>>())
                rowsFetchSpec.first()
            }
        }
    })
