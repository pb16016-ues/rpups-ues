package com.ues.edu.sv.rpups_ues.model.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "empresas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Empresa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empresa", nullable = true)
    private Long idEmpresa;

    @NotBlank(message = "El nombre comercial no puede estar vacío")
    @Size(max = 100, message = "El nombre comercial de la empresa debe tener un máximo de 100 caracteres")
    @Column(name = "nombre_comercial", nullable = false, length = 100)
    private String nombreComercial;

    @NotBlank(message = "El nombre legal no puede estar vacío")
    @Size(max = 150, message = "El nombre legal de la empresa debe tener un máximo de 150 caracteres")
    @Column(name = "nombre_legal", nullable = false, length = 150)
    private String nombreLegal;

    @NotBlank(message = "El nombre del contacto no puede estar vacío")
    @Size(max = 150, message = "El nombre del contacto debe tener un máximo de 150 caracteres")
    @Column(name = "contacto_nombre", nullable = false, length = 150)
    private String contactoNombre;

    @NotBlank(message = "El teléfono del contacto no puede estar vacío")
    @Size(max = 15, message = "El teléfono del contacto debe tener un máximo de 15 caracteres")
    @Column(name = "contacto_telefono", nullable = false, length = 12)
    private String contactoTelefono;

    @NotBlank(message = "El email del contacto no puede estar vacío")
    @Email(message = "El email del contacto debe ser válido")
    @Size(max = 100, message = "El email del contacto debe tener un máximo de 100 caracteres")
    @Column(name = "contacto_email", nullable = false, length = 100)
    private String contactoEmail;

    @Size(max = 250, message = "La dirección de la empresa debe tener un máximo de 250 caracteres")
    @Column(name = "direccion_detallada", columnDefinition = "TEXT", nullable = true, length = 250)
    private String direccionDetallada;

    @Column(name = "estado_activo", nullable = false)
    private Boolean estadoActivo;

    @NotNull(message = "El código de departamento no puede estar vacío o nulo")
    @Column(name = "codigo_departamento", nullable = false)
    private String codigoDepartamento;

    @NotNull(message = "El código de municipio no puede estar vacío o nulo")
    @Column(name = "codigo_municipio", nullable = false)
    private String codigoMunicipio;

    @NotNull(message = "El id del rubro de la empresa no puede estar vacío o nulo")
    @Column(name = "id_rubro", nullable = false)
    private Long idRubro;

    @NotNull(message = "El id del usuario creador de la solicitud no puede estar vacío o nulo")
    @Column(name = "id_user_creacion", nullable = false)
    private Long idUserCreador;

    @ManyToOne(optional = false)
    @JoinColumn(name = "codigo_departamento", referencedColumnName = "codigo", nullable = false, insertable = false, updatable = false)
    @JsonIgnore
    private Departamento departamento;

    @ManyToOne(optional = false)
    @JoinColumn(name = "codigo_municipio", referencedColumnName = "codigo", nullable = false, insertable = false, updatable = false)
    @JsonIgnore
    private Municipio municipio;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_rubro", referencedColumnName = "id_rubro", nullable = false, insertable = false, updatable = false)
    private Rubro rubro;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_user_creacion", referencedColumnName = "id_usuario", nullable = false, insertable = false, updatable = false)
    @JsonIgnore
    private Usuario userCreador;

    public Empresa(Long idEmpresa) {
        this.idEmpresa = idEmpresa;
    }
}