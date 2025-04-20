package com.hotela.model.database

import java.time.LocalDateTime
import java.util.UUID

data class CustomerAuth(
    val id: UUID,
    val customerId: UUID,
    val email: String,
    val passwordHash: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val lastLogin: LocalDateTime = LocalDateTime.now(),
    val active: Boolean = true,
)
