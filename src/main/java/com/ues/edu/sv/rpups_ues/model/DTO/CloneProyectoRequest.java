package com.ues.edu.sv.rpups_ues.model.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * DTO para solicitar la clonación de un proyecto con nuevos estudiantes
 */
@Data
public class CloneProyectoRequest {
    
    @NotNull(message = "La lista de IDs de estudiantes no puede ser nula")
    @Size(min = 1, max = 5, message = "Debe seleccionar entre 1 y 5 estudiantes")
    private List<Long> idsEstudiantes;
}
