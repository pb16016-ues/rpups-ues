package com.ues.edu.sv.rpups_ues.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO para respuesta con resumen de notificaciones.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificacionesResumenDTO {
    
    private long totalNoLeidas;
    private List<NotificacionDTO> notificaciones;
}
