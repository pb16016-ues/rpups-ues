package com.ues.edu.sv.rpups_ues.model.entity;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "departamentos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Departamento implements Serializable {

    @Id
    @Column(name = "codigo", length = 2, nullable = false, unique = true)
    private String codigo;

    @Column(name = "nombre", length = 50, nullable = false, unique = true)
    private String nombre;

    public Departamento(String codigo) {
        this.codigo = codigo;
    }
}
