package com.hotela.stubs.database

import com.hotela.model.database.Review
import java.time.LocalDateTime
import java.util.UUID

object ReviewStubs {
    fun create(): Review =
        Review(
            id = UUID.fromString("346a30a6-34ce-45ab-a077-8ae8274cda22"),
            customerId = UUID.fromString("4a55de02-693e-4008-8baf-1b4b9b625f16"),
            hotelId = UUID.fromString("d6382730-8f96-49ec-8b74-43c5489c8647"),
            roomId = UUID.fromString("46267831-9d13-4ee8-aca4-3048c51162c4"),
            rating = 5,
            comment = "Great stay!",
            reviewedAt = LocalDateTime.of(2021, 10, 1, 14, 0),
        )
}
