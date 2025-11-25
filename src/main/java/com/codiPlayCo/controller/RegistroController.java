package com.codiPlayCo.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.codiPlayCo.model.Rol;
import com.codiPlayCo.model.Usuario;
import com.codiPlayCo.model.Curso;
import com.codiPlayCo.service.CursoServiceImplement; // ← Tu implementación real
import com.codiPlayCo.service.EmailService;
import com.codiPlayCo.service.UsuarioServiceImplement;

@Controller
public class RegistroController {

	@Autowired
	private UsuarioServiceImplement usuarioServiceImplement;

	@Autowired
	private EmailService emailService;

	@Autowired
	private CursoServiceImplement cursoService; // ← AQUI

	@GetMapping("/registroclasegratis")
	public String mostrarRegistro(Model model) {

		// ✔ Cargar curso desde base de datos (ej: id = 1)
		List<Curso> curso = cursoService.findAll(); // ← Si tu método es distinto me dices

		model.addAttribute("curso", curso);
		model.addAttribute("usuario", new Usuario());

		return "registroclasegratis";
	}

	@PostMapping("/registroclasegratis")
	public String registrarUsuario(@ModelAttribute Usuario usuario, RedirectAttributes redirectAttrs) {
		try {

			if (usuarioServiceImplement.existsByEmail(usuario.getEmail())) {
				redirectAttrs.addFlashAttribute("error", "El correo electrónico ya está registrado.");
				return "redirect:/registroclasegratis";
			}

			usuario.setActivo("Sí");
			usuario.setFecharegistro(Date.valueOf(LocalDate.now()));
			usuario.setUltimoAcceso(Date.valueOf(LocalDate.now()));

			Rol rolEstudiante = new Rol();
			rolEstudiante.setId(3);
			usuario.setRol(rolEstudiante);

			usuarioServiceImplement.save(usuario);

			emailService.enviarCorreoConfirmacion(usuario.getEmail(), usuario.getNombre());

			redirectAttrs.addFlashAttribute("mensaje",
					"¡Registro exitoso! Se ha enviado un correo de confirmación a " + usuario.getEmail());

			return "redirect:/gracias";

		} catch (Exception e) {

			redirectAttrs.addFlashAttribute("error", "Error al registrar el usuario: " + e.getMessage());

			return "redirect:/registroclasegratis";
		}
	}
}
