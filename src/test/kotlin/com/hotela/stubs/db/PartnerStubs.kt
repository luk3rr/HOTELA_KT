package com.hotela.stubs.db

import com.hotela.model.db.Partner
import com.hotela.model.domain.ContactInfo
import com.hotela.model.domain.DocumentId
import com.hotela.model.domain.Email
import com.hotela.model.domain.PhoneNumber
import com.hotela.model.enum.DocumentIdType
import com.hotela.model.enum.PartnerStatus
import java.time.Instant
import java.util.UUID

object PartnerStubs {
    fun create(
        id: UUID = UUID.fromString("32136c3c-bb36-4e65-ae16-a91c61195319"),
        authCredentialId: UUID = UUID.fromString("ed6f5b9a-9c21-4b0d-85f9-47a7dc7e3c91"),
        companyName: String = "John Doe Tech Ltda",
        legalName: String = "John Doe Tecnologia e Serviços LTDA",
        contactInfo: ContactInfo =
            ContactInfo(
                email = Email("john@doe.com"),
                phone = PhoneNumber("+55 11 91234-5678"),
            ),
        documentId: DocumentId =
            DocumentId(
                type = DocumentIdType.CNPJ,
                value = "12345678000195",
            ),
        contractSignedAt: Instant = Instant.parse("2019-05-05T05:05:00Z"),
        status: PartnerStatus = PartnerStatus.ACTIVE,
        notes: String? = "Test partner",
    ): Partner =
        Partner(
            id = id,
            authCredentialId = authCredentialId,
            companyName = companyName,
            legalName = legalName,
            contactInfo = contactInfo,
            documentId = documentId,
            contractSignedAt = contractSignedAt,
            status = status,
            notes = notes,
        )
}
