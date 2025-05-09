package com.ues.edu.sv.rpups_ues.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

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

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_estudiante", referencedColumnName = "id_usuario", nullable = false)
    private Usuario estudiante;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_proyecto", referencedColumnName = "id_proyecto", nullable = false)
    private Proyecto proyecto;

    @ManyToOne(optional = false)
    @JoinColumn(name = "codigo_estado", referencedColumnName = "codigo_estado", nullable = false)
    private Estado estado;

    public Postulacion(Long idPostulacion) {
        this.idPostulacion = idPostulacion;
    }
}
