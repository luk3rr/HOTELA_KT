package com.hotela.repository.impl

import com.hotela.repository.RoomTypeRepository
import com.hotela.stubs.db.RoomTypeStubs
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.r2dbc.core.DatabaseClient
import java.util.UUID

class RoomTypeRepositoryImplTestInterface :
    ShouldSpec(),
    DatabaseIntegrationTestInterface {
    @Autowired
    private lateinit var roomTypeRepository: RoomTypeRepository

    @Autowired
    private lateinit var databaseClient: DatabaseClient

    override fun extensions() = listOf(SpringExtension)

    init {
        val roomType = RoomTypeStubs.createCasalDeluxe()
        val anotherRoomType = RoomTypeStubs.createStandardSolteiro()

        should("find room type by id") {
            val found = roomTypeRepository.findById(roomType.id)

            found shouldNotBe null
            found?.id shouldBe roomType.id
            found?.name shouldBe roomType.name
            found?.description shouldBe roomType.description
        }

        should("return null when id not found") {
            val found = roomTypeRepository.findById(UUID.fromString("3b42cbff-d42c-4aaa-822c-e63e265eb376"))
            found shouldBe null
        }

        should("find all room types") {
            val all = roomTypeRepository.findAll()

            all.any { it.id == roomType.id } shouldBe true
            all.any { it.id == anotherRoomType.id } shouldBe true
        }
    }
}
