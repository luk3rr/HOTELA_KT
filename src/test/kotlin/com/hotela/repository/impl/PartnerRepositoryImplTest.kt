package com.hotela.repository.impl

import com.hotela.model.database.Partner
import com.hotela.model.enum.PartnerStatus
import com.hotela.stubs.database.PartnerStubs
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
import java.time.LocalDateTime
import java.util.UUID
import java.util.function.BiFunction

class PartnerRepositoryImplTest :
    ShouldSpec({
        val databaseClient = mockk<DatabaseClient>()
        val partnerRepositoryImpl = PartnerRepositoryImpl(databaseClient)

        val partner = PartnerStubs.create()
        val genericDatabaseSpec = mockk<DatabaseClient.GenericExecuteSpec>()
        val mockRow = mockk<Row>()
        val rowsFetchSpec = mockk<RowsFetchSpec<Partner>>()
        val booleanRowsFetchSpec = mockk<RowsFetchSpec<Boolean>>()

        fun setupMockForDatabaseClient() {
            every { databaseClient.sql(any<String>()) } returns genericDatabaseSpec
            every { genericDatabaseSpec.bind(any<String>(), any()) } returns genericDatabaseSpec
            every { genericDatabaseSpec.bindNull(any<String>(), any()) } returns genericDatabaseSpec
        }

        fun setupMockRowForPartner() {
            every {
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, Partner>>())
            } answers {
                val function = args[0] as BiFunction<Row, RowMetadata, Partner>
                every { rowsFetchSpec.first() } returns Mono.just(function.apply(mockRow, mockk()))
                rowsFetchSpec
            }

            every { mockRow.get("id", UUID::class.java) } returns partner.id
            every { mockRow.get("name", String::class.java) } returns partner.name
            every { mockRow.get("cnpj", String::class.java) } returns partner.cnpj
            every { mockRow.get("email", String::class.java) } returns partner.email
            every { mockRow.get("phone", String::class.java) } returns partner.phone
            every { mockRow.get("address", String::class.java) } returns partner.address
            every { mockRow.get("contact_name", String::class.java) } returns partner.contactName
            every { mockRow.get("contact_email", String::class.java) } returns partner.contactEmail
            every { mockRow.get("contact_phone", String::class.java) } returns partner.contactPhone
            every { mockRow.get("contract_signed", Boolean::class.java) } returns partner.contractSigned
            every { mockRow.get("status", PartnerStatus::class.java) } returns partner.status
            every { mockRow.get("created_at", LocalDateTime::class.java) } returns partner.createdAt
            every { mockRow.get("notes", String::class.java) } returns partner.notes
        }

        beforeTest {
            setupMockForDatabaseClient()
            setupMockRowForPartner()
        }

        afterTest { clearAllMocks() }

        should("successfully find a partner by id") {
            partnerRepositoryImpl.findById(partner.id) shouldBe partner

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("id", partner.id)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, Partner>>())
                rowsFetchSpec.first()
            }
        }

        should("successfully find a partner by email") {
            partnerRepositoryImpl.findByEmail(partner.email) shouldBe partner

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("email", partner.email)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, Partner>>())
                rowsFetchSpec.first()
            }
        }

        should("successfully check if a partner exists by email") {
            every { mockRow.get("exists", Boolean::class.java) } returns true

            partnerRepositoryImpl.existsByEmail(partner.email) shouldBe true

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("email", partner.email)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, Boolean>>())
            }
        }

        should("successfully create a partner") {
            partnerRepositoryImpl.create(partner) shouldBe partner

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("id", partner.id)
                genericDatabaseSpec.bind("name", partner.name)
                genericDatabaseSpec.bind("cnpj", partner.cnpj)
                genericDatabaseSpec.bind("email", partner.email)
                genericDatabaseSpec.bind("phone", partner.phone)
                genericDatabaseSpec.bind("address", partner.address)
                genericDatabaseSpec.bind("contactName", partner.contactName)
                genericDatabaseSpec.bind("contactEmail", partner.contactEmail)
                genericDatabaseSpec.bind("contactPhone", partner.contactPhone)
                genericDatabaseSpec.bind("contractSigned", partner.contractSigned)
                genericDatabaseSpec.bind("status", partner.status)
                genericDatabaseSpec.bind("createdAt", partner.createdAt)
                genericDatabaseSpec.bind("notes", partner.notes)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, Partner>>())
                rowsFetchSpec.first()
            }
        }

        should("successfully update a partner") {
            partnerRepositoryImpl.update(partner) shouldBe partner

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("id", partner.id)
                genericDatabaseSpec.bind("name", partner.name)
                genericDatabaseSpec.bind("cnpj", partner.cnpj)
                genericDatabaseSpec.bind("email", partner.email)
                genericDatabaseSpec.bind("phone", partner.phone)
                genericDatabaseSpec.bind("address", partner.address)
                genericDatabaseSpec.bind("contactName", partner.contactName)
                genericDatabaseSpec.bind("contactEmail", partner.contactEmail)
                genericDatabaseSpec.bind("contactPhone", partner.contactPhone)
                genericDatabaseSpec.bind("contractSigned", partner.contractSigned)
                genericDatabaseSpec.bind("status", partner.status)
                genericDatabaseSpec.bind("createdAt", partner.createdAt)
                genericDatabaseSpec.bind("notes", partner.notes)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, Partner>>())
                rowsFetchSpec.first()
            }
        }
    })
