package com.ues.edu.sv.rpups_ues.model.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class GenerateSolicitudDTO {

    @NotNull
    private Long idSolicitud;

    @NotNull
    private String titulo;

    @NotNull
    private String descripcion;

    @NotNull
    private String requisitos;

    @NotNull
    private LocalDate fechaInicio;

    @NotNull
    private LocalDate fechaFin;

    @NotNull
    private Integer duracion;

    @NotNull
    private Integer maxEstudiantes;

    @NotNull
    private String direccionDetallada;

    @NotNull
    private Long idEmpresa;

    @NotNull
    private String codigoDepartamento;

    @NotNull
    private String codigoMunicipio;

    @NotNull
    private String codigoCarrera;

    @NotNull
    private String codigoModalidad;

    @NotNull
    private Long user_creador;

    @NotNull
    private String codEstadoSolicitud;

}
