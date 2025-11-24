package com.codiPlayCo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/CursoTrilogia1")
public class CursoTrilogia1Controller {

	@GetMapping("/")
	public String index() {
		return "CursoTrilogia1/index";
	}

	@GetMapping("/index")
	public String cursoIndex() {
		return "/CursoTrilogia1/index";
	}

	@GetMapping("/internet")
	public String internet() {
		return "/CursoTrilogia1/internet";
	}

	@GetMapping("/paginas-web")
	public String paginasWeb() {

		return "/CursoTrilogia1/paginas-web";
	}

	@GetMapping("/pagina")
	public String pagina() {
		return "/CursoTrilogia1/pagina";
	}

	@GetMapping("/home")
	public String home() {
		return "redirect:/CursoTrilogia1/";
	}

	@GetMapping("/inicio")
	public String inicio() {
		return "redirect:/PanelControlUsuario/inicio";
	}
}