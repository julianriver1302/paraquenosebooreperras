package com.codiPlayCo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.codiPlayCo.model.Usuario;
import com.codiPlayCo.model.Rol;
import com.codiPlayCo.model.AsignacionDocente;
import com.codiPlayCo.model.Curso;
import com.codiPlayCo.service.AsignacionDocenteServiceImplement;
import com.codiPlayCo.service.CursoServiceImplement;
import com.codiPlayCo.service.UsuarioServiceImplement;
import com.codiPlayCo.service.RolService;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import com.codiPlayCo.repository.IRolRepository;

@Controller
@RequestMapping("/PanelCodiplay")
public class AdminController {

	private final CursoServiceImplement cursoServiceImplement;
	private final UsuarioServiceImplement usuarioServiceImplement;
	private final RolService rolService;
	private final IRolRepository iRolRepository;

	public AdminController(CursoServiceImplement cursoServiceImplement, UsuarioServiceImplement usuarioServiceImplement,
			RolService rolService, IRolRepository rolRepository) {
		this.cursoServiceImplement = cursoServiceImplement;
		this.usuarioServiceImplement = usuarioServiceImplement;
		this.rolService = rolService;
		this.iRolRepository = rolRepository;
	}
	
	@GetMapping("/PanelCodiplay")
	public String panelPrincipal(Model model) {
		try {
			// Contar usuarios con rol ID 3 (estudiantes)
			List<Usuario> todosUsuarios = usuarioServiceImplement.findAll();
			System.out.println("Total usuarios encontrados: " + todosUsuarios.size());
			
			long totalEstudiantes = todosUsuarios.stream()
				.filter(usuario -> usuario.getRol() != null && usuario.getRol().getId() == 3)
				.count();
				
			System.out.println("Total estudiantes (rol ID 3): " + totalEstudiantes);
			model.addAttribute("totalEstudiantes", totalEstudiantes);
		} catch (Exception e) {
			System.err.println("Error al contar estudiantes: " + e.getMessage());
			e.printStackTrace();
			// En caso de error, usar valor por defecto
			model.addAttribute("totalEstudiantes", 58);
		}
		return "Admin/PanelCodiplay";
	}

	// ========== MÉTODOS PARA CURSOS ==========

	@Autowired
	private AsignacionDocenteServiceImplement asignacionDocenteService;

	@GetMapping("/Admin/crear_curso")
	public String mostrarFormularioCrearCurso(Model model) {
	    try {
	        // Obtener todas las asignaciones de docentes
	        List<AsignacionDocente> asignaciones = asignacionDocenteService.findAll();
	        
	        // Si no hay asignaciones, verificar si hay docentes registrados sin asignación
	        if (asignaciones.isEmpty()) {
	            List<Usuario> docentes = usuarioServiceImplement.findByRol("DOCENTE");
	            System.out.println("No hay asignaciones, pero se encontraron " + docentes.size() + " docentes registrados");
	            
	            // Crear asignaciones automáticamente para docentes que no las tengan
	            for (Usuario docente : docentes) {
	                AsignacionDocente nuevaAsignacion = new AsignacionDocente();
	                nuevaAsignacion.setUsuario(docente);
	                nuevaAsignacion.setAño(String.valueOf(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)));
	                nuevaAsignacion.setCursoAsirgnado("Sin asignar");
	                asignacionDocenteService.save(nuevaAsignacion);
	                System.out.println("✅ Asignación creada automáticamente para docente: " + docente.getNombre() + " " + docente.getApellido());
	            }
	            
	            // Recargar las asignaciones después de crearlas
	            asignaciones = asignacionDocenteService.findAll();
	        }
	        
	        model.addAttribute("asignaciones", asignaciones);

	        System.out.println("Asignaciones encontradas: " + asignaciones.size());
	        for (AsignacionDocente asignacion : asignaciones) {
	            if (asignacion.getUsuario() != null) {
	                System.out.println("Docente: " + asignacion.getUsuario().getNombre() + " " + asignacion.getUsuario().getApellido());
	            }
	        }

	    } catch (Exception e) {
	        model.addAttribute("asignaciones", List.of());
	        System.err.println("Error al cargar asignaciones: " + e.getMessage());
	        e.printStackTrace();
	    }

