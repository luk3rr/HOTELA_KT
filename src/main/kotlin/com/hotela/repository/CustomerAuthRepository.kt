package com.hotela.repository

import com.hotela.model.database.CustomerAuth
import java.util.UUID

interface CustomerAuthRepository {
    suspend fun findByEmail(email: String): CustomerAuth?

    suspend fun existsByEmail(email: String): Boolean

    suspend fun existsById(id: UUID): Boolean

    suspend fun create(customerAuth: CustomerAuth): CustomerAuth
}
