package com.codiPlayCo.model;

import java.util.HashMap;
import java.util.Map;

public class ProgresoPorModuloDTO {

	private Integer idEstudiante;
	private String nombreCompleto;

	// mapa: modulo -> (leccion -> completada)
	private Map<Integer, Map<Integer, Boolean>> progreso = new HashMap<>();

	// último módulo y lección completados por el estudiante
	private Integer moduloActual;
	private Integer leccionActual;

	public ProgresoPorModuloDTO(Integer idEstudiante, String nombreCompleto) {
		this.idEstudiante = idEstudiante;
		this.nombreCompleto = nombreCompleto;
	}

	public Integer getIdEstudiante() {
		return idEstudiante;
	}

	public String getNombreCompleto() {
		return nombreCompleto;
	}

	public Map<Integer, Map<Integer, Boolean>> getProgreso() {
		return progreso;
	}

	public void marcarLeccion(Integer modulo, Integer leccion, boolean completada) {
		progreso.computeIfAbsent(modulo, k -> new HashMap<>()).put(leccion, completada);
	}

	public boolean isCompletada(Integer modulo, Integer leccion) {
		return progreso.containsKey(modulo) && Boolean.TRUE.equals(progreso.get(modulo).get(leccion));
	}

	public Integer getModuloActual() {
		return moduloActual;
	}

	public void setModuloActual(Integer moduloActual) {
		this.moduloActual = moduloActual;
	}

	public Integer getLeccionActual() {
		return leccionActual;
	}

	public void setLeccionActual(Integer leccionActual) {
		this.leccionActual = leccionActual;
	}
}
