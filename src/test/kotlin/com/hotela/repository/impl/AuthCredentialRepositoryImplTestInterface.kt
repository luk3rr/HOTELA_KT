package com.hotela.repository.impl

import com.hotela.clearAllTables
import com.hotela.model.domain.Email
import com.hotela.repository.AuthCredentialRepository
import com.hotela.stubs.db.AuthCredentialStubs
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.r2dbc.core.DatabaseClient
import java.util.UUID

class AuthCredentialRepositoryImplTestInterface :
    ShouldSpec(),
    DatabaseIntegrationTestInterface {
    @Autowired
    private lateinit var authCredentialRepository: AuthCredentialRepository

    @Autowired
    private lateinit var databaseClient: DatabaseClient

    override fun extensions() = listOf(SpringExtension)

    init {
        val authCredential = AuthCredentialStubs.create()

        beforeSpec {
            databaseClient.clearAllTables()
        }

        beforeTest {
            databaseClient
                .sql("DELETE FROM auth_credential")
                .fetch()
                .rowsUpdated()
                .awaitSingle()
        }

        should("successfully create an auth credential") {
            val saved = authCredentialRepository.create(authCredential)

            saved.id shouldBe authCredential.id
            saved.loginEmail shouldBe authCredential.loginEmail
            saved.password shouldBe authCredential.password
            saved.role shouldBe authCredential.role
            saved.isActive shouldBe authCredential.isActive
            saved.createdAt shouldBe authCredential.createdAt
            saved.updatedAt shouldBe authCredential.updatedAt

            val found = authCredentialRepository.findById(authCredential.id)
            found shouldNotBe null
            found?.id shouldBe authCredential.id
        }

        should("successfully find by login email") {
            authCredentialRepository.create(authCredential)

            val result = authCredentialRepository.findByLoginEmail(authCredential.loginEmail)

            result shouldNotBe null
            result?.id shouldBe authCredential.id
        }

        should("return null when email not found") {
            val result = authCredentialRepository.findByLoginEmail(Email("unknown@domain.com"))
            result shouldBe null
        }

        should("successfully find by id") {
            authCredentialRepository.create(authCredential)

            val result = authCredentialRepository.findById(authCredential.id)

            result shouldNotBe null
            result?.id shouldBe authCredential.id
        }

        should("return null when id not found") {
            val result = authCredentialRepository.findById(UUID.randomUUID())
            result shouldBe null
        }

        should("successfully check existence by login email") {
            authCredentialRepository.create(authCredential)

            val exists = authCredentialRepository.existsByLoginEmail(authCredential.loginEmail)
            exists shouldBe true
        }

        should("return false when checking non-existent email") {
            val exists = authCredentialRepository.existsByLoginEmail(Email("notfound@email.com"))
            exists shouldBe false
        }

        should("successfully check existence by id") {
            authCredentialRepository.create(authCredential)

            val exists = authCredentialRepository.existsById(authCredential.id)
            exists shouldBe true
        }

        should("return false when checking non-existent id") {
            val exists = authCredentialRepository.existsById(UUID.fromString("a8845209-85c7-42a1-93e5-a3c2ddcfb1f5"))
            exists shouldBe false
        }
    }
}
