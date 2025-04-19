package com.hotela.repository;

import com.hotela.model.Example;
import java.util.UUID;

interface ExampleRepository {
    suspend fun findById(id: UUID): Example?;
    suspend fun getById(id: UUID): Example;
    suspend fun create(example: Example): UUID;
}