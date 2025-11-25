package com.codiPlayCo.service;

import java.util.List;
import java.util.Optional;
import com.codiPlayCo.model.Curso;

public interface ICursoService {

    Curso save(Curso curso);
    
    List<Curso> findCursosActivos();
    
    Optional<Curso> get(Integer id);
    
    Curso update(Curso curso);
    
    void delete(Integer id);
    
    List<Curso> findAll();

    // 🔥 Nuevo método para obtener cursos por usuario logueado
    List<Curso> findByUsuarioId(Integer usuarioId);

    // Cursos asignados a un docente
    List<Curso> findByDocenteId(Integer docenteId);
}
