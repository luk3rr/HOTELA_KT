package com.hotela.stubs.request

import com.hotela.model.dto.request.UpdateCustomerRequest
import java.time.LocalDate

object UpdateCustomerRequestStubs {
    fun create(): UpdateCustomerRequest =
        UpdateCustomerRequest(
            name = "Jane Doe",
            phone = "+1234567890",
            idDocument = "123456789",
            birthDate = LocalDate.of(2000, 1, 1),
            address = "345 Elm St, Springfield, USA",
        )
}
