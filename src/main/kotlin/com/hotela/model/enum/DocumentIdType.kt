package com.hotela.model.enum

enum class DocumentIdType {
    CNPJ,
    CPF,
    OTHER;

    companion object {
        fun fromString(value: String): DocumentIdType {
            return when (value.uppercase()) {
                "CNPJ" -> CNPJ
                "CPF" -> CPF
                else -> OTHER
            }
        }
    }
}