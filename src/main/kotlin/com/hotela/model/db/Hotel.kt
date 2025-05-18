package com.hotela.model.db

import com.hotela.model.domain.ContactInfo
import org.springframework.data.relational.core.mapping.Embedded
import java.math.BigDecimal
import java.util.UUID

data class Hotel(
    val id: UUID,
    val partnerId: UUID,
    val addressId: UUID,
    val name: String,
    @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL)
    val contactInfo: ContactInfo,
    val website: String? = null,
    val description: String? = null,
    val starRating: BigDecimal? = null,
) {
    companion object {
        val RATING_INTERVAL = BigDecimal.ZERO..BigDecimal("5.0")
        const val RATING_DECIMAL_PLACES = 1
        const val MAX_DESCRIPTION_LENGTH = 500
    }

    init {
        require(name.isNotBlank()) { "Hotel name cannot be blank" }
        starRating?.let {
            require(it.scale() == RATING_DECIMAL_PLACES) {
                "Hotel rating must have $RATING_DECIMAL_PLACES decimal places"
            }

            require(it in RATING_INTERVAL) { "Hotel rating must be between $RATING_INTERVAL" }
        }
        description?.let {
            require(it.length <= MAX_DESCRIPTION_LENGTH) {
                "Hotel description length must be less than or equal to 500"
            }
        }
        website?.let { require(it.isNotBlank()) { "Hotel website cannot be blank" } }
    }
}
