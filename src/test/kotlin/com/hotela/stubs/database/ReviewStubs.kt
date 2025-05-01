package com.hotela.stubs.database

import com.hotela.model.database.Review
import java.time.LocalDateTime
import java.util.UUID

object ReviewStubs {
    fun create(
        id: UUID = UUID.fromString("346a30a6-34ce-45ab-a077-8ae8274cda22"),
        bookingId: UUID = UUID.fromString("46267831-9d13-4ee8-aca4-3048c51162c4"),
        rating: Int = 5,
        comment: String = "Great stay!",
        reviewedAt: LocalDateTime = LocalDateTime.now(),
        updatedAt: LocalDateTime = LocalDateTime.now(),
    ): Review =
        Review(
            id = id,
            bookingId = bookingId,
            rating = rating,
            comment = comment,
            reviewedAt = reviewedAt,
            updatedAt = updatedAt,
        )
}
