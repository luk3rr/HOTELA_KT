package com.hotela.config

import com.hotela.model.enum.PartnerStatus
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import io.r2dbc.postgresql.codec.EnumCodec
import io.r2dbc.spi.ConnectionFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.net.URI

@Configuration
class R2DBCConfig(
    @Value("\${spring.r2dbc.url}")
    private val r2dbcUrl: String,
    @Value("\${spring.r2dbc.username}")
    private val r2dbcUsername: String,
    @Value("\${spring.r2dbc.password}")
    private val r2dbcPassword: String,
) {
    @Bean
    fun connectionFactory(): ConnectionFactory {
        val uri = URI(r2dbcUrl.removePrefix("r2dbc:"))

        return PostgresqlConnectionFactory(
            PostgresqlConnectionConfiguration
                .builder()
                .host(uri.host)
                .port(uri.port)
                .database(uri.path.removePrefix("/"))
                .username(r2dbcUsername)
                .password(r2dbcPassword)
                .codecRegistrar(EnumCodec.builder().withEnum("partner_status", PartnerStatus::class.java).build())
                .build(),
        )
    }
}
