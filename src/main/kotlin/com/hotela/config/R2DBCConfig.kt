package com.hotela.config

import com.hotela.converter.reader.EmailReadingConverter
import com.hotela.converter.reader.PhoneNumberReadingConverter
import com.hotela.converter.writer.EmailWritingConverter
import com.hotela.converter.writer.PhoneNumberWritingConverter
import com.hotela.model.enum.BookingStatus
import com.hotela.model.enum.PartnerStatus
import com.hotela.model.enum.PaymentMethod
import com.hotela.model.enum.PaymentStatus
import com.hotela.model.enum.RoomStatus
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import io.r2dbc.postgresql.codec.EnumCodec
import io.r2dbc.spi.ConnectionFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions
import org.springframework.data.r2dbc.dialect.PostgresDialect
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
                .codecRegistrar(
                    EnumCodec
                        .builder()
                        .withEnum("partner_status", PartnerStatus::class.java)
                        .withEnum("room_status", RoomStatus::class.java)
                        .withEnum("payment_method", PaymentMethod::class.java)
                        .withEnum("payment_status", PaymentStatus::class.java)
                        .withEnum("booking_status", BookingStatus::class.java)
                        .build(),
                ).build(),
        )
    }

    @Bean
    fun customConverter(): R2dbcCustomConversions {
        val converters = listOf(
            EmailWritingConverter(),
            EmailReadingConverter(),
            PhoneNumberWritingConverter(),
            PhoneNumberReadingConverter()
        )

        return R2dbcCustomConversions.of(PostgresDialect.INSTANCE, converters)
    }
}
