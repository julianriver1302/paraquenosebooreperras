package com.codiPlayCo.controller;

import java.util.HashMap;

import java.util.List;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;

import com.codiPlayCo.model.Curso;

import com.codiPlayCo.model.Foro;

import com.codiPlayCo.model.Usuario;

import com.codiPlayCo.repository.ForoRepository;

import com.codiPlayCo.service.ICursoService;

import com.codiPlayCo.service.IUsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller

@RequestMapping("/PanelControlUsuario/foros")

public class ForoController {

	@Autowired

	private ForoRepository foroRepository;

	@Autowired

	private IUsuarioService usuarioService;

	@Autowired

	private ICursoService cursoService;

	@GetMapping

	public String listarForosPorCurso(Model model, HttpSession session) {

		Integer idUsuario = (Integer) session.getAttribute("idUsuario");

		if (idUsuario == null) {

			return "redirect:/iniciosesion";

		}

		Usuario usuario = usuarioService.findById(idUsuario).orElse(null);

		if (usuario == null) {

			return "redirect:/iniciosesion";

		}

		// Cursos en los que está inscrito el estudiante según la relación en BD

		// (pagos / asociación usuario-curso), usando el servicio de cursos

		List<Curso> cursos = cursoService.findByUsuarioId(idUsuario);

		// Ajuste temporal: si aún no hay relación de inscripción en BD pero sabemos

		// que el estudiante con ID 3 debe ver el curso con ID 5, lo añadimos

		// solo para que pueda ver sus foros mientras se implementa el flujo de pago.

		if ((cursos == null || cursos.isEmpty()) && idUsuario != null && idUsuario == 3) {

			java.util.Optional<Curso> cursoOpt = cursoService.findById(5);

			if (cursoOpt.isPresent()) {

				cursos = new java.util.ArrayList<>();

				cursos.add(cursoOpt.get());

			}

		}

		Map<Integer, List<Foro>> forosPorCurso = new HashMap<>();

		for (Curso curso : cursos) {

			List<Foro> forosCurso = foroRepository.findByCursoId(curso.getId());

			forosPorCurso.put(curso.getId(), forosCurso);

		}

		model.addAttribute("usuario", usuario);

		model.addAttribute("cursos", cursos);

		model.addAttribute("forosPorCurso", forosPorCurso);

		return "PanelControlUsuario/foros";

	}

}