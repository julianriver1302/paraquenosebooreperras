package com.codiPlayCo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.codiPlayCo.model.Curso;

@Repository
public interface ICursoRepository extends JpaRepository<Curso, Integer> {
}
