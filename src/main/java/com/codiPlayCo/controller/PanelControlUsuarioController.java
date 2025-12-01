package com.codiPlayCo.controller;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.codiPlayCo.model.Usuario;
import com.codiPlayCo.model.Curso;
import com.codiPlayCo.service.IUsuarioService;
import com.codiPlayCo.service.ICursoService;

import jakarta.servlet.http.HttpSession;

@Controller
public class PanelControlUsuarioController {

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private ICursoService cursoService; // ✅ Servicio para manejar cursos

    @GetMapping("/PanelControlUsuario/inicio")
    public String inicio(HttpSession session, Model model) {
        Integer idUsuario = (Integer) session.getAttribute("idUsuario");

        if (idUsuario != null) {
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

        Usuario usuario = new Usuario();

        if (idUsuario != null) {
            Optional<Usuario> usuarioOpt = usuarioService.findById(idUsuario);
            if (usuarioOpt.isPresent()) {
                usuario = usuarioOpt.get();
            }
        }

        model.addAttribute("usuario", usuario);
        return "PanelControlUsuario/editar-perfil";
    }

    @PostMapping("/PanelControlUsuario/editar-perfil")
    public String actualizarPerfil(@ModelAttribute Usuario usuario, HttpSession session,
            RedirectAttributes redirectAttrs, MultipartFile avatarFile) {
        try {
            Integer idUsuario = (Integer) session.getAttribute("idUsuario");

            if (idUsuario != null) {
                Optional<Usuario> usuarioExistenteOpt = usuarioService.findById(idUsuario);
                if (usuarioExistenteOpt.isPresent()) {
                    Usuario usuarioExistente = usuarioExistenteOpt.get();

                    usuarioExistente.setNombre(usuario.getNombre());
                    usuarioExistente.setApellido(usuario.getApellido());
                    usuarioExistente.setEmail(usuario.getEmail());
                    usuarioExistente.setCelular(usuario.getCelular());
                    usuarioExistente.setDocumento(usuario.getDocumento());

                    if (avatarFile != null && !avatarFile.isEmpty()) {
                        String contentType = avatarFile.getContentType();
                        if (contentType != null && contentType.startsWith("image/")) {
                            if (avatarFile.getSize() <= 2 * 1024 * 1024) {
                                Path uploadDir = Paths.get("target/classes/static/uploads/avatars");
                                if (!Files.exists(uploadDir)) {
                                    Files.createDirectories(uploadDir);
                                }

                                String originalFilename = avatarFile.getOriginalFilename();
                                String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                                String newFilename = UUID.randomUUID().toString() + extension;

                                Path filePath = uploadDir.resolve(newFilename);
                                Files.copy(avatarFile.getInputStream(), filePath);

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

    // ✅ NUEVO MÉTODO: muestra los cursos del usuario autenticado
    @GetMapping("/PanelControlUsuario/mis_cursos")
    public String misCursos(Model model, HttpSession session) {
        Integer idUsuario = (Integer) session.getAttribute("idUsuario");

        if (idUsuario == null) {
            return "redirect:/login";
        }

        Optional<Usuario> usuarioOpt = usuarioService.findById(idUsuario);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            model.addAttribute("usuario", usuario);

            // 🔥 Obtiene los cursos del usuario desde la BD
            List<Curso> cursos = cursoService.findByUsuarioId(usuario.getId());
            model.addAttribute("cursos", cursos);
        }

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

    @GetMapping("/PanelControlUsuario/bandeja")
    public String bandeja(HttpSession session, Model model) {
        Integer idUsuario = (Integer) session.getAttribute("idUsuario");
        if (idUsuario != null) {
            usuarioService.findById(idUsuario).ifPresent(u -> model.addAttribute("usuario", u));
        }
        return "PanelControlUsuario/bandeja";
    }

    @PostMapping("/PanelControlUsuario/modulo2/leccion1/completar")
    public String completarLeccion1(HttpSession session) {
        Integer idUsuario = (Integer) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/login";
        }

        usuarioService.findById(idUsuario).ifPresent(u -> {
            u.setMod2L1Completada(true);
            usuarioService.save(u);
        });

        return "redirect:/PanelControlUsuario/modulo2";
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
        session.invalidate();
        return "redirect:/";
    }
}
