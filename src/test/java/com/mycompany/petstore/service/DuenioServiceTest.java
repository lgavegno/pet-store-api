package com.mycompany.petstore.service;

import com.mycompany.petstore.dto.DuenioDTO;
import com.mycompany.petstore.exceptions.ResourceNotFoundException;
import com.mycompany.petstore.model.Duenio;
import com.mycompany.petstore.repository.DuenioRepository;
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
class DuenioServiceTest {

    @Mock
    private DuenioRepository duenioRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private DuenioService duenioService;

    private Duenio testDuenio;
    private DuenioDTO testDuenioDTO;

    @BeforeEach
    void setUp() {
        // Setup test data
        testDuenio = new Duenio();
        testDuenio.setId(1L);
        testDuenio.setNombre("Juan");
        testDuenio.setApellido("Perez");
        testDuenio.setTelefono("123456789");
        testDuenio.setDireccion("Calle Falsa 123");

        testDuenioDTO = new DuenioDTO();
        testDuenioDTO.setId(1L);
        testDuenioDTO.setNombre("Juan");
        testDuenioDTO.setApellido("Perez");
        testDuenioDTO.setTelefono("123456789");
        testDuenioDTO.setDireccion("Calle Falsa 123");

        // Configure model mapper
        when(modelMapper.map(any(DuenioDTO.class), eq(Duenio.class))).thenReturn(testDuenio);
        when(modelMapper.map(any(Duenio.class), eq(DuenioDTO.class))).thenReturn(testDuenioDTO);
    }

    @Test
    void getAllDuenios_ShouldReturnAllDuenios() {
        // Given
        List<Duenio> duenios = Arrays.asList(testDuenio);
        when(duenioRepository.findAll()).thenReturn(duenios);

        // When
        List<DuenioDTO> result = duenioService.getAllDuenios();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNombre()).isEqualTo("Juan");
        verify(duenioRepository).findAll();
        verify(modelMapper).map(testDuenio, DuenioDTO.class);
    }

    @Test
    void getDuenioById_WithValidId_ShouldReturnDuenio() {
        // Given
        when(duenioRepository.findById(1L)).thenReturn(Optional.of(testDuenio));

        // When
        DuenioDTO result = duenioService.getDuenioById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getNombre()).isEqualTo("Juan");
        verify(duenioRepository).findById(1L);
        verify(modelMapper).map(testDuenio, DuenioDTO.class);
    }

    @Test
    void getDuenioById_WithInvalidId_ShouldThrowException() {
        // Given
        when(duenioRepository.findById(999L)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> duenioService.getDuenioById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Dueño no encontrado con id: 999");
    }

    @Test
    void createDuenio_WithValidData_ShouldReturnCreatedDuenio() {
        // Given
        when(duenioRepository.save(any(Duenio.class))).thenReturn(testDuenio);

        // When
        DuenioDTO result = duenioService.createDuenio(testDuenioDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getNombre()).isEqualTo("Juan");
        verify(duenioRepository).save(any(Duenio.class));
        verify(modelMapper).map(testDuenio, DuenioDTO.class);
    }

    @Test
    void updateDuenio_WithValidData_ShouldReturnUpdatedDuenio() {
        // Given
        when(duenioRepository.findById(1L)).thenReturn(Optional.of(testDuenio));
        when(duenioRepository.save(any(Duenio.class))).thenReturn(testDuenio);

        // Update some fields
        testDuenioDTO.setTelefono("987654321");
        testDuenioDTO.setDireccion("Avenida Siempre Viva 742");
        testDuenio.setTelefono("987654321");
        testDuenio.setDireccion("Avenida Siempre Viva 742");

        // When
        DuenioDTO result = duenioService.updateDuenio(1L, testDuenioDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTelefono()).isEqualTo("987654321");
        assertThat(result.getDireccion()).isEqualTo("Avenida Siempre Viva 742");
        verify(duenioRepository).findById(1L);
        verify(duenioRepository).save(any(Duenio.class));
    }

    @Test
    void updateDuenio_WithInvalidId_ShouldThrowException() {
        // Given
        when(duenioRepository.findById(999L)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> duenioService.updateDuenio(999L, testDuenioDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Dueño no encontrado con id: 999");
        
        verify(duenioRepository).findById(999L);
        verify(duenioRepository, never()).save(any(Duenio.class));
    }

    @Test
    void deleteDuenio_WithValidId_ShouldDeleteDuenio() {
        // Given
        when(duenioRepository.existsById(1L)).thenReturn(true);
        doNothing().when(duenioRepository).deleteById(1L);

        // When
        duenioService.deleteDuenio(1L);

        // Then
        verify(duenioRepository).existsById(1L);
        verify(duenioRepository).deleteById(1L);
    }

    @Test
    void deleteDuenio_WithInvalidId_ShouldThrowException() {
        // Given
        when(duenioRepository.existsById(999L)).thenReturn(false);

        // When/Then
        assertThatThrownBy(() -> duenioService.deleteDuenio(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Dueño no encontrado con id: 999");
        
        verify(duenioRepository).existsById(999L);
        verify(duenioRepository, never()).deleteById(anyLong());
    }

    @Test
    void searchDuenios_WithMatchingCriteria_ShouldReturnMatchingDuenios() {
        // Given
        String searchTerm = "Perez";
        List<Duenio> duenios = Arrays.asList(testDuenio);
        when(duenioRepository.findByNombreContainingOrApellidoContainingOrTelefonoContainingOrDireccionContaining(
                searchTerm, searchTerm, searchTerm, searchTerm)).thenReturn(duenios);

        // When
        List<DuenioDTO> result = duenioService.searchDuenios(searchTerm);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getApellido()).isEqualTo("Perez");
        verify(duenioRepository).findByNombreContainingOrApellidoContainingOrTelefonoContainingOrDireccionContaining(
                searchTerm, searchTerm, searchTerm, searchTerm);
        verify(modelMapper).map(testDuenio, DuenioDTO.class);
    }

    @Test
    void existsById_WithExistingId_ShouldReturnTrue() {
        // Given
        when(duenioRepository.existsById(1L)).thenReturn(true);

        // When
        boolean exists = duenioService.existsById(1L);

        // Then
        assertThat(exists).isTrue();
        verify(duenioRepository).existsById(1L);
    }

    @Test
    void existsById_WithNonExistingId_ShouldReturnFalse() {
        // Given
        when(duenioRepository.existsById(999L)).thenReturn(false);

        // When
        boolean exists = duenioService.existsById(999L);

        // Then
        assertThat(exists).isFalse();
        verify(duenioRepository).existsById(999L);
    }
}
