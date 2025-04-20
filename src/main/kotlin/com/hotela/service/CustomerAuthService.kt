package com.hotela.service

import com.hotela.model.database.CustomerAuth
import com.hotela.repository.CustomerAuthRepository
import org.springframework.stereotype.Service

@Service
class CustomerAuthService(
    private val customerAuthRepository: CustomerAuthRepository,
) {
    suspend fun findByEmail(email: String): CustomerAuth? = customerAuthRepository.findByEmail(email)

    suspend fun existsByEmail(email: String): Boolean = customerAuthRepository.existsByEmail(email)

    suspend fun save(customerAuth: CustomerAuth): CustomerAuth = customerAuthRepository.save(customerAuth)
}
