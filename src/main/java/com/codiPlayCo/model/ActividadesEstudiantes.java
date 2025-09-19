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
@Table(name = "actividadesEstudiantes")
public class ActividadesEstudiantes {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String fechaEnvio;
	private String estado;
	private String calificacion;
	private Integer idEstudiante;

	@ManyToOne
	private Actividades actividades;

	@ManyToOne
	private Usuario usuario;

	@OneToMany
	private List<Registro> registro;

	public ActividadesEstudiantes() {

	}

	public ActividadesEstudiantes(Integer id, String fechaEnvio, String estado, String calificacion, Integer idEstudiante,
			Actividades actividades, Usuario usuario, List<Registro> registro) {
		super();
		this.id = id;
		this.fechaEnvio = fechaEnvio;
		this.estado = estado;
		this.calificacion = calificacion;
		this.idEstudiante = idEstudiante;
		this.actividades = actividades;
		this.usuario = usuario;
		this.registro = registro;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFechaEnvio() {
		return fechaEnvio;
	}

	public void setFechaEnvio(String fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getCalificacion() {
		return calificacion;
	}

	public void setCalificacion(String calificacion) {
		this.calificacion = calificacion;
	}

	public Integer getIdEstudiante() {
		return idEstudiante;
	}

	public void setIdEstudiante(Integer idEstudiante) {
		this.idEstudiante = idEstudiante;
	}

	public Actividades getActividades() {
		return actividades;
	}

	public void setActividades(Actividades actividades) {
		this.actividades = actividades;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Registro> getRegistro() {
		return registro;
	}

	public void setRegistro(List<Registro> registro) {
		this.registro = registro;
	}

	@Override
	public String toString() {
		return "ActividadesEstudiantes [id=" + id + ", fechaEnvio=" + fechaEnvio + ", estado=" + estado
				+ ", calificacion=" + calificacion + ", idEstudiante=" + idEstudiante + "]";
	}

}
