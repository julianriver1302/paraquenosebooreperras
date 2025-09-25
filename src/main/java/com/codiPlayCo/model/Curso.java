package com.codiPlayCo.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "cursos")
public class Curso {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String curso;
	private String descripcion;
	private String dirigido;
	private String dificultad;
	private String precio;
	private String estado;

	@OneToMany(mappedBy = "curso")
	private List<Registro> registro;

	@ManyToOne
	private AsignacionDocente asignacionDocente;

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDificultad() {
		return dificultad;
	}

	public void setDificultad(String dificultad) {
		this.dificultad = dificultad;
	}

	public Curso() {

	}

	public String getDirigido() {
		return dirigido;
	}

	public void setDirigido(String dirigido) {
		this.dirigido = dirigido;
	}

	public String getPrecio() {
		return precio;
	}

	public void setPrecio(String precio) {
		this.precio = precio;
	}

	public Curso(Integer id, String curso, String descripcion, String dirigido, String dificultad, String precio,
			String estado, List<Registro> registro, AsignacionDocente asignacionDocente) {
		super();
		this.id = id;
		this.curso = curso;
		this.descripcion = descripcion;
		this.dirigido = dirigido;
		this.dificultad = dificultad;
		this.precio = precio;
		this.estado = estado;
		this.registro = registro;
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

	public List<Registro> getRegistro() {
		return registro;
	}

	public void setRegistro(List<Registro> registro) {
		this.registro = registro;
	}

	public AsignacionDocente getAsignacionDocente() {
		return asignacionDocente;
	}

	public void setAsignacionDocente(AsignacionDocente asignacionDocente) {
		this.asignacionDocente = asignacionDocente;
	}

	@Override
	public String toString() {
		return "Curso [id=" + id + ", curso=" + curso + ", descripcion=" + descripcion + ", dirigido=" + dirigido
				+ ", dificultad=" + dificultad + ", precio=" + precio + ", estado=" + estado + "]";
	}

}
