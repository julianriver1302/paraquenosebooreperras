package com.codiPlayCo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codiPlayCo.model.ForoRespuesta;
import com.codiPlayCo.model.ForoTema;

public interface IForoRespuestaRepository extends JpaRepository<ForoRespuesta, Integer> {

    List<ForoRespuesta> findByTemaOrderByFechaCreacionAsc(ForoTema tema);

}
