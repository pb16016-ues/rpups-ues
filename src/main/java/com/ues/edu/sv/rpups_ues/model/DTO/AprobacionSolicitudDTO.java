package com.ues.edu.sv.rpups_ues.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la solicitud de aprobación de una solicitud de proyecto.
 * Contiene los datos necesarios para aprobar una solicitud y crear el proyecto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AprobacionSolicitudDTO {

    /**
     * Observaciones del administrador sobre la aprobación (opcional)
     */
    private String observaciones;

    /**
     * Estado inicial del proyecto creado (por defecto DISP - Disponible)
     */
    private String codigoEstadoProyecto = "DISP";
}
