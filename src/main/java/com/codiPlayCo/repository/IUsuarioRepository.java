package com.codiPlayCo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.codiPlayCo.model.Usuario;
import java.util.Optional;

public interface IUsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByEmail(String email);
}
