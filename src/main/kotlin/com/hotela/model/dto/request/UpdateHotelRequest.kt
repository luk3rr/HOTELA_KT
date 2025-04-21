package com.hotela.model.dto.request

import java.math.BigDecimal

data class UpdateHotelRequest(
    val name: String? = null,
    val address: String? = null,
    val city: String? = null,
    val state: String? = null,
    val zipCode: String? = null,
    val phone: String? = null,
    val rating: BigDecimal? = null,
    val description: String? = null,
    val website: String? = null,
    val latitude: BigDecimal? = null,
    val longitude: BigDecimal? = null,
)
