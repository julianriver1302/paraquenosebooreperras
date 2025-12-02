package com.codiPlayCo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.codiPlayCo.model.ActividadesEstudiantes;

@Repository
public interface ActividadesEstudiantesRepository extends JpaRepository<ActividadesEstudiantes, Integer> {

    // Entregas de un estudiante para actividades de un curso concreto
    @Query("SELECT ae FROM ActividadesEstudiantes ae WHERE ae.idEstudiante = :idEstudiante AND ae.actividades.Curso = :cursoId")
    List<ActividadesEstudiantes> findByEstudianteAndCurso(@Param("idEstudiante") Integer idEstudiante,
                                                          @Param("cursoId") Integer cursoId);

    // Contar actividades completadas por estudiante y curso (estado = 'COMPLETADO')
    @Query("SELECT COUNT(ae) FROM ActividadesEstudiantes ae WHERE ae.idEstudiante = :idEstudiante AND ae.actividades.Curso = :cursoId AND UPPER(ae.estado) = 'COMPLETADO'")
    Long countCompletadasByEstudianteAndCurso(@Param("idEstudiante") Integer idEstudiante,
                                              @Param("cursoId") Integer cursoId);

    // Entregas asociadas a una actividad concreta
    @Query("SELECT ae FROM ActividadesEstudiantes ae WHERE ae.actividades.id = :actividadId")
    List<ActividadesEstudiantes> findByActividadId(@Param("actividadId") Integer actividadId);

    // Todas las entregas de un estudiante (para panel del niño)
    @Query("SELECT ae FROM ActividadesEstudiantes ae WHERE ae.idEstudiante = :idEstudiante")
    List<ActividadesEstudiantes> findByIdEstudiante(@Param("idEstudiante") Integer idEstudiante);

    // Verificar si un estudiante tiene una entrega COMPLETADA para una actividad concreta
    @Query("SELECT COUNT(ae) > 0 FROM ActividadesEstudiantes ae WHERE ae.idEstudiante = :idEstudiante AND ae.actividades.id = :actividadId AND UPPER(ae.estado) = 'COMPLETADO'")
    boolean existsCompletadaByEstudianteAndActividad(@Param("idEstudiante") Integer idEstudiante,
                                                   @Param("actividadId") Integer actividadId);
}
