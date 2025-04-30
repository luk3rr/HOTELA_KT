package com.hotela.stubs.database

import com.hotela.model.database.Hotel
import java.math.BigDecimal
import java.util.UUID

object HotelStubs {
    fun create(partnerId: UUID = UUID.fromString("99f9f48d-7956-4638-9b1a-891f19d28b58")) =
        Hotel(
            id = UUID.fromString("edab2302-c9d0-480c-a229-46e2b63f625b"),
            partnerId = partnerId,
            name = "Hotel Test",
            address = "123 Test St",
            city = "Test City",
            state = "Test State",
            zipCode = "12345",
            phone = "123-456-7890",
            rating = BigDecimal("4.7"),
            description = "A test hotel",
            website = "https://testhotel.com",
            latitude = BigDecimal("37.7749"),
            longitude = BigDecimal("-122.4194"),
        )
}
