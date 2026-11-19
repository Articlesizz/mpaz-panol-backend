package com.mpaz.panol.service;

import com.mpaz.panol.entity.Equipo;
import com.mpaz.panol.repository.EquipoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EquipoService {

    private final EquipoRepository equipoRepository;

    public EquipoService(EquipoRepository equipoRepository) {
        this.equipoRepository = equipoRepository;
    }

    @Transactional(readOnly = true)
    public List<Equipo> listarTodos() {
        return equipoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Equipo> buscarPorId(Long id) {
        return equipoRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Equipo> buscarPorCategoria(String categoria) {
        return equipoRepository.findByCategoriaIgnoreCase(categoria);
    }

    public Equipo guardar(Equipo equipo) {
        if (equipo.getCodigo() == null || equipo.getCodigo().trim().isEmpty()) {
            throw new IllegalArgumentException("El código del equipo es obligatorio");
        }
        if (equipo.getStockTotal() < 0) {
            throw new IllegalArgumentException("El stock total no puede ser negativo");
        }
        if (equipo.getStockDisponible() == null || equipo.getStockDisponible() > equipo.getStockTotal()) {
            equipo.setStockDisponible(equipo.getStockTotal());
        }
        return equipoRepository.save(equipo);
    }

    public Equipo actualizar(Long id, Equipo equipoActualizado) {
        return equipoRepository.findById(id).map(equipoExistente -> {
            equipoExistente.setNombre(equipoActualizado.getNombre());
            equipoExistente.setCategoria(equipoActualizado.getCategoria());
            equipoExistente.setStockTotal(equipoActualizado.getStockTotal());
            equipoExistente.setStockDisponible(equipoActualizado.getStockDisponible());
            equipoExistente.setEstado(equipoActualizado.getEstado());
            equipoExistente.setUbicacionFisica(equipoActualizado.getUbicacionFisica());
            return equipoRepository.save(equipoExistente);
        }).orElseThrow(() -> new RuntimeException("Equipo no encontrado con id: " + id));
    }

    public void eliminar(Long id) {
        if (!equipoRepository.existsById(id)) {
            throw new RuntimeException("No existe equipo con id: " + id);
        }
        equipoRepository.deleteById(id);
    }
}
