package com.hotela.model.enum

enum class AuthClaimKey(
    val key: String,
) {
    CUSTOMER("customerAuthId"),
    PARTNER("partnerAuthId"),
    ROLE("role"),
}
