package com.ues.edu.sv.rpups_ues.model.entity;

import java.io.Serializable;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "rubros")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Rubro implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rubro", nullable = false)
    private Long idRubro;

    @NotBlank(message = "El nombre del rubro no puede estar vacío")
    @Size(min = 1, max = 100, message = "El nombre del rubro debe tener entre 1 y 100 caracteres")
    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    public Rubro(Long idRubro) {
        this.idRubro = idRubro;
    }

}
