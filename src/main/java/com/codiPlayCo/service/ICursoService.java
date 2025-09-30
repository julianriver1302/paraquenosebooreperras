package com.codiPlayCo.service;

import java.util.List;
import java.util.Optional;
import com.codiPlayCo.model.Curso;

public interface ICursoService {
    Curso save(Curso curso);
    Optional<Curso> get(Integer id);
    Curso update(Curso curso);
    void delete(Integer id);
    List<Curso> findAll();
}