package com.hotela.model.db

import java.time.Instant
import java.util.UUID

data class Review(
    val id: UUID,
    val bookingId: UUID,
    val customerId: UUID,
    val hotelId: UUID,
    val rating: Int,
    val title: String? = null,
    val comment: String? = null,
    val isAnonymous: Boolean = false,
    val reviewedAt: Instant = Instant.now(),
    val updatedAt: Instant? = null,
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
