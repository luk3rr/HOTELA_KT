package com.hotela.config

import com.hotela.util.SystemInstantProvider
import com.hotela.util.TimeProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Instant

@Configuration
class AppConfig {
    @Bean
    fun timeProvider(): TimeProvider<Instant> = SystemInstantProvider()
}
