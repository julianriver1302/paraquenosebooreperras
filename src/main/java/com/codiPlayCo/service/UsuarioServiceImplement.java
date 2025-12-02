package com.codiPlayCo.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.codiPlayCo.model.Usuario;
import com.codiPlayCo.model.Curso;
import com.codiPlayCo.repository.IUsuarioRepository;
import com.codiPlayCo.service.ICursoService;

@Service
public class UsuarioServiceImplement implements IUsuarioService {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private ICursoService cursoService;

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
    
    @Override
    public List<Usuario> findByCursoComprado(Integer cursoId) {
        return usuarioRepository.findByCursoComprado(cursoId);
    }

    @Override
    public void inscribirEnCurso(Integer idEstudiante, Integer idCurso) {
        Usuario usuario = usuarioRepository.findById(idEstudiante).orElse(null);
        if (usuario == null) {
            return;
        }

        Curso curso = cursoService.get(idCurso).orElse(null);
        if (curso == null) {
            return;
        }

        if (usuario.getCursosComprados() == null) {
            usuario.setCursosComprados(new java.util.ArrayList<>());
        }

        if (!usuario.getCursosComprados().contains(curso)) {
            usuario.getCursosComprados().add(curso);
            usuarioRepository.save(usuario);
        }
    }
}