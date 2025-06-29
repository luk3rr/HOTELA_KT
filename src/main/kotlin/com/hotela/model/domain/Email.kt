package com.hotela.model.domain

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

@JvmInline
value class Email
    @JsonCreator
    constructor(
        @get:JsonValue
        val value: String,
    ) {
        companion object {
            val REGEX_EMAIL = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$")
        }

        init {
            require(value.isNotBlank()) { "Email cannot be blank" }
            require(isValidEmail(value)) { "Email format is invalid" }
        }

        private fun isValidEmail(email: String): Boolean = REGEX_EMAIL.matches(email)
    }
