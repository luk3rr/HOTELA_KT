package com.hotela.repository

import com.hotela.model.db.Address
import java.util.UUID

interface AddressRepository {
    suspend fun findById(id: UUID): Address?

    suspend fun create(address: Address): Address
}
