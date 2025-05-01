package com.hotela.stubs.dto.request

import com.hotela.model.dto.request.UpdateReviewRequest

object UpdateReviewRequestStubs {
    fun create(
        rating: Int,
        comment: String?,
    ): UpdateReviewRequest =
        UpdateReviewRequest(
            rating = rating,
            comment = comment,
        )
}
