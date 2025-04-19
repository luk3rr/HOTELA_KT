package com.hotela.controller

import com.hotela.model.Example
import com.hotela.service.ExampleService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/example")
class ExampleController(
        private val exampleService: ExampleService
) {

    @GetMapping("/examples/{id}")
    suspend fun getExampleById(@PathVariable id: UUID): ResponseEntity<Example> {
        val example = exampleService.findExampleById(id)
        return ResponseEntity.ok(example)
    }

    @PostMapping("/examples/create")
    suspend fun createExample(@RequestBody example: Example): ResponseEntity<UUID> {
        val id = exampleService.createExample(example)
        return ResponseEntity.status(HttpStatus.CREATED).body(id)
    }
}
