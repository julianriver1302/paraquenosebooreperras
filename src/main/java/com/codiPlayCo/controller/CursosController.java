package com.codiPlayCo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.codiPlayCo.service.ICursoService;

@Controller
@RequestMapping("/cursos")
public class CursosController {

	private final ICursoService ICursoService;

	CursosController(ICursoService ICursoService) {
		this.ICursoService = ICursoService;
	}

	@GetMapping("/InfoCursos/html_java_css")
	public String cursoHtmlCssJs() {
		return "InfoCursos/html_java_css";
	}

	@GetMapping("/InfoCursos/trilogia02")
	public String cursoTrilogia02() {
		return "InfoCursos/trilogia02"; // templates/InfoCursos/trilogia2.0.html
	}

	@GetMapping("/InfoCursos/scratch")
	public String cursoScratch() {
		return "InfoCursos/scratch"; // templates/InfoCursos/scratch.html
	}

	@GetMapping("/InfoCursos/roblox")
	public String cursoRoblox() {
		return "InfoCursos/roblox"; // templates/InfoCursos/roblox.html
	}

	@GetMapping("/InfoCursos/python")
	public String cursoPython() {
		return "InfoCursos/python"; // templates/InfoCursos/python.html
	}

	@GetMapping("/InfoCursos/unity")
	public String cursoUnity() {
		return "InfoCursos/unity"; // templates/InfoCursos/unity.html
	}

	

	@GetMapping
	public String listarCursos(Model model) {
		model.addAttribute("cursos", ICursoService.findAll());
		return "cursos";
	}
}