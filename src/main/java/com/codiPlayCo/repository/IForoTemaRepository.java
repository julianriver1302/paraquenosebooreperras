package com.codiPlayCo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codiPlayCo.model.ForoTema;

public interface IForoTemaRepository extends JpaRepository<ForoTema, Integer> {

    List<ForoTema> findByModuloOrderByFechaCreacionDesc(String modulo);

}
