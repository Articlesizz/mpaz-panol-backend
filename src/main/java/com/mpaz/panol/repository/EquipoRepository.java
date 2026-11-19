package com.mpaz.panol.repository;

import com.mpaz.panol.entity.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipoRepository extends JpaRepository<Equipo, Long> {
    Optional<Equipo> findByCodigo(String codigo);
    List<Equipo> findByCategoriaIgnoreCase(String categoria);
}
