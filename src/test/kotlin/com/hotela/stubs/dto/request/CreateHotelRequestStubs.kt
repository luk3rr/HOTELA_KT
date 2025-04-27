package com.hotela.stubs.dto.request

import com.hotela.model.database.Hotel
import com.hotela.model.dto.request.CreateHotelRequest
import java.math.BigDecimal

object CreateHotelRequestStubs {
    fun create(): CreateHotelRequest =
        CreateHotelRequest(
            name = "Hotel California",
            address = "123 Sunset Boulevard",
            city = "Los Angeles",
            state = "CA",
            zipCode = "90001",
            phone = "+1-800-555-0199",
            rating = BigDecimal("4.5"),
            description = "A lovely place to stay.",
            website = "https://www.hotelcalifornia.com",
            latitude = BigDecimal("34.0522"),
            longitude = BigDecimal("-118.2437"),
        )

    fun create(hotel: Hotel): CreateHotelRequest =
        CreateHotelRequest(
            name = hotel.name,
            address = hotel.address,
            city = hotel.city,
            state = hotel.state,
            zipCode = hotel.zipCode,
            phone = hotel.phone,
            rating = hotel.rating,
            description = hotel.description,
            website = hotel.website,
            latitude = hotel.latitude,
            longitude = hotel.longitude,
        )
}
