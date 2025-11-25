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
}
