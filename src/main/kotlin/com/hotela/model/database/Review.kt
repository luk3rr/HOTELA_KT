package com.hotela.model.database

import java.time.LocalDateTime
import java.util.UUID

data class Review(
    val id: UUID,
    val customerId: UUID,
    val hotelId: UUID,
    val roomId: UUID,
    val rating: Int,
    val comment: String? = null,
    val reviewedAt: LocalDateTime = LocalDateTime.now(),
) {
    companion object {
        const val MAX_COMMENT_LENGTH = 500
        val RATING_INTERVAL = 1..5
    }

    init {
        require(rating in RATING_INTERVAL) { "Rating must be between $RATING_INTERVAL" }

        comment?.let {
            require(it.length <= MAX_COMMENT_LENGTH) { "Comment length must be less than or equal to $MAX_COMMENT_LENGTH" }
        }
    }
}
