package com.ues.edu.sv.rpups_ues.model.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CambioEstadoPostulacionDTO {

    @NotNull(message = "El ID de la postulación es requerido")
    private Long idPostulacion;

    @NotBlank(message = "El nuevo estado es requerido")
    private String nuevoEstado;

    private String observaciones;
}
