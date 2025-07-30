package com.mycompany.petstore.service;

import com.mycompany.petstore.dto.MascotaDTO;
import com.mycompany.petstore.exceptions.ResourceNotFoundException;
import com.mycompany.petstore.model.Duenio;
import com.mycompany.petstore.model.Mascota;
import com.mycompany.petstore.repository.DuenioRepository;
import com.mycompany.petstore.repository.MascotaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MascotaServiceTest {

    @Mock
    private MascotaRepository mascotaRepository;

    @Mock
    private DuenioRepository duenioRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private MascotaService mascotaService;

    private Mascota testMascota;
    private MascotaDTO testMascotaDTO;
    private Duenio testDuenio;

    @BeforeEach
    void setUp() {
        // Setup test data
        testDuenio = new Duenio();
        testDuenio.setId(1L);
        testDuenio.setNombre("Juan");
        testDuenio.setApellido("Perez");

        testMascota = new Mascota();
        testMascota.setId(1L);
        testMascota.setNombre("Max");
        testMascota.setEspecie("Perro");
        testMascota.setRaza("Labrador");
        testMascota.setEdad(3);
        testMascota.setDuenio(testDuenio);

        testMascotaDTO = new MascotaDTO();
        testMascotaDTO.setId(1L);
        testMascotaDTO.setNombre("Max");
        testMascotaDTO.setEspecie("Perro");
        testMascotaDTO.setRaza("Labrador");
        testMascotaDTO.setEdad(3);
        testMascotaDTO.setDuenioId(1L);

        // Configure model mapper
        when(modelMapper.map(any(MascotaDTO.class), eq(Mascota.class))).thenReturn(testMascota);
        when(modelMapper.map(any(Mascota.class), eq(MascotaDTO.class))).thenReturn(testMascotaDTO);
    }

    @Test
    void getAllMascotas_ShouldReturnAllMascotas() {
        // Given
        List<Mascota> mascotas = Arrays.asList(testMascota);
        when(mascotaRepository.findAll()).thenReturn(mascotas);

        // When
        List<MascotaDTO> result = mascotaService.getAllMascotas();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNombre()).isEqualTo("Max");
        verify(mascotaRepository).findAll();
        verify(modelMapper).map(testMascota, MascotaDTO.class);
    }

    @Test
    void getMascotaById_WithValidId_ShouldReturnMascota() {
        // Given
        when(mascotaRepository.findById(1L)).thenReturn(Optional.of(testMascota));

        // When
        MascotaDTO result = mascotaService.getMascotaById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getNombre()).isEqualTo("Max");
        verify(mascotaRepository).findById(1L);
        verify(modelMapper).map(testMascota, MascotaDTO.class);
    }

    @Test
    void getMascotaById_WithInvalidId_ShouldThrowException() {
        // Given
        when(mascotaRepository.findById(999L)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> mascotaService.getMascotaById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Mascota no encontrada con id: 999");
    }

    @Test
    void createMascota_WithValidData_ShouldReturnCreatedMascota() {
        // Given
        when(duenioRepository.findById(1L)).thenReturn(Optional.of(testDuenio));
        when(mascotaRepository.save(any(Mascota.class))).thenReturn(testMascota);

        // When
        MascotaDTO result = mascotaService.createMascota(testMascotaDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getNombre()).isEqualTo("Max");
        verify(duenioRepository).findById(1L);
        verify(mascotaRepository).save(any(Mascota.class));
        verify(modelMapper).map(testMascota, MascotaDTO.class);
    }

    @Test
    void createMascota_WithInvalidDuenioId_ShouldThrowException() {
        // Given
        when(duenioRepository.findById(999L)).thenReturn(Optional.empty());
        testMascotaDTO.setDuenioId(999L);

        // When/Then
        assertThatThrownBy(() -> mascotaService.createMascota(testMascotaDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Dueño no encontrado con id: 999");
        
        verify(duenioRepository).findById(999L);
        verify(mascotaRepository, never()).save(any(Mascota.class));
    }

    @Test
    void updateMascota_WithValidData_ShouldReturnUpdatedMascota() {
        // Given
        when(mascotaRepository.findById(1L)).thenReturn(Optional.of(testMascota));
        when(duenioRepository.findById(1L)).thenReturn(Optional.of(testDuenio));
        when(mascotaRepository.save(any(Mascota.class))).thenReturn(testMascota);

        // Update some fields
        testMascotaDTO.setNombre("Max Updated");
        testMascotaDTO.setEdad(4);
        testMascota.setNombre("Max Updated");
        testMascota.setEdad(4);

        // When
        MascotaDTO result = mascotaService.updateMascota(1L, testMascotaDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getNombre()).isEqualTo("Max Updated");
        assertThat(result.getEdad()).isEqualTo(4);
        verify(mascotaRepository).findById(1L);
        verify(duenioRepository).findById(1L);
        verify(mascotaRepository).save(any(Mascota.class));
    }

    @Test
    void updateMascota_WithInvalidId_ShouldThrowException() {
        // Given
        when(mascotaRepository.findById(999L)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> mascotaService.updateMascota(999L, testMascotaDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Mascota no encontrada con id: 999");
        
        verify(mascotaRepository).findById(999L);
        verify(mascotaRepository, never()).save(any(Mascota.class));
    }

    @Test
    void deleteMascota_WithValidId_ShouldDeleteMascota() {
        // Given
        when(mascotaRepository.existsById(1L)).thenReturn(true);
        doNothing().when(mascotaRepository).deleteById(1L);

        // When
        mascotaService.deleteMascota(1L);

        // Then
        verify(mascotaRepository).existsById(1L);
        verify(mascotaRepository).deleteById(1L);
    }

    @Test
    void deleteMascota_WithInvalidId_ShouldThrowException() {
        // Given
        when(mascotaRepository.existsById(999L)).thenReturn(false);

        // When/Then
        assertThatThrownBy(() -> mascotaService.deleteMascota(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Mascota no encontrada con id: 999");
        
        verify(mascotaRepository).existsById(999L);
        verify(mascotaRepository, never()).deleteById(anyLong());
    }

    @Test
    void getMascotasByDuenioId_ShouldReturnMascotas() {
        // Given
        List<Mascota> mascotas = Arrays.asList(testMascota);
        when(duenioRepository.existsById(1L)).thenReturn(true);
        when(mascotaRepository.findByDuenioId(1L)).thenReturn(mascotas);

        // When
        List<MascotaDTO> result = mascotaService.getMascotasByDuenioId(1L);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDuenioId()).isEqualTo(1L);
        verify(duenioRepository).existsById(1L);
        verify(mascotaRepository).findByDuenioId(1L);
        verify(modelMapper).map(testMascota, MascotaDTO.class);
    }

    @Test
    void getMascotasByDuenioId_WithInvalidDuenioId_ShouldThrowException() {
        // Given
        when(duenioRepository.existsById(999L)).thenReturn(false);

        // When/Then
        assertThatThrownBy(() -> mascotaService.getMascotasByDuenioId(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Dueño no encontrado con id: 999");
        
        verify(duenioRepository).existsById(999L);
        verify(mascotaRepository, never()).findByDuenioId(anyLong());
    }
}
