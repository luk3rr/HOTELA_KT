package com.hotela.stubs.db

import com.hotela.model.db.RoomType
import java.util.UUID

object RoomTypeStubs {
    fun createStandardSolteiro(
        id: UUID = UUID.fromString("50d14717-f2bd-43ea-9f24-600b850734a6"),
        name: String = "Standard Solteiro",
        description: String = "Quarto padrão com uma cama de solteiro",
    ): RoomType =
        RoomType(
            id = id,
            name = name,
            description = description,
        )

    fun createCasalDeluxe(
        id: UUID = UUID.fromString("b996b787-0fee-4852-ba4e-2ce3165d3408"),
        name: String = "Casal Deluxe",
        description: String = "Quarto com cama de casal, varanda e amenidades premium",
    ): RoomType =
        RoomType(
            id = id,
            name = name,
            description = description,
        )
}
