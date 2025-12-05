package com.codiPlayCo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.codiPlayCo.model.Curso;

import java.util.List;

@Repository
public interface ICursoRepository extends JpaRepository<Curso, Integer> {

	// Obtener todos los cursos comprados o asociados a un usuario específico
	@Query("SELECT c FROM Curso c JOIN c.usuarios u WHERE u.id = :usuarioId")
	List<Curso> findByUsuarioId(@Param("usuarioId") Integer usuarioId);

	// Obtener cursos en los que el usuario está inscrito
	@Query("SELECT DISTINCT c FROM Curso c JOIN c.usuarios u WHERE u.id = :usuarioId")
	List<Curso> findCursosCompradosByUsuarioId(@Param("usuarioId") Integer usuarioId);

	// Obtener cursos asignados a un docente específico (por AsignacionDocente)
	@Query("SELECT DISTINCT c FROM Curso c LEFT JOIN FETCH c.usuarios WHERE c.asignacionDocente.usuario.id = :docenteId")
	List<Curso> findByDocenteId(@Param("docenteId") Integer docenteId);
	
	// Verificar si un usuario está inscrito en un curso específico
	@Query("SELECT COUNT(c) > 0 FROM Curso c JOIN c.usuarios u WHERE c.id = :cursoId AND u.id = :usuarioId")
	boolean existsByIdAndUsuariosId(@Param("cursoId") Integer cursoId, @Param("usuarioId") Integer usuarioId);

	

}
