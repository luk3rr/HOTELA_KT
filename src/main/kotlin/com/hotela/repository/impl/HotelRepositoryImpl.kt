package com.hotela.repository.impl

import com.hotela.model.database.Hotel
import com.hotela.repository.HotelRepository
import io.r2dbc.spi.Row
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.awaitSingleOrNull
import org.springframework.r2dbc.core.bind
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.util.UUID

@Component
class HotelRepositoryImpl(
    private val databaseClient: DatabaseClient,
) : HotelRepository {
    override suspend fun findById(id: UUID): Hotel? =
        databaseClient
            .sql(FIND_BY_ID)
            .bind("id", id)
            .map { row, _ ->
                mapper(row)
            }.awaitSingleOrNull()

    override suspend fun findByPartnerId(partnerId: UUID): List<Hotel> =
        databaseClient
            .sql(FIND_BY_PARTNER_ID)
            .bind("partnerId", partnerId)
            .map { row, _ ->
                mapper(row)
            }.all()
            .collectList()
            .awaitSingleOrNull() ?: emptyList()

    override suspend fun create(hotel: Hotel): Hotel =
        databaseClient
            .sql(SAVE)
            .bind("id", hotel.id)
            .bind("partnerId", hotel.partnerId)
            .bind("name", hotel.name)
            .bind("address", hotel.address)
            .bind("city", hotel.city)
            .bind("state", hotel.state)
            .bind("zipCode", hotel.zipCode)
            .bind("phone", hotel.phone)
            .bind("rating", hotel.rating)
            .bind("description", hotel.description)
            .bind("website", hotel.website)
            .bind("latitude", hotel.latitude)
            .bind("longitude", hotel.longitude)
            .map { row, _ ->
                mapper(row)
            }.awaitSingleOrNull()!!

    override suspend fun update(hotel: Hotel): Hotel =
        databaseClient
            .sql(UPDATE)
            .bind("id", hotel.id)
            .bind("name", hotel.name)
            .bind("address", hotel.address)
            .bind("city", hotel.city)
            .bind("state", hotel.state)
            .bind("zipCode", hotel.zipCode)
            .bind("phone", hotel.phone)
            .bind("rating", hotel.rating)
            .bind("description", hotel.description)
            .bind("website", hotel.website)
            .bind("latitude", hotel.latitude)
            .bind("longitude", hotel.longitude)
            .map { row, _ ->
                mapper(row)
            }.awaitSingleOrNull()!!

    private fun mapper(row: Row): Hotel =
        Hotel(
            id = row.get("id", UUID::class.java)!!,
            partnerId = row.get("partner_id", UUID::class.java)!!,
            name = row.get("name", String::class.java)!!,
            address = row.get("address", String::class.java)!!,
            city = row.get("city", String::class.java)!!,
            state = row.get("state", String::class.java)!!,
            zipCode = row.get("zip_code", String::class.java)!!,
            phone = row.get("phone", String::class.java)!!,
            rating = row.get("rating", BigDecimal::class.java)!!,
            description = row.get("description", String::class.java),
            website = row.get("website", String::class.java),
            latitude = row.get("latitude", BigDecimal::class.java)!!,
            longitude = row.get("longitude", BigDecimal::class.java)!!,
        )

    companion object {
        private const val FIND_BY_ID = """
            SELECT * FROM hotel WHERE id = :id
        """

        private const val FIND_BY_PARTNER_ID = """
            SELECT * FROM hotel WHERE partner_id = :partnerId
        """

        private const val SAVE = """
            INSERT INTO hotel (
                id, partner_id, name, address, city, state, zip_code, phone, rating, description, website, latitude, longitude
            ) VALUES (
                :id, :partnerId, :name, :address, :city, :state, :zipCode, :phone, :rating, :description, :website, :latitude, :longitude
            )
            RETURNING *
        """

        private const val UPDATE = """
            UPDATE hotel SET
                name = :name,
                address = :address,
                city = :city,
                state = :state,
                zip_code = :zipCode,
                phone = :phone,
                rating = :rating,
                description = :description,
                website = :website,
                latitude = :latitude,
                longitude = :longitude
            WHERE id = :id
            RETURNING *
        """
    }
}
