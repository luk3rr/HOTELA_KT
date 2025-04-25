package com.hotela.model.database

import com.hotela.model.enum.PartnerStatus
import java.time.LocalDateTime
import java.util.UUID

data class Partner(
    val id: UUID,
    val name: String,
    val cnpj: String,
    val email: String,
    val phone: String,
    val address: String,
    val contactName: String?,
    val contactEmail: String?,
    val contactPhone: String?,
    val contractSigned: Boolean = false,
    val status: PartnerStatus = PartnerStatus.ACTIVE,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val notes: String? = null,
) {
    init {
        require(name.isNotBlank()) { "Partner name cannot be blank" }
        require(cnpj.isNotBlank()) { "Partner CNPJ cannot be blank" }
        require(email.isNotBlank()) { "Partner email cannot be blank" }
        require(phone.isNotBlank()) { "Partner phone cannot be blank" }
        require(address.isNotBlank()) { "Partner address cannot be blank" }
        contactName?.let { require(it.isNotBlank()) { "Partner contact name cannot be blank" } }
        contactEmail?.let { require(it.isNotBlank()) { "Partner contact email cannot be blank" } }
        contactPhone?.let { require(it.isNotBlank()) { "Partner contact phone cannot be blank" } }
        notes?.let { require(it.isNotBlank()) { "Partner notes cannot be blank" } }
    }
}
