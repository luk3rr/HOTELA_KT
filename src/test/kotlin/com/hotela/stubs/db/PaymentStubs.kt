package com.hotela.stubs.db

import com.hotela.model.db.Payment
import com.hotela.model.enum.PaymentMethod
import com.hotela.model.enum.PaymentStatus
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

object PaymentStubs {
    fun create(
        id: UUID = UUID.fromString("960942e8-4768-4b3d-97eb-465c97143d36"),
        bookingId: UUID = UUID.fromString("ef6f8038-85ba-411e-9959-ed35350f6f55"),
        externalTransactionId: String = "1234567890",
        amountPaid: BigDecimal = BigDecimal("100.00"),
        paymentMethod: PaymentMethod = PaymentMethod.PIX,
        status: PaymentStatus = PaymentStatus.CONFIRMED,
        processedAt: Instant? = Instant.parse("2021-10-01T14:00:00Z"),
        metadata: String? = null,
    ): Payment =
        Payment(
            id = id,
            bookingId = bookingId,
            externalTransactionId = externalTransactionId,
            amountPaid = amountPaid,
            paymentMethod = paymentMethod,
            status = status,
            processedAt = processedAt,
            metadata = metadata,
        )
}
