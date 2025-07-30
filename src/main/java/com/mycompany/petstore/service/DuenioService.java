package com.mycompany.petstore.service;

import com.mycompany.petstore.model.Duenio;
import com.mycompany.petstore.repository.DuenioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DuenioService {

    private final DuenioRepository duenioRepository;

    @Transactional(readOnly = true)
    public List<Duenio> findAll() {
        return duenioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Duenio findById(Long id) {
        return duenioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Dueño no encontrado con ID: " + id));
    }

    @Transactional
    public Duenio save(Duenio duenio) {
        return duenioRepository.save(duenio);
    }

    @Transactional
    public Duenio update(Long id, Duenio duenioDetails) {
        Duenio duenio = findById(id);
        duenio.setNombre(duenioDetails.getNombre());
        duenio.setCelular(duenioDetails.getCelular());
        return duenioRepository.save(duenio);
    }

    @Transactional
    public void delete(Long id) {
        Duenio duenio = findById(id);
        duenioRepository.delete(duenio);
    }
}
