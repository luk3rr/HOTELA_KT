package com.hotela.service

import com.hotela.model.Example
import com.hotela.repository.ExampleRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ExampleService(
    private val exampleRepository: ExampleRepository,
) {
    suspend fun createExample(example: Example): UUID = exampleRepository.create(example)

    suspend fun getExampleById(id: UUID): Example = exampleRepository.getById(id)

    suspend fun findExampleById(id: UUID): Example? = exampleRepository.findById(id)
}
