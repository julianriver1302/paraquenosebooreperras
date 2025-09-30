package com.codiPlayCo.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.codiPlayCo.model.Curso;
import com.codiPlayCo.service.CursoServiceImplement;

@Controller
@RequestMapping("/PanelCodiplay")
public class AdminController {

    private final CursoServiceImplement cursoServiceImplement;

    public AdminController(CursoServiceImplement cursoServiceImplement) {
        this.cursoServiceImplement = cursoServiceImplement;
    }
    
    @GetMapping("/Admin/cambiar_estado_curso/{id}")
    public String cambiarEstadoCurso(@PathVariable Integer id) {
        try {
            Optional<Curso> cursoOptional = cursoServiceImplement.get(id);
            if (cursoOptional.isPresent()) {
                Curso curso = cursoOptional.get();
                if ("Activo".equals(curso.getEstado())) {
                    curso.setEstado("Inactivo");
                } else {
                    curso.setEstado("Activo");
                }
                cursoServiceImplement.save(curso);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/PanelCodiplay/Admin/listar_cursos";
    }

    // MÉTODO PARA MOSTRAR FORMULARIO DE EDICIÓN
    @GetMapping("/Admin/editar_curso/{id}")
    public String mostrarFormularioEditarCurso(@PathVariable Integer id, Model model) {
        try {
            Optional<Curso> cursoOptional = cursoServiceImplement.get(id);
            if (cursoOptional.isPresent()) {
                model.addAttribute("curso", cursoOptional.get());
                return "Admin/editar_curso";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/PanelCodiplay/Admin/listar_cursos";
    }

    // MÉTODO PARA ACTUALIZAR CURSO
    @PostMapping("/Admin/actualizar_curso/{id}")
    public String actualizarCurso(
        @PathVariable Integer id,
        @RequestParam("curso") String nombreCurso,
        @RequestParam("dirigido") String dirigido,
        @RequestParam("docente") String docente,
        @RequestParam("descripcion") String descripcion,
        @RequestParam("dificultad") Integer dificultad,
        @RequestParam("precio") Double precio,
        @RequestParam("estado") String estado) {
        
        try {
            Optional<Curso> cursoOptional = cursoServiceImplement.get(id);
            if (cursoOptional.isPresent()) {
                Curso curso = cursoOptional.get();
                curso.setCurso(nombreCurso);
                curso.setDirigido(dirigido);
                curso.setAsignacionDocente(docente);
                curso.setDescripcion(descripcion);
                curso.setDificultad(dificultad != null ? dificultad : 1);
                curso.setPrecio(precio != null ? precio : 0.0);
                curso.setEstado(estado != null ? estado : "Activo");
                
                cursoServiceImplement.save(curso);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/PanelCodiplay/Admin/editar_curso/" + id + "?error=true";
        }
        return "redirect:/PanelCodiplay/Admin/listar_cursos";
    }


    // MÉTODO PARA MOSTRAR FORMULARIO DE CREAR CURSO
    @GetMapping("/Admin/crear_curso")
    public String mostrarFormularioCrearCurso(Model model) {
        model.addAttribute("curso", new Curso());
        
        return "Admin/crear_curso";
    }

    // MÉTODO PARA GUARDAR CURSO
    @PostMapping("/Admin/guardar_curso")
    public String guardarCurso(
        @RequestParam("curso") String nombreCurso,
        @RequestParam("dirigido") String dirigido,
        @RequestParam("docente") String docente,
        @RequestParam("descripcion") String descripcion,
        @RequestParam("dificultad") Integer dificultad,
        @RequestParam("precio") Double precio) {
        
        try {
            Curso curso = new Curso();
            curso.setCurso(nombreCurso);
            curso.setDirigido(dirigido);
            curso.setAsignacionDocente(docente);
            curso.setDescripcion(descripcion);
            curso.setDificultad(dificultad != null ? dificultad : 1);
            curso.setPrecio(precio != null ? precio : 0.0);
            curso.setEstado("Activo");
            
            cursoServiceImplement.save(curso);
            return "redirect:/PanelCodiplay/Admin/listar_cursos"; // Redirige a la lista
            
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/PanelCodiplay/Admin/crear_curso?error=true";
        }
    }

    // MÉTODO PARA LISTAR CURSOS EN EL PANEL ADMIN
    @GetMapping("/Admin/listar_cursos")
    public String listarCursos(Model model) {
        model.addAttribute("cursos", cursoServiceImplement.findAll());
        return "Admin/listar_cursos";
    }

    // MÉTODO PARA ELIMINAR CURSO
    @GetMapping("/Admin/eliminar_curso/{id}")
    public String eliminarCurso(@PathVariable Integer id) {
        cursoServiceImplement.delete(id);
        return "redirect:/PanelCodiplay/Admin/listar_cursos";
    }
    
    // TUS OTROS MÉTODOS EXISTENTES
    @GetMapping("")
    public String PanelCodiplay() {
        return "Admin/PanelCodiplay";
    }
    
    @GetMapping("/Admin/editar_usuarios")
    public String editar_usuarios() {
        return "Admin/editar_usuarios";
    }
    
    @GetMapping("/Admin/registrar_docentes")
    public String registrar_docentes() {
        return "Admin/registrar_docentes";
    }
    
    @GetMapping("/Admin/proceso_estudiantes")
    public String proceso_estudiantes() {
        return "Admin/proceso_estudiantes";
    }
}