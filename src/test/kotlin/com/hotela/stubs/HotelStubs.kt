package com.hotela.stubs

import com.hotela.model.database.Hotel
import java.math.BigDecimal
import java.util.UUID

object HotelStubs {
    fun create() =
        Hotel(
            id = UUID.fromString("edab2302-c9d0-480c-a229-46e2b63f625b"),
            partnerId = UUID.fromString("1f69f883-db90-4296-91b1-e99260084808"),
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
