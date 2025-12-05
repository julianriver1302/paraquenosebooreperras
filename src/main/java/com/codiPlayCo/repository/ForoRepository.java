package com.codiPlayCo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.codiPlayCo.model.Foro;

@Repository
public interface ForoRepository extends JpaRepository<Foro, Integer> {

    List<Foro> findByCursoId(Integer cursoId);
    List<Foro> findByDocenteId(Integer docenteId);
    
    @Query("SELECT f FROM Foro f WHERE f.curso.id = :cursoId ORDER BY f.fechaCreacion DESC")
    List<Foro> findByCursoIdOrderByFechaCreacionDesc(@Param("cursoId") Integer cursoId);
    
    @Query("SELECT f FROM Foro f ORDER BY f.fechaCreacion DESC")
    List<Foro> findAllByOrderByFechaCreacionDesc();
}
