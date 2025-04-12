package com.hotela.controller;

import com.hotela.model.Example;
import com.hotela.service.ExampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/example")
public class ExampleController {
    private final ExampleService exampleService;

    @Autowired
    public ExampleController(ExampleService exampleService) {
        this.exampleService = exampleService;
    }

    @GetMapping("/examples/{id}")
    public Example getExampleById(@PathVariable UUID id) {
        return exampleService.findExampleById(id);
    }

    @PostMapping("/examples/create")
    public ResponseEntity<UUID> createExample(@RequestBody Example example) {
        UUID id = exampleService.createExample(example);

        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }
}
