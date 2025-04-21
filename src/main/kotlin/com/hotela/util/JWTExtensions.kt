package com.hotela.util

import com.hotela.error.HotelaException
import com.hotela.model.enum.AuthClaimKey
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import java.util.UUID

fun JwtAuthenticationToken.getPartnerAuthId(): UUID =
    token.claims[AuthClaimKey.PARTNER.key]?.let {
        UUID.fromString(it.toString())
    } ?: throw HotelaException.InvalidCredentialsException()

fun JwtAuthenticationToken.getCustomerAuthId(): UUID =
    token.claims[AuthClaimKey.CUSTOMER.key]?.let {
        UUID.fromString(it.toString())
    } ?: throw HotelaException.InvalidCredentialsException()
