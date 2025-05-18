package com.hotela.repository

import com.hotela.model.db.AuthCredential
import com.hotela.model.domain.Email
import java.util.UUID

interface AuthCredentialRepository {
    suspend fun findByLoginEmail(email: Email): AuthCredential?

    suspend fun findById(id: UUID): AuthCredential?

    suspend fun existsByLoginEmail(email: Email): Boolean

    suspend fun existsById(id: UUID): Boolean

    suspend fun create(authCredential: AuthCredential): AuthCredential
}
