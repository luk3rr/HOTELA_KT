package com.hotela.stubs.dto.request

import com.hotela.model.domain.ContactInfo
import com.hotela.model.domain.DocumentId
import com.hotela.model.domain.Email
import com.hotela.model.domain.PhoneNumber
import com.hotela.model.dto.request.UpdatePartnerRequest
import com.hotela.model.enum.DocumentIdType
import com.hotela.model.enum.PartnerStatus
import java.time.Instant

object UpdatePartnerRequestStubs {
    fun create(
        companyName: String? = "John F Doe Ltda",
        legalName: String? = "John F Doe Tecnologia e Serviços LTDA",
        contactInfo: ContactInfo? =
            ContactInfo(
                email = Email("janeF@doe.com"),
                phone = PhoneNumber("+0987654321"),
            ),
        documentId: DocumentId? =
            DocumentId(
                type = DocumentIdType.CNPJ,
                value = "12345678000190",
            ),
        contractSignedAt: Instant? = Instant.parse("2023-01-15T00:00:00Z"),
        status: PartnerStatus? = PartnerStatus.INACTIVE,
        notes: String? = "This is a test note",
    ): UpdatePartnerRequest =
        UpdatePartnerRequest(
            companyName = companyName,
            legalName = legalName,
            contactInfo = contactInfo,
            documentId = documentId,
            contractSignedAt = contractSignedAt,
            status = status,
            notes = notes,
        )
}
