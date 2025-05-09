package com.ues.edu.sv.rpups_ues.model.entity;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Rol implements Serializable {

    @Id
    @Column(name = "codigo", length = 5, nullable = false, unique = true)
    private String codigo;

    @Column(name = "nombre", length = 50, nullable = false, unique = true)
    private String nombre;

    public Rol(String codigo) {
        this.codigo = codigo;
    }
}
