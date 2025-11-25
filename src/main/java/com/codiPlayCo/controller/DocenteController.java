package com.codiPlayCo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.codiPlayCo.model.Usuario;
import com.codiPlayCo.model.Curso;
import com.codiPlayCo.model.ProgresoEstudianteDTO;
import com.codiPlayCo.model.Actividades;
import com.codiPlayCo.model.ActividadesEstudiantes;
import com.codiPlayCo.model.Foro;
import com.codiPlayCo.service.IUsuarioService;
import com.codiPlayCo.service.ICursoService;
import com.codiPlayCo.repository.ActividadesRepository;
import com.codiPlayCo.repository.ActividadesEstudiantesRepository;
import com.codiPlayCo.repository.ForoRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/InterfazDocente")
public class DocenteController {
	
	@Autowired
    private IUsuarioService usuarioService;

	@Autowired
	private ICursoService cursoService;

	@Autowired
	private ActividadesRepository actividadesRepository;

	@Autowired
	private ActividadesEstudiantesRepository actividadesEstudiantesRepository;

	@Autowired
	private ForoRepository foroRepository;

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

    @PostMapping("/subir-foto")
    public String subirFotoDocente(@RequestParam("foto") MultipartFile foto,
                                   HttpSession session,
                                   RedirectAttributes redirectAttrs) {
        Integer idUsuario = (Integer) session.getAttribute("idUsuario");
        Integer rol = (Integer) session.getAttribute("rol");

        if (idUsuario == null || rol == null || rol != 2) {
            redirectAttrs.addFlashAttribute("error", "Debe iniciar sesión como docente para subir una foto.");
            return "redirect:/iniciosesion?error=acceso_denegado";
        }

        if (foto == null || foto.isEmpty()) {
            redirectAttrs.addFlashAttribute("error", "Debe seleccionar una imagen.");
            return "redirect:/InterfazDocente/paneldocente";
        }

        try {
            String contentType = foto.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                redirectAttrs.addFlashAttribute("error", "Solo se permiten archivos de imagen.");
                return "redirect:/InterfazDocente/paneldocente";
            }

            if (foto.getSize() > 2 * 1024 * 1024) {
                redirectAttrs.addFlashAttribute("error", "El archivo es demasiado grande. Máximo 2MB.");
                return "redirect:/InterfazDocente/paneldocente";
            }

            Path uploadDir = Paths.get("target/classes/static/uploads/avatars");
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            String originalFilename = foto.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".")
                    ? originalFilename.substring(originalFilename.lastIndexOf('.'))
                    : "";
            String newFilename = UUID.randomUUID().toString() + extension;

            Path filePath = uploadDir.resolve(newFilename);
            Files.copy(foto.getInputStream(), filePath);

