package com.hotela.model.domain

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

@JvmInline
value class PhoneNumber
    @JsonCreator
    constructor(
        @get:JsonValue
        val value: String,
    ) {
        companion object {
            val REGEX_PHONE = Regex("^[+\\d\\s\\-\\(\\)]+$")
            const val MIN_DIGITS = 7
        }

        init {
            require(value.isNotBlank()) { "Phone number cannot be blank" }

            require(isValidPhoneNumber(value)) { "Phone number format is invalid" }

            val digitsCount = value.count { it.isDigit() }
            require(digitsCount >= MIN_DIGITS) { "Phone number must have at least $MIN_DIGITS digits" }
        }

        private fun isValidPhoneNumber(phoneNumber: String): Boolean = REGEX_PHONE.matches(phoneNumber)
    }