	    return "Admin/crear_curso";
	}


	@PostMapping("/Admin/guardar_curso")
	public String guardarCurso(
	        @RequestParam("curso") String nombreCurso,
	        @RequestParam("dirigido") String dirigido,
	        @RequestParam("asignacionDocente") Integer idAsignacionDocente,
	        @RequestParam("descripcion") String descripcion,
	        @RequestParam("dificultad") Integer dificultad,
	        @RequestParam("precio") Double precio) {

	    try {
	        // Buscar la asignación de docente por ID
	        AsignacionDocente asignacionDocente = asignacionDocenteService.findById(idAsignacionDocente);

	        if (asignacionDocente == null) {
	            System.err.println("⚠️ No se encontró la asignación del docente con ID: " + idAsignacionDocente);
	            return "redirect:/PanelCodiplay/Admin/crear_curso?error=docente";
	        }

	        // Crear y llenar el curso
	        Curso curso = new Curso();
	        curso.setCurso(nombreCurso);
	        curso.setDirigido(dirigido);
	        curso.setAsignacionDocente(asignacionDocente);
	        curso.setDescripcion(descripcion);
	        curso.setDificultad(dificultad != null ? dificultad : 1);
	        curso.setPrecio(precio != null ? precio : 0.0);
	        curso.setEstado("Activo");

	        // Guardar
	        cursoServiceImplement.save(curso);

	        System.out.println("✅ Curso guardado correctamente: " + curso.getCurso());
	        return "redirect:/PanelCodiplay/Admin/listar_cursos";

	    } catch (Exception e) {
	        e.printStackTrace();
	        return "redirect:/PanelCodiplay/Admin/crear_curso?error=true";
	    }
	}


	@GetMapping("/Admin/listar_cursos")
	public String listarCursos(Model model) {
		model.addAttribute("cursos", cursoServiceImplement.findAll());
		return "Admin/listar_cursos";
	}

	@GetMapping("/Admin/eliminar_curso/{id}")
	public String eliminarCurso(@PathVariable Integer id) {
		cursoServiceImplement.delete(id);
		return "redirect:/PanelCodiplay/Admin/listar_cursos";
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

	@GetMapping("/Admin/editar_curso/{id}")
	public String mostrarFormularioEditarCurso(@PathVariable Integer id, Model model) {
		try {
			Optional<Curso> cursoOptional = cursoServiceImplement.get(id);
			if (cursoOptional.isPresent()) {
				Curso curso = cursoOptional.get();
				model.addAttribute("curso", curso);

				// Obtener todas las asignaciones de docentes (igual que en crear curso)
				List<AsignacionDocente> asignaciones = asignacionDocenteService.findAll();
				
				// Si no hay asignaciones, verificar si hay docentes registrados sin asignación
				if (asignaciones.isEmpty()) {
					List<Usuario> docentes = usuarioServiceImplement.findByRol("DOCENTE");
					System.out.println("No hay asignaciones, pero se encontraron " + docentes.size() + " docentes registrados");
					
					// Crear asignaciones automáticamente para docentes que no las tengan
					for (Usuario docente : docentes) {
						AsignacionDocente nuevaAsignacion = new AsignacionDocente();
						nuevaAsignacion.setUsuario(docente);
						nuevaAsignacion.setAño(String.valueOf(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)));
						nuevaAsignacion.setCursoAsirgnado("Sin asignar");
						asignacionDocenteService.save(nuevaAsignacion);
						System.out.println("✅ Asignación creada automáticamente para docente: " + docente.getNombre() + " " + docente.getApellido());
					}
					
					// Recargar las asignaciones después de crearlas
					asignaciones = asignacionDocenteService.findAll();
				}
				
				model.addAttribute("asignaciones", asignaciones);

				System.out.println("Asignaciones encontradas para editar curso: " + asignaciones.size());
				for (AsignacionDocente asignacion : asignaciones) {
					if (asignacion.getUsuario() != null) {
						System.out.println("Docente: " + asignacion.getUsuario().getNombre() + " " + asignacion.getUsuario().getApellido());
					}
				}
				
				return "Admin/editar_curso";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/PanelCodiplay/Admin/listar_cursos";
	}

	@PostMapping("/Admin/actualizar_curso/{id}")
	public String actualizarCurso(@PathVariable Integer id, @RequestParam("curso") String nombreCurso,
			@RequestParam("dirigido") String dirigido,
			@RequestParam("asignacionDocente") Integer idAsignacionDocente,
			@RequestParam("descripcion") String descripcion, @RequestParam("dificultad") Integer dificultad,
			@RequestParam("precio") Double precio) {

		try {
			Optional<Curso> cursoOptional = cursoServiceImplement.get(id);
			if (cursoOptional.isPresent()) {
				// Buscar la asignación de docente por ID
				AsignacionDocente asignacionDocente = asignacionDocenteService.findById(idAsignacionDocente);
				
				if (asignacionDocente == null) {
					System.err.println("⚠️ No se encontró la asignación del docente con ID: " + idAsignacionDocente);
					return "redirect:/PanelCodiplay/Admin/editar_curso/" + id + "?error=docente";
				}
				
				Curso curso = cursoOptional.get();
				curso.setCurso(nombreCurso);
				curso.setDirigido(dirigido);
				curso.setAsignacionDocente(asignacionDocente);
				curso.setDescripcion(descripcion);
				curso.setDificultad(dificultad != null ? dificultad : 1);
				curso.setPrecio(precio != null ? precio : 0.0);
				curso.setEstado(curso.getEstado() != null ? curso.getEstado() : "Activo");
				cursoServiceImplement.save(curso);
				
				System.out.println("✅ Curso actualizado correctamente: " + curso.getCurso());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/PanelCodiplay/Admin/editar_curso/" + id + "?error=true";
		}
		return "redirect:/PanelCodiplay/Admin/listar_cursos";
	}

	// ========== MÉTODOS PARA DOCENTES ==========

	@GetMapping("/Admin/registrar_docentes")
	public String mostrarFormularioDocentes(Model model) {
		// Obtener cursos activos
		   try {
		        List<Curso> cursosActivos = cursoServiceImplement.findAll();
		        model.addAttribute("cursosActivos", cursosActivos);
		        model.addAttribute("usuario", new Usuario()); // Formulario limpio
		        
		    } catch (Exception e) {
		        model.addAttribute("cursosActivos", List.of());
		        model.addAttribute("usuario", new Usuario());
		    }
		return "Admin/registrar_docentes";
	}

	@PostMapping("/Admin/guardar_docente")
	public String guardarDocente(@RequestParam("nombre") String nombre, @RequestParam("apellido") String apellido,
			@RequestParam("email") String email, @RequestParam("celular") String celular,
			@RequestParam("documento") String documento, @RequestParam("tipoDocumento") String tipoDocumento,

			@RequestParam("password") String password,
			@RequestParam(value = "cursos", required = false) List<Integer> cursosSeleccionados,
			RedirectAttributes redirectAttributes)

	{ // ← NUEVO PARÁMETRO

		try {
			// Verificar si el email ya existe
			if (usuarioServiceImplement.existsByEmail(email)) {
				return "redirect:/PanelCodiplay/Admin/registrar_docentes?error=email_existente";
			}

			// Crear nuevo usuario
			Usuario nuevoDocente = new Usuario();
			nuevoDocente.setNombre(nombre);
			nuevoDocente.setApellido(apellido);
			nuevoDocente.setEmail(email);
			nuevoDocente.setCelular(celular);
			nuevoDocente.setTipoDocumento(tipoDocumento);
			nuevoDocente.setDocumento(documento);
			nuevoDocente.setPassword(password); // En producción, esto debería estar encriptado
			nuevoDocente.setActivo("activo");
			nuevoDocente.setFecharegistro(new Date(System.currentTimeMillis()));

			// Asignar rol de docente - BUSCAR EL ROL PRIMERO
			List<Rol> todosRoles = rolService.findAll(); // o roIService.findAll()
			Rol rolDocente = null;

			for (Rol rol : todosRoles) {
				if ("DOCENTE".equalsIgnoreCase(rol.getNombre())) {
					rolDocente = rol;
					break;
				}
			}

			if (rolDocente == null) {
				return "redirect:/PanelCodiplay/Admin/registrar_docentes?rol_no_encontrado";
			}

			nuevoDocente.setRol(rolDocente);

			// Guardar el docente
			Usuario docenteGuardado = usuarioServiceImplement.save(nuevoDocente);

			// Crear automáticamente una AsignacionDocente para el docente registrado
			// Verificar si ya existe una asignación para este docente
			List<AsignacionDocente> asignacionesExistentes = asignacionDocenteService.findAll();
			boolean asignacionExiste = asignacionesExistentes.stream()
					.anyMatch(asig -> asig.getUsuario() != null && asig.getUsuario().getId().equals(docenteGuardado.getId()));

			if (!asignacionExiste) {
				AsignacionDocente nuevaAsignacion = new AsignacionDocente();
				nuevaAsignacion.setUsuario(docenteGuardado);
				nuevaAsignacion.setAño(String.valueOf(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)));
				nuevaAsignacion.setCursoAsirgnado("Sin asignar");
				asignacionDocenteService.save(nuevaAsignacion);
				System.out.println("✅ Asignación docente creada automáticamente para: " + docenteGuardado.getNombre() + " " + docenteGuardado.getApellido());
			}

			redirectAttributes.addFlashAttribute("success", "Docente registrado exitosamente");
			return "redirect:/PanelCodiplay/Admin/registrar_docentes?success";

		} catch (Exception e) {
			System.err.println("Error al registrar docente: " + e.getMessage());
			e.printStackTrace();
			return "redirect:/PanelCodiplay/Admin/registrar_docentes?error";
		}

	}

	@GetMapping("")
	public String PanelCodiplay() {
		return "Admin/PanelCodiplay";
	}

	@GetMapping("/Admin/editar_usuarios")
	public String editar_usuarios() {
		return "Admin/editar_usuarios";
	}

	@GetMapping("/Admin/proceso_estudiantes")
	public String proceso_estudiantes() {
		return "Admin/proceso_estudiantes";
	}
	
	
}