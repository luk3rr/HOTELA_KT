package com.hotela.stubs.dto.request

import com.hotela.model.dto.request.UpdateReviewRequest

object UpdateReviewRequestStubs {
    fun create(
        rating: Int = 5,
        title: String? = "Amazing stay!",
        comment: String? = "Updated great experience!",
        isAnonymous: Boolean? = false,
    ): UpdateReviewRequest =
        UpdateReviewRequest(
            rating = rating,
            title = title,
            comment = comment,
            isAnonymous = isAnonymous,
        )
}
