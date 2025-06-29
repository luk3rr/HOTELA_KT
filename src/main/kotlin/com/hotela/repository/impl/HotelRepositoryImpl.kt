package com.hotela.repository.impl

import com.hotela.model.db.Hotel
import com.hotela.model.domain.ContactInfo
import com.hotela.model.domain.Email
import com.hotela.model.domain.PhoneNumber
import com.hotela.repository.HotelRepository
import io.r2dbc.spi.Row
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.awaitSingle
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
            .bind("addressId", hotel.addressId)
            .bind("name", hotel.name)
            .bind("email", hotel.contactInfo.email.value)
            .bind("phone", hotel.contactInfo.phone.value)
            .bind("website", hotel.website)
            .bind("description", hotel.description)
            .bind("starRating", hotel.starRating)
            .map { row, _ ->
                mapper(row)
            }.awaitSingle()

    override suspend fun update(hotel: Hotel): Hotel =
        databaseClient
            .sql(UPDATE)
            .bind("id", hotel.id)
            .bind("name", hotel.name)
            .bind("email", hotel.contactInfo.email.value)
            .bind("phone", hotel.contactInfo.phone.value)
            .bind("website", hotel.website)
            .bind("description", hotel.description)
            .bind("starRating", hotel.starRating)
            .map { row, _ ->
                mapper(row)
            }.awaitSingle()

    private fun mapper(row: Row): Hotel =
        Hotel(
            id = row.get("id", UUID::class.java)!!,
            partnerId = row.get("partner_id", UUID::class.java)!!,
            addressId = row.get("address_id", UUID::class.java)!!,
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
            website = row.get("website", String::class.java),
            description = row.get("description", String::class.java),
            starRating = row.get("star_rating", BigDecimal::class.java),
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
                id, partner_id, address_id, name, email, phone, website, description, star_rating
            ) VALUES (
                :id, :partnerId, :addressId, :name, :email, :phone, :website, :description, :starRating
            )
            RETURNING *
        """

        private const val UPDATE = """
            UPDATE hotel SET
                name = :name,
                email = :email,
                phone = :phone,
                website = :website,
                description = :description,
                star_rating = :starRating
            WHERE id = :id
            RETURNING *
        """
    }
}
