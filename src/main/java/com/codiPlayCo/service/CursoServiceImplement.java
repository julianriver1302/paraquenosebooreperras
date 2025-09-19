package com.codiPlayCo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codiPlayCo.model.Curso;
import com.codiPlayCo.repository.ICursoRepository;
@Service
public class CursoServiceImplement {

	@Autowired
	private ICursoRepository cursoRepository;

	public List<Curso> findAll() {
		return cursoRepository.findAll();
	}


}
