package com.codiPlayCo.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codiPlayCo.model.Usuario;

public interface IUsuarioRepository extends JpaRepository<Usuario, Integer> {
	Optional<Usuario> findByEmail(String email);

	@Query("SELECT u FROM Usuario u WHERE LOWER(u.email) = LOWER(:email)")
	Optional<Usuario> findByEmailIgnoreCase(@Param("email") String email);
//metodo para comparar sin importar mayusculas o minusculas
}
