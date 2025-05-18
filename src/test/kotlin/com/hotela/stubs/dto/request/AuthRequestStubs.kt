package com.hotela.stubs.dto.request

import com.hotela.model.domain.Email
import com.hotela.model.dto.request.AuthRequest

object AuthRequestStubs {
    fun create(): AuthRequest = AuthRequest(email = Email("john@doe.com"), password = "password")
}
