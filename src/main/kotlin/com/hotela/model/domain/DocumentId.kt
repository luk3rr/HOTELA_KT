package com.hotela.model.domain

import com.hotela.model.enum.DocumentIdType

class DocumentId(
    val type: DocumentIdType,
    val value: String,
) {
    companion object {
        val CNPJ_REGEX = Regex("[0-9]{2}\\.?[0-9]{3}\\.?[0-9]{3}/?[0-9]{4}-?[0-9]{2}")
        val CPF_REGEX = Regex("[0-9]{3}\\.?[0-9]{3}\\.?[0-9]{3}-?[0-9]{2}")
    }

    init {
        require(value.isNotBlank()) { "Document ID cannot be blank" }
        require(isValidDocumentId(value)) { "Document ID format is invalid" }
    }

    private fun isValidDocumentId(documentId: String): Boolean =
        when (type) {
            DocumentIdType.CNPJ -> CNPJ_REGEX.matches(documentId)
            DocumentIdType.CPF -> CPF_REGEX.matches(documentId)
            DocumentIdType.OTHER -> true
        }
}
