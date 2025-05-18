package com.hotela.model.domain

data class Email(
    val value: String
) {
    companion object {
        val REGEX_EMAIL = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$")
    }

    init {
        require(value.isNotBlank()) { "Email cannot be blank" }
        require(isValidEmail(value)) { "Email format is invalid" }
    }

    private fun isValidEmail(email: String): Boolean {
        return REGEX_EMAIL.matches(email)
    }
}
