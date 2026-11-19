package com.mpaz.panol.repository;

import com.mpaz.panol.entity.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {
    List<Solicitud> findAllByOrderByIdDesc();
    List<Solicitud> findByDocenteEmailOrderByIdDesc(String docenteEmail);
    Optional<Solicitud> findByCodigoTicket(String codigoTicket);
}
