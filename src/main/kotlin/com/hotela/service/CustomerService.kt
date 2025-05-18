package com.hotela.service

import com.hotela.error.HotelaException
import com.hotela.model.db.Customer
import com.hotela.model.dto.request.UpdateCustomerRequest
import com.hotela.repository.CustomerRepository
import com.hotela.util.getUserId
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CustomerService(
    private val customerRepository: CustomerRepository,
) {
    suspend fun findById(id: UUID): Customer? = customerRepository.findById(id)

    suspend fun updateCustomer(
        payload: UpdateCustomerRequest,
        token: JwtAuthenticationToken,
    ): Customer {
        val customerId = token.getUserId()

        val existingCustomer =
            findById(customerId)
                ?: throw HotelaException.CustomerNotFoundException(customerId)

        val updatedCustomer =
            existingCustomer.copy(
                name = payload.name ?: existingCustomer.name,
                contactInfo = payload.contactInfo
                    ?: existingCustomer.contactInfo,
                documentId = payload.documentId
                    ?: existingCustomer.documentId,
                birthDate = payload.birthDate
                    ?: existingCustomer.birthDate,
            )

        return customerRepository.update(updatedCustomer)
    }
}
