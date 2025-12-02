package com.codiPlayCo.dto;

public class PaymentRequest {

    // precio en pesos (Integer) — lo usa el front
    private Integer precio;

    // amount para Stripe (centavos) — se calcula automáticamente
    private Integer amount;

    private String currency;    // ej: "cop"
    private String description;

    // datos del cliente (opcional)
    private String nombre;
    private String email;
    private String telefono;

    // Getters / Setters
    public Integer getPrecio() { return precio; }
    public void setPrecio(Integer precio) {
        this.precio = precio;
        // conversion automática a centavos (Integer)
        if (precio != null) {
            this.amount = precio * 100;
        } else {
            this.amount = null;
        }
    }

    public Integer getAmount() { return amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}
