package com.hotela.service

import com.hotela.model.Example
import com.hotela.repository.ExampleRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ExampleService(private val exampleRepository: ExampleRepository) {

    suspend fun createExample(example: Example): UUID {
        return exampleRepository.create(example)
    }

    suspend fun getExampleById(id: UUID): Example {
        return exampleRepository.getById(id)
    }

    suspend fun findExampleById(id: UUID): Example? {
        return exampleRepository.findById(id)
    }
}
