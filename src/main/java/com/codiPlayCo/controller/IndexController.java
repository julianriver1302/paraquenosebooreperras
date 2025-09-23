package com.codiPlayCo.controller;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class IndexController {

	
	@GetMapping("/quienessomos")
	public String quienessomos() {
		return "quienessomos";
	}

	@GetMapping("/preguntasfrecuentes")
	public String preguntasfrecuentes() {
		return "preguntasfrecuentes";
	}

	@GetMapping("/iniciosesion")
	public String iniciosesion() {
		return "iniciosesion";
	}
	
	@GetMapping("/registroclasegratis")
	public String registroclasegratis() {
		return "registroclasegratis";
	}
	
	@GetMapping("/registropago")
	public String registropago() {
		return "registropago";
	}
}
