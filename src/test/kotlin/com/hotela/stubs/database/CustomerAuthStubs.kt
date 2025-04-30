package com.hotela.stubs.database

import com.hotela.model.database.CustomerAuth
import java.time.LocalDateTime
import java.util.UUID

object CustomerAuthStubs {
    fun create(customerId: UUID = UUID.fromString("6740350c-8907-45f6-bb3f-9abd7fa8611f")): CustomerAuth =
        CustomerAuth(
            id = UUID.fromString("c489e321-9749-4ef3-b846-00b78778ad5e"),
            customerId = customerId,
            email = "john@doe.com",
            passwordHash = "hashed_password",
            createdAt = LocalDateTime.of(2023, 1, 1, 0, 0),
            lastLogin = LocalDateTime.of(2023, 1, 1, 0, 0),
            active = true,
        )
}
