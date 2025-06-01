package com.hotela.stubs.dto.request

import com.hotela.model.domain.ContactInfo
import com.hotela.model.domain.DocumentId
import com.hotela.model.domain.Email
import com.hotela.model.domain.PhoneNumber
import com.hotela.model.dto.request.PartnerRegisterRequest
import com.hotela.model.enum.DocumentIdType
import com.hotela.model.enum.PartnerStatus
import java.time.Instant

object PartnerRegisterRequestStubs {
    fun create(
        loginEmail: Email = Email("john@doe.com"),
        password: String = "password",
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
                value = "12345678000190",
            ),
        contractSignedAt: Instant = Instant.parse("2019-05-05T05:05:00Z"),
        status: PartnerStatus = PartnerStatus.ACTIVE,
        notes: String? = "Test partner",
    ): PartnerRegisterRequest =
        PartnerRegisterRequest(
            loginEmail = loginEmail,
            password = password,
            companyName = companyName,
            legalName = legalName,
            contactInfo = contactInfo,
            documentId = documentId,
            contractSignedAt = contractSignedAt,
            status = status,
            notes = notes,
        )
}
