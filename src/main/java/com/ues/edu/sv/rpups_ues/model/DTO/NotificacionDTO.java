package com.ues.edu.sv.rpups_ues.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para respuesta de notificación (evita exponer la entidad completa).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificacionDTO {
    
    private Long idNotificacion;
    private Long idUsuario;
    private String titulo;
    private String mensaje;
    private String tipo;
    private Boolean leida;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaLectura;
    private String enlace;
    private Long idReferencia;
    private String tipoReferencia;

    // Campos adicionales para UI
    private String tiempoTranscurrido; // "Hace 5 minutos", "Hace 2 horas", etc.
}
