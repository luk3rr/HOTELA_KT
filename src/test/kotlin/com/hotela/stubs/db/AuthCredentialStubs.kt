package com.hotela.stubs.db

import com.hotela.model.db.AuthCredential
import com.hotela.model.domain.Email
import com.hotela.model.enum.Role
import java.time.Instant
import java.util.UUID

object AuthCredentialStubs {
    fun create(
        id: UUID = UUID.fromString("c489e321-9749-4ef3-b846-00b78778ad5e"),
        loginEmail: Email = Email("john@doe.com"),
        password: String = "hashed_password",
        role: Role = Role.CUSTOMER,
        isActive: Boolean = true,
        lastLoginAt: Instant? = Instant.parse("2023-01-01T00:00:00Z"),
        createdAt: Instant = Instant.parse("2023-01-01T00:00:00Z"),
        updatedAt: Instant? = null,
    ): AuthCredential =
        AuthCredential(
            id = id,
            loginEmail = loginEmail,
            password = password,
            role = role,
            isActive = isActive,
            lastLoginAt = lastLoginAt,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )
}
