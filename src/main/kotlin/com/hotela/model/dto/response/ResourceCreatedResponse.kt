package com.hotela.model.dto.response

import java.util.UUID

data class ResourceCreatedResponse(
    val id: UUID,
    val message: String,
)
