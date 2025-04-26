package com.hotela.model.dto.request

import java.time.LocalDate

data class UpdateCustomerRequest(
    val name: String?,
    val phone: String?,
    val idDocument: String?,
    val birthDate: LocalDate?,
    val address: String?,
)
