package com.ues.edu.sv.rpups_ues.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "solicitudes_proyectos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SolicitudProyecto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_solicitud", nullable = false)
    private Long idSolicitud;

    @NotBlank(message = "El título del proyecto propuesto no puede estar vacío")
    @Size(max = 250, message = "El título del proyecto propuesto debe tener un máximo de 150 caracteres")
    @Column(name = "titulo", nullable = false, length = 250)
    private String titulo;

    @NotBlank(message = "La descripción del proyecto propuesto no puede estar vacía")
    @Column(name = "descripcion", columnDefinition = "TEXT", nullable = false)
    private String descripcion;

    @NotBlank(message = "Los requisitos del proyecto propuesto no pueden estar vacíos")
    @Column(name = "requisitos", columnDefinition = "TEXT", nullable = false)
    private String requisitos;

    @NotNull(message = "La fecha de inicio del proyecto propuesto no puede estar vacía")
    @FutureOrPresent(message = "La fecha de inicio del proyecto propuesto debe ser una fecha futura o presente")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de finalización del proyecto propuesto no puede estar vacía")
    @FutureOrPresent(message = "La fecha de finalización del proyecto propuesto debe ser una fecha futura o presente")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @NotNull(message = "La duración del proyecto propuesto no puede estar vacía")
    @Min(value = 1, message = "La duración del proyecto propuesto debe ser mayor a 0")
    @Column(name = "duracion", nullable = false)
    private Integer duracion;

    @NotNull(message = "El número máximo de estudiantes para el proyecto no puede estar vacío")
    @Min(value = 1, message = "El número de estudiantes para el proyecto debe ser mayor a 0")
    @Column(name = "max_estudiantes", nullable = false)
    private Integer maxEstudiantes;

    @Size(max = 255, message = "La dirección detallada de la ejecución del proyecto propuesto debe tener un máximo de 255 caracteres")
    @Column(name = "direccion_detallada", columnDefinition = "TEXT", nullable = true)
    private String direccionDetallada;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "fecha_revision", nullable = true)
    private LocalDateTime fechaRevision;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "fecha_creacion", nullable = true)
    private LocalDateTime fechaCreacion;

    @Column(name = "observaciones", columnDefinition = "TEXT", nullable = true)
    private String observaciones;

    @NotNull(message = "El id de empresa no puede estar vacío o nulo")
    @Column(name = "id_empresa", nullable = false)
    private Long idEmpresa;

    @NotNull(message = "El código de departamento no puede estar vacío o nulo")
    @Column(name = "codigo_departamento", nullable = false)
    private String codigoDepartamento;

    @NotNull(message = "El código de municipio no puede estar vacío o nulo")
    @Column(name = "codigo_municipio", nullable = false)
    private String codigoMunicipio;

    @NotNull(message = "El código de carrera no puede estar vacío o nulo")
    @Column(name = "codigo_carrera", nullable = false)
    private String codigoCarrera;

    @NotNull(message = "El código de modalidad no puede estar vacío o nulo")
    @Column(name = "codigo_modalidad", nullable = false)
    private String codigoModalidad;

    @NotNull(message = "El código de estado no puede estar vacío o nulo")
    @Column(name = "codigo_estado", nullable = false)
    private String codigoEstado;

    @Column(name = "id_admin_revision", nullable = true)
    private Long idAdminRevisor;

    @NotNull(message = "El id del usuario creador de la solicitud no puede estar vacío o nulo")
    @Column(name = "id_user_creacion", nullable = false)
    private Long idUserCreador;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_empresa", referencedColumnName = "id_empresa", nullable = false, insertable = false, updatable = false)
    private Empresa empresa;

    @ManyToOne(optional = false)
    @JoinColumn(name = "codigo_departamento", referencedColumnName = "codigo", nullable = false, insertable = false, updatable = false)
    private Departamento departamento;

    @ManyToOne(optional = false)
    @JoinColumn(name = "codigo_municipio", referencedColumnName = "codigo", nullable = false, insertable = false, updatable = false)
    private Municipio municipio;

    @ManyToOne(optional = false)
    @JoinColumn(name = "codigo_carrera", referencedColumnName = "codigo", nullable = false, insertable = false, updatable = false)
    private Carrera carrera;

    @ManyToOne(optional = false)
    @JoinColumn(name = "codigo_modalidad", referencedColumnName = "codigo_modalidad", nullable = false, insertable = false, updatable = false)
    private Modalidad modalidad;

    @ManyToOne(optional = false)
    @JoinColumn(name = "codigo_estado", referencedColumnName = "codigo_estado", nullable = false, insertable = false, updatable = false)
    private Estado estado;

    @ManyToOne(optional = true)
    @JoinColumn(name = "id_admin_revision", referencedColumnName = "id_usuario", nullable = true, insertable = false, updatable = false)
    private Usuario adminRevisor;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_user_creacion", referencedColumnName = "id_usuario", nullable = false, insertable = false, updatable = false)
    private Usuario userCreador;

    public SolicitudProyecto(Long idSolicitud) {
        this.idSolicitud = idSolicitud;
    }
}