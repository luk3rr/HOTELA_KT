package com.hotela.model.database

import java.time.LocalDate
import java.util.UUID

data class Customer(
    val id: UUID,
    val name: String,
    val email: String,
    val phone: String,
    val idDocument: String,
    val birthDate: LocalDate,
    val address: String,
)
