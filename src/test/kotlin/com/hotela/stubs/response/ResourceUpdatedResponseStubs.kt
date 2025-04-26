package com.hotela.stubs.response

import com.hotela.model.dto.response.ResourceUpdatedResponse

object ResourceUpdatedResponseStubs {
    fun create(): ResourceUpdatedResponse =
        ResourceUpdatedResponse(
            message = "Resource updated successfully",
        )
}
