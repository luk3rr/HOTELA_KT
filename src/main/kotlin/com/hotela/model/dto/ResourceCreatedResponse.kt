package com.hotela.model.dto

import java.util.UUID

data class ResourceCreatedResponse(
    val id: UUID,
    val message: String,
)
