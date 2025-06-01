package com.hotela.repository.impl

import com.hotela.model.db.Hotel
import com.hotela.model.domain.Email
import com.hotela.model.domain.PhoneNumber
import com.hotela.stubs.db.HotelStubs
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.r2dbc.spi.Row
import io.r2dbc.spi.RowMetadata
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.RowsFetchSpec
import org.springframework.r2dbc.core.bind
import reactor.core.publisher.Mono
import java.math.BigDecimal
import java.util.UUID
import java.util.function.BiFunction

class HotelRepositoryImplTest :
    ShouldSpec({
        val databaseClient = mockk<DatabaseClient>()
        val hotelRepositoryImpl = HotelRepositoryImpl(databaseClient)

        val hotel = HotelStubs.create()

        val genericDatabaseSpec = mockk<DatabaseClient.GenericExecuteSpec>()
        val mockRow = mockk<Row>()
        val rowsFetchSpec = mockk<RowsFetchSpec<Hotel>>()

        fun setupMockForDatabaseClient() {
            every { databaseClient.sql(any<String>()) } returns genericDatabaseSpec
            every { genericDatabaseSpec.bind(any<String>(), any()) } returns genericDatabaseSpec
        }

        fun setupMockRowForHotel() {
            every {
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, Hotel>>())
            } answers {
                val function = args[0] as BiFunction<Row, RowMetadata, Hotel>
                every { rowsFetchSpec.first() } returns Mono.just(function.apply(mockRow, mockk()))
                every { rowsFetchSpec.all().collectList() } returns Mono.just(listOf(function.apply(mockRow, mockk())))
                rowsFetchSpec
            }

            every { mockRow.get("id", UUID::class.java) } returns hotel.id
            every { mockRow.get("partner_id", UUID::class.java) } returns hotel.partnerId
            every { mockRow.get("address_id", UUID::class.java) } returns hotel.addressId
            every { mockRow.get("name", String::class.java) } returns hotel.name
            every { mockRow.get("phone", PhoneNumber::class.java) } returns hotel.contactInfo.phone
            every { mockRow.get("email", Email::class.java) } returns hotel.contactInfo.email
            every { mockRow.get("website", String::class.java) } returns hotel.website
            every { mockRow.get("description", String::class.java) } returns hotel.description
            every { mockRow.get("star_rating", BigDecimal::class.java) } returns hotel.starRating
        }

        beforeTest {
            setupMockForDatabaseClient()
            setupMockRowForHotel()
        }

        afterTest { clearAllMocks() }

        should("successfully create a hotel") {
            hotelRepositoryImpl.create(hotel) shouldBe hotel

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("id", hotel.id)
                genericDatabaseSpec.bind("partnerId", hotel.partnerId)
                genericDatabaseSpec.bind("addressId", hotel.addressId)
                genericDatabaseSpec.bind("name", hotel.name)
                genericDatabaseSpec.bind("email", hotel.contactInfo.email)
                genericDatabaseSpec.bind("phone", hotel.contactInfo.phone)
                genericDatabaseSpec.bind("website", hotel.website)
                genericDatabaseSpec.bind("description", hotel.description)
                genericDatabaseSpec.bind("starRating", hotel.starRating)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, Hotel>>())
                rowsFetchSpec.first()
            }
        }

        should("successfully update a hotel") {
            hotelRepositoryImpl.update(hotel) shouldBe hotel

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("id", hotel.id)
                genericDatabaseSpec.bind("name", hotel.name)
                genericDatabaseSpec.bind("email", hotel.contactInfo.email)
                genericDatabaseSpec.bind("phone", hotel.contactInfo.phone)
                genericDatabaseSpec.bind("website", hotel.website)
                genericDatabaseSpec.bind("description", hotel.description)
                genericDatabaseSpec.bind("starRating", hotel.starRating)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, Hotel>>())
                rowsFetchSpec.first()
            }
        }

        should("successfully find a hotel by id") {
            hotelRepositoryImpl.findById(hotel.id) shouldBe hotel

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("id", hotel.id)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, Hotel>>())
                rowsFetchSpec.first()
            }
        }

        should("successfully find hotels by partner id") {
            hotelRepositoryImpl.findByPartnerId(hotel.partnerId) shouldBe listOf(hotel)

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("partnerId", hotel.partnerId)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, Hotel>>())
                rowsFetchSpec.all()
            }
        }
    })
