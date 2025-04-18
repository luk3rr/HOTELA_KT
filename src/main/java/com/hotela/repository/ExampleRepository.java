package com.hotela.repository;

import com.hotela.model.Example;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExampleRepository extends JpaRepository<Example, UUID> {
    @NonNull
    Optional<Example> findById(@NonNull UUID id);
}
