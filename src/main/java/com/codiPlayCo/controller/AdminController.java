package com.codiPlayCo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.codiPlayCo.model.Usuario;
import com.codiPlayCo.model.Rol;
import com.codiPlayCo.model.Curso;
import com.codiPlayCo.service.CursoServiceImplement;
import com.codiPlayCo.service.UsuarioServiceImplement;
import com.codiPlayCo.service.RolService;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import com.codiPlayCo.repository.IRolRepository;

@Controller
@RequestMapping("/PanelCodiplay")
public class AdminController {

	private final CursoServiceImplement cursoServiceImplement;
	private final UsuarioServiceImplement usuarioServiceImplement;
	private final RolService rolService;
	private final IRolRepository rolRepository;

	public AdminController(CursoServiceImplement cursoServiceImplement, UsuarioServiceImplement usuarioServiceImplement,
			RolService rolService, IRolRepository rolRepository) {
		this.cursoServiceImplement = cursoServiceImplement;
		this.usuarioServiceImplement = usuarioServiceImplement;
		this.rolService = rolService;
		this.rolRepository = rolRepository;
	}

	// ========== MÉTODOS PARA CURSOS ==========

	@GetMapping("/Admin/crear_curso")
	public String mostrarFormularioCrearCurso(Model model) {
		try {
			// Obtener docentes (usuarios con rol de docente)
			List<Usuario> docentes = usuarioServiceImplement.findByRol("DOCENTE");
			model.addAttribute("docentes", docentes);

			System.out.println("Docentes encontrados: " + docentes.size());
			for (Usuario docente : docentes) {
				System.out.println("Docente: " + docente.getNombre() + " " + docente.getApellido());
			}

		} catch (Exception e) {
			model.addAttribute("docentes", List.of());
			System.err.println("Error al cargar docentes: " + e.getMessage());
		}
		return "Admin/crear_curso";
	}

	@PostMapping("/Admin/guardar_curso")
	public String guardarCurso(@RequestParam("curso") String nombreCurso, @RequestParam("dirigido") String dirigido,
			@RequestParam("docente") String docente, @RequestParam("descripcion") String descripcion,
			@RequestParam("dificultad") Integer dificultad, @RequestParam("precio") Double precio) {

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
				model.addAttribute("curso", cursoOptional.get());
				return "Admin/editar_curso";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/PanelCodiplay/Admin/listar_cursos";
	}

	@PostMapping("/Admin/actualizar_curso/{id}")
	public String actualizarCurso(@PathVariable Integer id, @RequestParam("curso") String nombreCurso,
			@RequestParam("dirigido") String dirigido, @RequestParam("docente") String docente,
			@RequestParam("descripcion") String descripcion, @RequestParam("dificultad") Integer dificultad,
			@RequestParam("precio") Double precio, @RequestParam("estado") String estado) {

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
			usuarioServiceImplement.save(nuevoDocente);

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