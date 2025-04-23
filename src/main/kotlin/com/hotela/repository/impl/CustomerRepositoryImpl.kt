package com.hotela.repository.impl

import com.hotela.model.database.Customer
import com.hotela.repository.CustomerRepository
import io.r2dbc.spi.Row
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.awaitSingle
import org.springframework.r2dbc.core.awaitSingleOrNull
import org.springframework.stereotype.Component
import java.time.LocalDate
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

    override suspend fun findByEmail(email: String): Customer? =
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

    override suspend fun create(customer: Customer): Customer =
        databaseClient
            .sql(SAVE)
            .bind("id", customer.id)
            .bind("name", customer.name)
            .bind("email", customer.email)
            .bind("phone", customer.phone)
            .bind("idDocument", customer.idDocument)
            .bind("birthDate", customer.birthDate)
            .bind("address", customer.address)
            .map { row, _ ->
                mapper(row)
            }.awaitSingle()

    private fun mapper(row: Row): Customer =
        Customer(
            id = row.get("id", UUID::class.java)!!,
            name = row.get("name", String::class.java)!!,
            email = row.get("email", String::class.java)!!,
            phone = row.get("phone", String::class.java)!!,
            idDocument = row.get("id_document", String::class.java)!!,
            birthDate = row.get("birth_date", LocalDate::class.java)!!,
            address = row.get("address", String::class.java)!!,
        )

    companion object {
        private const val FIND_BY_ID = """
        SELECT * FROM customer WHERE id = :id
        """

        private const val FIND_BY_EMAIL = """
        SELECT * FROM customer WHERE email = :email
        """

        private const val EXISTS_BY_EMAIL = """
        SELECT EXISTS(SELECT 1 FROM customer WHERE email = :email)
        """

        private const val SAVE = """
        INSERT INTO customer (id, name, email, phone, id_document, birth_date, address)
        VALUES (:id, :name, :email, :phone, :idDocument, :birthDate, :address)
        RETURNING *
        """
    }
}
