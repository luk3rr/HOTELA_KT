package com.hotela.repository.impl

import com.hotela.clearAllTables
import com.hotela.model.db.Room
import com.hotela.model.enum.RoomStatus
import com.hotela.repository.AddressRepository
import com.hotela.repository.AuthCredentialRepository
import com.hotela.repository.HotelRepository
import com.hotela.repository.PartnerRepository
import com.hotela.repository.RoomRepository
import com.hotela.stubs.db.AddressStubs
import com.hotela.stubs.db.AuthCredentialStubs
import com.hotela.stubs.db.HotelStubs
import com.hotela.stubs.db.PartnerStubs
import com.hotela.stubs.db.RoomStubs
import com.hotela.stubs.db.RoomTypeStubs
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.r2dbc.core.DatabaseClient
import java.math.BigDecimal
import java.util.UUID

class RoomRepositoryImplTestInterface :
    ShouldSpec(),
    DatabaseIntegrationTestInterface {
    @Autowired
    private lateinit var roomRepository: RoomRepository

    @Autowired
    private lateinit var hotelRepository: HotelRepository

    @Autowired
    private lateinit var partnerRepository: PartnerRepository

    @Autowired
    private lateinit var addressRepository: AddressRepository

    @Autowired
    private lateinit var authCredentialRepository: AuthCredentialRepository

    @Autowired
    private lateinit var databaseClient: DatabaseClient

    override fun extensions() = listOf(SpringExtension)

    init {
        lateinit var room: Room

        beforeSpec {
            databaseClient.clearAllTables()

            val address = addressRepository.create(AddressStubs.create())
            val credential = authCredentialRepository.create(AuthCredentialStubs.create())
            val partner = partnerRepository.create(PartnerStubs.create(authCredentialId = credential.id))
            val hotel = hotelRepository.create(HotelStubs.create(addressId = address.id, partnerId = partner.id))
            val roomType = RoomTypeStubs.createStandardSolteiro()

            room = RoomStubs.create(hotelId = hotel.id, roomTypeId = roomType.id)
        }

        beforeTest {
            databaseClient
                .sql("DELETE FROM room")
                .fetch()
                .rowsUpdated()
                .awaitSingle()
        }

        should("create room successfully") {
            val saved = roomRepository.create(room)

            saved.id shouldBe room.id
            saved.hotelId shouldBe room.hotelId
            saved.roomTypeId shouldBe room.roomTypeId
            saved.roomCode shouldBe room.roomCode
            saved.floor shouldBe room.floor
            saved.pricePerNight shouldBe room.pricePerNight
            saved.capacity shouldBe room.capacity
            saved.status shouldBe room.status
            saved.description shouldBe room.description
        }

        should("update room successfully") {
            roomRepository.create(room)
            val updatedRoom =
                room.copy(
                    roomCode = "A-202",
                    floor = 2,
                    pricePerNight = BigDecimal("199.99"),
                    capacity = 4,
                    status = RoomStatus.UNAVAILABLE,
                    description = "Updated room description",
                )

            val updated = roomRepository.update(updatedRoom)

            updated.roomCode shouldBe "A-202"
            updated.floor shouldBe 2
            updated.pricePerNight shouldBe BigDecimal("199.99")
            updated.capacity shouldBe 4
            updated.status shouldBe RoomStatus.UNAVAILABLE
            updated.description shouldBe "Updated room description"
        }

        should("find room by id") {
            roomRepository.create(room)

            val found = roomRepository.findById(room.id)

            found.shouldNotBeNull()
            found.id shouldBe room.id
        }

        should("return null when id not found") {
            val found = roomRepository.findById(UUID.randomUUID())
            found.shouldBeNull()
        }

        should("find rooms by hotel id") {
            roomRepository.create(room)

            val found = roomRepository.findByHotelId(room.hotelId)

            found shouldHaveSize 1
            found.first().id shouldBe room.id
        }

        should("return empty list when hotel id not found") {
            val found = roomRepository.findByHotelId(UUID.randomUUID())
            found shouldBe emptyList()
        }
    }
}
