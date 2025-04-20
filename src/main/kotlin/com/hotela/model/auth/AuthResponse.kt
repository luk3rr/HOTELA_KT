package com.hotela.model.auth

import java.time.LocalDateTime

data class AuthResponse(
    val token: String,
    val type: String = "Bearer",
    val expireAt: LocalDateTime = LocalDateTime.now().plusHours(1),
)
