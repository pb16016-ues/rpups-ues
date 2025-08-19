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

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_estudiante", referencedColumnName = "id_usuario", nullable = false, insertable = false, updatable = false)
    private Usuario estudiante;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_proyecto", referencedColumnName = "id_proyecto", nullable = false, insertable = false, updatable = false)
    private Proyecto proyecto;

    public Postulacion(Long idPostulacion) {
        this.idPostulacion = idPostulacion;
    }
}
