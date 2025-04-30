package com.hotela.stubs.dto.request

import com.hotela.model.dto.request.UpdatePartnerRequest
import com.hotela.model.enum.PartnerStatus

object UpdatePartnerRequestStubs {
    fun create(): UpdatePartnerRequest =
        UpdatePartnerRequest(
            name = "John F Doe",
            cnpj = "12.345.678/0001-90",
            phone = "+1234567890",
            address = "345 Elm St, Springfield, USA",
            contactName = "Jane F Doe",
            contactPhone = "+0987654321",
            contactEmail = "janeF@doe.com",
            contractSigned = false,
            status = PartnerStatus.INACTIVE,
            notes = "This is a test note",
        )
}
