package com.codiPlayCo.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.codiPlayCo.model.ForoTema;
import com.codiPlayCo.model.Usuario;
import com.codiPlayCo.repository.IForoTemaRepository;
import com.codiPlayCo.service.IUsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/PanelControlUsuario/foros")
public class ForoController {

    @Autowired
    private IForoTemaRepository foroTemaRepository;

    @Autowired
    private IUsuarioService usuarioService;

    @GetMapping
    public String listarTemas(Model model, HttpSession session) {
        Integer idUsuario = (Integer) session.getAttribute("idUsuario");
        if (idUsuario != null) {
            usuarioService.findById(idUsuario).ifPresent(u -> model.addAttribute("usuario", u));
        }

        // Por ahora mostramos todos los temas, luego se puede filtrar por módulo
        List<ForoTema> temas = foroTemaRepository.findAll();
        model.addAttribute("temas", temas);
        return "PanelControlUsuario/forosListado";
    }
}
