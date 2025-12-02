package com.codiPlayCo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.codiPlayCo.model.RegistroPago;
import com.codiPlayCo.repository.RegistroPagoRepository;

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
}
