package com.hotela.model.dto.response

data class AuthResponse(
    val token: String,
    val type: String = "Bearer",
)
