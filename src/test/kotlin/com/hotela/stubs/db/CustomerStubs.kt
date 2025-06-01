package com.hotela.stubs.db

import com.hotela.model.db.Customer
import com.hotela.model.domain.ContactInfo
import com.hotela.model.domain.DocumentId
import com.hotela.model.domain.Email
import com.hotela.model.domain.PhoneNumber
import com.hotela.model.enum.DocumentIdType
import java.time.Instant
import java.util.UUID

object CustomerStubs {
    fun create(
        id: UUID = UUID.fromString("ebaf6044-da61-47e3-84b6-82ebb3d738e1"),
        authCredentialId: UUID = UUID.fromString("c489e321-9749-4ef3-b846-00b78778ad5e"),
        addressId: UUID = UUID.fromString("f7f52963-8e4f-4b86-aea9-90b6b95ac775"),
        name: String = "John Doe",
        contactInfo: ContactInfo =
            ContactInfo(
                email = Email("john@doe.com"),
                phone = PhoneNumber("+55 11 91234-5678"),
            ),
        documentId: DocumentId =
            DocumentId(
                type = DocumentIdType.CPF,
                value = "12345678901",
            ),
        birthDate: Instant = Instant.parse("1980-01-01T00:00:00Z"),
    ): Customer =
        Customer(
            id = id,
            authCredentialId = authCredentialId,
            addressId = addressId,
            name = name,
            contactInfo = contactInfo,
            documentId = documentId,
            birthDate = birthDate,
        )
}
