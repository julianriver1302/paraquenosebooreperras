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
	private String estado;

	@OneToMany(mappedBy = "curso")
	private List<Registro> registro;

	@ManyToOne
	private AsignacionDocente asignacionDocente;

	public Curso() {

	}

	public Curso(Integer id, String curso, String estado, List<Registro> registro,
			AsignacionDocente asignacionDocente) {
		super();
		this.id = id;
		this.curso = curso;
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
		return "Curso [id=" + id + ", curso=" + curso + ", estado=" + estado + "]";
	}

}
