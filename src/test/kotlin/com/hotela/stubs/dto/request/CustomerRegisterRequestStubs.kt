package com.hotela.stubs.dto.request

import com.hotela.model.domain.ContactInfo
import com.hotela.model.domain.DocumentId
import com.hotela.model.domain.Email
import com.hotela.model.domain.PhoneNumber
import com.hotela.model.dto.request.CreateAddressRequest
import com.hotela.model.dto.request.CustomerRegisterRequest
import com.hotela.model.enum.DocumentIdType
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

object CustomerRegisterRequestStubs {
    fun create(
        loginEmail: Email = Email("john@doe.com"),
        password: String = "password",
        name: String = "John Doe",
        contactInfo: ContactInfo =
            ContactInfo(
                email = Email("john@doe.com"),
                phone = PhoneNumber("+5511999999999"),
            ),
        documentId: DocumentId =
            DocumentId(
                type = DocumentIdType.CPF,
                value = "12345678909",
            ),
        birthDate: Instant = LocalDate.of(1990, 1, 1).atStartOfDay(ZoneOffset.UTC).toInstant(),
        address: CreateAddressRequest = CreateAddressRequestStubs.create(),
    ): CustomerRegisterRequest =
        CustomerRegisterRequest(
            loginEmail = loginEmail,
            password = password,
            name = name,
            contactInfo = contactInfo,
            documentId = documentId,
            birthDate = birthDate,
            address = address,
        )
}
