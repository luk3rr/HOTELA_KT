package com.hotela.repository

import com.hotela.model.db.Customer
import com.hotela.model.domain.Email
import java.util.UUID

interface CustomerRepository {
    suspend fun findById(id: UUID): Customer?

    suspend fun findByAuthId(authId: UUID): Customer?

    suspend fun findByEmail(email: Email): Customer?

    suspend fun existsByEmail(email: Email): Boolean

    suspend fun create(customer: Customer): Customer

    suspend fun update(customer: Customer): Customer
}
