package com.hotela.stubs.dto.request

import com.hotela.model.dto.request.CreateReviewRequest
import java.util.UUID

object CreateReviewRequestStubs {
    fun create(
        bookingId: UUID = UUID.fromString("9e33708a-e03f-495f-bbd6-bff40f73d596"),
        rating: Int = 5,
        comment: String? = "Great experience!",
    ): CreateReviewRequest =
        CreateReviewRequest(
            bookingId = bookingId,
            rating = rating,
            comment = comment,
        )
}
