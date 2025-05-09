package com.ues.edu.sv.rpups_ues.model.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @NotBlank(message = "Los nombres del usuario no pueden estar vacíos")
    @Size(min = 1, max = 50, message = "Los nombres del usuario deben tener entre 1 y 50 caracteres")
    @Column(name = "nombres", length = 50, nullable = false)
    private String nombres;

    @NotBlank(message = "Los apellidos del usuario no pueden estar vacíos")
    @Size(min = 1, max = 50, message = "Los apellidos del usuario deben tener entre 1 y 50 caracteres")
    @Column(name = "apellidos", length = 50, nullable = false)
    private String apellidos;

    @Size(min = 8, max = 8, message = "El carnet del estudiante debe tener exactamente 8 caracteres")
    @Column(name = "carnet", length = 8, unique = true)
    private String carnet;

    @NotBlank(message = "El correo institucional del usuario no puede estar vacío")
    @Size(min = 1, max = 100, message = "El correo institucional del usuario debe tener entre 1 y 100 caracteres")
    @Column(name = "correo_institucional", length = 100, nullable = false, unique = true)
    private String correoInstitucional;

    @Size(max = 100, message = "El correo personal del usuario no puede exceder los 100 caracteres")
    @Column(name = "correo_personal", length = 100)
    private String correoPersonal;

    @NotBlank(message = "El teléfono del usuario no puede estar vacío")
    @Size(min = 8, max = 12, message = "El teléfono del usuario debe tener entre 8 y 12 caracteres")
    @Column(name = "telefono", length = 12, nullable = false)
    private String telefono;

    @NotBlank(message = "El username no puede estar vacío")
    @Size(min = 5, max = 15, message = "El username debe tener entre 5 y 15 caracteres")
    @Column(name = "username", length = 15, nullable = false, unique = true)
    private String username;

    @NotBlank(message = "La contraseña del usuario no puede estar vacía")
    @Size(min = 8, max = 255, message = "La contraseña del usuario debe tener entre 8 y 255 caracteres")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password", length = 255, nullable = false)
    private String password;

    @ManyToOne(optional = false)
    @JoinColumn(name = "codigo_rol", referencedColumnName = "codigo", nullable = false)
    private Rol rol;

    public Usuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }
}