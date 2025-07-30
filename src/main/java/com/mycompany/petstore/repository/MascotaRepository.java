package com.mycompany.petstore.repository;

import com.mycompany.petstore.model.Mascota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MascotaRepository extends JpaRepository<Mascota, Long> {
    // Custom query methods can be added here if needed
}
