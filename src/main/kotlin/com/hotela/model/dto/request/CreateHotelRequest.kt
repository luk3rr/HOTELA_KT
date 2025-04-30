package com.hotela.model.dto.request

import java.math.BigDecimal

data class CreateHotelRequest(
    val name: String,
    val address: String,
    val city: String,
    val state: String,
    val zipCode: String,
    val phone: String,
    val rating: BigDecimal,
    val description: String? = null,
    val website: String? = null,
    val latitude: BigDecimal,
    val longitude: BigDecimal,
)
