package com.hotela.service

import com.hotela.model.database.Customer
import com.hotela.repository.CustomerRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CustomerService(
    private val customerRepository: CustomerRepository,
) {
    suspend fun findById(id: UUID): Customer? = customerRepository.findById(id)

    suspend fun save(customer: Customer): Customer = customerRepository.create(customer)
}
