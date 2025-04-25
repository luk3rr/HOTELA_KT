package com.hotela.model.database

import java.math.BigDecimal
import java.util.UUID

data class Hotel(
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
) {
    companion object {
        val COORDINATES_INTERVAL = -90.0..90.0
        val RATING_INTERVAL = BigDecimal.ZERO..BigDecimal("5.0")
        const val RATING_DECIMAL_PLACES = 1
        const val MAX_DESCRIPTION_LENGTH = 500
    }

    init {
        require(name.isNotBlank()) { "Hotel name cannot be blank" }
        require(address.isNotBlank()) { "Hotel address cannot be blank" }
        require(city.isNotBlank()) { "Hotel city cannot be blank" }
        require(state.isNotBlank()) { "Hotel state cannot be blank" }
        require(zipCode.isNotBlank()) { "Hotel zip code cannot be blank" }
        require(phone.isNotBlank()) { "Hotel phone cannot be blank" }
        require(rating in RATING_INTERVAL) { "Hotel rating must be between $RATING_INTERVAL" }
        require(rating.scale() == RATING_DECIMAL_PLACES) {
            "Hotel rating must have $RATING_DECIMAL_PLACES decimal places"
        }
        description?.let {
            require(it.length <= MAX_DESCRIPTION_LENGTH) {
                "Hotel description length must be less than or equal to 500"
            }
        }
        website?.let { require(it.isNotBlank()) { "Hotel website cannot be blank" } }
        require(latitude.toDouble() in COORDINATES_INTERVAL) { "Hotel latitude must be between $COORDINATES_INTERVAL" }
        require(longitude.toDouble() in COORDINATES_INTERVAL) { "Hotel longitude must be between $COORDINATES_INTERVAL" }
    }
}
