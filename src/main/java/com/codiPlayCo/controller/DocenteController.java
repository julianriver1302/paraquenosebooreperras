package com.codiPlayCo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/panel/docente")
public class DocenteController {

    @GetMapping
    public String panel() {
        return "InterfazDocente/paneldocente";
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
}
