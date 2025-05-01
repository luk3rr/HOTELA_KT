package com.hotela.stubs.dto.request

import com.hotela.model.dto.request.CreateReviewRequest
import java.util.UUID

object CreateReviewRequestStubs {
    fun create(
        bookingId: UUID,
        rating: Int,
        comment: String?,
    ): CreateReviewRequest =
        CreateReviewRequest(
            bookingId = bookingId,
            rating = rating,
            comment = comment,
        )
}
