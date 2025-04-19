package com.hotela.model

import java.time.LocalDateTime
import java.util.UUID

class Review(
    val id: UUID,
    val customerId: UUID,
    val hotelId: UUID,
    val roomId: UUID,
    val rating: Int,
    val comment: String? = null,
    val reviewedAt: LocalDateTime = LocalDateTime.now(),
)
