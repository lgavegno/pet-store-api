package com.mycompany.petstore.controller;

import com.mycompany.petstore.BaseIntegrationTest;
import com.mycompany.petstore.model.Duenio;
import com.mycompany.petstore.repository.DuenioRepository;
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

class DuenioControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private DuenioRepository duenioRepository;

    private Duenio testDuenio;

    @BeforeEach
    void setUp() {
        // Clear the database before each test
        duenioRepository.deleteAll();
        
        // Create a test duenio
        testDuenio = new Duenio();
        testDuenio.setNombre("Juan");
        testDuenio.setApellido("Perez");
        testDuenio.setTelefono("123456789");
        testDuenio.setDireccion("Calle Falsa 123");
        testDuenio = duenioRepository.save(testDuenio);
    }

    @Test
    void getAllDuenios_ShouldReturnAllDuenios() throws Exception {
        // When
        ResultActions result = mockMvc.perform(get("/api/duenios")
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre", is(testDuenio.getNombre())))
                .andExpect(jsonPath("$[0].apellido", is(testDuenio.getApellido())));
    }

    @Test
    void getDuenioById_WithValidId_ShouldReturnDuenio() throws Exception {
        // When
        ResultActions result = mockMvc.perform(get("/api/duenios/{id}", testDuenio.getId())
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testDuenio.getId().intValue())))
                .andExpect(jsonPath("$.nombre", is(testDuenio.getNombre())))
                .andExpect(jsonPath("$.apellido", is(testDuenio.getApellido())));
    }

    @Test
    void getDuenioById_WithInvalidId_ShouldReturnNotFound() throws Exception {
        // Given
        Long invalidId = 999L;

        // When
        ResultActions result = mockMvc.perform(get("/api/duenios/{id}", invalidId)
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        result.andExpect(status().isNotFound());
    }

    @Test
    void createDuenio_WithValidData_ShouldReturnCreatedDuenio() throws Exception {
        // Given
        Duenio newDuenio = new Duenio();
        newDuenio.setNombre("Maria");
        newDuenio.setApellido("Gonzalez");
        newDuenio.setTelefono("987654321");
        newDuenio.setDireccion("Avenida Siempreviva 742");

        // When
        ResultActions result = mockMvc.perform(post("/api/duenios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newDuenio)));

        // Then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.nombre", is(newDuenio.getNombre())))
                .andExpect(jsonPath("$.apellido", is(newDuenio.getApellido())));

        // Verify the duenio was saved in the database
        List<Duenio> duenios = duenioRepository.findAll();
        assertThat(duenios).hasSize(2);
        assertThat(duenios.get(1).getNombre()).isEqualTo("Maria");
    }

    @Test
    void updateDuenio_WithValidData_ShouldReturnUpdatedDuenio() throws Exception {
        // Given
        testDuenio.setNombre("Juan Carlos");
        testDuenio.setTelefono("555-1234");

        // When
        ResultActions result = mockMvc.perform(put("/api/duenios/{id}", testDuenio.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testDuenio)));

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testDuenio.getId().intValue())))
                .andExpect(jsonPath("$.nombre", is("Juan Carlos")))
                .andExpect(jsonPath("$.telefono", is("555-1234")));

        // Verify the duenio was updated in the database
        Duenio updatedDuenio = duenioRepository.findById(testDuenio.getId()).orElseThrow();
        assertThat(updatedDuenio.getNombre()).isEqualTo("Juan Carlos");
        assertThat(updatedDuenio.getTelefono()).isEqualTo("555-1234");
    }

    @Test
    void deleteDuenio_WithValidId_ShouldReturnNoContent() throws Exception {
        // When
        ResultActions result = mockMvc.perform(delete("/api/duenios/{id}", testDuenio.getId())
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        result.andExpect(status().isNoContent());

        // Verify the duenio was deleted from the database
        assertThat(duenioRepository.existsById(testDuenio.getId())).isFalse();
    }
}
