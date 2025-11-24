package com.codiPlayCo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/CursoTrilogia1/modulo2")
public class CursoModulo2Controller {

    @GetMapping("/leccion1")
    public String leccion1() {
        return "CursoTrilogia1/modulo2/leccion1";
    }

    @GetMapping("/leccion2")
    public String leccion2() {
        return "CursoTrilogia1/modulo2/leccion2";
    }

    @GetMapping("/leccion3")
    public String leccion3() {
        return "CursoTrilogia1/Modulo2/leccion3";
    }

    @GetMapping("/leccion4")
    public String leccion4() {
        return "CursoTrilogia1/Modulo2/leccion4";
    }

    @GetMapping("/leccion5")
    public String leccion5() {
        return "CursoTrilogia1/Modulo2/leccion5";
    }
}
