package com.hotela.repository.impl

import com.hotela.model.database.CustomerAuth
import com.hotela.repository.CustomerAuthRepository
import io.r2dbc.spi.Row
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.awaitSingle
import org.springframework.r2dbc.core.awaitSingleOrNull
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.UUID

@Component
class CustomerAuthRepositoryImpl(
    private val databaseClient: DatabaseClient,
) : CustomerAuthRepository {
    override suspend fun findByEmail(email: String): CustomerAuth? =
        databaseClient
            .sql(FIND_BY_EMAIL)
            .bind("email", email)
            .map { row, _ ->
                mapper(row)
            }.awaitSingleOrNull()

    override suspend fun existsByEmail(email: String): Boolean =
        databaseClient
            .sql(EXISTS_BY_EMAIL)
            .bind("email", email)
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

    override suspend fun create(customerAuth: CustomerAuth): CustomerAuth =
        databaseClient
            .sql(SAVE)
            .bind("id", customerAuth.id)
            .bind("customerId", customerAuth.customerId)
            .bind("email", customerAuth.email)
            .bind("passwordHash", customerAuth.passwordHash)
            .map { row, _ -> mapper(row) }
            .awaitSingle()

    private fun mapper(row: Row): CustomerAuth =
        CustomerAuth(
            id = row.get("id", UUID::class.java)!!,
            customerId = row.get("customer_id", UUID::class.java)!!,
            email = row.get("email", String::class.java)!!,
            passwordHash = row.get("password_hash", String::class.java)!!,
            createdAt = row.get("created_at", LocalDateTime::class.java)!!,
            lastLogin = row.get("last_login", LocalDateTime::class.java)!!,
            active = row.get("active", Boolean::class.java)!!,
        )

    companion object {
        private const val FIND_BY_EMAIL = """
        SELECT * FROM customer_auth WHERE email = :email
        """

        private const val EXISTS_BY_EMAIL = """
        SELECT EXISTS(SELECT 1 FROM customer_auth WHERE email = :email) AS exists
        """

        private const val EXISTS_BY_ID = """
        SELECT EXISTS(SELECT 1 FROM customer_auth WHERE id = :id) AS exists
        """

        private const val SAVE = """
        INSERT INTO customer_auth (id, customer_id, email, password_hash)
        VALUES (:id, :customerId, :email, :passwordHash)
        RETURNING *
        """
    }
}
