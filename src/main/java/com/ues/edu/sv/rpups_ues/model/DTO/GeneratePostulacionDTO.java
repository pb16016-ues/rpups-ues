package com.ues.edu.sv.rpups_ues.model.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GeneratePostulacionDTO {

    @NotNull
    private Long idUsuario;
    @NotNull
    private Long idProyecto;

}
