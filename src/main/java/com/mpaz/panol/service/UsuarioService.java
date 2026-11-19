package com.mpaz.panol.service;

import com.mpaz.panol.entity.Usuario;
import com.mpaz.panol.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> autenticar(String email, String password) {
        return usuarioRepository.findByEmail(email)
                .filter(u -> u.getPassword().equals(password));
    }

    public Usuario registrar(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("El correo ya se encuentra registrado");
        }
        if (usuario.getRol() == null || usuario.getRol().trim().isEmpty()) {
            usuario.setRol("DOCENTE");
        }
        return usuarioRepository.save(usuario);
    }
}
