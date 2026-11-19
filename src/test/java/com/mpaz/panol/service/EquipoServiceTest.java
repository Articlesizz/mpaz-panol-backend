package com.mpaz.panol.service;

import com.mpaz.panol.entity.Equipo;
import com.mpaz.panol.repository.EquipoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EquipoServiceTest {

    @Mock
    private EquipoRepository equipoRepository;

    @InjectMocks
    private EquipoService equipoService;

    private Equipo equipoDemo;

    @BeforeEach
    void setUp() {
        equipoDemo = new Equipo(
                1L,
                "NOT-015",
                "Notebook Lenovo V15",
                "Notebooks",
                12,
                8,
                "Disponible",
                "Armario B-1"
        );
    }

    @Test
    @DisplayName("Debe listar todos los equipos correctamente")
    void listarTodos_retornaListaDeEquipos() {
        when(equipoRepository.findAll()).thenReturn(Arrays.asList(equipoDemo));

        List<Equipo> resultado = equipoService.listarTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("NOT-015", resultado.get(0).getCodigo());
        verify(equipoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe buscar equipo por ID cuando existe")
    void buscarPorId_cuandoExiste_retornaEquipo() {
        when(equipoRepository.findById(1L)).thenReturn(Optional.of(equipoDemo));

        Optional<Equipo> resultado = equipoService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Notebook Lenovo V15", resultado.get().getNombre());
        verify(equipoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debe guardar un equipo válido exitosamente")
    void guardar_equipoValido_persisteExitosamente() {
        when(equipoRepository.save(any(Equipo.class))).thenReturn(equipoDemo);

        Equipo guardado = equipoService.guardar(equipoDemo);

        assertNotNull(guardado);
        assertEquals("NOT-015", guardado.getCodigo());
        verify(equipoRepository, times(1)).save(equipoDemo);
    }

    @Test
    @DisplayName("Debe lanzar excepción si el equipo no tiene código")
    void guardar_sinCodigo_lanzaExcepcion() {
        equipoDemo.setCodigo("");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            equipoService.guardar(equipoDemo);
        });

        assertEquals("El código del equipo es obligatorio", ex.getMessage());
        verify(equipoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe ajustar stock disponible al total si viene mayor")
    void guardar_stockDisponibleMayorATotal_ajustaDisponible() {
        equipoDemo.setStockTotal(5);
        equipoDemo.setStockDisponible(10); // Inválido: más disponible que total

        when(equipoRepository.save(any(Equipo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Equipo resultado = equipoService.guardar(equipoDemo);

        assertEquals(5, resultado.getStockDisponible());
        verify(equipoRepository, times(1)).save(any(Equipo.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción al intentar eliminar un ID inexistente")
    void eliminar_idInexistente_lanzaExcepcion() {
        when(equipoRepository.existsById(99L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            equipoService.eliminar(99L);
        });

        assertTrue(ex.getMessage().contains("No existe equipo con id: 99"));
        verify(equipoRepository, never()).deleteById(any());
    }
}
