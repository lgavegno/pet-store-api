package com.mycompany.petstore.service;

import com.mycompany.petstore.dto.MascotaDTO;
import com.mycompany.petstore.model.Mascota;
import com.mycompany.petstore.model.Duenio;
import com.mycompany.petstore.repository.MascotaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MascotaService {

    private final MascotaRepository mascotaRepository;
    private final DuenioService duenioService;

    @Transactional(readOnly = true)
    public List<MascotaDTO> findAll() {
        return mascotaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MascotaDTO findById(Long id) {
        Mascota mascota = mascotaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mascota no encontrada con ID: " + id));
        return convertToDTO(mascota);
    }

    @Transactional
    public MascotaDTO save(MascotaDTO mascotaDTO) {
        // Create or update the owner first
        Duenio duenio = new Duenio();
        duenio.setNombre(mascotaDTO.getNombreDuenio());
        duenio.setCelular(mascotaDTO.getCelularDuenio());
        duenio = duenioService.save(duenio);

        // Create and save the pet
        Mascota mascota = new Mascota();
        mascota.setNombre(mascotaDTO.getNombre());
        mascota.setRaza(mascotaDTO.getRaza());
        mascota.setColor(mascotaDTO.getColor());
        mascota.setAlergico(mascotaDTO.getAlergico());
        mascota.setAtencionEspecial(mascotaDTO.getAtencionEspecial());
        mascota.setObservaciones(mascotaDTO.getObservaciones());
        mascota.setDuenio(duenio);

        mascota = mascotaRepository.save(mascota);
        return convertToDTO(mascota);
    }

    @Transactional
    public MascotaDTO update(Long id, MascotaDTO mascotaDTO) {
        Mascota mascota = mascotaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mascota no encontrada con ID: " + id));

        // Update pet details
        mascota.setNombre(mascotaDTO.getNombre());
        mascota.setRaza(mascotaDTO.getRaza());
        mascota.setColor(mascotaDTO.getColor());
        mascota.setAlergico(mascotaDTO.getAlergico());
        mascota.setAtencionEspecial(mascotaDTO.getAtencionEspecial());
        mascota.setObservaciones(mascotaDTO.getObservaciones());

        // Update owner details
        Duenio duenio = mascota.getDuenio();
        duenio.setNombre(mascotaDTO.getNombreDuenio());
        duenio.setCelular(mascotaDTO.getCelularDuenio());
        duenioService.update(duenio.getId(), duenio);

        mascota = mascotaRepository.save(mascota);
        return convertToDTO(mascota);
    }

    @Transactional
    public void delete(Long id) {
        Mascota mascota = mascotaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mascota no encontrada con ID: " + id));
        
        // Delete the pet first to maintain referential integrity
        mascotaRepository.delete(mascota);
        
        // Then delete the owner if they don't have any other pets
        Duenio duenio = mascota.getDuenio();
        if (mascotaRepository.countByDuenioId(duenio.getId()) == 0) {
            duenioService.delete(duenio.getId());
        }
    }

    private MascotaDTO convertToDTO(Mascota mascota) {
        MascotaDTO dto = new MascotaDTO();
        dto.setId(mascota.getId());
        dto.setNombre(mascota.getNombre());
        dto.setRaza(mascota.getRaza());
        dto.setColor(mascota.getColor());
        dto.setAlergico(mascota.getAlergico());
        dto.setAtencionEspecial(mascota.getAtencionEspecial());
        dto.setObservaciones(mascota.getObservaciones());
        
        if (mascota.getDuenio() != null) {
            dto.setNombreDuenio(mascota.getDuenio().getNombre());
            dto.setCelularDuenio(mascota.getDuenio().getCelular());
        }
        
        return dto;
    }
}
