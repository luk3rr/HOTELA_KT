package com.hotela.repository.impl

import com.hotela.model.db.Partner
import com.hotela.model.domain.ContactInfo
import com.hotela.model.domain.DocumentId
import com.hotela.model.domain.Email
import com.hotela.model.domain.PhoneNumber
import com.hotela.model.enum.DocumentIdType
import com.hotela.model.enum.PartnerStatus
import com.hotela.repository.PartnerRepository
import io.r2dbc.spi.Row
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.awaitSingle
import org.springframework.r2dbc.core.awaitSingleOrNull
import org.springframework.r2dbc.core.bind
import org.springframework.stereotype.Component
import java.time.Instant
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

    override suspend fun findByAuthId(authId: UUID): Partner? =
        databaseClient
            .sql(FIND_BY_AUTH_ID)
            .bind("authId", authId)
            .map { row, _ ->
                mapper(row)
            }.awaitSingleOrNull()

    override suspend fun findByEmail(email: Email): Partner? =
        databaseClient
            .sql(FIND_BY_EMAIL)
            .bind("email", email.value)
            .map { row, _ ->
                mapper(row)
            }.awaitSingleOrNull()

    override suspend fun existsByEmail(email: Email): Boolean =
        databaseClient
            .sql(EXISTS_BY_EMAIL)
            .bind("email", email.value)
            .map { row, _ ->
                row.get("exists", Boolean::class.java)!!
            }.awaitSingle()

    override suspend fun create(partner: Partner): Partner =
        databaseClient
            .sql(SAVE)
            .bind("id", partner.id)
            .bind("authCredentialId", partner.authCredentialId)
            .bind("companyName", partner.companyName)
            .bind("legalName", partner.legalName)
            .bind("email", partner.contactInfo.email.value)
            .bind("phone", partner.contactInfo.phone.value)
            .bind("documentIdType", partner.documentId.type)
            .bind("documentIdValue", partner.documentId.value)
            .bind("contractSignedAt", partner.contractSignedAt)
            .bind("status", partner.status)
            .bind("notes", partner.notes)
            .map { row, _ -> mapper(row) }
            .awaitSingle()

    override suspend fun update(partner: Partner): Partner =
        databaseClient
            .sql(UPDATE)
            .bind("id", partner.id)
            .bind("companyName", partner.companyName)
            .bind("legalName", partner.legalName)
            .bind("email", partner.contactInfo.email.value)
            .bind("phone", partner.contactInfo.phone.value)
            .bind("documentIdType", partner.documentId.type)
            .bind("documentIdValue", partner.documentId.value)
            .bind("contractSignedAt", partner.contractSignedAt)
            .bind("status", partner.status)
            .bind("notes", partner.notes)
            .map { row, _ -> mapper(row) }
            .awaitSingle()

    private fun mapper(row: Row): Partner =
        Partner(
            id = row.get("id", UUID::class.java)!!,
            authCredentialId = row.get("auth_credential_id", UUID::class.java)!!,
            companyName = row.get("company_name", String::class.java)!!,
            legalName = row.get("legal_name", String::class.java)!!,
            contactInfo =
                ContactInfo(
                    email =
                        Email(
                            row.get("email", String::class.java)!!,
                        ),
                    phone =
                        PhoneNumber(
                            row.get("phone", String::class.java)!!,
                        ),
                ),
            documentId =
                DocumentId(
                    type =
                        DocumentIdType.fromString(
                            row.get("document_id_type", String::class.java)!!,
                        ),
                    value = row.get("document_id_value", String::class.java)!!,
                ),
            contractSignedAt = row.get("contract_signed_at", Instant::class.java)!!,
            status = row.get("status", PartnerStatus::class.java)!!,
            notes = row.get("notes", String::class.java),
        )

    companion object {
        private const val FIND_BY_ID = """
            SELECT * FROM partner WHERE id = :id
        """

        private const val FIND_BY_AUTH_ID = """
            SELECT * FROM partner WHERE auth_credential_id = :authId
        """

        private const val FIND_BY_EMAIL = """
            SELECT * FROM partner WHERE email = :email
        """

        private const val EXISTS_BY_EMAIL = """
            SELECT EXISTS(SELECT 1 FROM partner WHERE email = :email) AS exists
        """

        private const val SAVE = """
            INSERT INTO partner (id, auth_credential_id, company_name, legal_name, email, phone,
                document_id_type, document_id_value, contract_signed_at, status, notes)
            VALUES (:id, :authCredentialId, :companyName, :legalName, :email, :phone,
                :documentIdType, :documentIdValue, :contractSignedAt, :status, :notes)
            RETURNING *
        """

        private const val UPDATE = """
            UPDATE partner
            SET company_name = :companyName,
                legal_name = :legalName,
                email = :email,
                phone = :phone,
                document_id_type = :documentIdType,
                document_id_value = :documentIdValue,
                contract_signed_at = :contractSignedAt,
                status = :status,
                notes = :notes
            WHERE id = :id
            RETURNING *
        """
    }
}
