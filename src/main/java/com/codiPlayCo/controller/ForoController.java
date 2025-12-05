package com.codiPlayCo.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.codiPlayCo.model.Curso;
import com.codiPlayCo.model.Foro;
import com.codiPlayCo.model.ForoRespuesta;
import com.codiPlayCo.model.Usuario;
import com.codiPlayCo.repository.ForoRepository;
import com.codiPlayCo.repository.ForoRespuestaRepository;
import com.codiPlayCo.service.ICursoService;
import com.codiPlayCo.service.IUsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/InterfazDocente/foros")
public class ForoController {

    @Autowired
    private ForoRepository foroRepository;

    @Autowired
    private ForoRespuestaRepository foroRespuestaRepository;

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private ICursoService cursoService;

    @GetMapping
    public String listarForosDocente(Model model, HttpSession session) {
        Integer docenteId = (Integer) session.getAttribute("idUsuario");
        
        if (docenteId == null) {
            return "redirect:/InterfazDocente/login";
        }
        
        Usuario docente = usuarioService.findById(docenteId).orElse(null);
        if (docente == null) {
            return "redirect:/InterfazDocente/login";
        }
        
        // Obtener los cursos que imparte el docente
        List<Curso> cursos = cursoService.findByDocenteId(docenteId);
        Map<Integer, List<Foro>> forosPorCurso = new HashMap<>();
        Map<Integer, Integer> respuestasPorForo = new HashMap<>();
        
        for (Curso curso : cursos) {
            List<Foro> forosCurso = foroRepository.findByCursoId(curso.getId());
            forosPorCurso.put(curso.getId(), forosCurso);
            
            // Contar respuestas para cada foro
            for (Foro foro : forosCurso) {
                int count = foroRespuestaRepository.countByForoId(foro.getId());
                respuestasPorForo.put(foro.getId(), count);
            }
        }
        
        model.addAttribute("docente", docente);
        model.addAttribute("cursos", cursos);
        model.addAttribute("forosPorCurso", forosPorCurso);
        model.addAttribute("respuestasPorForo", respuestasPorForo);
        
        return "InterfazDocente/Foros";
    }
    
    @PostMapping("/crear")
    public String crearForo(
            @RequestParam("titulo") String titulo,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("cursoId") Integer cursoId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        Integer docenteId = (Integer) session.getAttribute("idUsuario");
        if (docenteId == null) {
            return "redirect:/InterfazDocente/login";
        }
        
        try {
            Usuario docente = usuarioService.findById(docenteId)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado"));
            
            Optional<Curso> cursoOpt = cursoService.findById(cursoId);
            if (cursoOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Curso no encontrado");
                return "redirect:/InterfazDocente/foros";
            }
            
            Curso curso = cursoOpt.get();
            
            // Verificar que el docente está asignado a este curso
            if (curso.getAsignacionDocente() == null || 
                !curso.getAsignacionDocente().getUsuario().getId().equals(docenteId)) {
                redirectAttributes.addFlashAttribute("error", "No tiene permiso para crear foros en este curso");
                return "redirect:/InterfazDocente/foros";
            }
            
            Foro foro = new Foro();
            foro.setTitulo(titulo);
            foro.setDescripcion(descripcion);
            foro.setFechaCreacion(LocalDateTime.now());
            foro.setDocente(docente);
            foro.setCurso(curso);
            
            foroRepository.save(foro);
            redirectAttributes.addFlashAttribute("success", "Foro creado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear el foro: " + e.getMessage());
        }
        
        return "redirect:/InterfazDocente/foros";
    }
    
    @PostMapping("/editar")
    public String editarForo(
            @RequestParam("foroId") Integer foroId,
            @RequestParam("titulo") String titulo,
            @RequestParam("descripcion") String descripcion,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        Integer docenteId = (Integer) session.getAttribute("idUsuario");
        if (docenteId == null) {
            return "redirect:/InterfazDocente/login";
        }
        
        try {
            Foro foro = foroRepository.findById(foroId)
                .orElseThrow(() -> new RuntimeException("Foro no encontrado"));
            
            // Verificar que el foro pertenece a un curso del docente
            if (foro.getCurso().getAsignacionDocente() == null || 
                !foro.getCurso().getAsignacionDocente().getUsuario().getId().equals(docenteId)) {
                redirectAttributes.addFlashAttribute("error", "No tiene permiso para editar este foro");
                return "redirect:/InterfazDocente/foros";
            }
            
            foro.setTitulo(titulo);
            foro.setDescripcion(descripcion);
            foroRepository.save(foro);
            
            redirectAttributes.addFlashAttribute("success", "Foro actualizado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el foro: " + e.getMessage());
        }
        
        return "redirect:/InterfazDocente/foros";
    }
    
    @GetMapping("/detalle")
    public String verForo(
            @RequestParam("foroId") Integer foroId,
            Model model,
            HttpSession session) {
        
        Integer docenteId = (Integer) session.getAttribute("idUsuario");
        if (docenteId == null) {
            return "redirect:/InterfazDocente/login";
        }
        
        try {
            Foro foro = foroRepository.findById(foroId)
                .orElseThrow(() -> new RuntimeException("Foro no encontrado"));
            
            // Verificar que el foro pertenece a un curso del docente
            if (foro.getCurso().getAsignacionDocente() == null || 
                !foro.getCurso().getAsignacionDocente().getUsuario().getId().equals(docenteId)) {
                return "redirect:/InterfazDocente/foros?error=No tiene permiso para ver este foro";
            }
            
            // Obtener todas las respuestas de los estudiantes para este foro
            List<ForoRespuesta> respuestas = foroRespuestaRepository.findByForoIdOrderByFechaCreacionAsc(foroId);
            
            model.addAttribute("foro", foro);
            model.addAttribute("respuestas", respuestas);
            return "InterfazDocente/ForoDetalle";
            
        } catch (Exception e) {
            return "redirect:/InterfazDocente/foros?error=" + e.getMessage();
        }
    }
    
    @PostMapping("/eliminar")
    public String eliminarForo(
            @RequestParam("foroId") Integer foroId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        Integer docenteId = (Integer) session.getAttribute("idUsuario");
        if (docenteId == null) {
            return "redirect:/InterfazDocente/login";
        }
        
        try {
            Foro foro = foroRepository.findById(foroId)
                .orElseThrow(() -> new RuntimeException("Foro no encontrado"));
            
            // Verificar que el foro pertenece a un curso del docente
            if (foro.getCurso().getAsignacionDocente() == null || 
                !foro.getCurso().getAsignacionDocente().getUsuario().getId().equals(docenteId)) {
                redirectAttributes.addFlashAttribute("error", "No tiene permiso para eliminar este foro");
                return "redirect:/InterfazDocente/foros";
            }
            
            // Primero eliminar respuestas asociadas para evitar problemas de FK
            List<ForoRespuesta> respuestas = foroRespuestaRepository.findByForoId(foroId);
            if (respuestas != null && !respuestas.isEmpty()) {
                foroRespuestaRepository.deleteAll(respuestas);
            }
            
            foroRepository.delete(foro);
            redirectAttributes.addFlashAttribute("success", "Foro eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el foro: " + e.getMessage());
        }
        
        return "redirect:/InterfazDocente/foros";
    }
}
