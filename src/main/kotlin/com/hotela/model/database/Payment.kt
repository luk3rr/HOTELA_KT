package com.hotela.model.database

import com.hotela.model.enum.PaymentMethod
import com.hotela.model.enum.PaymentStatus
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

data class Payment(
    val id: UUID,
    val bookingId: UUID,
    val transactionId: String,
    val amount: BigDecimal,
    val paymentMethod: PaymentMethod,
    val status: PaymentStatus,
    val paidAt: LocalDateTime = LocalDateTime.now(),
)
