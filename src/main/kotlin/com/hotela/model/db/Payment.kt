package com.hotela.model.db

import com.hotela.model.enum.PaymentMethod
import com.hotela.model.enum.PaymentStatus
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

data class Payment(
    val id: UUID,
    val bookingId: UUID,
    val externalTransactionId: String,
    val amountPaid: BigDecimal,
    val paymentMethod: PaymentMethod,
    val status: PaymentStatus = PaymentStatus.PENDING,
    val createdAt: Instant = Instant.now(),
    val processedAt: Instant? = null,
    val metadata: String? = null,
) {
    init {
        require(externalTransactionId.isNotBlank()) { "Transaction ID cannot be blank" }
        require(amountPaid > BigDecimal.ZERO) { "Amount must be greater than zero" }
    }
}
