package com.codiPlayCo.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.codiPlayCo.model.Usuario;
import com.codiPlayCo.repository.IUsuarioRepository;

@Service
public class UsuarioServiceImplement implements IUsuarioService {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Override
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    public void delete(Integer id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public Optional<Usuario> findById(Integer id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    @Override
    public List<Usuario> findByRol(String rolNombre) {
        List<Usuario> todos = usuarioRepository.findAll();
        return todos.stream()
                .filter(usuario -> usuario.getRol() != null)
                .filter(usuario -> rolNombre.equalsIgnoreCase(usuario.getRol().getNombre()))
                .toList();
    }

    @Override
    public List<Usuario> findDocentesActivos() {
        List<Usuario> todos = usuarioRepository.findAll();
        return todos.stream()
                .filter(usuario -> usuario.getRol() != null)
                .filter(usuario -> "docente".equals(usuario.getRol().getNombre()))
                .filter(usuario -> "Sí".equals(usuario.getActivo()))
                .toList();
    }
    
    
    
}