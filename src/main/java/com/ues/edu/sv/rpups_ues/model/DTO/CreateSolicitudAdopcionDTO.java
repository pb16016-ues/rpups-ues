package com.ues.edu.sv.rpups_ues.model.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateSolicitudAdopcionDTO {

    @NotNull(message = "El ID de la mascota no puede ser nulo")
    private Long idMascota;
    @NotBlank(message = "El motivo de solicitud no puede estar vacío")
    @Size(min = 1, max = 150, message = "El motivo de solicitud debe tener entre 1 y 150 caracteres")
    private String titulo;

    private String descripcion;
}
