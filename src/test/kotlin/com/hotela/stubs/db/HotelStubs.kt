package com.hotela.stubs.db

import com.hotela.model.db.Hotel
import com.hotela.model.domain.ContactInfo
import com.hotela.model.domain.Email
import com.hotela.model.domain.PhoneNumber
import java.math.BigDecimal
import java.util.UUID

object HotelStubs {
    fun create(
        id: UUID = UUID.fromString("edab2302-c9d0-480c-a229-46e2b63f625b"),
        partnerId: UUID = UUID.fromString("99f9f48d-7956-4638-9b1a-891f19d28b58"),
        addressId: UUID = UUID.fromString("35e9f4d8-90ea-4a91-bd9b-c5df9d888264"),
        name: String = "Hotel Test",
        contactInfo: ContactInfo =
            ContactInfo(
                email = Email("contact@testhotel.com"),
                phone = PhoneNumber("+55 11 91234-5678"),
            ),
        website: String = "https://testhotel.com",
        description: String = "A test hotel with modern amenities and excellent service.",
        starRating: BigDecimal = BigDecimal("4.7"),
    ): Hotel =
        Hotel(
            id = id,
            partnerId = partnerId,
            addressId = addressId,
            name = name,
            contactInfo = contactInfo,
            website = website,
            description = description,
            starRating = starRating,
        )
}
