package com.ues.edu.sv.rpups_ues.model.entity;

import jakarta.validation.constraints.*;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "carreras")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Carrera implements Serializable {

    @Id
    @Size(max = 10, message = "El código no puede tener más de 10 caracteres.")
    @NotBlank(message = "El código no puede estar vacío.")
    @Column(name = "codigo", length = 10, nullable = false, unique = true)
    private String codigo;

    @Size(max = 250, message = "El nombre de la carrera no puede tener más de 250 caracteres.")
    @NotBlank(message = "El nombre de la carrera no puede estar vacío.")
    private String nombre;

    @Column(name = "id_depto_carrera", nullable = false)
    private Long idDepartamentoCarrera;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_depto_carrera", referencedColumnName = "id_depto_carrera", nullable = false, insertable = false, updatable = false)
    private DepartamentoCarrera departamentoCarrera;

    public Carrera(String codigo) {
        this.codigo = codigo;
    }
}
