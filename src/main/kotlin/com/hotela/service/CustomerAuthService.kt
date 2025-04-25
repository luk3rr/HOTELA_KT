package com.hotela.service

import com.hotela.error.HotelaException
import com.hotela.model.database.CustomerAuth
import com.hotela.repository.CustomerAuthRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CustomerAuthService(
    private val customerAuthRepository: CustomerAuthRepository,
) {
    suspend fun findByEmail(email: String): CustomerAuth? = customerAuthRepository.findByEmail(email)

    suspend fun existsByEmail(email: String): Boolean = customerAuthRepository.existsByEmail(email)

    suspend fun existsById(id: UUID): Boolean = customerAuthRepository.existsById(id)

    suspend fun createCustomerAuth(customerAuth: CustomerAuth): CustomerAuth {
        if (existsById(customerAuth.id)) {
            throw HotelaException.CustomerAuthAlreadyExistsException(customerAuth.id)
        }

        return customerAuthRepository.create(customerAuth)
    }
}
