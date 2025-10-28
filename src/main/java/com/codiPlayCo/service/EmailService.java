package com.codiPlayCo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Método genérico
    public void enviarCorreo(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    // Método específico para la clase gratis
    public void enviarCorreoConfirmacion(String email, String nombre) {
        String asunto = "🎉 ¡Tu clase gratis está confirmada! - CodiPlayCo";
        String cuerpo = "Hola " + nombre + ",\n\n"
                + "🎮 ¡Gracias por registrarte a tu clase gratuita en *CodiPlayCo*! 🎮\n\n"
                + "🗓 Fecha: Sábado 2 de noviembre de 2025\n"
                + "⏰ Hora: 10:00 a.m.\n"
                + "📍 Lugar: CodiPlayCo - Duitama, Boyacá\n\n"
                + "Te esperamos con toda la energía para aprender programación de forma divertida 🚀\n\n"
                + "Si tienes alguna duda, puedes escribirnos a codiplayco@gmail.com.\n\n"
                + "¡Nos vemos pronto!\n"
                + "El equipo de CodiPlayCo 💚";

        enviarCorreo(email, asunto, cuerpo);
    }
}
