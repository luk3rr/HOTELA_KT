package com.hotela.stubs.database

import com.hotela.model.database.Payment
import com.hotela.model.enum.PaymentMethod
import com.hotela.model.enum.PaymentStatus
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

object PaymentStubs {
    fun create(): Payment =
        Payment(
            id = UUID.fromString("960942e8-4768-4b3d-97eb-465c97143d36"),
            bookingId = UUID.fromString("ef6f8038-85ba-411e-9959-ed35350f6f55"),
            transactionId = "1234567890",
            amount = BigDecimal("100.00"),
            paymentMethod = PaymentMethod.PIX,
            status = PaymentStatus.COMPLETED,
            paidAt = LocalDateTime.of(2021, 10, 1, 14, 0),
        )
}
