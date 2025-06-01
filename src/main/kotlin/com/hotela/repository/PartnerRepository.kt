package com.hotela.repository

import com.hotela.model.db.Partner
import com.hotela.model.domain.Email
import java.util.UUID

interface PartnerRepository {
    suspend fun findById(id: UUID): Partner?

    suspend fun findByAuthId(authId: UUID): Partner?

    suspend fun findByEmail(email: Email): Partner?

    suspend fun existsByEmail(email: Email): Boolean

    suspend fun create(partner: Partner): Partner

    suspend fun update(partner: Partner): Partner
}
