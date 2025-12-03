package com.codiPlayCo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.codiPlayCo.model.Actividades;

@Repository
public interface ActividadesRepository extends JpaRepository<Actividades, Integer> {

    // Actividades asociadas a un curso (campo 'Curso' es el id del curso en la entidad)
    @Query("SELECT a FROM Actividades a WHERE a.Curso = :cursoId")
    List<Actividades> findByCurso(@Param("cursoId") Integer cursoId);

    // Actividades de un curso y un módulo concreto ordenadas por número de lección
    @Query("SELECT a FROM Actividades a WHERE a.Curso = :cursoId AND a.modulo = :modulo ORDER BY a.leccion ASC")
    List<Actividades> findByCursoAndModulo(@Param("cursoId") Integer cursoId, @Param("modulo") Integer modulo);

    // Módulos distintos que tiene un curso
    @Query("SELECT DISTINCT a.modulo FROM Actividades a WHERE a.Curso = :cursoId ORDER BY a.modulo ASC")
    List<Integer> findModulosByCurso(@Param("cursoId") Integer cursoId);
}
