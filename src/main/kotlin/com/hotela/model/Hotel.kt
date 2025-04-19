package com.hotela.model

import java.math.BigDecimal
import java.util.UUID

class Hotel(
    val id: UUID,
    val partnerId: UUID,
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
