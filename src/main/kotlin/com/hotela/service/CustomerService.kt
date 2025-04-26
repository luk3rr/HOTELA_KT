package com.hotela.service

import com.hotela.error.HotelaException
import com.hotela.model.database.Customer
import com.hotela.model.dto.request.UpdateCustomerRequest
import com.hotela.repository.CustomerAuthRepository
import com.hotela.repository.CustomerRepository
import com.hotela.util.getCustomerAuthId
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CustomerService(
    private val customerRepository: CustomerRepository,
    private val customerAuthRepository: CustomerAuthRepository,
) {
    suspend fun findById(id: UUID): Customer? = customerRepository.findById(id)

    suspend fun createCustomer(customer: Customer): Customer {
        if (customerRepository.findById(customer.id) != null) {
            throw HotelaException.CustomerAlreadyExistsException(customer.id)
        }

        return customerRepository.create(customer)
    }

    suspend fun updateCustomer(
        payload: UpdateCustomerRequest,
        token: JwtAuthenticationToken,
    ): Customer {
        val customerAuthId = token.getCustomerAuthId()

        val customerAuth =
            customerAuthRepository.findById(customerAuthId)
                ?: throw HotelaException.CustomerAuthNotFoundException(customerAuthId)

        val existingCustomer =
            findById(customerAuth.customerId)
                ?: throw HotelaException.CustomerNotFoundException(customerAuth.customerId)

        val updatedCustomer =
            existingCustomer.copy(
                name = payload.name ?: existingCustomer.name,
                phone = payload.phone ?: existingCustomer.phone,
                idDocument = payload.idDocument ?: existingCustomer.idDocument,
                birthDate = payload.birthDate ?: existingCustomer.birthDate,
                address = payload.address ?: existingCustomer.address,
            )

        return customerRepository.update(updatedCustomer)
    }
}
