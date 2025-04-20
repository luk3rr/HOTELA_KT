package com.hotela.model.auth

import java.util.UUID

data class RegisterResponse(
    val id: UUID,
    val message: String,
)
