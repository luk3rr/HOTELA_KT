package com.hotela.model.db

import com.hotela.model.domain.ContactInfo
import com.hotela.model.domain.DocumentId
import org.springframework.data.relational.core.mapping.Embedded
import java.time.Instant
import java.util.UUID

data class Customer(
    val id: UUID,
    val authCredentialId: UUID,
    val addressId: UUID,
    val name: String,
    @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL)
    val contactInfo: ContactInfo,
    @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL, prefix = "document_id_")
    val documentId: DocumentId,
    val birthDate: Instant?,
) {
    init {
        require(name.isNotBlank()) { "Customer name cannot be blank" }
    }
}
