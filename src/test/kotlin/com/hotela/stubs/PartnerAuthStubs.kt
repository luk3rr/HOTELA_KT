package com.hotela.stubs

import com.hotela.model.database.PartnerAuth
import java.time.LocalDateTime
import java.util.UUID

object PartnerAuthStubs {
    fun create(): PartnerAuth = PartnerAuth(
        id = UUID.fromString("99f9f48d-7956-4638-9b1a-891f19d28b58"),
        partnerId = UUID.fromString("ae09a055-9763-4640-afbe-335b147a1bcb"),
        email = "john@doe.com",
        passwordHash = "hashed_password",
        createdAt = LocalDateTime.of(2021, 10, 1, 14, 0),
        lastLogin = LocalDateTime.of(2021, 10, 3, 14, 0),
        active = true,
    )
}