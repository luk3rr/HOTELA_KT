package com.hotela.util

import com.hotela.error.HotelaException
import com.hotela.model.enum.AuthClaimKey
import com.hotela.model.enum.Role
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import java.util.UUID

fun JwtAuthenticationToken.getAuthId(): UUID =
    token.claims[AuthClaimKey.AUTHID.key]?.let {
        UUID.fromString(it.toString())
    } ?: throw HotelaException.InvalidCredentialsException()

fun JwtAuthenticationToken.getUserId(): UUID =
    token.claims[AuthClaimKey.USERID.key]?.let {
        UUID.fromString(it.toString())
    } ?: throw HotelaException.InvalidCredentialsException()

fun JwtAuthenticationToken.getRole(): Role =
    token.claims[AuthClaimKey.ROLE.key]?.let {
        Role.valueOf(it.toString())
    } ?: throw HotelaException.InvalidCredentialsException()
