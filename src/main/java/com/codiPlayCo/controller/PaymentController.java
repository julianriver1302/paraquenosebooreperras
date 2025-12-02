package com.codiPlayCo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.codiPlayCo.dto.PaymentRequest;
import com.codiPlayCo.model.Pago;
import com.codiPlayCo.repository.ICursoRepository; // si lo usas
import com.codiPlayCo.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

	@Autowired
	private PaymentService paymentService;

	// 1) Crear PaymentIntent
	@PostMapping("/create-payment-intent")
	public ResponseEntity<?> createPaymentIntent(@RequestBody PaymentRequest request) {
		try {
			Map<String, Object> resp = paymentService.createPaymentIntent(request);
			return ResponseEntity.ok(resp);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}
	}

	// 2) Confirmar y guardar pago
	// El front enviará { registroId, precio, stripePaymentId, estado, cursoId,
	// usuarioId(optional) }
	@PostMapping("/confirm")
	public ResponseEntity<?> confirmarPago(@RequestBody Pago pago,
			@RequestParam(name = "registroId", required = false) Integer registroId) {
		try {
			Pago pagoGuardado = paymentService.guardarPago(pago, registroId);
			return ResponseEntity.ok(Map.of("status", "success", "data", pagoGuardado));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(Map.of("status", "error", "message", e.getMessage()));
		}
	}
}
