package com.hotela.model.dto.request

import java.math.BigDecimal

data class CreateAddressRequest(
    val streetAddress: String,
    val number: String? = null,
    val complement: String? = null,
    val neighborhood: String? = null,
    val city: String,
    val stateProvince: String,
    val postalCode: String,
    val countryCode: String,
    val latitude: BigDecimal? = null,
    val longitude: BigDecimal? = null,
)
