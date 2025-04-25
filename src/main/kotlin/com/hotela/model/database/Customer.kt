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
) {
    init {
        require(name.isNotBlank()) { "Customer name cannot be blank" }
        require(email.isNotBlank()) { "Customer email cannot be blank" }
        require(phone.isNotBlank()) { "Customer phone cannot be blank" }
        require(idDocument.isNotBlank()) { "Customer ID document cannot be blank" }
        require(address.isNotBlank()) { "Customer address cannot be blank" }
    }
}
