package com.codiPlayCo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.codiPlayCo.model.Pago;
import com.codiPlayCo.repository.PagoRepository;

@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    public Pago guardar(Pago pago) {
        return pagoRepository.save(pago);
    }
}
