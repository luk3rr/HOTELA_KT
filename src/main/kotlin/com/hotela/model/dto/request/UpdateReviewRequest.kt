package com.hotela.model.dto.request

data class UpdateReviewRequest(
    val rating: Int,
    val comment: String? = null,
)
