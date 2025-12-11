package com.ues.edu.sv.rpups_ues.service;

import com.ues.edu.sv.rpups_ues.model.DTO.CrearNotificacionDTO;
import com.ues.edu.sv.rpups_ues.model.DTO.NotificacionDTO;
import com.ues.edu.sv.rpups_ues.model.DTO.NotificacionesResumenDTO;
import com.ues.edu.sv.rpups_ues.model.entity.Notificacion;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar las notificaciones in-app del sistema.
 */
public interface NotificacionService {

    /**
     * Crea una nueva notificación para un usuario.
     */
    Notificacion crear(CrearNotificacionDTO dto);

    /**
     * Crea una notificación directamente desde los parámetros.
     */
    Notificacion crear(Long idUsuario, String titulo, String mensaje, String tipo, 
                       String enlace, Long idReferencia, String tipoReferencia);

    /**
     * Obtiene una notificación por su ID.
     */
    Optional<Notificacion> findById(Long idNotificacion);

    /**
     * Obtiene todas las notificaciones de un usuario.
     */
    List<NotificacionDTO> findByUsuario(Long idUsuario);

    /**
     * Obtiene las notificaciones de un usuario con paginación.
     */
    Page<NotificacionDTO> findByUsuario(Long idUsuario, Pageable pageable);

    /**
     * Obtiene las notificaciones no leídas de un usuario.
     */
    List<NotificacionDTO> findNoLeidasByUsuario(Long idUsuario);

    /**
     * Obtiene el resumen de notificaciones (conteo + últimas N).
     */
    NotificacionesResumenDTO getResumen(Long idUsuario, int limite);

    /**
     * Cuenta las notificaciones no leídas de un usuario.
     */
    long contarNoLeidas(Long idUsuario);

    /**
     * Marca una notificación como leída.
     */
    Notificacion marcarComoLeida(Long idNotificacion);

    /**
     * Marca todas las notificaciones de un usuario como leídas.
     */
    int marcarTodasComoLeidas(Long idUsuario);

    /**
     * Elimina una notificación por su ID.
     */
    void eliminar(Long idNotificacion);

    /**
     * Elimina notificaciones antiguas leídas (limpieza periódica).
     */
    int eliminarAntiguasLeidas(int diasAntiguedad);

    // ========== Métodos de utilidad para crear notificaciones específicas ==========

    /**
     * Crea notificación cuando se aprueba una solicitud de proyecto.
     */
    void notificarSolicitudAprobada(Long idUsuario, Long idSolicitud, String tituloProyecto);

    /**
     * Crea notificación cuando se rechaza una solicitud de proyecto.
     */
    void notificarSolicitudRechazada(Long idUsuario, Long idSolicitud, String observaciones);

    /**
     * Crea notificación cuando una solicitud tiene observaciones.
     */
    void notificarSolicitudConObservaciones(Long idUsuario, Long idSolicitud, String observaciones);

    /**
     * Crea notificación cuando se acepta una postulación.
     */
    void notificarPostulacionAceptada(Long idEstudiante, Long idPostulacion, String nombreProyecto);

    /**
     * Crea notificación cuando se rechaza una postulación.
     */
    void notificarPostulacionRechazada(Long idEstudiante, Long idPostulacion, String nombreProyecto, String observaciones);

    /**
     * Crea notificación cuando hay una nueva postulación a un proyecto (para admin/coordinador).
     */
    void notificarNuevaPostulacion(Long idAdmin, Long idPostulacion, String nombreEstudiante, String nombreProyecto);

    /**
     * Crea notificación cuando se asigna un revisor a una solicitud.
     */
    void notificarAsignacionRevisor(Long idRevisor, Long idSolicitud, String tituloSolicitud);
}
