package com.ues.edu.sv.rpups_ues.model.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

/**
 * Entidad para las notificaciones in-app del sistema.
 * Permite notificar a los usuarios sobre eventos importantes como:
 * - Aprobación/rechazo de solicitudes
 * - Postulaciones aceptadas/rechazadas
 * - Asignación a proyectos
 * - Cambios de estado en sus entidades
 */
@Entity
@Table(name = "notificaciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "usuario")
public class Notificacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificacion")
    private Long idNotificacion;

    @NotNull(message = "El ID del usuario es requerido")
    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @NotBlank(message = "El título de la notificación no puede estar vacío")
    @Size(max = 150, message = "El título no puede exceder los 150 caracteres")
    @Column(name = "titulo", length = 150, nullable = false)
    private String titulo;

    @NotBlank(message = "El mensaje de la notificación no puede estar vacío")
    @Column(name = "mensaje", columnDefinition = "TEXT", nullable = false)
    private String mensaje;

    /**
     * Tipo de notificación:
     * - INFO: Informativa general
     * - SUCCESS: Acción exitosa (aprobación, aceptación)
     * - WARNING: Advertencia (observaciones, cambios pendientes)
     * - ERROR: Error o rechazo
     * - ALERT: Alerta importante que requiere atención
     */
    @NotBlank(message = "El tipo de notificación es requerido")
    @Size(max = 20, message = "El tipo no puede exceder los 20 caracteres")
    @Column(name = "tipo", length = 20, nullable = false)
    @Builder.Default
    private String tipo = "INFO";

    @Column(name = "leida", nullable = false)
    @Builder.Default
    private Boolean leida = false;

    @Column(name = "fecha_creacion", nullable = false)
    @Builder.Default
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(name = "fecha_lectura")
    private LocalDateTime fechaLectura;

    /**
     * Enlace para navegar al detalle del elemento relacionado.
     * Ejemplo: "/admin/solicitudes/123" o "/estudiante/proyectos/456"
     */
    @Size(max = 255, message = "El enlace no puede exceder los 255 caracteres")
    @Column(name = "enlace", length = 255)
    private String enlace;

    /**
     * ID de la entidad relacionada (solicitud, proyecto, postulación, etc.)
     */
    @Column(name = "id_referencia")
    private Long idReferencia;

    /**
     * Tipo de entidad referenciada:
     * - SOLICITUD
     * - PROYECTO
     * - POSTULACION
     * - USUARIO
     */
    @Size(max = 50, message = "El tipo de referencia no puede exceder los 50 caracteres")
    @Column(name = "tipo_referencia", length = 50)
    private String tipoReferencia;

    // Relación con Usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario", insertable = false, updatable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "password"})
    private Usuario usuario;

    // Constantes para tipos de notificación
    public static final String TIPO_INFO = "INFO";
    public static final String TIPO_SUCCESS = "SUCCESS";
    public static final String TIPO_WARNING = "WARNING";
    public static final String TIPO_ERROR = "ERROR";
    public static final String TIPO_ALERT = "ALERT";

    // Constantes para tipos de referencia
    public static final String REF_SOLICITUD = "SOLICITUD";
    public static final String REF_PROYECTO = "PROYECTO";
    public static final String REF_POSTULACION = "POSTULACION";
    public static final String REF_USUARIO = "USUARIO";

    /**
     * Marca la notificación como leída.
     */
    public void marcarComoLeida() {
        this.leida = true;
        this.fechaLectura = LocalDateTime.now();
    }
}
