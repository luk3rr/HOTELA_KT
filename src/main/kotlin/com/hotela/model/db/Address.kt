package com.hotela.model.db

import java.math.BigDecimal
import java.util.Locale
import java.util.UUID

data class Address(
    val id: UUID,
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
) {
    companion object {
        val LATITUDE_INTERVAL = -90.0..90.0
        val LONGITUDE_INTERVAL = -180.0..180.0
    }

    init {
        require(streetAddress.isNotBlank()) { "Street address cannot be blank" }
        require(city.isNotBlank()) { "City cannot be blank" }
        require(stateProvince.isNotBlank()) { "State/Province cannot be blank" }
        require(postalCode.isNotBlank()) { "Postal code cannot be blank" }

        require(countryCode.length == 2) { "Country code must be a 2-letter ISO 3166-1 alpha-2 code" }
        require(countryCode.uppercase(Locale.getDefault()) == countryCode) {
            "Country code must be uppercase (e.g., BR, US, PT)"
        }

        longitude?.let {
            require(it.toDouble() in LATITUDE_INTERVAL) { "Longitude must be between $LONGITUDE_INTERVAL" }
        }
        latitude?.let {
            require(it.toDouble() in LONGITUDE_INTERVAL) { "Latitude must be between $LATITUDE_INTERVAL" }
        }
    }

    override fun toString(): String =
        "$streetAddress${if (!number.isNullOrBlank()) ", $number" else ""}, $city - $stateProvince, $postalCode, $countryCode"
}
