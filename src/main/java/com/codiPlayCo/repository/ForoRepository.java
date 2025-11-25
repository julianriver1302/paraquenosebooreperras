package com.codiPlayCo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codiPlayCo.model.Foro;

@Repository
public interface ForoRepository extends JpaRepository<Foro, Integer> {

	List<Foro> findByCursoId(Integer cursoId);
}
