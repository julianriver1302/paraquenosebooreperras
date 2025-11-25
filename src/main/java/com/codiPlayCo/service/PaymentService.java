package com.codiPlayCo.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.codiPlayCo.dto.PaymentRequest;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import jakarta.annotation.PostConstruct;

@Service
public class PaymentService {

    @Value("${stripe.secret.key}")
    private String secretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    public Map<String, Object> createPaymentIntent(PaymentRequest request) {

        try {

            if (request.getPrecio() == null) {
                throw new RuntimeException("El precio en PaymentRequest llegó NULL");
            }

            Long amountEnCentavos = request.getPrecio().longValue() * 100;

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amountEnCentavos)
                    .setCurrency(request.getCurrency())
                    .setDescription(request.getDescription())
                    .putMetadata("nombre", request.getNombre())
                    .putMetadata("email", request.getEmail())
                    .putMetadata("telefono", request.getTelefono())
                    .build();

            PaymentIntent intent = PaymentIntent.create(params);

            Map<String, Object> response = new HashMap<>();
            response.put("clientSecret", intent.getClientSecret());
            return response;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
