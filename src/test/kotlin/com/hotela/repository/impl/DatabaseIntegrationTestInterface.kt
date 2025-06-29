package com.hotela.repository.impl

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
interface DatabaseIntegrationTestInterface {
    companion object {
        const val POSTGRES_VERSION = "15-alpine"
        const val CONTAINER_PORT = 5432
        const val DATABASE_SCHEMA = "hotela"

        @Container
        private val postgreSQLContainer =
            PostgreSQLContainer("postgres:${POSTGRES_VERSION}")
                .apply {
                    withDatabaseName("hotela")
                    withUsername("hotela_admins")
                    withPassword("postgres")
                    withInitScript("db/test-setup.sql")
                }.also { it.start() }

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            val r2dbcUrl =
                "r2dbc:postgresql://${postgreSQLContainer.host}:${postgreSQLContainer.getMappedPort(
                    CONTAINER_PORT,
                )}/${postgreSQLContainer.databaseName}"
            val jdbcUrl =
                "jdbc:postgresql://${postgreSQLContainer.host}:${postgreSQLContainer.getMappedPort(
                    CONTAINER_PORT,
                )}/${postgreSQLContainer.databaseName}"

            registry.add("spring.r2dbc.url") { r2dbcUrl }
            registry.add("spring.liquibase.url") { jdbcUrl }
            registry.add("spring.r2dbc.username") { postgreSQLContainer.username }
            registry.add("spring.r2dbc.password") { postgreSQLContainer.password }
            registry.add("spring.liquibase.user") { postgreSQLContainer.username }
            registry.add("spring.liquibase.password") { postgreSQLContainer.password }

            registry.add("app.datasource.schema") { DATABASE_SCHEMA }
        }
    }
}
