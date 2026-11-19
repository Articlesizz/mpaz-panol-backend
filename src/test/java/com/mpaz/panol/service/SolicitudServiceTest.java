package com.mpaz.panol.service;

import com.mpaz.panol.entity.Solicitud;
import com.mpaz.panol.repository.SolicitudRepository;
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
class SolicitudServiceTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    @InjectMocks
    private SolicitudService solicitudService;

    private Solicitud solicitudDemo;

    @BeforeEach
    void setUp() {
        solicitudDemo = new Solicitud(
                1L,
                "MP-0268",
                "María López",
                "m.lopez@escuela.cl",
                "Laboratorio de Computación 1",
                "21 oct. 2026",
                "14:00",
                "16:00",
                "1 × Kit LEGO WeDo 2.0",
                5,
                true,
                "Pendiente",
                null,
                "21 oct. 2026 - 09:00"
        );
    }

    @Test
    @DisplayName("Debe listar todas las solicitudes ordenadas por ID descendente")
    void listarTodas_retornaListaDescendente() {
        when(solicitudRepository.findAllByOrderByIdDesc()).thenReturn(Arrays.asList(solicitudDemo));

        List<Solicitud> resultado = solicitudService.listarTodas();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("MP-0268", resultado.get(0).getCodigoTicket());
        verify(solicitudRepository, times(1)).findAllByOrderByIdDesc();
    }

    @Test
    @DisplayName("Regla de negocio: Debe lanzar excepción si la anticipación es menor a 3 horas")
    void crearSolicitud_conMenosDe3HorasAnticipacion_lanzaExcepcion() {
        solicitudDemo.setHorasAnticipacion(2); // Menos del mínimo de 3 horas

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            solicitudService.crearSolicitud(solicitudDemo);
        });

        assertEquals("La solicitud debe realizarse con un mínimo de 3 horas de anticipación", ex.getMessage());
        assertFalse(solicitudDemo.getCumpleAnticipacion());
        verify(solicitudRepository, never()).save(any());
    }

    @Test
    @DisplayName("Regla de negocio: Debe crear solicitud exitosamente con >= 3 horas de anticipación")
    void crearSolicitud_conAnticipacionValida_persisteExitosamente() {
        solicitudDemo.setHorasAnticipacion(4);
        when(solicitudRepository.save(any(Solicitud.class))).thenReturn(solicitudDemo);

        Solicitud creada = solicitudService.crearSolicitud(solicitudDemo);

        assertNotNull(creada);
        assertTrue(creada.getCumpleAnticipacion());
        assertEquals("Pendiente", creada.getEstadoTicket());
        verify(solicitudRepository, times(1)).save(solicitudDemo);
    }

    @Test
    @DisplayName("Regla de negocio: Al rechazar una solicitud, la justificación es obligatoria")
    void cambiarEstado_aRechazadaSinJustificacion_lanzaExcepcion() {
        when(solicitudRepository.findById(1L)).thenReturn(Optional.of(solicitudDemo));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            solicitudService.cambiarEstado(1L, "Rechazada", "");
        });

        assertEquals("La justificación es obligatoria para rechazar una solicitud", ex.getMessage());
        verify(solicitudRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe rechazar solicitud correctamente cuando se incluye justificación")
    void cambiarEstado_aRechazadaConJustificacion_actualizaEstado() {
        when(solicitudRepository.findById(1L)).thenReturn(Optional.of(solicitudDemo));
        when(solicitudRepository.save(any(Solicitud.class))).thenAnswer(i -> i.getArgument(0));

        Solicitud rechazada = solicitudService.cambiarEstado(
                1L,
                "Rechazada",
                "Equipos en mantenimiento de software."
        );

        assertNotNull(rechazada);
        assertEquals("Rechazada", rechazada.getEstadoTicket());
        assertEquals("Equipos en mantenimiento de software.", rechazada.getJustificacionRechazo());
        verify(solicitudRepository, times(1)).save(any(Solicitud.class));
    }

    @Test
    @DisplayName("Debe aprobar solicitud y actualizar estado a Aceptada")
    void cambiarEstado_aAprobada_actualizaEstadoTicket() {
        when(solicitudRepository.findById(1L)).thenReturn(Optional.of(solicitudDemo));
        when(solicitudRepository.save(any(Solicitud.class))).thenAnswer(i -> i.getArgument(0));

        Solicitud aprobada = solicitudService.cambiarEstado(1L, "Aceptada", null);

        assertNotNull(aprobada);
        assertEquals("Aceptada", aprobada.getEstadoTicket());
        verify(solicitudRepository, times(1)).save(any(Solicitud.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción si la solicitud no existe al cambiar de estado")
    void cambiarEstado_idInexistente_lanzaExcepcion() {
        when(solicitudRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            solicitudService.cambiarEstado(999L, "Aceptada", null);
        });

        assertTrue(ex.getMessage().contains("Solicitud no encontrada con id: 999"));
        verify(solicitudRepository, never()).save(any());
    }
}
