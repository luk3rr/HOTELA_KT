package com.hotela.stubs.dto.request

import com.hotela.model.dto.request.AuthRequest

object AuthRequestStubs {
    fun create(): AuthRequest = AuthRequest(email = "john@doe.com", password = "password")
}
