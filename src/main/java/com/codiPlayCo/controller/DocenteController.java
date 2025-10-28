package com.codiPlayCo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.codiPlayCo.model.Usuario;
import com.codiPlayCo.service.IUsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/InterfazDocente")
public class DocenteController {
	
	@Autowired
    private IUsuarioService usuarioService;

	@GetMapping ("/paneldocente")
    public String panel(HttpSession session, Model model) {
        // Verificar si el usuario está logueado y es un docente
        Integer idUsuario = (Integer) session.getAttribute("idUsuario");
        Integer rol = (Integer) session.getAttribute("rol");
        
        if (idUsuario != null && rol != null && rol == 2) { // Rol 2 = Docente
            // Buscar el docente por ID
            Usuario docente = usuarioService.findById(idUsuario).orElse(null);
            if (docente != null && docente.getRol() != null && docente.getRol().getId() == 2) {
                model.addAttribute("docente", docente);
                return "InterfazDocente/paneldocente";
            }
        }
        
        // Si no está autorizado, redirigir al login
        return "redirect:/iniciosesion?error=acceso_denegado";
    }

    @GetMapping("/tareas")
    public String tareas() {
        return "InterfazDocente/Tareas";
    }

    @GetMapping("/asistencia")
    public String asistencia() {
        return "InterfazDocente/Asistencia";
    }

    @GetMapping("/mensajes")
    public String mensajes() {
        return "InterfazDocente/Mensajes";
    }

    @GetMapping("/foros")
    public String foros() {
        return "InterfazDocente/Foros";
    }

    @GetMapping("/mis-cursos")
    public String misCursos() {
        return "InterfazDocente/MisCursos";
    }
    @GetMapping ("InterfazDocente/paneldocente")
    public String inicio() {
        return "InterfazDocente/paneldocente";
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // Invalidar la sesión
        session.invalidate();
        return "redirect:/iniciosesion?logout=true";
    }
}
