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
    @Column(name = "titulo", nullable = false, length = 150)
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
    @Column(name = "fecha_inicio", nullable = true)
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de finalización del proyecto propuesto no puede estar vacía")
    @FutureOrPresent(message = "La fecha de finalización del proyecto propuesto debe ser una fecha futura o presente")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "fecha_fin", nullable = true)
    private LocalDate fechaFin;

    @NotNull(message = "La duración del proyecto propuesto no puede estar vacía")
    @Min(value = 1, message = "La duración del proyecto propuesto debe ser mayor a 0")
    @Column(name = "duracion", nullable = false)
    private Integer duracion;

    @NotNull(message = "El número máximo de estudiantes para el proyecto no puede estar vacío")
    @Min(value = 1, message = "El número de estudiantes para el proyecto debe ser mayor a 0")
    @Column(name = "max_estudiantes", nullable = false)
    private Integer maxEstudiantes;

    @Size(max = 250, message = "La dirección detallada de la ejecución del proyecto propuesto debe tener un máximo de 250 caracteres")
    @Column(name = "direccion_detallada", columnDefinition = "TEXT", nullable = true)
    private String direccionDetallada;

    @NotNull(message = "La fecha de revisión del proyecto propuesto no puede estar vacía")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "fecha_revision", nullable = false)
    private LocalDateTime fechaRevision;

    @NotBlank(message = "Las observaciones de la propuesta de proyecto no pueden estar vacías")
    @Column(name = "observaciones", columnDefinition = "TEXT", nullable = false)
    private String observaciones;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_empresa", referencedColumnName = "id_empresa", nullable = false)
    private Empresa empresa;

    @ManyToOne(optional = false)
    @JoinColumn(name = "codigo_departamento", referencedColumnName = "codigo", nullable = false)
    private Departamento departamento;

    @ManyToOne(optional = false)
    @JoinColumn(name = "codigo_municipio", referencedColumnName = "codigo", nullable = false)
    private Municipio municipio;

    @ManyToOne(optional = false)
    @JoinColumn(name = "codigo_carrera", referencedColumnName = "codigo", nullable = false)
    private Carrera carrera;

    @ManyToOne(optional = false)
    @JoinColumn(name = "codigo_modalidad", referencedColumnName = "codigo_modalidad", nullable = false)
    private Modalidad modalidad;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_admin_revision", referencedColumnName = "id_usuario", nullable = false)
    private Usuario administrador;

    @ManyToOne(optional = false)
    @JoinColumn(name = "codigo_estado", referencedColumnName = "codigo_estado", nullable = false)
    private Estado estado;

    public SolicitudProyecto(Long idSolicitud) {
        this.idSolicitud = idSolicitud;
    }
}