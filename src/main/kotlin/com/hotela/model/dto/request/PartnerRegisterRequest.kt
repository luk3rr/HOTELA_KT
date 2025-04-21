package com.hotela.model.dto.request

import com.hotela.model.enum.PartnerStatus

data class PartnerRegisterRequest(
    val email: String,
    val password: String,
    val name: String,
    val cnpj: String,
    val phone: String,
    val address: String,
    val contactName: String?,
    val contactPhone: String?,
    val contactEmail: String?,
    val contractSigned: Boolean = false,
    val status: PartnerStatus = PartnerStatus.ACTIVE,
    val notes: String? = null,
)
