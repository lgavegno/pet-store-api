package com.mycompany.petstore.repository;

import com.mycompany.petstore.model.Duenio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DuenioRepository extends JpaRepository<Duenio, Long> {
    // Custom query methods can be added here if needed
}
