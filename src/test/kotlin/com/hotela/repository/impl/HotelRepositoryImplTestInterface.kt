package com.hotela.repository.impl

import com.hotela.clearAllTables
import com.hotela.repository.AddressRepository
import com.hotela.repository.AuthCredentialRepository
import com.hotela.repository.HotelRepository
import com.hotela.repository.PartnerRepository
import com.hotela.stubs.db.AddressStubs
import com.hotela.stubs.db.AuthCredentialStubs
import com.hotela.stubs.db.HotelStubs
import com.hotela.stubs.db.PartnerStubs
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.r2dbc.core.DatabaseClient
import java.util.UUID

class HotelRepositoryImplTestInterface :
    ShouldSpec(),
    DatabaseIntegrationTestInterface {
    @Autowired
    private lateinit var partnerRepository: PartnerRepository

    @Autowired
    private lateinit var authCredentialRepository: AuthCredentialRepository

    @Autowired
    private lateinit var addressRepository: AddressRepository

    @Autowired
    private lateinit var hotelRepository: HotelRepository

    @Autowired
    private lateinit var databaseClient: DatabaseClient

    override fun extensions() = listOf(SpringExtension)

    init {
        val address = AddressStubs.create()
        val partnerAuthCredential = AuthCredentialStubs.create()
        val partner = PartnerStubs.create(authCredentialId = partnerAuthCredential.id)
        val hotel = HotelStubs.create(partnerId = partner.id, addressId = address.id)
        val anotherHotel =
            HotelStubs.create(
                id = UUID.fromString("29db6b41-e1c4-4c05-ad4a-e605547fe5a2"),
                partnerId = partner.id,
                addressId = address.id,
            )

        beforeSpec {
            databaseClient.clearAllTables()

            addressRepository.create(address)
            authCredentialRepository.create(partnerAuthCredential)
            partnerRepository.create(partner)
        }

        beforeTest {
            databaseClient
                .sql("DELETE FROM hotel")
                .fetch()
                .rowsUpdated()
                .awaitSingle()
        }

        should("successfully create a hotel") {
            val saved = hotelRepository.create(hotel)

            saved.id shouldBe hotel.id
            saved.partnerId shouldBe hotel.partnerId
            saved.addressId shouldBe hotel.addressId
            saved.name shouldBe hotel.name
            saved.contactInfo shouldBe hotel.contactInfo
            saved.website shouldBe hotel.website
            saved.description shouldBe hotel.description
            saved.starRating shouldBe hotel.starRating

            val found = hotelRepository.findById(hotel.id)
            found shouldNotBe null
            found?.id shouldBe hotel.id
        }

        should("successfully update a hotel") {
            hotelRepository.create(hotel)

            val updatedHotel = hotel.copy(name = "Updated Hotel Name")
            val updated = hotelRepository.update(updatedHotel)

            updated.id shouldBe hotel.id
            updated.name shouldBe "Updated Hotel Name"

            val found = hotelRepository.findById(hotel.id)
            found shouldNotBe null
            found?.name shouldBe "Updated Hotel Name"
        }

        should("successfully find hotel by id") {
            hotelRepository.create(hotel)

            val found = hotelRepository.findById(hotel.id)

            found shouldNotBe null
            found?.id shouldBe hotel.id
        }

        should("return null when hotel id not found") {
            val found = hotelRepository.findById(UUID.randomUUID())
            found shouldBe null
        }

        should("successfully find hotels by partner id") {
            hotelRepository.create(hotel)
            hotelRepository.create(anotherHotel.copy(partnerId = hotel.partnerId))

            val hotels = hotelRepository.findByPartnerId(hotel.partnerId)

            hotels.size shouldBe 2
            hotels.any { it.id == hotel.id } shouldBe true
            hotels.any { it.id == anotherHotel.id } shouldBe true
        }

        should("return empty list when partner has no hotels") {
            val result = hotelRepository.findByPartnerId(UUID.randomUUID())
            result shouldBe emptyList()
        }
    }
}
