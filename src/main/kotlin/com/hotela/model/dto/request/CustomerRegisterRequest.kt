package com.hotela.model.dto.request

import com.hotela.model.domain.ContactInfo
import com.hotela.model.domain.DocumentId
import com.hotela.model.domain.Email
import org.springframework.data.relational.core.mapping.Embedded
import java.time.Instant

data class CustomerRegisterRequest(
    val loginEmail: Email,
    val password: String,
    val name: String,
    @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL)
    val contactInfo: ContactInfo,
    @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL)
    val documentId: DocumentId,
    val birthDate: Instant? = null,
    @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL)
    val address: CreateAddressRequest,
)
