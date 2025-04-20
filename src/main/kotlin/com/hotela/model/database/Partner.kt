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
    val contactName: String,
    val contactEmail: String,
    val contactPhone: String,
    val contractSigned: Boolean = false,
    val status: PartnerStatus = PartnerStatus.ACTIVE,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val notes: String? = null,
)
