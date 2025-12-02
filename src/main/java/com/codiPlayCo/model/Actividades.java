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
@Table(name = "actividaes")
public class Actividades {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String fecha;
	private String tarea;
	private String fechaEntrega;
	private Integer Curso;

	// Módulo y lección a la que pertenece esta actividad
	private Integer modulo;
	private Integer leccion;

	@ManyToOne
	private Usuario usuario;

	@OneToMany(mappedBy = "actividades")
	private List<ActividadesEstudiantes> actividadesEstuduiantes;

	public Actividades() {

	}

	public Actividades(Integer id, String fecha, String tarea, String fechaEntrega, Integer curso, Integer modulo, Integer leccion, Usuario usuario,
			List<ActividadesEstudiantes> actividadesEstuduiantes) {
		super();
		this.id = id;
		this.fecha = fecha;
		this.tarea = tarea;
		this.fechaEntrega = fechaEntrega;
		Curso = curso;
		this.modulo = modulo;
		this.leccion = leccion;
		this.usuario = usuario;
		this.actividadesEstuduiantes = actividadesEstuduiantes;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getTarea() {
		return tarea;
	}

	public void setTarea(String tarea) {
		this.tarea = tarea;
	}

	public String getFechaEntrega() {
		return fechaEntrega;
	}

	public void setFechaEntrega(String fechaEntrega) {
		this.fechaEntrega = fechaEntrega;
	}

	public Integer getCurso() {
		return Curso;
	}

	public void setCurso(Integer curso) {
		Curso = curso;
	}

	public Integer getModulo() {
		return modulo;
	}

	public void setModulo(Integer modulo) {
		this.modulo = modulo;
	}

	public Integer getLeccion() {
		return leccion;
	}

	public void setLeccion(Integer leccion) {
		this.leccion = leccion;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<ActividadesEstudiantes> getActividadesEstuduiantes() {
		return actividadesEstuduiantes;
	}

	public void setActividadesEstuduiantes(List<ActividadesEstudiantes> actividadesEstuduiantes) {
		this.actividadesEstuduiantes = actividadesEstuduiantes;
	}

	@Override
	public String toString() {
		return "Actividades [id=" + id + ", fecha=" + fecha + ", tarea=" + tarea + ", fechaEntrega=" + fechaEntrega
				+ ", Curso=" + Curso + "]";
	}

}