package com.mpaz.panol.service;

import com.mpaz.panol.entity.Solicitud;
import com.mpaz.panol.repository.SolicitudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class SolicitudService {

    private final SolicitudRepository solicitudRepository;

    public SolicitudService(SolicitudRepository solicitudRepository) {
        this.solicitudRepository = solicitudRepository;
    }

    @Transactional(readOnly = true)
    public List<Solicitud> listarTodas() {
        return solicitudRepository.findAllByOrderByIdDesc();
    }

    @Transactional(readOnly = true)
    public List<Solicitud> listarPorDocente(String email) {
        return solicitudRepository.findByDocenteEmailOrderByIdDesc(email);
    }

    @Transactional(readOnly = true)
    public Optional<Solicitud> buscarPorId(Long id) {
        return solicitudRepository.findById(id);
    }

    public Solicitud crearSolicitud(Solicitud solicitud) {
        // Regla Crítica de Negocio: Mínimo 3 horas de anticipación
        if (solicitud.getHorasAnticipacion() != null && solicitud.getHorasAnticipacion() < 3) {
            solicitud.setCumpleAnticipacion(false);
            throw new IllegalArgumentException("La solicitud debe realizarse con un mínimo de 3 horas de anticipación");
        }
        solicitud.setCumpleAnticipacion(true);

        // Generar código de ticket si viene vacío
        if (solicitud.getCodigoTicket() == null || solicitud.getCodigoTicket().trim().isEmpty()) {
            int randomNum = 100 + new Random().nextInt(900);
            solicitud.setCodigoTicket("MP-0" + randomNum);
        }

        // Estado inicial por defecto
        if (solicitud.getEstadoTicket() == null || solicitud.getEstadoTicket().trim().isEmpty()) {
            solicitud.setEstadoTicket("Pendiente");
        }

        return solicitudRepository.save(solicitud);
    }

    public Solicitud cambiarEstado(Long id, String nuevoEstado, String justificacion) {
        Solicitud solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada con id: " + id));

        // Regla Crítica de Negocio: Si se rechaza, la justificación es obligatoria
        if ("Rechazada".equalsIgnoreCase(nuevoEstado)) {
            if (justificacion == null || justificacion.trim().isEmpty()) {
                throw new IllegalArgumentException("La justificación es obligatoria para rechazar una solicitud");
            }
            solicitud.setJustificacionRechazo(justificacion.trim());
        }

        solicitud.setEstadoTicket(nuevoEstado);
        return solicitudRepository.save(solicitud);
    }
}
