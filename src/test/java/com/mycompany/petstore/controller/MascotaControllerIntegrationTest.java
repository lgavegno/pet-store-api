package com.mycompany.petstore.controller;

import com.mycompany.petstore.BaseIntegrationTest;
import com.mycompany.petstore.dto.MascotaDTO;
import com.mycompany.petstore.model.Mascota;
import com.mycompany.petstore.repository.MascotaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MascotaControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MascotaRepository mascotaRepository;

    private Mascota testMascota;

    @BeforeEach
    void setUp() {
        // Clear the database before each test
        mascotaRepository.deleteAll();
        
        // Create a test mascota
        testMascota = new Mascota();
        testMascota.setNombre("Max");
        testMascota.setEspecie("Perro");
        testMascota.setRaza("Labrador");
        testMascota.setEdad(3);
        testMascota = mascotaRepository.save(testMascota);
    }

    @Test
    void getAllMascotas_ShouldReturnAllMascotas() throws Exception {
        // When
        ResultActions result = mockMvc.perform(get("/api/mascotas")
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre", is(testMascota.getNombre())))
                .andExpect(jsonPath("$[0].especie", is(testMascota.getEspecie())));
    }

    @Test
    void getMascotaById_WithValidId_ShouldReturnMascota() throws Exception {
        // When
        ResultActions result = mockMvc.perform(get("/api/mascotas/{id}", testMascota.getId())
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testMascota.getId().intValue())))
                .andExpect(jsonPath("$.nombre", is(testMascota.getNombre())))
                .andExpect(jsonPath("$.especie", is(testMascota.getEspecie())));
    }

    @Test
    void getMascotaById_WithInvalidId_ShouldReturnNotFound() throws Exception {
        // Given
        Long invalidId = 999L;

        // When
        ResultActions result = mockMvc.perform(get("/api/mascotas/{id}", invalidId)
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        result.andExpect(status().isNotFound());
    }

    @Test
    void createMascota_WithValidData_ShouldReturnCreatedMascota() throws Exception {
        // Given
        MascotaDTO newMascota = new MascotaDTO();
        newMascota.setNombre("Luna");
        newMascota.setEspecie("Gato");
        newMascota.setRaza("Siamés");
        newMascota.setEdad(2);

        // When
        ResultActions result = mockMvc.perform(post("/api/mascotas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newMascota)));

        // Then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.nombre", is(newMascota.getNombre())))
                .andExpect(jsonPath("$.especie", is(newMascota.getEspecie())));

        // Verify the mascota was saved in the database
        List<Mascota> mascotas = mascotaRepository.findAll();
        assertThat(mascotas).hasSize(2);
        assertThat(mascotas.get(1).getNombre()).isEqualTo("Luna");
    }

    @Test
    void updateMascota_WithValidData_ShouldReturnUpdatedMascota() throws Exception {
        // Given
        testMascota.setNombre("Max Updated");
        testMascota.setEdad(4);

        // When
        ResultActions result = mockMvc.perform(put("/api/mascotas/{id}", testMascota.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testMascota)));

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testMascota.getId().intValue())))
                .andExpect(jsonPath("$.nombre", is("Max Updated")))
                .andExpect(jsonPath("$.edad", is(4)));

        // Verify the mascota was updated in the database
        Mascota updatedMascota = mascotaRepository.findById(testMascota.getId()).orElseThrow();
        assertThat(updatedMascota.getNombre()).isEqualTo("Max Updated");
        assertThat(updatedMascota.getEdad()).isEqualTo(4);
    }

    @Test
    void deleteMascota_WithValidId_ShouldReturnNoContent() throws Exception {
        // When
        ResultActions result = mockMvc.perform(delete("/api/mascotas/{id}", testMascota.getId())
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        result.andExpect(status().isNoContent());

        // Verify the mascota was deleted from the database
        assertThat(mascotaRepository.existsById(testMascota.getId())).isFalse();
    }
}