            Usuario docente = usuarioService.findById(idUsuario).orElse(null);
            if (docente != null) {
                docente.setAvatar(newFilename);
                usuarioService.save(docente);
                redirectAttrs.addFlashAttribute("mensaje", "Foto actualizada correctamente.");
            } else {
                redirectAttrs.addFlashAttribute("error", "No se encontró el docente.");
            }

        } catch (IOException e) {
            redirectAttrs.addFlashAttribute("error", "Error al subir la imagen. Inténtalo de nuevo.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Error al actualizar la foto del docente.");
        }

        return "redirect:/InterfazDocente/paneldocente";
    }

    @GetMapping("/tareas")
    public String tareas(HttpSession session, Model model) {
        Integer idUsuario = (Integer) session.getAttribute("idUsuario");
        Integer rol = (Integer) session.getAttribute("rol");

        if (idUsuario != null && rol != null && rol == 2) { // Docente
            Usuario docente = usuarioService.findById(idUsuario).orElse(null);
            if (docente != null && docente.getRol() != null && docente.getRol().getId() == 2) {
                java.util.List<Curso> cursosDocente = cursoService.findByDocenteId(idUsuario);

                java.util.Map<Integer, java.util.List<Actividades>> actividadesPorCurso = new java.util.HashMap<>();
                java.util.Map<Integer, java.util.List<ActividadesEstudiantes>> entregasPorActividad = new java.util.HashMap<>();

                for (Curso curso : cursosDocente) {
                    java.util.List<Actividades> actividadesCurso = actividadesRepository.findByCurso(curso.getId());
                    actividadesPorCurso.put(curso.getId(), actividadesCurso);

                    for (Actividades act : actividadesCurso) {
                        java.util.List<ActividadesEstudiantes> entregas = actividadesEstudiantesRepository.findByActividadId(act.getId());
                        entregasPorActividad.put(act.getId(), entregas);
                    }
                }

                model.addAttribute("docente", docente);
                model.addAttribute("cursos", cursosDocente);
                model.addAttribute("actividadesPorCurso", actividadesPorCurso);
                model.addAttribute("entregasPorActividad", entregasPorActividad);
                return "InterfazDocente/Tareas";
            }
        }
        return "redirect:/iniciosesion?error=acceso_denegado";
    }

    @PostMapping("/tareas/calificar")
    public String calificarTarea(@RequestParam("entregaId") Integer entregaId,
                                 @RequestParam("resultado") String resultado,
                                 @RequestParam(value = "calificacion", required = false) String calificacion,
                                 HttpSession session) {

        Integer idUsuario = (Integer) session.getAttribute("idUsuario");
        Integer rol = (Integer) session.getAttribute("rol");

        if (idUsuario == null || rol == null || rol != 2) {
            return "redirect:/iniciosesion?error=acceso_denegado";
        }

        ActividadesEstudiantes entrega = actividadesEstudiantesRepository.findById(entregaId).orElse(null);
        if (entrega != null) {
            // Normalizamos resultado a mayúsculas para estado
            String estado = resultado != null ? resultado.toUpperCase() : "";
            if ("APROBADO".equals(estado) || "NO_APROBADO".equals(estado)) {
                entrega.setEstado(estado);
            }
            if (calificacion != null) {
                entrega.setCalificacion(calificacion);
            }
            actividadesEstudiantesRepository.save(entrega);
        }

        return "redirect:/InterfazDocente/tareas";
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
	public String foros(HttpSession session, Model model) {
		Integer idUsuario = (Integer) session.getAttribute("idUsuario");
		Integer rol = (Integer) session.getAttribute("rol");

		if (idUsuario == null || rol == null || rol != 2) {
			return "redirect:/iniciosesion?error=acceso_denegado";
		}

		Usuario docente = usuarioService.findById(idUsuario).orElse(null);
		if (docente == null || docente.getRol() == null || docente.getRol().getId() != 2) {
			return "redirect:/iniciosesion?error=acceso_denegado";
		}

		List<Curso> cursosDocente = cursoService.findByDocenteId(idUsuario);
		Map<Integer, List<Foro>> forosPorCurso = new HashMap<>();

		for (Curso curso : cursosDocente) {
			List<Foro> forosCurso = foroRepository.findByCursoId(curso.getId());
			forosPorCurso.put(curso.getId(), forosCurso);
		}

		model.addAttribute("docente", docente);
		model.addAttribute("cursos", cursosDocente);
		model.addAttribute("forosPorCurso", forosPorCurso);

		return "InterfazDocente/Foros";
	}

	@PostMapping("/foros/crear")
	public String crearForo(@RequestParam("cursoId") Integer cursoId,
	                      @RequestParam("titulo") String titulo,
	                      @RequestParam("descripcion") String descripcion,
	                      HttpSession session,
	                      RedirectAttributes redirectAttrs) {
		Integer idUsuario = (Integer) session.getAttribute("idUsuario");
		Integer rol = (Integer) session.getAttribute("rol");

		if (idUsuario == null || rol == null || rol != 2) {
			return "redirect:/iniciosesion?error=acceso_denegado";
		}

		Usuario docente = usuarioService.findById(idUsuario).orElse(null);
		Curso curso = cursoService.get(cursoId).orElse(null);

		if (docente == null || curso == null) {
			redirectAttrs.addFlashAttribute("error", "No se pudo crear el foro. Verifique el curso seleccionado.");
			return "redirect:/InterfazDocente/foros";
		}

		Foro foro = new Foro();
		foro.setTitulo(titulo);
		foro.setDescripcion(descripcion);
		foro.setFechaCreacion(LocalDateTime.now());
		foro.setCurso(curso);
		foro.setDocente(docente);
		foroRepository.save(foro);

		redirectAttrs.addFlashAttribute("mensaje", "Foro creado correctamente.");
		return "redirect:/InterfazDocente/foros";
	}

    @GetMapping("/mis-cursos")
    public String misCursos(HttpSession session, Model model) {
        Integer idUsuario = (Integer) session.getAttribute("idUsuario");
        Integer rol = (Integer) session.getAttribute("rol");

        if (idUsuario != null && rol != null && rol == 2) { // Rol 2 = Docente
            Usuario docente = usuarioService.findById(idUsuario).orElse(null);
            if (docente != null && docente.getRol() != null && docente.getRol().getId() == 2) {
                // Cursos asignados a este docente
                java.util.List<Curso> cursosDocente = cursoService.findByDocenteId(idUsuario);
                model.addAttribute("docente", docente);
                model.addAttribute("cursos", cursosDocente);

                // Calcular progreso de estudiantes por curso
                java.util.Map<Integer, java.util.List<ProgresoEstudianteDTO>> progresoPorCurso = new java.util.HashMap<>();

                for (Curso curso : cursosDocente) {
                    java.util.List<Usuario> estudiantesCurso = curso.getUsuarios();
                    int totalActividades = actividadesRepository.findByCurso(curso.getId()).size();
                    java.util.List<ProgresoEstudianteDTO> progresos = new java.util.ArrayList<>();

                    for (Usuario est : estudiantesCurso) {
                        Long completadas = actividadesEstudiantesRepository
                                .countCompletadasByEstudianteAndCurso(est.getId(), curso.getId());
                        double total = (double) (totalActividades <= 0 ? 1 : totalActividades);
                        double porcentaje = (completadas == null ? 0.0 : completadas.doubleValue() * 100.0 / total);
                        String nombreCompleto = (est.getNombre() != null ? est.getNombre() : "") + " "
                                + (est.getApellido() != null ? est.getApellido() : "");
                        progresos.add(new ProgresoEstudianteDTO(est.getId(), nombreCompleto.trim(), porcentaje));
                    }

                    progresoPorCurso.put(curso.getId(), progresos);
                }

                model.addAttribute("progresoPorCurso", progresoPorCurso);
                return "InterfazDocente/MisCursos";
            }
        }

        return "redirect:/iniciosesion?error=acceso_denegado";
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // Invalidar la sesión
        session.invalidate();
        return "redirect:/iniciosesion?logout=true";
    }
}
