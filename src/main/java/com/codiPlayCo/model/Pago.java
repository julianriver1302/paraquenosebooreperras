package com.codiPlayCo.model;

import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "pagos")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer precio;               // precio en pesos (Integer)

    private String stripePaymentId;       // PaymentIntent.id

    private String estado;                // "succeeded", "requires_payment_method", "failed", "pending"

    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaPago = new Date();

    @ManyToOne
    @JoinColumn(name = "curso_id")
    private Curso curso;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public Pago() {}

    // getters / setters (incluyendo curso y usuario)
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getPrecio() { return precio; }
    public void setPrecio(Integer precio) { this.precio = precio; }

    public String getStripePaymentId() { return stripePaymentId; }
    public void setStripePaymentId(String stripePaymentId) { this.stripePaymentId = stripePaymentId; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Date getFechaPago() { return fechaPago; }
    public void setFechaPago(Date fechaPago) { this.fechaPago = fechaPago; }

    public Curso getCurso() { return curso; }
    public void setCurso(Curso curso) { this.curso = curso; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    @Override
    public String toString() {
        return "Pago [id=" + id + ", precio=" + precio + ", stripePaymentId=" + stripePaymentId + ", estado=" + estado
                + ", fechaPago=" + fechaPago + "]";
    }
}
