package com.hotela.repository.impl

import com.hotela.model.db.Customer
import com.hotela.model.domain.ContactInfo
import com.hotela.model.domain.DocumentId
import com.hotela.model.domain.Email
import com.hotela.model.domain.PhoneNumber
import com.hotela.model.enum.DocumentIdType
import com.hotela.repository.CustomerRepository
import io.r2dbc.spi.Row
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.awaitSingle
import org.springframework.r2dbc.core.awaitSingleOrNull
import org.springframework.r2dbc.core.bind
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.UUID

@Component
class CustomerRepositoryImpl(
    private val databaseClient: DatabaseClient,
) : CustomerRepository {
    override suspend fun findById(id: UUID): Customer? =
        databaseClient
            .sql(FIND_BY_ID)
            .bind("id", id)
            .map { row, _ ->
                mapper(row)
            }.awaitSingleOrNull()

    override suspend fun findByAuthId(authId: UUID): Customer? =
        databaseClient
            .sql(FIND_BY_AUTH_ID)
            .bind("authId", authId)
            .map { row, _ ->
                mapper(row)
            }.awaitSingleOrNull()

    override suspend fun findByEmail(email: Email): Customer? =
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

    override suspend fun create(customer: Customer): Customer =
        databaseClient
            .sql(SAVE)
            .bind("id", customer.id)
            .bind("authCredentialId", customer.authCredentialId)
            .bind("mainAddressId", customer.addressId)
            .bind("name", customer.name)
            .bind("email", customer.contactInfo.email.value)
            .bind("phone", customer.contactInfo.phone.value)
            .bind("documentIdType", customer.documentId.type)
            .bind("documentIdValue", customer.documentId.value)
            .bind("birthDate", customer.birthDate)
            .map { row, _ ->
                mapper(row)
            }.awaitSingle()

    override suspend fun update(customer: Customer): Customer =
        databaseClient
            .sql(UPDATE)
            .bind("id", customer.id)
            .bind("name", customer.name)
            .bind("email", customer.contactInfo.email.value)
            .bind("phone", customer.contactInfo.phone.value)
            .bind("documentIdType", customer.documentId.type)
            .bind("documentIdValue", customer.documentId.value)
            .bind("birthDate", customer.birthDate)
            .map { row, _ ->
                mapper(row)
            }.awaitSingle()

    private fun mapper(row: Row): Customer =
        Customer(
            id = row.get("id", UUID::class.java)!!,
            authCredentialId = row.get("auth_credential_id", UUID::class.java)!!,
            addressId = row.get("main_address_id", UUID::class.java)!!,
            name = row.get("name", String::class.java)!!,
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
            birthDate = row.get("birth_date", Instant::class.java),
        )

    companion object {
        private const val FIND_BY_ID = """
        SELECT * FROM customer WHERE id = :id
        """

        private const val FIND_BY_AUTH_ID = """
        SELECT * FROM customer WHERE auth_credential_id = :authId
        """

        private const val FIND_BY_EMAIL = """
        SELECT * FROM customer WHERE email = :email
        """

        private const val EXISTS_BY_EMAIL = """
        SELECT EXISTS(SELECT 1 FROM customer WHERE email = :email)
        """

        private const val SAVE = """
        INSERT INTO customer (
        id, auth_credential_id, main_address_id, name, email, phone, document_id_type, document_id_value, birth_date
        )
        VALUES (:id, :authCredentialId, :mainAddressId, :name, :email, :phone, :documentIdType, :documentIdValue, :birthDate)
        RETURNING *
        """

        private const val UPDATE = """
        UPDATE customer
        SET name = :name, email = :email, phone = :phone, document_id_type = :documentIdType, document_id_value = :documentIdValue, birth_date = :birthDate
        WHERE id = :id
        RETURNING *
        """
    }
}
