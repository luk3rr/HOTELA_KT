package com.hotela.repository.impl

import com.hotela.clearAllTables
import com.hotela.model.domain.Email
import com.hotela.repository.AuthCredentialRepository
import com.hotela.repository.PartnerRepository
import com.hotela.stubs.db.AuthCredentialStubs
import com.hotela.stubs.db.PartnerStubs
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.r2dbc.core.DatabaseClient
import java.util.UUID

class PartnerRepositoryImplTestInterface :
    ShouldSpec(),
    DatabaseIntegrationTestInterface {
    @Autowired
    private lateinit var authCredentialRepository: AuthCredentialRepository

    @Autowired
    private lateinit var partnerRepository: PartnerRepository

    @Autowired
    private lateinit var databaseClient: DatabaseClient

    override fun extensions() = listOf(SpringExtension)

    init {
        val authCredential = AuthCredentialStubs.create()
        val partner = PartnerStubs.create(authCredentialId = authCredential.id)

        beforeSpec {
            databaseClient.clearAllTables()

            authCredentialRepository.create(authCredential)
        }

        beforeTest {
            databaseClient
                .sql("DELETE FROM partner")
                .fetch()
                .rowsUpdated()
                .awaitSingle()
        }

        should("successfully create a partner") {
            val saved = partnerRepository.create(partner)

            saved.id shouldBe partner.id
            saved.authCredentialId shouldBe partner.authCredentialId
            saved.companyName shouldBe partner.companyName
            saved.legalName shouldBe partner.legalName
            saved.contactInfo shouldBe partner.contactInfo
            saved.documentId.value shouldBe partner.documentId.value
            saved.documentId.type shouldBe partner.documentId.type
            saved.contractSignedAt shouldBe partner.contractSignedAt
            saved.status shouldBe partner.status
            saved.notes shouldBe partner.notes
        }

        should("successfully update a partner") {
            partnerRepository.create(partner)

            val updatedPartner = partner.copy(companyName = "New Company Name")
            val updated = partnerRepository.update(updatedPartner)

            updated.id shouldBe partner.id
            updated.companyName shouldBe "New Company Name"

            val found = partnerRepository.findById(partner.id)
            found shouldNotBe null
            found?.companyName shouldBe "New Company Name"
        }

        should("successfully find partner by id") {
            partnerRepository.create(partner)

            val found = partnerRepository.findById(partner.id)

            found shouldNotBe null
            found?.id shouldBe partner.id
        }

        should("return null when partner id not found") {
            val found = partnerRepository.findById(UUID.randomUUID())
            found shouldBe null
        }

        should("successfully find partner by auth id") {
            partnerRepository.create(partner)

            val found = partnerRepository.findByAuthId(partner.authCredentialId)

            found shouldNotBe null
            found?.id shouldBe partner.id
        }

        should("return null when auth id not found") {
            val found = partnerRepository.findByAuthId(UUID.randomUUID())
            found shouldBe null
        }

        should("successfully find partner by email") {
            partnerRepository.create(partner)

            val found = partnerRepository.findByEmail(partner.contactInfo.email)

            found shouldNotBe null
            found?.id shouldBe partner.id
        }

        should("return null when email not found") {
            val found = partnerRepository.findByEmail(Email("nonexisting@email.com"))
            found shouldBe null
        }

        should("successfully check if email exists") {
            partnerRepository.create(partner)

            val exists = partnerRepository.existsByEmail(partner.contactInfo.email)

            exists shouldBe true
        }

        should("return false when email does not exist") {
            val exists = partnerRepository.existsByEmail(Email("nonexisting@email"))

            exists shouldBe false
        }
    }
}
