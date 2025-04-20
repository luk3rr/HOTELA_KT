package com.hotela.model.dto

import java.time.LocalDateTime
import java.util.UUID

data class AuthResponse(
    val authId: UUID,
    val token: String,
    val type: String = "Bearer",
    val expireAt: LocalDateTime = LocalDateTime.now().plusHours(1),
)
