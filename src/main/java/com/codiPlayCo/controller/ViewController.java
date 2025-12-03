package com.codiPlayCo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

	@GetMapping("/payment-success")
	public String paymentSuccess() {
		return "payment-success";
	}

	@GetMapping("/payment-failed")
	public String paymentFailed() {
		return "payment-failed";
	}
}
