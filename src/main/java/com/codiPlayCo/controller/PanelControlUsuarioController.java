package com.codiPlayCo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PanelControlUsuarioController {
	
	
	@GetMapping("/PanelControlUsuario/inicio")
	public String inicio() {
		return "PanelControlUsuario/inicio";
	}
	
	@GetMapping("/PanelControlUsuario/mis_cursos")
	public String mis_cursos() {
		return "PanelControlUsuario/mis_cursos";
	}
	
	@GetMapping("/PanelControlUsuario/mis_logros")
	public String mis_logros() {
		return "PanelControlUsuario/mis_logros";
	}
	
	@GetMapping("/PanelControlUsuario/modulo1")
	public String modulo1() {
		return "PanelControlUsuario/modulo1";
	}
	
	@GetMapping("/PanelControlUsuario/modulo2")
	public String modulo2 () {
		return "PanelControlUsuario/modulo2";
	}
	
	@GetMapping("/PanelControlUsuario/modulo3")
	public String modulo3() {
		return "PanelControlUsuario/modulo3";
	}
	
	@GetMapping("/PanelControlUsuario/modulo4")
	public String modulo4() {
		return "PanelControlUsuario/modulo4";
	}
	
	@GetMapping("/PanelControlUsuario/soporte")
	public String soporte() {
		return "PanelControlUsuario/soporte";
	}
	
	
	
	

}
