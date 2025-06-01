package com.hotela.repository.impl

import com.hotela.model.db.Partner
import com.hotela.model.domain.Email
import com.hotela.model.domain.PhoneNumber
import com.hotela.model.enum.DocumentIdType
import com.hotela.model.enum.PartnerStatus
import com.hotela.stubs.db.PartnerStubs
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
            every { mockRow.get("auth_credential_id", UUID::class.java) } returns partner.authCredentialId
            every { mockRow.get("company_name", String::class.java) } returns partner.companyName
            every { mockRow.get("legal_name", String::class.java) } returns partner.legalName
            every { mockRow.get("email", Email::class.java) } returns partner.contactInfo.email
            every { mockRow.get("phone", PhoneNumber::class.java) } returns partner.contactInfo.phone
            every { mockRow.get("document_id_type", DocumentIdType::class.java) } returns partner.documentId.type
            every { mockRow.get("document_id_value", String::class.java) } returns partner.documentId.value
            every { mockRow.get("contract_signed_at", Instant::class.java) } returns partner.contractSignedAt
            every { mockRow.get("status", PartnerStatus::class.java) } returns partner.status
            every { mockRow.get("notes", String::class.java) } returns partner.notes
        }

        beforeTest {
            setupMockForDatabaseClient()
            setupMockRowForPartner()
        }

        afterTest { clearAllMocks() }

        should("successfully find a partner by id") {
            val result = partnerRepositoryImpl.findById(partner.id)

            result?.id shouldBe partner.id
            result?.authCredentialId shouldBe partner.authCredentialId

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("id", partner.id)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, Partner>>())
                rowsFetchSpec.first()
            }
        }

        should("successfully find a partner by email") {
            val result = partnerRepositoryImpl.findByEmail(partner.contactInfo.email)

            result?.id shouldBe partner.id
            result?.authCredentialId shouldBe partner.authCredentialId
            result?.contactInfo?.email shouldBe partner.contactInfo.email

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("email", partner.contactInfo.email)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, Partner>>())
                rowsFetchSpec.first()
            }
        }

        should("successfully check if a partner exists by email") {
            every { mockRow.get("exists", Boolean::class.java) } returns true

            partnerRepositoryImpl.existsByEmail(partner.contactInfo.email) shouldBe true

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("email", partner.contactInfo.email)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, Boolean>>())
            }
        }

        should("successfully create a partner") {
            val result = partnerRepositoryImpl.create(partner)

            result.id shouldBe partner.id
            result.authCredentialId shouldBe partner.authCredentialId
            result.companyName shouldBe partner.companyName

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("id", partner.id)
                genericDatabaseSpec.bind("authCredentialId", partner.authCredentialId)
                genericDatabaseSpec.bind("companyName", partner.companyName)
                genericDatabaseSpec.bind("legalName", partner.legalName)
                genericDatabaseSpec.bind("email", partner.contactInfo.email)
                genericDatabaseSpec.bind("phone", partner.contactInfo.phone)
                genericDatabaseSpec.bind("documentIdType", partner.documentId.type)
                genericDatabaseSpec.bind("documentIdValue", partner.documentId.value)
                genericDatabaseSpec.bind("contractSignedAt", partner.contractSignedAt)
                genericDatabaseSpec.bind("status", partner.status)
                genericDatabaseSpec.bind("notes", partner.notes)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, Partner>>())
                rowsFetchSpec.first()
            }
        }

        should("successfully update a partner") {
            val result = partnerRepositoryImpl.update(partner)

            result.id shouldBe partner.id
            result.authCredentialId shouldBe partner.authCredentialId

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("id", partner.id)
                genericDatabaseSpec.bind("companyName", partner.companyName)
                genericDatabaseSpec.bind("legalName", partner.legalName)
                genericDatabaseSpec.bind("email", partner.contactInfo.email)
                genericDatabaseSpec.bind("phone", partner.contactInfo.phone)
                genericDatabaseSpec.bind("documentIdType", partner.documentId.type)
                genericDatabaseSpec.bind("documentIdValue", partner.documentId.value)
                genericDatabaseSpec.bind("contractSignedAt", partner.contractSignedAt)
                genericDatabaseSpec.bind("status", partner.status)
                genericDatabaseSpec.bind("notes", partner.notes)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, Partner>>())
                rowsFetchSpec.first()
            }
        }
    })
