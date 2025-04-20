package com.hotela.repository.impl

import com.hotela.error.HotelaException
import com.hotela.model.database.Example
import com.hotela.repository.ExampleRepository
import io.r2dbc.spi.Row
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.awaitSingle
import org.springframework.r2dbc.core.awaitSingleOrNull
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class ExampleRepositoryImpl(
    private val databaseClient: DatabaseClient,
) : ExampleRepository {
    override suspend fun findById(id: UUID): Example? =
        databaseClient
            .sql(FIND_BY_ID)
            .bind("id", id)
            .map { row, _ ->
                mapper(row)
            }.awaitSingleOrNull()

    override suspend fun getById(id: UUID): Example = findById(id) ?: throw HotelaException.ExampleNotFoundException(id)

    override suspend fun create(example: Example): UUID {
        findById(example.id)?.let {
            throw HotelaException.ExampleAlreadyExistsException(example.id)
        }

        return databaseClient
            .sql(CREATE)
            .bind("id", example.id)
            .bind("name", example.name)
            .map { row, _ ->
                mapper(row)
            }.awaitSingle()
            .id
    }

    private fun mapper(row: Row): Example =
        Example(
            id = row.get("id", UUID::class.java)!!,
            name = row.get("name", String::class.java)!!,
        )
}

private const val FIND_BY_ID = """
SELECT * FROM example WHERE id = :id
"""

private const val CREATE = """
INSERT INTO example (id, name) VALUES (:id, :name)
RETURNING id, name
"""
