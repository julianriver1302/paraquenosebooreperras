package com.codiPlayCo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.codiPlayCo.dto.PaymentRequest;
import com.codiPlayCo.model.Pago;
import com.codiPlayCo.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

	@Autowired
	private PaymentService paymentService;

	/**
	 * 1️⃣ Crear el PaymentIntent en Stripe
	 */
	@PostMapping("/create-payment-intent")
	public ResponseEntity<?> createPaymentIntent(@RequestBody PaymentRequest request) {
		try {
			return ResponseEntity.ok(paymentService.createPaymentIntent(request));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}
	}

	/**
	 * 2️⃣ Confirmar el pago y guardarlo en BD Este endpoint lo llama el front
	 * cuando Stripe confirma el pago.
	 */
	@PostMapping("/confirm")
	public ResponseEntity<?> confirmarPago(@RequestBody Pago pago) {
		try {
			Pago pagoGuardado = paymentService.guardar(pago);
			return ResponseEntity
					.ok(Map.of("status", "success", "message", "Pago guardado exitosamente", "data", pagoGuardado));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(Map.of("status", "error", "message", e.getMessage()));
		}
	}
}
