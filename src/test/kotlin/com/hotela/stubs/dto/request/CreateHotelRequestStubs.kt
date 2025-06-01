package com.hotela.stubs.dto.request

import com.hotela.model.db.Address
import com.hotela.model.db.Hotel
import com.hotela.model.domain.ContactInfo
import com.hotela.model.domain.Email
import com.hotela.model.domain.PhoneNumber
import com.hotela.model.dto.request.CreateHotelRequest
import com.hotela.stubs.db.AddressStubs

object CreateHotelRequestStubs {
    fun create(
        name: String = "Hotel California",
        address: Address = AddressStubs.create(),
        email: Email = Email("contact@hotelcalifornia.com"),
        phone: PhoneNumber = PhoneNumber("+1-800-555-0199"),
        website: String? = "https://www.hotelcalifornia.com",
        description: String? = "A lovely place to stay.",
    ): CreateHotelRequest =
        CreateHotelRequest(
            name = name,
            address = address,
            contactInfo =
                ContactInfo(
                    email = email,
                    phone = phone,
                ),
            website = website,
            description = description,
        )

    fun create(hotel: Hotel): CreateHotelRequest =
        CreateHotelRequest(
            name = hotel.name,
            address = AddressStubs.create(id = hotel.addressId),
            contactInfo = hotel.contactInfo,
            website = hotel.website,
            description = hotel.description,
        )
}
