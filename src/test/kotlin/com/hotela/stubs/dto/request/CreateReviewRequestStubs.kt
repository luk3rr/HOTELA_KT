package com.hotela.stubs.dto.request

import com.hotela.model.dto.request.CreateReviewRequest
import java.util.UUID

object CreateReviewRequestStubs {
    fun create(
        bookingId: UUID = UUID.fromString("9e33708a-e03f-495f-bbd6-bff40f73d596"),
        rating: Int = 5,
        title: String? = "Amazing stay!",
        comment: String? = "Great experience!",
        isAnonymous: Boolean = false,
    ): CreateReviewRequest =
        CreateReviewRequest(
            bookingId = bookingId,
            rating = rating,
            title = title,
            comment = comment,
            isAnonymous = isAnonymous,
        )
}
