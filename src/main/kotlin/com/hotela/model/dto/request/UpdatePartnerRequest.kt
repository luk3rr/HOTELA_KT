package com.hotela.model.dto.request

import com.hotela.model.domain.ContactInfo
import com.hotela.model.domain.DocumentId
import com.hotela.model.enum.PartnerStatus
import org.springframework.data.relational.core.mapping.Embedded

data class UpdatePartnerRequest(
    val companyName: String?,
    val legalName: String?,
    @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL)
    val contactInfo: ContactInfo?,
    @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL)
    val documentId: DocumentId?,
    val contractSignedAt: String?,
    val status: PartnerStatus?,
    val notes: String?,
)
