package com.codiPlayCo.model;

import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "pagos")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer precio;

    private String stripePaymentId;

    private String estado;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaPago = new Date();

    // 🔥 Campos que pediste para asociar registros
    @Column(name = "usuario_id")
    private Integer usuarioId;

    @Column(name = "curso_id")
    private Integer cursoId;

    // 🔥 Relaciones opcionales si las necesitas
    @ManyToOne
    @JoinColumn(name = "curso_id", insertable = false, updatable = false)
    private Curso curso;

    @ManyToOne
    @JoinColumn(name = "usuario_id", insertable = false, updatable = false)
    private Usuario usuario;

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

    public Integer getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }

    public Integer getCursoId() { return cursoId; }
    public void setCursoId(Integer cursoId) { this.cursoId = cursoId; }

    public Pago() {}

    public Pago(Integer id, Integer precio, String stripePaymentId, String estado, Date fechaPago, Integer cursoId, Integer usuarioId) {
        this.id = id;
        this.precio = precio;
        this.stripePaymentId = stripePaymentId;
        this.estado = estado;
        this.fechaPago = fechaPago;
        this.cursoId = cursoId;
        this.usuarioId = usuarioId;
    }

    @Override
    public String toString() {
        return "Pago [id=" + id + ", precio=" + precio + ", stripePaymentId=" + stripePaymentId +
                ", estado=" + estado + ", fechaPago=" + fechaPago +
                ", usuarioId=" + usuarioId + ", cursoId=" + cursoId + "]";
    }
}
