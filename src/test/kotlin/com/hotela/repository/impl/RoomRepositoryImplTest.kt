package com.hotela.repository.impl

import com.hotela.model.database.Room
import com.hotela.model.enum.RoomStatus
import com.hotela.stubs.database.RoomStubs
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

class RoomRepositoryImplTest :
    ShouldSpec({
        val databaseClient = mockk<DatabaseClient>()
        val roomRepositoryImpl = RoomRepositoryImpl(databaseClient)

        val room = RoomStubs.create()

        val genericDatabaseSpec = mockk<DatabaseClient.GenericExecuteSpec>()
        val mockRow = mockk<Row>()
        val rowsFetchSpec = mockk<RowsFetchSpec<Room>>()

        fun setupMockForDatabaseClient() {
            every { databaseClient.sql(any<String>()) } returns genericDatabaseSpec
            every { genericDatabaseSpec.bind(any<String>(), any()) } returns genericDatabaseSpec
        }

        fun setupMockRowForRoom() {
            every {
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, Room>>())
            } answers {
                val function = args[0] as BiFunction<Row, RowMetadata, Room>
                every { rowsFetchSpec.first() } returns Mono.just(function.apply(mockRow, mockk()))
                every { rowsFetchSpec.all().collectList() } returns Mono.just(listOf(function.apply(mockRow, mockk())))
                rowsFetchSpec
            }

            every { mockRow.get("id", UUID::class.java) } returns room.id
            every { mockRow.get("hotel_id", UUID::class.java) } returns room.hotelId
            every { mockRow.get("number", String::class.java) } returns room.number
            every { mockRow.get("floor", Int::class.java) } returns room.floor
            every { mockRow.get("type", String::class.java) } returns room.type
            every { mockRow.get("price", BigDecimal::class.java) } returns room.price
            every { mockRow.get("capacity", Int::class.java) } returns room.capacity
            every { mockRow.get("status", RoomStatus::class.java) } returns room.status
            every { mockRow.get("description", String::class.java) } returns room.description
        }

        beforeSpec {
            setupMockForDatabaseClient()
            setupMockRowForRoom()
        }

        afterSpec {
            clearAllMocks()
        }

        should("successfully create a room") {
            roomRepositoryImpl.create(room) shouldBe room

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("id", room.id)
                genericDatabaseSpec.bind("hotelId", room.hotelId)
                genericDatabaseSpec.bind("number", room.number)
                genericDatabaseSpec.bind("floor", room.floor)
                genericDatabaseSpec.bind("type", room.type)
                genericDatabaseSpec.bind("price", room.price)
                genericDatabaseSpec.bind("capacity", room.capacity)
                genericDatabaseSpec.bind("status", room.status)
                genericDatabaseSpec.bind("description", room.description)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, Room>>())
                rowsFetchSpec.first()
            }
        }

        should("successfully update a room") {
            roomRepositoryImpl.update(room) shouldBe room

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("id", room.id)
                genericDatabaseSpec.bind("number", room.number)
                genericDatabaseSpec.bind("floor", room.floor)
                genericDatabaseSpec.bind("type", room.type)
                genericDatabaseSpec.bind("price", room.price)
                genericDatabaseSpec.bind("capacity", room.capacity)
                genericDatabaseSpec.bind("status", room.status)
                genericDatabaseSpec.bind("description", room.description)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, Room>>())
                rowsFetchSpec.first()
            }
        }

        should("successfully delete a room") {
            every { mockRow.get("deleted", Boolean::class.java) } returns true
            roomRepositoryImpl.delete(room.id) shouldBe true

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("id", room.id)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, Boolean>>())
                rowsFetchSpec.first()
            }
        }

        should("successfully find room by id") {
            roomRepositoryImpl.findById(room.id) shouldBe room

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("id", room.id)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, Room>>())
                rowsFetchSpec.first()
            }
        }

        should("successfully find a room by hotelId") {
            roomRepositoryImpl.findByHotelId(room.hotelId) shouldBe listOf(room)

            verify(exactly = 1) {
                databaseClient.sql(any<String>())
                genericDatabaseSpec.bind("hotelId", room.hotelId)
                genericDatabaseSpec.map(any<BiFunction<Row, RowMetadata, Room>>())
                rowsFetchSpec.all().collectList()
            }
        }
    })
