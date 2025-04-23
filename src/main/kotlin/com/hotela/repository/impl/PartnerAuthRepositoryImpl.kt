package com.hotela.repository.impl

import com.hotela.model.database.PartnerAuth
import com.hotela.repository.PartnerAuthRepository
import io.r2dbc.spi.Row
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.awaitSingle
import org.springframework.r2dbc.core.awaitSingleOrNull
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.UUID

@Component
class PartnerAuthRepositoryImpl(
    private val databaseClient: DatabaseClient,
) : PartnerAuthRepository {
    override suspend fun findById(id: UUID): PartnerAuth? =
        databaseClient
            .sql(FIND_BY_ID)
            .bind("id", id)
            .map { row, _ ->
                mapper(row)
            }.awaitSingleOrNull()

    override suspend fun findByEmail(email: String): PartnerAuth? =
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

    override suspend fun create(partnerAuth: PartnerAuth): PartnerAuth =
        databaseClient
            .sql(SAVE)
            .bind("id", partnerAuth.id)
            .bind("partnerId", partnerAuth.partnerId)
            .bind("email", partnerAuth.email)
            .bind("passwordHash", partnerAuth.passwordHash)
            .map { row, _ -> mapper(row) }
            .awaitSingle()

    private fun mapper(row: Row): PartnerAuth =
        PartnerAuth(
            id = row.get("id", UUID::class.java)!!,
            partnerId = row.get("partner_id", UUID::class.java)!!,
            email = row.get("email", String::class.java)!!,
            passwordHash = row.get("password_hash", String::class.java)!!,
            createdAt = row.get("created_at", LocalDateTime::class.java)!!,
            lastLogin = row.get("last_login", LocalDateTime::class.java)!!,
            active = row.get("active", Boolean::class.java)!!,
        )

    companion object {
        private const val FIND_BY_ID = """
            SELECT * FROM partner_auth WHERE id = :id
        """

        private const val FIND_BY_EMAIL = """
            SELECT * FROM partner_auth WHERE email = :email
        """

        private const val EXISTS_BY_EMAIL = """
            SELECT EXISTS(SELECT 1 FROM partner_auth WHERE email = :email) AS exists
        """

        private const val SAVE = """
            INSERT INTO partner_auth (id, partner_id, email, password_hash)
            VALUES (:id, :partnerId, :email, :passwordHash)
            RETURNING *
        """
    }
}
