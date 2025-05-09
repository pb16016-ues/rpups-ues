package com.ues.edu.sv.rpups_ues.model.entity;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "municipios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Municipio implements Serializable {

    @Id
    @Column(name = "codigo", length = 4, nullable = false, unique = true)
    private String codigo;

    @Column(name = "nombre", length = 50, nullable = false)
    private String nombre;

    @Column(name = "codigo_departamento", length = 2, nullable = false)
    private String codigoDepartamento;

    @ManyToOne
    @JoinColumn(name = "codigo_departamento", referencedColumnName = "codigo", insertable = false, updatable = false)
    private Departamento departamento;

    public Municipio(String codigo) {
        this.codigo = codigo;
    }
}
