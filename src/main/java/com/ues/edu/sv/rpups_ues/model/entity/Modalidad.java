package com.ues.edu.sv.rpups_ues.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Entity
@Table(name = "modalidades")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Modalidad implements Serializable {

    @Id
    @Column(name = "codigo_modalidad", length = 3, nullable = false, unique = true)
    private String codigoModalidad;

    @Column(name = "nombre", length = 50, nullable = false, unique = true)
    private String nombre;

    public Modalidad(String codigoModalidad) {
        this.codigoModalidad = codigoModalidad;
    }
}