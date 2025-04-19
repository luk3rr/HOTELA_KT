package com.hotela.model

import java.time.LocalDateTime
import java.util.UUID

class CustomerAuth(
    val id: UUID,
    val customerId: UUID,
    val email: String,
    val passwordHash: String,
    val passwordSalt: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val lastLoginAt: LocalDateTime = LocalDateTime.now(),
    val active: Boolean = true,
)
