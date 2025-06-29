package com.hotela.repository.impl

import com.hotela.model.db.Address
import com.hotela.repository.AddressRepository
import io.r2dbc.spi.Row
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.awaitSingleOrNull
import org.springframework.r2dbc.core.bind
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.util.UUID

@Component
class AddressRepositoryImpl(
    private val databaseClient: DatabaseClient,
) : AddressRepository {
    override suspend fun findById(id: UUID): Address? =
        databaseClient
            .sql(FIND_BY_ID)
            .bind("id", id)
            .map { row, _ ->
                mapper(row)
            }.awaitSingleOrNull()

    override suspend fun create(address: Address): Address =
        databaseClient
            .sql(SAVE)
            .bind("id", address.id)
            .bind("streetAddress", address.streetAddress)
            .bind("number", address.number)
            .bind("complement", address.complement)
            .bind("neighborhood", address.neighborhood)
            .bind("city", address.city)
            .bind("stateProvince", address.stateProvince)
            .bind("postalCode", address.postalCode)
            .bind("countryCode", address.countryCode)
            .bind("latitude", address.latitude)
            .bind("longitude", address.longitude)
            .map { row, _ ->
                mapper(row)
            }.awaitSingleOrNull()!!

    private fun mapper(row: Row): Address =
        Address(
            id = row.get("id", UUID::class.java)!!,
            streetAddress = row.get("street_address", String::class.java)!!,
            number = row.get("number", String::class.java)!!,
            complement = row.get("complement", String::class.java),
            neighborhood = row.get("neighborhood", String::class.java)!!,
            city = row.get("city", String::class.java)!!,
            stateProvince = row.get("state_province", String::class.java)!!,
            postalCode = row.get("postal_code", String::class.java)!!,
            countryCode = row.get("country_code", String::class.java)!!,
            latitude = row.get("latitude", BigDecimal::class.java),
            longitude = row.get("longitude", BigDecimal::class.java),
        )

    companion object {
        private const val FIND_BY_ID = """
            SELECT * FROM address WHERE id = :id
        """

        private const val SAVE = """
            INSERT INTO address (
                id, street_address, number, complement, neighborhood, city, state_province, postal_code, country_code, latitude, longitude
            )
            VALUES (
                :id, :streetAddress, :number, :complement, :neighborhood, :city, :stateProvince, :postalCode, :countryCode, :latitude, :longitude
            )
            RETURNING *
        """
    }
}
