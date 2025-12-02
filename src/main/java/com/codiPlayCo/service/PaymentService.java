package com.codiPlayCo.service;

import com.codiPlayCo.dto.PaymentRequest;
import com.codiPlayCo.model.Pago;
import com.codiPlayCo.model.RegistroPago;
import com.codiPlayCo.repository.PagoRepository;
import com.codiPlayCo.repository.RegistroPagoRepository;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class PaymentService {

    private final PagoRepository pagoRepository;
    private final RegistroPagoRepository registroRepo;

    @Value("${stripe.secret.key}")
    private String secretKey;

    public PaymentService(PagoRepository pagoRepository, RegistroPagoRepository registroRepo) {
        this.pagoRepository = pagoRepository;
        this.registroRepo = registroRepo;
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

    // Guardar pago en BD y actualizar registroPago si existe
    public Pago guardarPago(Pago pago, Integer registroPagoId) {
        // Si se pasó registroPagoId, actualizar estado
        if (registroPagoId != null) {
            Optional<RegistroPago> rpOpt = registroRepo.findById(registroPagoId);
            if (rpOpt.isPresent()) {
                RegistroPago rp = rpOpt.get();
                rp.setEstadoPago("pagado");
                registroRepo.save(rp);
            }
        }
        return pagoRepository.save(pago);
    }
}
