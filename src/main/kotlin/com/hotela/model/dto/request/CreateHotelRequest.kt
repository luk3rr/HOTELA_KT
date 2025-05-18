package com.hotela.model.dto.request

import com.hotela.model.db.Address
import com.hotela.model.domain.ContactInfo
import org.springframework.data.relational.core.mapping.Embedded

data class CreateHotelRequest(
    val name: String,
    @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL)
    val address: Address,
    @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL)
    val contactInfo: ContactInfo,
    val website: String? = null,
    val description: String? = null,
)
