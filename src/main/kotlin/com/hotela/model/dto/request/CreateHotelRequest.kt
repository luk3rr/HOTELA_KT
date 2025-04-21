package com.hotela.model.dto.request

import java.math.BigDecimal
import java.util.UUID

data class CreateHotelRequest(
    val partnerAuthId: UUID,
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
