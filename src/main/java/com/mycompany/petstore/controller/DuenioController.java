package com.mycompany.petstore.controller;

import com.mycompany.petstore.model.Duenio;
import com.mycompany.petstore.service.DuenioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/duenios")
@RequiredArgsConstructor
public class DuenioController {

    private final DuenioService duenioService;

    @GetMapping
    public ResponseEntity<List<Duenio>> getAllDuenios() {
        return ResponseEntity.ok(duenioService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Duenio> getDuenioById(@PathVariable Long id) {
        return ResponseEntity.ok(duenioService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Duenio> createDuenio(@Valid @RequestBody Duenio duenio) {
        return new ResponseEntity<>(duenioService.save(duenio), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Duenio> updateDuenio(
            @PathVariable Long id, 
            @Valid @RequestBody Duenio duenio) {
        return ResponseEntity.ok(duenioService.update(id, duenio));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDuenio(@PathVariable Long id) {
        duenioService.delete(id);
    }
}
