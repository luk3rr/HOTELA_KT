package com.hotela.model.dto.request

import java.util.UUID

data class CreateReviewRequest(
    val bookingId: UUID,
    val rating: Int,
    val comment: String? = null,
)
