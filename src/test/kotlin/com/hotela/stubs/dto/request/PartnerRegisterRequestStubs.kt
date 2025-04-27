package com.hotela.stubs.dto.request

import com.hotela.model.dto.request.PartnerRegisterRequest

object PartnerRegisterRequestStubs {
    fun create(): PartnerRegisterRequest =
        PartnerRegisterRequest(
            email = "john@doe.com",
            password = "password",
            name = "John Doe",
            cnpj = "12.345.678/0001-90",
            phone = "+55 11 91234-5678",
            address = "123 Main St, City, State, 12345-678",
        )
}
