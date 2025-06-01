package com.hotela.model.dto.request

import com.hotela.model.domain.ContactInfo
import com.hotela.model.domain.DocumentId
import org.springframework.data.relational.core.mapping.Embedded
import java.time.Instant

data class UpdateCustomerRequest(
    val name: String?,
    @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL)
    val contactInfo: ContactInfo?,
    @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL)
    val documentId: DocumentId?,
    val birthDate: Instant?,
)
