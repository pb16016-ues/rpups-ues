package com.ues.edu.sv.rpups_ues.model.DTO;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class VerificarSolicitudDTO {

    private Long idSolicitud;
    private String codEstadoSolicitud;
    private String observaciones;
    private LocalDateTime fechaRevision;
    private Long idAdminRevision;

}
