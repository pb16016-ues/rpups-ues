package com.ues.edu.sv.rpups_ues.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CuposProyectoDTO {

    private Long idProyecto;
    private String tituloProyecto;
    private Integer maxEstudiantes;
    private Long estudiantesAceptados;
    private Integer cuposDisponibles;
    private Long postulacionesPendientes;

    public CuposProyectoDTO(Long idProyecto, Integer maxEstudiantes, Long estudiantesAceptados, Integer cuposDisponibles) {
        this.idProyecto = idProyecto;
        this.maxEstudiantes = maxEstudiantes;
        this.estudiantesAceptados = estudiantesAceptados;
        this.cuposDisponibles = cuposDisponibles;
    }
}
