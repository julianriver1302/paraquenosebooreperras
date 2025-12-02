package com.codiPlayCo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.codiPlayCo.model.Usuario;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario, Integer> {
    
    // Buscar usuario por email
    Optional<Usuario> findByEmail(String email);

    // Verificar si un email ya existe
    boolean existsByEmail(String email);
    
    // Buscar usuarios por nombre de rol (Ej: "Docente", "Estudiante")
    @Query("SELECT u FROM Usuario u JOIN u.rol r WHERE r.nombre = :rolNombre")
    List<Usuario> findByRolNombre(@Param("rolNombre") String rolNombre);

    // Usuarios inscritos/compradores de un curso concreto (relación ManyToMany cursosComprados)
    @Query("SELECT DISTINCT u FROM Usuario u JOIN u.cursosComprados c WHERE c.id = :cursoId")
    List<Usuario> findByCursoComprado(@Param("cursoId") Integer cursoId);
}
