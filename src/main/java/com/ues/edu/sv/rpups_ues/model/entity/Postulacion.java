package com.ues.edu.sv.rpups_ues.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "postulaciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Postulacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_postulacion", nullable = false)
    private Long idPostulacion;

    @NotNull(message = "El id del estudiante no puede ser nulo")
    @Column(name = "id_estudiante", nullable = false)
    private Long idEstudiante;

    @NotNull(message = "El id del proyecto no puede ser nulo")
    @Column(name = "id_proyecto", nullable = false)
    private Long idProyecto;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "fecha_postulacion", nullable = true)
    private LocalDateTime fechaPostulacion;

    @NotNull(message = "El código de estado no puede ser nulo")
    @Column(name = "codigo_estado", nullable = false)
    private String codigoEstado = "PEND";

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "fecha_cambio_estado", nullable = true)
    private LocalDateTime fechaCambioEstado;

    @Column(name = "id_admin_cambio_estado", nullable = true)
    private Long idAdminCambioEstado;

    @Column(name = "observaciones", columnDefinition = "TEXT", nullable = true)
    private String observaciones;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_estudiante", referencedColumnName = "id_usuario", nullable = false, insertable = false, updatable = false)
    private Usuario estudiante;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_proyecto", referencedColumnName = "id_proyecto", nullable = false, insertable = false, updatable = false)
    private Proyecto proyecto;

    @ManyToOne(optional = false)
    @JoinColumn(name = "codigo_estado", referencedColumnName = "codigo_estado", nullable = false, insertable = false, updatable = false)
    private Estado estado;

    @ManyToOne(optional = true)
    @JoinColumn(name = "id_admin_cambio_estado", referencedColumnName = "id_usuario", nullable = true, insertable = false, updatable = false)
    private Usuario adminCambioEstado;

    public Postulacion(Long idPostulacion) {
        this.idPostulacion = idPostulacion;
    }
}
