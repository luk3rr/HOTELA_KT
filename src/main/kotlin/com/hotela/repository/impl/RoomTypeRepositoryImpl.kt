package com.hotela.repository.impl

import com.hotela.model.db.RoomType
import com.hotela.repository.RoomTypeRepository
import io.r2dbc.spi.Row
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.awaitSingleOrNull
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class RoomTypeRepositoryImpl(
    private val databaseClient: DatabaseClient,
) : RoomTypeRepository {
    override suspend fun findById(id: UUID): RoomType? =
        databaseClient
            .sql(FIND_BY_ID)
            .bind("id", id)
            .map { row, _ ->
                mapper(row)
            }.awaitSingleOrNull()

    override suspend fun findAll(): List<RoomType> =
        databaseClient
            .sql(FIND_ALL)
            .map { row, _ ->
                mapper(row)
            }.all()
            .collectList()
            .awaitSingleOrNull() ?: emptyList()

    private fun mapper(row: Row): RoomType =
        RoomType(
            id = row.get("id", UUID::class.java)!!,
            name = row.get("name", String::class.java)!!,
            description = row.get("description", String::class.java)!!,
        )

    companion object {
        private const val FIND_BY_ID = """
            SELECT * FROM room_type WHERE id = :id
        """

        private const val FIND_ALL = """
            SELECT * FROM room_type
        """
    }
}
