package com.codiPlayCo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cursos")
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String curso;
    private String estado;
    private String descripcion;
    private Integer dificultad; // CAMBIADO de String a Integer
    private String dirigido;
    private Double precio;

    @Column(name = "asignacionDocente_id")
    private String asignacionDocente; // CAMBIADO de Integer a String

    // Constructor vacío
    public Curso() {}

    // Getters y setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getCurso() { return curso; }
    public void setCurso(String curso) { this.curso = curso; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Integer getDificultad() { return dificultad; }
    public void setDificultad(Integer dificultad) { this.dificultad = dificultad; }

    public String getDirigido() { return dirigido; }
    public void setDirigido(String dirigido) { this.dirigido = dirigido; }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }

    public String getAsignacionDocente() { return asignacionDocente; }
    public void setAsignacionDocente(String asignacionDocente) { this.asignacionDocente = asignacionDocente; }
}