package com.hotela.stubs.dto.response

import com.hotela.model.dto.response.ResourceCreatedResponse
import java.util.UUID

object ResourceCreatedResponseStubs {
    fun create(): ResourceCreatedResponse =
        ResourceCreatedResponse(
            id = UUID.fromString("22b765f2-a01e-4406-aa45-1fc6d174482c"),
            message = "Resource created successfully",
        )
}
