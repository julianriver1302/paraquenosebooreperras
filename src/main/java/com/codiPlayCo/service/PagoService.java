package com.codiPlayCo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codiPlayCo.model.Curso;
import com.codiPlayCo.model.Pago;
import com.codiPlayCo.model.Usuario;

import com.codiPlayCo.repository.ICursoRepository;
import com.codiPlayCo.repository.IUsuarioRepository;
import com.codiPlayCo.repository.PagoRepository;

@Service
public class PagoService {

	@Autowired
	private PagoRepository pagoRepository;

	@Autowired
	private IUsuarioRepository usuarioRepository;

	@Autowired
	private ICursoRepository cursoRepository;

	/**
	 * Guarda el pago en la base de datos
	 */
	public Pago guardar(Pago pago) {
		return pagoRepository.save(pago);
	}

	/**
	 * Procesa el pago, valida usuario y curso y realiza la inscripción.
	 */
	public Pago procesarPago(Pago pago) {

		// 1) Guardar el pago
		Pago pagoGuardado = pagoRepository.save(pago);

		// 2) Solo inscribir si está aprobado
		if (!"aprobado".equalsIgnoreCase(pago.getEstado())) {
			return pagoGuardado;
		}

		// 3) Obtener usuario
		Usuario usuario = usuarioRepository.findById(pago.getUsuarioId())
				.orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + pago.getUsuarioId()));

		// 4) Obtener curso
		Curso curso = cursoRepository.findById(pago.getCursoId())
				.orElseThrow(() -> new RuntimeException("Curso no encontrado: " + pago.getCursoId()));

		// 5) Verificar si ya está inscrito
		if (!curso.getUsuarios().contains(usuario)) {
			curso.getUsuarios().add(usuario);
			cursoRepository.save(curso);
		}

		return pagoGuardado;
	}
}
