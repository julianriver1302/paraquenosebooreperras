package com.codiPlayCo.model;

public class ProgresoEstudianteDTO {

    private Integer idEstudiante;
    private String nombreCompleto;
    private Double porcentaje;

    public ProgresoEstudianteDTO(Integer idEstudiante, String nombreCompleto, Double porcentaje) {
        this.idEstudiante = idEstudiante;
        this.nombreCompleto = nombreCompleto;
        this.porcentaje = porcentaje;
    }

    public Integer getIdEstudiante() {
        return idEstudiante;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public Double getPorcentaje() {
        return porcentaje;
    }
}
