package com.codiPlayCo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.codiPlayCo.model.Usuario;
import com.codiPlayCo.service.IUsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
public class PanelControlUsuarioController {

	@Autowired
	private IUsuarioService usuarioService;

	@GetMapping("/PanelControlUsuario/inicio")
	public String inicio(HttpSession session, Model model) {
		// Obtener el ID del usuario de la sesión
		Integer idUsuario = (Integer) session.getAttribute("idUsuario");

		if (idUsuario != null) {
			// Buscar el usuario en la base de datos
			Optional<Usuario> usuarioOpt = usuarioService.findById(idUsuario);
			if (usuarioOpt.isPresent()) {
				Usuario usuario = usuarioOpt.get();
				model.addAttribute("usuario", usuario);
			}
		}
		return "PanelControlUsuario/inicio";
	}

	@GetMapping("/PanelControlUsuario/editar-perfil")
	public String editarPerfil(HttpSession session, Model model) {
		Integer idUsuario = (Integer) session.getAttribute("idUsuario");

		Usuario usuario = new Usuario(); // 👈 crea un objeto vacío por defecto

		if (idUsuario != null) {
			Optional<Usuario> usuarioOpt = usuarioService.findById(idUsuario);
			if (usuarioOpt.isPresent()) {
				usuario = usuarioOpt.get(); // si lo encuentra, lo usa
			}
		}

		model.addAttribute("usuario", usuario); // 👈 siempre agrega el objeto

		return "PanelControlUsuario/editar-perfil";
	}

	@PostMapping("/PanelControlUsuario/editar-perfil")
	public String actualizarPerfil(@ModelAttribute Usuario usuario, HttpSession session,
			RedirectAttributes redirectAttrs, MultipartFile avatarFile) {
		try {
			// Obtener el ID del usuario de la sesión
			Integer idUsuario = (Integer) session.getAttribute("idUsuario");

			if (idUsuario != null) {
				// Buscar el usuario existente
				Optional<Usuario> usuarioExistenteOpt = usuarioService.findById(idUsuario);
				if (usuarioExistenteOpt.isPresent()) {
					Usuario usuarioExistente = usuarioExistenteOpt.get();

					// Actualizar los campos del usuario
					usuarioExistente.setNombre(usuario.getNombre());
					usuarioExistente.setApellido(usuario.getApellido());
					usuarioExistente.setEmail(usuario.getEmail());
					usuarioExistente.setCelular(usuario.getCelular());
					usuarioExistente.setDocumento(usuario.getDocumento());

					// Manejar la subida del avatar
					if (avatarFile != null && !avatarFile.isEmpty()) {
						// Validar el tipo de archivo
						String contentType = avatarFile.getContentType();
						if (contentType != null && contentType.startsWith("image/")) {
							// Validar el tamaño (máximo 2MB)
							if (avatarFile.getSize() <= 2 * 1024 * 1024) {
								// Crear directorio si no existe
								Path uploadDir = Paths.get("target/classes/static/uploads/avatars");
								if (!Files.exists(uploadDir)) {
									Files.createDirectories(uploadDir);
								}

								// Generar nombre único para el archivo
								String originalFilename = avatarFile.getOriginalFilename();
								String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
								String newFilename = UUID.randomUUID().toString() + extension;

								// Guardar el archivo
								Path filePath = uploadDir.resolve(newFilename);
								Files.copy(avatarFile.getInputStream(), filePath);

								// Actualizar el nombre del avatar en la base de datos
								usuarioExistente.setAvatar(newFilename);
							} else {
								redirectAttrs.addFlashAttribute("error", "El archivo es demasiado grande. Máximo 2MB.");
								return "redirect:/PanelControlUsuario/editar-perfil";
							}
						} else {
							redirectAttrs.addFlashAttribute("error", "Solo se permiten archivos de imagen.");
							return "redirect:/PanelControlUsuario/editar-perfil";
						}
					}

					// Guardar los cambios
					usuarioService.save(usuarioExistente);
					redirectAttrs.addFlashAttribute("mensaje", "Perfil actualizado exitosamente.");
				}
			}

			return "redirect:/PanelControlUsuario/editar-perfil";

		} catch (IOException e) {
			redirectAttrs.addFlashAttribute("error", "Error al subir la imagen. Inténtalo de nuevo.");
			return "redirect:/PanelControlUsuario/editar-perfil";
		} catch (Exception e) {
			redirectAttrs.addFlashAttribute("error", "Error al actualizar el perfil. Inténtalo de nuevo.");
			return "redirect:/PanelControlUsuario/editar-perfil";
		}
	}

	@GetMapping("/PanelControlUsuario/mis_cursos")
	public String misCursos(Model model, HttpSession session) {
		Object usuario = session.getAttribute("usuario");
		if (usuario == null) {
			return "PanelControlUsuario/mis_cursos";
		}
		model.addAttribute("usuario", usuario);
		return "PanelControlUsuario/mis_cursos";
	}

	@GetMapping("/PanelControlUsuario/mis_logros")
	public String misLogros(Model model, HttpSession session) {
		Object usuario = session.getAttribute("usuario");
		if (usuario == null) {
			return "/PanelControlUsuario/mis_logros";
		}
		model.addAttribute("usuario", usuario);
		return "PanelControlUsuario/mis_logros";
	}

	@GetMapping("/PanelControlUsuario/modulo1")
	public String modulo1() {
		return "PanelControlUsuario/modulo1";
	}

	@GetMapping("/PanelControlUsuario/modulo2")
	public String modulo2() {
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

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		// Invalidar la sesión
		session.invalidate();
		return "redirect:/";
	}

}
