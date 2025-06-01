package com.hotela.model.db

import com.hotela.model.domain.ContactInfo
import com.hotela.model.domain.DocumentId
import com.hotela.model.enum.PartnerStatus
import org.springframework.data.relational.core.mapping.Embedded
import java.time.Instant
import java.util.UUID

data class Partner(
    val id: UUID,
    val authCredentialId: UUID,
    val companyName: String,
    val legalName: String,
    @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL)
    val contactInfo: ContactInfo,
    @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL, prefix = "document_id_")
    val documentId: DocumentId,
    val contractSignedAt: Instant,
    val status: PartnerStatus,
    val notes: String? = null,
) {
    init {
        require(companyName.isNotBlank()) { "Company name cannot be blank" }
        require(legalName.isNotBlank()) { "Legal name cannot be blank" }
        notes?.let { require(it.isNotBlank()) { "Partner notes cannot be blank" } }
    }
}
