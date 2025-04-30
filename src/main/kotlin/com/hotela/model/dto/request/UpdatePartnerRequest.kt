package com.hotela.model.dto.request

import com.hotela.model.enum.PartnerStatus

data class UpdatePartnerRequest(
    val name: String?,
    val cnpj: String?,
    val phone: String?,
    val address: String?,
    val contactName: String?,
    val contactPhone: String?,
    val contactEmail: String?,
    val contractSigned: Boolean?,
    val status: PartnerStatus?,
    val notes: String?,
)
