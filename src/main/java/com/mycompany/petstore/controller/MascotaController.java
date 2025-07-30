package com.mycompany.petstore.controller;

import com.mycompany.petstore.dto.MascotaDTO;
import com.mycompany.petstore.service.MascotaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mascotas")
@RequiredArgsConstructor
public class MascotaController {

    private final MascotaService mascotaService;

    @GetMapping
    public ResponseEntity<List<MascotaDTO>> getAllMascotas() {
        return ResponseEntity.ok(mascotaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MascotaDTO> getMascotaById(@PathVariable Long id) {
        return ResponseEntity.ok(mascotaService.findById(id));
    }

    @PostMapping
    public ResponseEntity<MascotaDTO> createMascota(@Valid @RequestBody MascotaDTO mascotaDTO) {
        return new ResponseEntity<>(mascotaService.save(mascotaDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MascotaDTO> updateMascota(
            @PathVariable Long id,
            @Valid @RequestBody MascotaDTO mascotaDTO) {
        return ResponseEntity.ok(mascotaService.update(id, mascotaDTO));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMascota(@PathVariable Long id) {
        mascotaService.delete(id);
    }
}
