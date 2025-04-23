package com.hotela.stubs

import com.hotela.model.database.Customer
import java.time.LocalDate
import java.util.UUID

object CustomerStubs {
    fun create(): Customer = Customer(
        id = UUID.fromString("ebaf6044-da61-47e3-84b6-82ebb3d738e1"),
        name = "John Doe",
        email = "john@doe.com",
        phone = "+55 11 91234-5678",
        idDocument = "12345678901",
        birthDate = LocalDate.of(1990, 1, 1),
        address = "123 Main St, Springfield, USA",
    )
}