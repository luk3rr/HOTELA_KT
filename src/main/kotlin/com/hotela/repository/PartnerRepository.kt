package com.hotela.repository

import com.hotela.model.database.Partner
import java.util.UUID

interface PartnerRepository {
    suspend fun findById(id: UUID): Partner?

    suspend fun findByEmail(email: String): Partner?

    suspend fun existsByEmail(email: String): Boolean

    suspend fun save(partner: Partner): Partner
}
