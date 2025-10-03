package com.codiPlayCo.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.codiPlayCo.model.Usuario;
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
	public String iniciosesion() {
		return "iniciosesion";
	}

	@GetMapping("/registropago")
	public String registropago() {
		return "registropago";
	}

	@Autowired
	private IUsuarioService usuarioService;

	
	 @PostMapping("/login")
	    public String login(@RequestParam("username") String email, 
	                       @RequestParam("password") String password, 
	                       HttpSession session) {
	        LOGGER.info("Intentando acceder con usuario: {}", email);

	        String emailNormalizado = email.toLowerCase();
	        Optional<Usuario> userEmail = usuarioService.findByEmail(emailNormalizado);

	        if (userEmail.isPresent()) {
	            Usuario user = userEmail.get();
	            LOGGER.info("Usuario encontrado en DB: {}", user.getEmail());

	            if (!user.getPassword().equals(password)) {
	                LOGGER.warn("Contraseña incorrecta para usuario {}", email);
	                return "redirect:/iniciosesion?error";
	            }

	            session.setAttribute("idUsuario", user.getId());
	            session.setAttribute("rol", user.getRol().getId());

	            if (user.getRol().getId() == 1) {
	                return "redirect:/AdministradorCodiplay/PanelCodiplay";
	            } else if (user.getRol().getId() == 2) {
	                return "redirect:/InterfazDocente/paneldocente";
	            } else if (user.getRol().getId() == 3) {
	                return "redirect:/PanelControlUsuario/inicio";
	            } else {
	                LOGGER.warn("Rol no reconocido para usuario {}", user.getEmail());
	                return "redirect:/iniciosesion?error";
	            }

	        } else {
	            LOGGER.warn("Usuario no existe en DB con email {}", email);
	            return "redirect:/iniciosesion?error";
	        }
	    }

	   
	}