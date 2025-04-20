package com.hotela.repository

import com.hotela.model.database.PartnerAuth
import java.util.UUID

interface PartnerAuthRepository {
    suspend fun findById(id: UUID): PartnerAuth?

    suspend fun findByEmail(email: String): PartnerAuth?

    suspend fun existsByEmail(email: String): Boolean

    suspend fun save(partner: PartnerAuth): PartnerAuth
}
