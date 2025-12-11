package com.ues.edu.sv.rpups_ues.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para crear una nueva notificación.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrearNotificacionDTO {
    
    private Long idUsuario;
    private String titulo;
    private String mensaje;
    private String tipo;
    private String enlace;
    private Long idReferencia;
    private String tipoReferencia;
}
