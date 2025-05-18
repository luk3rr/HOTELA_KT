package com.hotela.model.dto.request

import com.hotela.model.domain.Email

data class AuthRequest(
    val email: Email,
    val password: String,
)
