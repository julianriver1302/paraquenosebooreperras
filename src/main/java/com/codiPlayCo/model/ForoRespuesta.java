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
@Table(name = "foro_respuestas")
public class ForoRespuesta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "foro_id")
	private Foro foro;

	@ManyToOne
	@JoinColumn(name = "autor_id")
	private Usuario autor;

	private String mensaje;

	private LocalDateTime fechaCreacion;

	public ForoRespuesta() {
	}

	public ForoRespuesta(Foro foro, Usuario autor, String mensaje, LocalDateTime fechaCreacion) {
		this.foro = foro;
		this.autor = autor;
		this.mensaje = mensaje;
		this.fechaCreacion = fechaCreacion;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Foro getForo() {
		return foro;
	}

	public void setForo(Foro foro) {
		this.foro = foro;
	}

	public Usuario getAutor() {
		return autor;
	}

	public void setAutor(Usuario autor) {
		this.autor = autor;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
}
