package com.codiPlayCo.service;

import com.codiPlayCo.dto.PaymentRequest;
import com.codiPlayCo.model.Pago;
import com.codiPlayCo.model.RegistroPago;
import com.codiPlayCo.model.Usuario;
import com.codiPlayCo.model.Rol;
import com.codiPlayCo.repository.PagoRepository;
import com.codiPlayCo.repository.RegistroPagoRepository;
import com.codiPlayCo.service.IUsuarioService;
import com.codiPlayCo.service.RolService;
import com.codiPlayCo.service.EmailService;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class PaymentService {

    private final PagoRepository pagoRepository;
    private final RegistroPagoRepository registroRepo;
    private final IUsuarioService usuarioService;
    private final RolService rolService;
    private final EmailService emailService;

    @Value("${stripe.secret.key}")
    private String secretKey;

    public PaymentService(PagoRepository pagoRepository, RegistroPagoRepository registroRepo,
            IUsuarioService usuarioService, RolService rolService, EmailService emailService) {
        this.pagoRepository = pagoRepository;
        this.registroRepo = registroRepo;
        this.usuarioService = usuarioService;
        this.rolService = rolService;
        this.emailService = emailService;
    }

    // Crear PaymentIntent
    public Map<String, Object> createPaymentIntent(PaymentRequest request) throws Exception {
        Stripe.apiKey = secretKey;

        // request.getAmount() ya es Integer de centavos
        if (request.getAmount() == null) {
            throw new IllegalArgumentException("Amount (centavos) no puede ser null");
        }

        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(request.getAmount().longValue()) // long conversion
                        .setCurrency(request.getCurrency())
                        .setDescription(request.getDescription())
                        .build();

        PaymentIntent intent = PaymentIntent.create(params);

        Map<String, Object> response = new HashMap<>();
        response.put("clientSecret", intent.getClientSecret());
        response.put("paymentIntentId", intent.getId());
        return response;
    }

    // Guardar pago en BD, actualizar registroPago si existe e inscribir al usuario
    public Pago guardarPago(Pago pago, Integer registroPagoId) {
        System.out.println("=== INICIANDO GUARDADO DE PAGO ===");
        System.out.println("Pago recibido: " + pago);
        System.out.println("registroPagoId: " + registroPagoId);
        
        // Si se pasó registroPagoId, actualizar estado y crear/obtener usuario estudiante
        // asociado al curso del registro
        if (registroPagoId != null) {
            System.out.println("Buscando registro de pago con ID: " + registroPagoId);
            Optional<RegistroPago> rpOpt = registroRepo.findById(registroPagoId);
            if (rpOpt.isPresent()) {
                RegistroPago rp = rpOpt.get();
                System.out.println("Registro encontrado: " + rp);
                
                rp.setEstadoPago("pagado");
                registroRepo.save(rp);
                System.out.println("Estado de registro actualizado a 'pagado'");

                String email = rp.getEmail();
                Integer cursoId = rp.getCursoId();
                System.out.println("Email del registro: " + email);
                System.out.println("Curso ID del registro: " + cursoId);

                if (email != null && cursoId != null) {
                    System.out.println("Buscando usuario con email: " + email);
                    Optional<Usuario> usuarioOpt = usuarioService.findByEmail(email);
                    Usuario usuario;

                    if (usuarioOpt.isPresent()) {
                        // Usuario ya existe: actualizar datos faltantes desde RegistroPago
                        usuario = usuarioOpt.get();
                        System.out.println("Usuario existente encontrado: " + usuario.getId());

                        if (usuario.getNombre() == null || usuario.getNombre().isBlank()) {
                            usuario.setNombre(rp.getNombre());
                        }
                        if (usuario.getApellido() == null || usuario.getApellido().isBlank()) {
                            usuario.setApellido(rp.getApellido());
                        }
                        if (usuario.getTipoDocumento() == null || usuario.getTipoDocumento().isBlank()) {
                            usuario.setTipoDocumento(rp.getTipoDocumento());
                        }
                        if (usuario.getDocumento() == null || usuario.getDocumento().isBlank()) {
                            usuario.setDocumento(rp.getDocumento());
                        }
                        if (usuario.getCelular() == null || usuario.getCelular().isBlank()) {
                            usuario.setCelular(rp.getTelefono());
                        }
                        if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
                            usuario.setPassword(rp.getPassword());
                        }
                        if (usuario.getActivo() == null || usuario.getActivo().isBlank()) {
                            usuario.setActivo("activo");
                        }

                        if (usuario.getRol() == null) {
                            Optional<Rol> rolEstudianteOpt = rolService.findByNombre("ESTUDIANTE");
                            rolEstudianteOpt.ifPresent(usuario::setRol);
                        }

                        usuario = usuarioService.save(usuario);
                        System.out.println("Usuario actualizado: " + usuario.getId());
                    } else {
                        // Usuario nuevo a partir del registro de pago
                        System.out.println("Creando nuevo usuario");
                        Usuario nuevo = new Usuario();
                        nuevo.setNombre(rp.getNombre());
                        nuevo.setApellido(rp.getApellido());
                        nuevo.setTipoDocumento(rp.getTipoDocumento());
                        nuevo.setDocumento(rp.getDocumento());
                        nuevo.setCelular(rp.getTelefono());
                        nuevo.setPassword(rp.getPassword());
                        nuevo.setEmail(email);
                        nuevo.setActivo("activo");
                        nuevo.setFecharegistro(new Date(System.currentTimeMillis()));

                        Optional<Rol> rolEstudianteOpt = rolService.findByNombre("ESTUDIANTE");
                        rolEstudianteOpt.ifPresent(nuevo::setRol);

                        usuario = usuarioService.save(nuevo);
                        System.out.println("Nuevo usuario creado: " + usuario.getId());
                    }

                    if (usuario != null) {
                        pago.setUsuarioId(usuario.getId());
                        pago.setCursoId(cursoId);
                        System.out.println("Asignado usuarioId: " + usuario.getId() + " y cursoId: " + cursoId + " al pago");
                    }
                } else {
                    System.out.println("ERROR: Email o cursoId son nulos en el registro");
                }
            } else {
                System.out.println("ERROR: No se encontró registro con ID: " + registroPagoId);
            }
        } else {
            System.out.println("ADVERTENCIA: registroPagoId es nulo");
        }

        Pago pagoGuardado = pagoRepository.save(pago);
        System.out.println("Pago guardado en BD: " + pagoGuardado.getId());

        // Inscribir en el curso siempre que existan ids válidos
        System.out.println("=== VERIFICANDO CONDICIONES PARA ENVÍO DE CORREO ===");
        System.out.println("pagoGuardado.getUsuarioId(): " + pagoGuardado.getUsuarioId());
        System.out.println("pagoGuardado.getCursoId(): " + pagoGuardado.getCursoId());
        
        if (pagoGuardado.getUsuarioId() != null && pagoGuardado.getCursoId() != null) {
            System.out.println("=== CONDICIONES CUMPLIDAS - PROCEDIENDO CON INSCRIPCIÓN Y CORREO ===");
            try {
                System.out.println("Inscribiendo usuario en curso...");
                usuarioService.inscribirEnCurso(pagoGuardado.getUsuarioId(), pagoGuardado.getCursoId());
                System.out.println("Usuario inscrito exitosamente");

                // Enviar correo de confirmación de pago al usuario
                System.out.println("=== INICIANDO ENVÍO DE CORREO ===");
                Optional<Usuario> usuarioOpt = usuarioService.findById(pagoGuardado.getUsuarioId());
                if (usuarioOpt.isPresent()) {
                    Usuario u = usuarioOpt.get();
                    System.out.println("Usuario encontrado para correo: " + u.getNombre() + " (" + u.getEmail() + ")");
                    
                    String subject = "Pago confirmado - Acceso a tu curso en CodiPlayCo";
                    String body = "Hola " + u.getNombre() + ",\n\n" +
                            "Hemos recibido tu pago correctamente. Ya tienes acceso al curso que acabas de comprar.\n\n" +
                            "Puedes iniciar sesión en CodiPlayCo con tu correo " + u.getEmail() + ".\n\n" +
                            "Gracias por aprender con nosotros.\n" +
                            "Equipo CodiPlayCo";
                    
                    System.out.println("Llamando a emailService.enviarCorreo...");
                    emailService.enviarCorreo(u.getEmail(), subject, body);
                    System.out.println("=== CORREO ENVIADO EXITOSAMENTE ===");
                } else {
                    System.out.println("ERROR: No se encontró el usuario para enviar correo");
                }
            } catch (Exception e) {
                // No romper el flujo de pago si falla la inscripción o el envío de correo
                System.err.println("Error al enviar correo de confirmación: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("=== ERROR: CONDICIONES NO CUMPLIDAS PARA ENVÍO DE CORREO ===");
            System.out.println("No se puede enviar correo - usuarioId: " + pagoGuardado.getUsuarioId() + ", cursoId: " + pagoGuardado.getCursoId());
        }

        return pagoGuardado;
    }
}
