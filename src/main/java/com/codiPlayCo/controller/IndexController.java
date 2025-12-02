package com.codiPlayCo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.codiPlayCo.model.Curso;
import com.codiPlayCo.model.Usuario;
import com.codiPlayCo.service.ICursoService;
import com.codiPlayCo.service.IUsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller

public class IndexController {

	private final Logger LOGGER = (Logger) LoggerFactory.getLogger(IndexController.class);

	@GetMapping("/quienessomos")
	public String quienessomos() {
		return "quienessomos";
	}

	@GetMapping("/preguntasfrecuentes")
	public String preguntasfrecuentes() {
		return "preguntasfrecuentes";
	}

	@GetMapping("/iniciosesion")
	public String iniciosesion(Model model) {
		model.addAttribute("usuario", new Usuario());
		return "iniciosesion";
	}

	@GetMapping("/registropago")
	public String registropago(@RequestParam(name = "cursoId", required = false) Integer cursoId, HttpSession session,
			Model model) {

		if (cursoId != null) {

			// Guardarlo en la sesión
			session.setAttribute("cursoIdSeleccionado", cursoId);

			// Buscar el curso
			Optional<Curso> cursoOpt = cursoService.get(cursoId);

			if (cursoOpt.isEmpty()) {
				return "redirect:/error";
			}

			Curso curso = cursoOpt.get();

			// Enviar curso al HTML
			model.addAttribute("curso", curso);
			model.addAttribute("cursoId", cursoId);
		}

		return "registropago"; // esta vista ahora sí tendrá "curso"
	}

	@GetMapping("/gracias")
	public String gracias() {
		return "gracias";
	}

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private ICursoService cursoService;

	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("usuario", new Usuario());
		return "iniciosesion"; // tu archivo HTML/Thymeleaf con el formulario
	}

	@PostMapping("/login")
	public String procesarLogin(@ModelAttribute Usuario usuario, HttpSession session, IndexController redirectAttrs) {
		LOGGER.info("Intentando acceder con usuario: {}", usuario.getEmail());

		// Normalizar email a minúsculas
		String emailNormalizado = usuario.getEmail().toLowerCase();

		// Buscar por email ignorando mayúsculas/minúsculas
		Optional<Usuario> userEmail = usuarioService.findByEmail(emailNormalizado);

		if (userEmail.isPresent()) {
			Usuario user = userEmail.get();
			LOGGER.info("Usuario encontrado en DB: {}", user.getEmail());

			// Validar contraseña
			if (!user.getPassword().equals(usuario.getPassword())) {
				LOGGER.warn("Contraseña incorrecta para usuario {}", usuario.getEmail());
				return "redirect:/login?error";
			}

			// Guardar en la sesión el ID y rol
			session.setAttribute("idUsuario", user.getId());
			session.setAttribute("rol", user.getRol().getId()); // relación Usuario -> Rol

			// Redirecciones según rol
			if (user.getRol().getId() == 1) {
				return "redirect:/PanelCodiplay";
			} else if (user.getRol().getId() == 2) {
				return "redirect:/InterfazDocente/paneldocente";
			} else if (user.getRol().getId() == 3) {
				return "redirect:/PanelControlUsuario/inicio";
			} else {
				LOGGER.warn("Rol no reconocido para usuario {}", user.getEmail());
				return "redirect:/iniciosesion?error";
			}

		} else {
			LOGGER.warn("Usuario no existe en DB con email {}", usuario.getEmail());
			return "redirect:/iniciosesion?error";
		}
	}

	@PostMapping("/registropago/confirmar")
	public String confirmarPago(HttpSession session) {
		Integer idUsuario = (Integer) session.getAttribute("idUsuario");
		Integer rol = (Integer) session.getAttribute("rol");
		Integer cursoId = (Integer) session.getAttribute("cursoIdSeleccionado");

		if (idUsuario == null || rol == null || rol != 3 || cursoId == null) {
			return "redirect:/iniciosesion";
		}

		Optional<Usuario> usuarioOpt = usuarioService.findById(idUsuario);
		Optional<Curso> cursoOpt = cursoService.get(cursoId);

		if (usuarioOpt.isPresent() && cursoOpt.isPresent()) {
			Usuario usuario = usuarioOpt.get();
			Curso curso = cursoOpt.get();

			List<Curso> cursosComprados = usuario.getCursosComprados();
			if (cursosComprados == null) {
				cursosComprados = new ArrayList<>();
			}
			if (!cursosComprados.contains(curso)) {
				cursosComprados.add(curso);
			}
			usuario.setCursosComprados(cursosComprados);
			usuarioService.save(usuario);
		}

		return "redirect:/gracias";
	}
}