package com.hotela.stubs.dto.request

import com.hotela.model.domain.ContactInfo
import com.hotela.model.domain.DocumentId
import com.hotela.model.domain.Email
import com.hotela.model.domain.PhoneNumber
import com.hotela.model.dto.request.UpdateCustomerRequest
import com.hotela.model.enum.DocumentIdType
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

object UpdateCustomerRequestStubs {
    fun create(
        name: String? = "Jane Doe",
        contactInfo: ContactInfo? =
            ContactInfo(
                email = Email("jane@doe.com"),
                phone = PhoneNumber("+1234567890"),
            ),
        documentId: DocumentId? =
            DocumentId(
                type = DocumentIdType.CPF,
                value = "12345678900",
            ),
        birthDate: Instant = LocalDate.of(1990, 1, 1).atStartOfDay(ZoneOffset.UTC).toInstant(),
    ): UpdateCustomerRequest =
        UpdateCustomerRequest(
            name = name,
            contactInfo = contactInfo,
            documentId = documentId,
            birthDate = birthDate,
        )
}
