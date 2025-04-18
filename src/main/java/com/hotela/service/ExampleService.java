package com.hotela.service;

import com.hotela.error.HotelaException;
import com.hotela.model.Example;
import com.hotela.repository.ExampleRepository;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExampleService {
    private final ExampleRepository exampleRepository;

    @Autowired
    public ExampleService(ExampleRepository exampleRepository) {
        this.exampleRepository = exampleRepository;
    }

    public UUID createExample(Example example) {
        if (example.getId() == null) {
            example.setId(UUID.randomUUID());
        }

        exampleRepository.save(example);
        return example.getId();
    }

    public Example findExampleById(UUID id) {
        return exampleRepository
                .findById(id)
                .orElseThrow(() -> new HotelaException.ExampleNotFoundException(id));
    }
}
