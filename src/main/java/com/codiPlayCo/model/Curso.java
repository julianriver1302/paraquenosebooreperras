package com.codiPlayCo.model;

import java.util.List;

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

	@ManyToOne
	private AsignacionDocente asignacionDocente;

	// 🔹 Relación inversa (usuarios que compraron este curso)
	@ManyToMany(mappedBy = "cursosComprados")
	private List<Usuario> usuarios;
	@ManyToOne
	private Usuario usuario;

	public Curso() {
	}

	public Curso(Integer id, String curso, String estado, String descripcion, Integer dificultad, String dirigido,
			Double precio, AsignacionDocente asignacionDocente) {
		super();
		this.id = id;
		this.curso = curso;
		this.estado = estado;
		this.descripcion = descripcion;
		this.dificultad = dificultad;
		this.dirigido = dirigido;
		this.precio = precio;
		this.asignacionDocente = asignacionDocente;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCurso() {
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Integer getDificultad() {
		return dificultad;
	}

	public void setDificultad(Integer dificultad) {
		this.dificultad = dificultad;
	}

	public String getDirigido() {
		return dirigido;
	}

	public void setDirigido(String dirigido) {
		this.dirigido = dirigido;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public AsignacionDocente getAsignacionDocente() {
		return asignacionDocente;
	}

	public void setAsignacionDocente(AsignacionDocente docente) {
		this.asignacionDocente = docente;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Override
	public String toString() {
		return "Curso [id=" + id + ", curso=" + curso + ", estado=" + estado + ", descripcion=" + descripcion
				+ ", dificultad=" + dificultad + ", dirigido=" + dirigido + ", precio=" + precio + "]";
	}
}
