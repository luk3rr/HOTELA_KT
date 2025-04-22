package com.hotela.stubs

import com.hotela.model.dto.request.UpdateHotelRequest

object UpdateHotelRequestStubs {
    fun create(): UpdateHotelRequest =
        UpdateHotelRequest(
            name = "John Smith",
            phone = "+1-555-1234",
            description = "Lorem ipsum dolor sit amet.",
            website = "https://example.com",
        )
}
