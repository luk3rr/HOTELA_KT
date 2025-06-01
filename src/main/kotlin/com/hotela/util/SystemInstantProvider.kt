package com.hotela.util

import java.time.Instant

class SystemInstantProvider : TimeProvider<Instant> {
    override fun now(): Instant = Instant.now()
}
