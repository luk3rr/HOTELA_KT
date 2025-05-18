package com.hotela.model.db

import com.hotela.model.domain.Email
import com.hotela.model.enum.Role
import java.time.Instant
import java.util.UUID

data class AuthCredential(
    val id: UUID,
    val loginEmail: Email,
    val password: String,
    val role: Role,
    val isActive: Boolean = true,
    val lastLoginAt: Instant? = null,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant? = null,
) {
    init {
        require(password.isNotBlank()) { "Customer password cannot be blank" }
    }
}
