package com.hotela.repository

import com.hotela.model.database.Customer
import java.util.UUID

interface CustomerRepository {
    suspend fun findById(id: UUID): Customer?

    suspend fun findByEmail(email: String): Customer?

    suspend fun existsByEmail(email: String): Boolean

    suspend fun create(customer: Customer): Customer
}
