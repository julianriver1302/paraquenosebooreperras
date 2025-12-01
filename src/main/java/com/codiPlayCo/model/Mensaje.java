package com.codiPlayCo.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "mensajes")
public class Mensaje {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "remitente_id", nullable = false)
    private Usuario remitente;
    
    @ManyToOne
    @JoinColumn(name = "destinatario_id", nullable = false)
    private Usuario destinatario;
    
    private String asunto;
    private String contenido;
    private LocalDateTime fechaEnvio;
    private boolean leido;
    
    public Mensaje() {
        this.fechaEnvio = LocalDateTime.now();
        this.leido = false;
    }
    
    // Getters and Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Usuario getRemitente() {
        return remitente;
    }
    
    public void setRemitente(Usuario remitente) {
        this.remitente = remitente;
    }
    
    public Usuario getDestinatario() {
        return destinatario;
    }
    
    public void setDestinatario(Usuario destinatario) {
        this.destinatario = destinatario;
    }
    
    public String getAsunto() {
        return asunto;
    }
    
    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }
    
    public String getContenido() {
        return contenido;
    }
    
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
    
    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }
    
    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }
    
    public boolean isLeido() {
        return leido;
    }
    
    public void setLeido(boolean leido) {
        this.leido = leido;
    }
}
