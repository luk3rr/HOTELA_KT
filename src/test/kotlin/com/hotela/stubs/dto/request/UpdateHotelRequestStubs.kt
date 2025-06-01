package com.hotela.stubs.dto.request

import com.hotela.model.db.Address
import com.hotela.model.domain.ContactInfo
import com.hotela.model.domain.Email
import com.hotela.model.domain.PhoneNumber
import com.hotela.model.dto.request.UpdateHotelRequest
import com.hotela.stubs.db.AddressStubs

object UpdateHotelRequestStubs {
    fun create(
        name: String? = "John Smith",
        address: Address? = AddressStubs.create(),
        contactInfo: ContactInfo? =
            ContactInfo(
                email = Email("contact@hotel.com"),
                phone = PhoneNumber("+1-555-1234"),
            ),
        website: String? = "https://example.com",
        description: String? = "Lorem ipsum dolor sit amet.",
    ): UpdateHotelRequest =
        UpdateHotelRequest(
            name = name,
            address = address,
            contactInfo = contactInfo,
            website = website,
            description = description,
        )
}
