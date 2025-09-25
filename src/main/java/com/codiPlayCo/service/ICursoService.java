package com.codiPlayCo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.codiPlayCo.model.Curso;

@Service
public interface ICursoService {


	public Curso save(Curso curso);

	public Optional<Curso> get(Integer id);

	public Curso update(Curso curso);

	public void delete(Integer id);
	
	public List<Curso> findAll();

}
