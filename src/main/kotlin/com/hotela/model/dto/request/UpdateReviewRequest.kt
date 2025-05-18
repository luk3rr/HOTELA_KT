package com.hotela.model.dto.request

data class UpdateReviewRequest(
    val rating: Int?,
    val title: String?,
    val comment: String?,
    val isAnonymous: Boolean?,
)
