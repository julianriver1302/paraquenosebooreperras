package com.codiPlayCo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.codiPlayCo.model.Pago;
import com.codiPlayCo.model.RegistroPago;
import com.codiPlayCo.repository.RegistroPagoRepository;
import com.codiPlayCo.service.PagoService;

@RestController
@RequestMapping("/api/registro")
public class RegistroPagoRestController {

    @Autowired
    private RegistroPagoRepository repo;

    @PostMapping("/guardar")
    public RegistroPago guardar(@RequestBody RegistroPago registro) {
        registro.setEstadoPago("pendiente");
        return repo.save(registro);
    }

    @Autowired
    private PagoService pagoService;

    @PostMapping("/procesar")
    public ResponseEntity<String> procesarPago(@RequestBody Pago pago) {

        if (!"aprobado".equalsIgnoreCase(pago.getEstado())) {
            return ResponseEntity.badRequest().body("El pago no fue aprobado");
        }

        try {
            pagoService.procesarPago(pago);
            return ResponseEntity.ok("Pago procesado e inscripción realizada con éxito.");

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al procesar pago: " + e.getMessage());
        }
    }
}
