package com.codiPlayCo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
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
import com.codiPlayCo.model.ProgresoPorModuloDTO;
import com.codiPlayCo.model.Actividades;
import com.codiPlayCo.model.ActividadesEstudiantes;
import com.codiPlayCo.model.Mensaje;
import com.codiPlayCo.service.IUsuarioService;
import com.codiPlayCo.service.ICursoService;
import com.codiPlayCo.repository.ActividadesRepository;
import com.codiPlayCo.repository.ActividadesEstudiantesRepository;
import com.codiPlayCo.repository.MensajeRepository;
import com.codiPlayCo.repository.IUsuarioRepository;

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
	private MensajeRepository mensajeRepository;

	@Autowired
	private IUsuarioRepository usuarioRepository;

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

    @GetMapping("/mensajes")
    public String mensajes(HttpSession session, Model model) {
        Integer idUsuario = (Integer) session.getAttribute("idUsuario");
        Integer rol = (Integer) session.getAttribute("rol");

        if (idUsuario == null || rol == null || rol != 2) {
            return "redirect:/iniciosesion?error=acceso_denegado";
        }

        Usuario docente = usuarioService.findById(idUsuario).orElse(null);
        if (docente == null || docente.getRol() == null || docente.getRol().getId() != 2) {
            return "redirect:/iniciosesion?error=acceso_denegado";
        }

        // Estudiantes disponibles (usando rol Estudiante). Si luego quieres filtrar por curso podemos afinar.
        java.util.List<Usuario> estudiantes = usuarioService.findByRol("Estudiante");

        // Bandeja simple: mensajes enviados y recibidos por el docente
        List<Mensaje> enviados = mensajeRepository.findByRemitenteIdOrderByFechaEnvioDesc(idUsuario);
        List<Mensaje> recibidos = mensajeRepository.findByDestinatarioIdOrderByFechaEnvioDesc(idUsuario);

        model.addAttribute("docente", docente);
        model.addAttribute("estudiantes", estudiantes);
        model.addAttribute("mensajesEnviados", enviados);
        model.addAttribute("mensajesRecibidos", recibidos);

        return "InterfazDocente/Mensajes";
    }

    @PostMapping("/mensajes/enviar")
    public String enviarMensaje(@RequestParam("destinatarioId") Integer destinatarioId,
            @RequestParam("contenido") String contenido,
            HttpSession session,
            RedirectAttributes redirectAttrs) {
        Integer idUsuario = (Integer) session.getAttribute("idUsuario");
        Integer rol = (Integer) session.getAttribute("rol");

        if (idUsuario == null || rol == null || rol != 2) {
            return "redirect:/iniciosesion?error=acceso_denegado";
        }

        Usuario remitente = usuarioService.findById(idUsuario).orElse(null);
        Usuario destinatario = usuarioService.findById(destinatarioId).orElse(null);
        if (remitente == null || destinatario == null) {
            redirectAttrs.addFlashAttribute("error", "No se pudo enviar el mensaje.");
            return "redirect:/InterfazDocente/mensajes";
        }

        if (contenido == null || contenido.trim().isEmpty()) {
            redirectAttrs.addFlashAttribute("error", "El contenido del mensaje no puede estar vacío.");
            return "redirect:/InterfazDocente/mensajes";
        }

        Mensaje mensaje = new Mensaje();
        mensaje.setRemitente(remitente);
        mensaje.setDestinatario(destinatario);
        mensaje.setContenido(contenido.trim());
        mensaje.setLeido(false);
        mensajeRepository.save(mensaje);

        redirectAttrs.addFlashAttribute("mensaje", "Mensaje enviado correctamente.");
        return "redirect:/InterfazDocente/mensajes";
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
                System.out.println("=== Cursos del docente " + idUsuario + ": " + cursosDocente.size() + " cursos encontrados ===");
                for (Curso c : cursosDocente) {
                    System.out.println(" - Curso: " + c.getCurso() + " (ID: " + c.getId() + ")");
                }
                model.addAttribute("docente", docente);
                model.addAttribute("cursos", cursosDocente);
                
                // INSCRIBIR ESTUDIANTES MANUALMENTE PARA PRUEBA
                if (!cursosDocente.isEmpty()) {
                    Curso cursoPrueba = cursosDocente.get(0);
                    
                    // Usar el método con FETCH JOIN para evitar LazyInitializationException
                    Usuario estudiante1 = usuarioRepository.findByIdWithCursos(3).orElse(null);
                    Usuario estudiante2 = usuarioRepository.findByIdWithCursos(7).orElse(null);
                    
                    if (estudiante1 != null && estudiante1.getCursosComprados() == null) {
                        estudiante1.setCursosComprados(new java.util.ArrayList<>());
                    }
                    if (estudiante2 != null && estudiante2.getCursosComprados() == null) {
                        estudiante2.setCursosComprados(new java.util.ArrayList<>());
                    }
                    
                    if (estudiante1 != null && !estudiante1.getCursosComprados().contains(cursoPrueba)) {
                        estudiante1.getCursosComprados().add(cursoPrueba);
                        usuarioService.save(estudiante1);
                        System.out.println("Estudiante 1 inscrito en curso " + cursoPrueba.getId());
                    }
                    
                    if (estudiante2 != null && !estudiante2.getCursosComprados().contains(cursoPrueba)) {
                        estudiante2.getCursosComprados().add(cursoPrueba);
                        usuarioService.save(estudiante2);
                        System.out.println("Estudiante 2 inscrito en curso " + cursoPrueba.getId());
                    }
                }
                
                // Calcular progreso general de estudiantes por curso (porcentaje)
                java.util.Map<Integer, java.util.List<ProgresoEstudianteDTO>> progresoPorCurso = new java.util.HashMap<>();
                // Calcular progreso detallado por módulo / lección
                java.util.Map<Integer, java.util.List<ProgresoPorModuloDTO>> progresoModulosPorCurso = new java.util.HashMap<>();
                // Número real de lecciones por módulo, según las vistas del PanelControlUsuario
                java.util.Map<Integer, Integer> leccionesPorModulo = new java.util.HashMap<>();
                leccionesPorModulo.put(1, 9); // módulo 1: 9 tarjetas/lecciones en modulo1.html
                leccionesPorModulo.put(2, 6); // módulo 2: 6 tarjetas/lecciones en modulo2.html
                leccionesPorModulo.put(3, 10); // módulo 3: 10 tarjetas/lecciones en modulo3.html
                leccionesPorModulo.put(4, 15); // módulo 4: 15 tarjetas/lecciones en modulo4.html
                for (Curso curso : cursosDocente) {
                    java.util.List<Usuario> estudiantesCurso = usuarioService.findByCursoComprado(curso.getId());
                    
                    // Verificación detallada para depuración
                    System.out.println("Curso " + curso.getCurso() + " (ID: " + curso.getId() + "): " + 
                        (estudiantesCurso != null ? estudiantesCurso.size() : 0) + " estudiantes encontrados");
                    
                    // Verificar cada estudiante encontrado
                    if (estudiantesCurso != null) {
                        for (Usuario est : estudiantesCurso) {
                            System.out.println("   - Estudiante: " + est.getNombre() + " " + est.getApellido() + 
                                " (ID: " + est.getId() + ", Rol: " + 
                                (est.getRol() != null ? est.getRol().getId() : "null") + ")");
                        }
                    } else {
                        System.out.println("   - La consulta retornó null");
                    }
                    java.util.List<Actividades> actividadesCurso = actividadesRepository.findByCurso(curso.getId());
                    int totalActividades = actividadesCurso.size();
                    
                    
                    java.util.List<ProgresoEstudianteDTO> progresos = new java.util.ArrayList<>();
                    java.util.List<ProgresoPorModuloDTO> progresosDetallados = new java.util.ArrayList<>();
                    
                    // Null check to prevent NPE
                    if (estudiantesCurso != null && !estudiantesCurso.isEmpty()) {
                        for (Usuario est : estudiantesCurso) {
                        // Progreso general
                        Long completadas = actividadesEstudiantesRepository
                                .countCompletadasByEstudianteAndCurso(est.getId(), curso.getId());
                        double total = (double) (totalActividades <= 0 ? 1 : totalActividades);
                        double porcentaje = (completadas == null ? 0.0 : completadas.doubleValue() * 100.0 / total);
                        String nombreCompleto = (est.getNombre() != null ? est.getNombre() : "") + " "
                                + (est.getApellido() != null ? est.getApellido() : "");
                        progresos.add(new ProgresoEstudianteDTO(est.getId(), nombreCompleto.trim(), porcentaje));
                        // Progreso por módulo / lección: calcular último módulo y lección completados
                        ProgresoPorModuloDTO progDet = new ProgresoPorModuloDTO(est.getId(), nombreCompleto.trim());
                        Integer ultimoModulo = null;
                        Integer ultimaLeccion = null;
                        for (Actividades act : actividadesCurso) {
                            Integer moduloAct = act.getModulo();
                            Integer leccionAct = act.getLeccion();
                            if (moduloAct == null || leccionAct == null) {
                                continue;
                            }
                            boolean done = actividadesEstudiantesRepository
                                    .existsCompletadaByEstudianteAndActividad(est.getId(), act.getId());
                            progDet.marcarLeccion(moduloAct, leccionAct, done);
                            if (done) {
                                if (ultimoModulo == null
                                        || moduloAct > ultimoModulo
                                        || (ultimoModulo != null && moduloAct.equals(ultimoModulo) && (ultimaLeccion != null && leccionAct > ultimaLeccion))) {
                                    ultimoModulo = moduloAct;
                                    ultimaLeccion = leccionAct;
                                }
                            }
                        }
                        progDet.setModuloActual(ultimoModulo);
                        progDet.setLeccionActual(ultimaLeccion);
                        progresosDetallados.add(progDet);
                        }
                    } // Close the null check
                    progresoPorCurso.put(curso.getId(), progresos);
                    progresoModulosPorCurso.put(curso.getId(), progresosDetallados);
                    
                    // Debug: Imprimir resumen del progreso
                    
                    
                }
                
                // Debug: Imprimir información final
                System.out.println("=== RESUMEN FINAL ===");
                System.out.println("Cursos procesados: " + cursosDocente.size());
                System.out.println("ProgresoPorCurso keys: " + progresoPorCurso.keySet());
                for (Integer key : progresoPorCurso.keySet()) {
                    System.out.println("Curso " + key + ": " + progresoPorCurso.get(key).size() + " estudiantes");
                }
                
                // Verificación adicional: consultar todos los estudiantes con rol 3 y sus cursos
                java.util.List<Usuario> todosEstudiantes = usuarioRepository.findEstudiantesConCursos();
                System.out.println("=== Total estudiantes con rol 'Estudiante': " + todosEstudiantes.size() + " ===");
                for (Usuario est : todosEstudiantes) {
                    System.out.println(" - " + est.getNombre() + " " + est.getApellido() + 
                        " (ID: " + est.getId() + ", Rol: " + 
                        (est.getRol() != null ? est.getRol().getNombre() : "null") + ")");
                    // Ahora sí podemos acceder a cursosComprados porque usamos FETCH JOIN
                    if (est.getCursosComprados() != null) {
                        System.out.println("   Cursos comprados: " + est.getCursosComprados().size());
                        for (Curso cursoEst : est.getCursosComprados()) {
                            System.out.println("     - " + cursoEst.getCurso() + " (ID: " + cursoEst.getId() + ")");
                        }
                    } else {
                        System.out.println("   Cursos comprados: 0");
                    }
                }
                
                model.addAttribute("progresoPorCurso", progresoPorCurso);
                model.addAttribute("progresoModulosPorCurso", progresoModulosPorCurso);
                model.addAttribute("leccionesPorModulo", leccionesPorModulo);
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
