package com.hotela.stubs

import com.hotela.model.dto.request.CustomerRegisterRequest
import java.time.LocalDate

object CustomerRegisterRequestStubs {
    fun create(): CustomerRegisterRequest =
        CustomerRegisterRequest(
            email = "john@doe.com",
            password = "password",
            name = "John Doe",
            phone = "1234567890",
            idDocument = "AB123456",
            birthDate = LocalDate.of(1990, 1, 1),
            address = "123 Main Street, Springfield",
        )
}
