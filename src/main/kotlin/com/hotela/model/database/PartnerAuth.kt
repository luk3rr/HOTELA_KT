package com.hotela.model.database

import java.time.LocalDateTime
import java.util.UUID

data class PartnerAuth(
    val id: UUID,
    val partnerId: UUID,
    val email: String,
    val passwordHash: String,
    val passwordSalt: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val lastLogin: LocalDateTime = LocalDateTime.now(),
    val active: Boolean = true,
)
