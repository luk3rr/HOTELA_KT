package com.hotela.stubs.db

import com.hotela.model.db.Review
import java.time.Instant
import java.util.UUID

object ReviewStubs {
    fun create(
        id: UUID = UUID.fromString("346a30a6-34ce-45ab-a077-8ae8274cda22"),
        bookingId: UUID = UUID.fromString("46267831-9d13-4ee8-aca4-3048c51162c4"),
        customerId: UUID = UUID.fromString("a1b2c3d4-e5f6-7890-1234-56789abcdef0"),
        hotelId: UUID = UUID.fromString("12345678-90ab-cdef-1234-567890abcdef"),
        rating: Int = 5,
        title: String? = null,
        comment: String = "Great stay!",
        isAnonymous: Boolean = false,
        reviewedAt: Instant = Instant.now(),
        updatedAt: Instant? = null,
    ): Review =
        Review(
            id = id,
            bookingId = bookingId,
            customerId = customerId,
            hotelId = hotelId,
            rating = rating,
            title = title,
            comment = comment,
            isAnonymous = isAnonymous,
            reviewedAt = reviewedAt,
            updatedAt = updatedAt,
        )
}
