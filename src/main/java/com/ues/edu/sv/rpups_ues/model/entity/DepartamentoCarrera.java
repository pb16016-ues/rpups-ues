package com.ues.edu.sv.rpups_ues.model.entity;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "departamentos_carreras")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DepartamentoCarrera implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_depto_carrera", nullable = true)
    private Long idDepartamentoCarrera;

    @Column(name = "nombre", length = 250, nullable = false, unique = true)
    private String nombre;

    public DepartamentoCarrera(Long idDepartamentoCarrera) {
        this.idDepartamentoCarrera = idDepartamentoCarrera;
    }
}
