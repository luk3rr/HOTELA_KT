package com.hotela.model.dto.request

import java.util.UUID

data class CreateReviewRequest(
    val bookingId: UUID,
    val rating: Int,
    val title: String? = null,
    val comment: String? = null,
    val isAnonymous: Boolean = false,
)
