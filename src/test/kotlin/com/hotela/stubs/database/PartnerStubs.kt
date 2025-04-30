package com.hotela.stubs.database

import com.hotela.model.database.Partner
import com.hotela.model.enum.PartnerStatus
import java.time.LocalDateTime
import java.util.UUID

object PartnerStubs {
    fun create(): Partner =
        Partner(
            id = UUID.fromString("32136c3c-bb36-4e65-ae16-a91c61195319"),
            name = "John Doe",
            cnpj = "12345678000195",
            email = "john@doe.com",
            phone = "1234567890",
            address = "123 Main St, Springfield, USA",
            contactName = "Jane Doe",
            contactEmail = "jane@doe.com",
            contactPhone = "0987654321",
            contractSigned = true,
            status = PartnerStatus.ACTIVE,
            createdAt = LocalDateTime.of(2019, 5, 5, 5, 5),
            notes = "Test partner",
        )
}
