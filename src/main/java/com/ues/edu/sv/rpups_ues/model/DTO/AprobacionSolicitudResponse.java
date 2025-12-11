package com.ues.edu.sv.rpups_ues.model.DTO;

import com.ues.edu.sv.rpups_ues.model.entity.Proyecto;
import com.ues.edu.sv.rpups_ues.model.entity.SolicitudProyecto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta para la aprobación de una solicitud de proyecto.
 * Contiene tanto la solicitud actualizada como el proyecto creado.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AprobacionSolicitudResponse {

    /**
     * La solicitud de proyecto que fue aprobada
     */
    private SolicitudProyecto solicitud;

    /**
     * El proyecto creado automáticamente a partir de la solicitud
     */
    private Proyecto proyecto;

    /**
     * Mensaje informativo sobre la operación
     */
    private String mensaje;

    public AprobacionSolicitudResponse(SolicitudProyecto solicitud, Proyecto proyecto) {
        this.solicitud = solicitud;
        this.proyecto = proyecto;
        this.mensaje = "Solicitud aprobada y proyecto creado exitosamente";
    }
}
