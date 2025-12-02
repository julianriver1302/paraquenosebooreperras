package com.codiPlayCo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.codiPlayCo.model.Curso;
import com.codiPlayCo.service.ICursoService;

import org.springframework.ui.Model;

@Controller
@RequestMapping("/pago")
public class RegistroPagoController {

	@Autowired
	private ICursoService cursoService;

	@GetMapping("/{idCurso}")
	public String registrarPago(@PathVariable Integer idCurso, Model model) {

	    Optional<Curso> curso = cursoService.findById(idCurso);

	    if (curso.isEmpty()) {
	        return "redirect:/error";
	    }

	    model.addAttribute("curso", curso.get());
	    return "registro_pago";
	}
}


