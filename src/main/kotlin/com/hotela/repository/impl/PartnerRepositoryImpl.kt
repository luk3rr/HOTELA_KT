package com.hotela.repository.impl

import com.hotela.error.HotelaException
import com.hotela.model.database.Partner
import com.hotela.model.enum.PartnerStatus
import com.hotela.repository.PartnerRepository
import io.r2dbc.spi.Row
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.awaitSingle
import org.springframework.r2dbc.core.awaitSingleOrNull
import org.springframework.r2dbc.core.bind
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.UUID

@Component
class PartnerRepositoryImpl(
    private val databaseClient: DatabaseClient,
) : PartnerRepository {
    override suspend fun findById(id: UUID): Partner? =
        databaseClient
            .sql(FIND_BY_ID)
            .bind("id", id)
            .map { row, _ ->
                mapper(row)
            }.awaitSingleOrNull()

    override suspend fun findByEmail(email: String): Partner? =
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

    override suspend fun save(partner: Partner): Partner {
        if (existsByEmail(partner.email)) {
            throw HotelaException.EmailAlreadyRegisteredException()
        }

        return databaseClient
            .sql(SAVE)
            .bind("id", partner.id)
            .bind("name", partner.name)
            .bind("cnpj", partner.cnpj)
            .bind("email", partner.email)
            .bind("phone", partner.phone)
            .bind("address", partner.address)
            .bind("contact_name", partner.contactName)
            .bind("contact_email", partner.contactEmail)
            .bind("contact_phone", partner.contactPhone)
            .bind("contract_signed", partner.contractSigned)
            .bind("status", partner.status)
            .bind("created_at", partner.createdAt)
            .bind("notes", partner.notes)
            .map { row, _ -> mapper(row) }
            .awaitSingle()
    }

    private fun mapper(row: Row): Partner =
        Partner(
            id = row.get("id", UUID::class.java)!!,
            name = row.get("name", String::class.java)!!,
            cnpj = row.get("cnpj", String::class.java)!!,
            email = row.get("email", String::class.java)!!,
            phone = row.get("phone", String::class.java)!!,
            address = row.get("address", String::class.java)!!,
            contactName = row.get("contact_name", String::class.java)!!,
            contactEmail = row.get("contact_email", String::class.java)!!,
            contactPhone = row.get("contact_phone", String::class.java)!!,
            contractSigned = row.get("contract_signed", Boolean::class.java)!!,
            status = row.get("status", PartnerStatus::class.java)!!,
            createdAt = row.get("created_at", LocalDateTime::class.java)!!,
            notes = row.get("notes", String::class.java)!!,
        )

    companion object {
        private const val FIND_BY_ID = """
            SELECT * FROM partner WHERE id = :id
        """

        private const val FIND_BY_EMAIL = """
            SELECT * FROM partner WHERE email = :email
        """

        private const val EXISTS_BY_EMAIL = """
            SELECT EXISTS(SELECT 1 FROM partner WHERE email = :email) AS exists
        """

        private const val SAVE = """
            INSERT INTO partner (id, name, cnpj, email, phone, address, contact_name, contact_email, contact_phone, contract_signed, status, created_at, notes)
            VALUES (:id, :name, :cnpj, :email, :phone, :address, :contact_name, :contact_email, :contact_phone, :contract_signed, :status, :created_at, :notes)
            RETURNING *
        """
    }
}
