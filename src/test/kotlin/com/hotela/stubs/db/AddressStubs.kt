package com.hotela.stubs.db

import com.hotela.model.db.Address
import java.math.BigDecimal
import java.util.UUID

object AddressStubs {
    fun create(
        id: UUID = UUID.fromString("f7f52963-8e4f-4b86-aea9-90b6b95ac775"),
        streetAddress: String = "123 Main St",
        number: String? = "100",
        complement: String? = "Apt 202",
        neighborhood: String? = "Downtown",
        city: String = "Springfield",
        stateProvince: String = "SP",
        postalCode: String = "12345-678",
        countryCode: String = "BR",
        latitude: BigDecimal? = BigDecimal(" -23.55052"),
        longitude: BigDecimal? = BigDecimal("-46.633308")
    ): Address =
        Address(
            id = id,
            streetAddress = streetAddress,
            number = number,
            complement = complement,
            neighborhood = neighborhood,
            city = city,
            stateProvince = stateProvince,
            postalCode = postalCode,
            countryCode = countryCode,
            latitude = latitude,
            longitude = longitude
        )
}
