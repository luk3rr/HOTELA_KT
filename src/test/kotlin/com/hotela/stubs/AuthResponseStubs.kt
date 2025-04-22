package com.hotela.stubs

import com.hotela.model.dto.response.AuthResponse
import java.util.UUID

object AuthResponseStubs {
    fun create(): AuthResponse =
        AuthResponse(
            authId = UUID.fromString("c742d828-9690-4b45-b763-d29e8e01d64b"),
            token =
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6Ikpv" +
                    "aG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.KMUFsIDTnFmyG3nMiGM6H9FNFUROf3wh7SmqJp-QV30",
        )
}
