package com.hotela.stubs.dto.request

import com.hotela.model.dto.request.CreateAddressRequest
import java.math.BigDecimal

object CreateAddressRequestStubs {
    fun create(
        streetAddress: String = "123 Main St",
        number: String? = "100",
        complement: String? = "Apt 202",
        neighborhood: String? = "Downtown",
        city: String = "Springfield",
        stateProvince: String = "SP",
        postalCode: String = "12345-678",
        countryCode: String = "BR",
        latitude: BigDecimal? = BigDecimal("-23.55052"),
        longitude: BigDecimal? = BigDecimal("-46.633308"),
    ): CreateAddressRequest =
        CreateAddressRequest(
            streetAddress = streetAddress,
            number = number,
            complement = complement,
            neighborhood = neighborhood,
            city = city,
            stateProvince = stateProvince,
            postalCode = postalCode,
            countryCode = countryCode,
            latitude = latitude,
            longitude = longitude,
        )
}
