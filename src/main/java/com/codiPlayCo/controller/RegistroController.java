package com.codiPlayCo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.codiPlayCo.model.Usuario;
import com.codiPlayCo.service.UsuarioServiceImplement;

@Controller
public class RegistroController {

	@Autowired
	private UsuarioServiceImplement usuarioServiceImplement;

	@GetMapping("/registroclasegratis")
	public String mostrarRegistro(Model model) {
		model.addAttribute("usuario", new Usuario());
		return "registroclasegratis"; // tu HTML
	}

	@PostMapping("/registroclasegratis")
	public String registrarUsuario(@ModelAttribute Usuario usuario, RedirectAttributes redirectAttrs) {
		usuarioServiceImplement.save(usuario);
		redirectAttrs.addFlashAttribute("mensaje", "¡Registro exitoso! Revisa tu correo.");
		return "redirect:/iniciosesion";
	}
}
