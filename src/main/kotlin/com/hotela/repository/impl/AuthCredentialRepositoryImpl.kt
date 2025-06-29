package com.hotela.repository.impl

import com.hotela.model.db.AuthCredential
import com.hotela.model.domain.Email
import com.hotela.model.enum.Role
import com.hotela.repository.AuthCredentialRepository
import io.r2dbc.spi.Row
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.awaitSingle
import org.springframework.r2dbc.core.awaitSingleOrNull
import org.springframework.r2dbc.core.bind
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.UUID

@Component
class AuthCredentialRepositoryImpl(
    private val databaseClient: DatabaseClient,
) : AuthCredentialRepository {
    override suspend fun findByLoginEmail(email: Email): AuthCredential? =
        databaseClient
            .sql(FIND_BY_LOGIN_EMAIL)
            .bind("loginEmail", email.value)
            .map { row, _ ->
                mapper(row)
            }.awaitSingleOrNull()

    override suspend fun findById(id: UUID): AuthCredential? =
        databaseClient
            .sql(FIND_BY_ID)
            .bind("id", id)
            .map { row, _ ->
                mapper(row)
            }.awaitSingleOrNull()

    override suspend fun existsByLoginEmail(email: Email): Boolean =
        databaseClient
            .sql(EXISTS_BY_LOGIN_EMAIL)
            .bind("loginEmail", email.value)
            .map { row, _ ->
                row.get("exists", Boolean::class.java)!!
            }.awaitSingle()

    override suspend fun existsById(id: UUID): Boolean =
        databaseClient
            .sql(EXISTS_BY_ID)
            .bind("id", id)
            .map { row, _ ->
                row.get("exists", Boolean::class.java)!!
            }.awaitSingle()

    override suspend fun create(authCredential: AuthCredential): AuthCredential =
        databaseClient
            .sql(SAVE)
            .bind("id", authCredential.id)
            .bind("loginEmail", authCredential.loginEmail.value)
            .bind("password", authCredential.password)
            .bind("role", authCredential.role)
            .bind("isActive", authCredential.isActive)
            .bind("lastLoginAt", authCredential.lastLoginAt)
            .bind("createdAt", authCredential.createdAt)
            .bind("updatedAt", authCredential.updatedAt)
            .map { row, _ -> mapper(row) }
            .awaitSingle()

    private fun mapper(row: Row): AuthCredential =
        AuthCredential(
            id = row.get("id", UUID::class.java)!!,
            loginEmail =
                Email(
                    row.get("login_email", String::class.java)!!,
                ),
            password = row.get("password", String::class.java)!!,
            role = row.get("role", Role::class.java)!!,
            isActive = row.get("is_active", Boolean::class.java)!!,
            lastLoginAt = row.get("last_login_at", Instant::class.java),
            createdAt = row.get("created_at", Instant::class.java)!!,
            updatedAt = row.get("updated_at", Instant::class.java),
        )

    companion object {
        private const val FIND_BY_LOGIN_EMAIL = """
        SELECT * FROM auth_credential WHERE login_email = :loginEmail
        """

        private const val FIND_BY_ID = """
        SELECT * FROM auth_credential WHERE id = :id
        """

        private const val EXISTS_BY_LOGIN_EMAIL = """
        SELECT EXISTS(SELECT 1 FROM auth_credential WHERE login_email = :loginEmail) AS exists
        """

        private const val EXISTS_BY_ID = """
        SELECT EXISTS(SELECT 1 FROM auth_credential WHERE id = :id) AS exists
        """

        private const val SAVE = """
        INSERT INTO auth_credential (id, login_email, password, role, is_active, last_login_at, created_at, updated_at)
        VALUES (:id, :loginEmail, :password, :role, :isActive, :lastLoginAt, :createdAt, :updatedAt)
        RETURNING *
        """
    }
}
