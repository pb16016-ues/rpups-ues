package com.ues.edu.sv.rpups_ues.model.entity;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "estados")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Estado implements Serializable {
    @Id
    @Column(name = "codigo_estado", length = 5, nullable = false, unique = true)
    private String codigoEstado;

    @Column(name = "nombre", length = 50, nullable = false, unique = true)
    private String nombre;

    public Estado(String codigoEstado) {
        this.codigoEstado = codigoEstado;
    }
}
