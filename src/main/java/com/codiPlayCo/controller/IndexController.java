package com.codiPlayCo.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.codiPlayCo.model.Usuario;
import com.codiPlayCo.service.IUsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping()
public class IndexController {

	private final Logger LOGGER = (Logger) LoggerFactory.getLogger(IndexController.class);

	@GetMapping("/quienessomos")
	public String quienessomos() {
		return "quienessomos";
	}
	
	@GetMapping("/gracias")
	public String gracias() {
		return "gracias";
	}

	@GetMapping("/preguntasfrecuentes")
	public String preguntasfrecuentes() {
		return "preguntasfrecuentes";
	}

	@GetMapping("/iniciosesion")
	public String iniciosesion() {
		return "iniciosesion";
	}

	@GetMapping("/registropago")
	public String registropago() {
		return "registropago";
	}

	@GetMapping("/login")
	public String login() {
		return "iniciosesion"; // tu archivo HTML/Thymeleaf con el formulario
	}

	@Autowired
	private IUsuarioService usuarioService;

	// Procesar login
	@PostMapping("/acceder")
	public String acceder(@RequestParam("email") String email, @RequestParam("password") String password,
			HttpSession session, RedirectAttributes redirectAttrs) {

		LOGGER.info("Intento de login con: {}", email);

		// Buscar usuario por email
		Optional<Usuario> optUser = usuarioService.findByEmail(email.toLowerCase());

		if (optUser.isEmpty()) {
			redirectAttrs.addFlashAttribute("error", "Usuario o contraseña incorrectos");
			return "redirect:/iniciosesion";
		}

		Usuario user = optUser.get();

		// Validar contraseña (en producción usar BCrypt)
		if (!user.getPassword().equals(password)) {
			redirectAttrs.addFlashAttribute("error", "Usuario o contraseña incorrectos");
			return "redirect:/iniciosesion";
		}

		// Guardar en sesión
		session.setAttribute("idUsuario", user.getId());
		session.setAttribute("rol", user.getRol().getId());

		// Redirigir según rol
		switch (user.getRol().getId()) {
		case 1: // Admin
			return "redirect:/PanelCodiplay";
		case 2: // Docente
			return "redirect:/InterfazDocente/paneldocente";
		case 3: // Estudiante
			return "redirect:/PanelControlUsuario/inicio";
		default:
			redirectAttrs.addFlashAttribute("error", "Rol no reconocido");
			return "redirect:/iniciosesion";
		}
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/iniciosesion";
	}

}
