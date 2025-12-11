package com.ues.edu.sv.rpups_ues.service.impl;

import com.ues.edu.sv.rpups_ues.model.DTO.CrearNotificacionDTO;
import com.ues.edu.sv.rpups_ues.model.DTO.NotificacionDTO;
import com.ues.edu.sv.rpups_ues.model.DTO.NotificacionesResumenDTO;
import com.ues.edu.sv.rpups_ues.model.entity.Notificacion;
import com.ues.edu.sv.rpups_ues.model.repository.NotificacionRepository;
import com.ues.edu.sv.rpups_ues.service.NotificacionService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de notificaciones.
 */
@Service
@Transactional
public class NotificacionServiceImpl implements NotificacionService {

    private final NotificacionRepository notificacionRepository;

    public NotificacionServiceImpl(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    @Override
    public Notificacion crear(CrearNotificacionDTO dto) {
        Notificacion notificacion = Notificacion.builder()
                .idUsuario(dto.getIdUsuario())
                .titulo(dto.getTitulo())
                .mensaje(dto.getMensaje())
                .tipo(dto.getTipo() != null ? dto.getTipo() : Notificacion.TIPO_INFO)
                .enlace(dto.getEnlace())
                .idReferencia(dto.getIdReferencia())
                .tipoReferencia(dto.getTipoReferencia())
                .leida(false)
                .fechaCreacion(LocalDateTime.now())
                .build();
        
        return notificacionRepository.save(notificacion);
    }

    @Override
    public Notificacion crear(Long idUsuario, String titulo, String mensaje, String tipo,
                              String enlace, Long idReferencia, String tipoReferencia) {
        CrearNotificacionDTO dto = CrearNotificacionDTO.builder()
                .idUsuario(idUsuario)
                .titulo(titulo)
                .mensaje(mensaje)
                .tipo(tipo)
                .enlace(enlace)
                .idReferencia(idReferencia)
                .tipoReferencia(tipoReferencia)
                .build();
        return crear(dto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Notificacion> findById(Long idNotificacion) {
        return notificacionRepository.findById(idNotificacion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificacionDTO> findByUsuario(Long idUsuario) {
        return notificacionRepository.findByIdUsuarioOrderByFechaCreacionDesc(idUsuario)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificacionDTO> findByUsuario(Long idUsuario, Pageable pageable) {
        return notificacionRepository.findByIdUsuarioOrderByFechaCreacionDesc(idUsuario, pageable)
                .map(this::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificacionDTO> findNoLeidasByUsuario(Long idUsuario) {
        return notificacionRepository.findByIdUsuarioAndLeidaFalseOrderByFechaCreacionDesc(idUsuario)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public NotificacionesResumenDTO getResumen(Long idUsuario, int limite) {
        long totalNoLeidas = notificacionRepository.countByIdUsuarioAndLeidaFalse(idUsuario);
        List<NotificacionDTO> notificaciones = notificacionRepository
                .findTopByIdUsuario(idUsuario, PageRequest.of(0, limite))
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        return NotificacionesResumenDTO.builder()
                .totalNoLeidas(totalNoLeidas)
                .notificaciones(notificaciones)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public long contarNoLeidas(Long idUsuario) {
        return notificacionRepository.countByIdUsuarioAndLeidaFalse(idUsuario);
    }

    @Override
    public Notificacion marcarComoLeida(Long idNotificacion) {
        Notificacion notificacion = notificacionRepository.findById(idNotificacion)
                .orElseThrow(() -> new IllegalArgumentException("Notificación no encontrada con ID: " + idNotificacion));
        
        notificacion.marcarComoLeida();
        return notificacionRepository.save(notificacion);
    }

    @Override
    public int marcarTodasComoLeidas(Long idUsuario) {
        return notificacionRepository.marcarTodasComoLeidas(idUsuario);
    }

    @Override
    public void eliminar(Long idNotificacion) {
        notificacionRepository.deleteById(idNotificacion);
    }

    @Override
    public int eliminarAntiguasLeidas(int diasAntiguedad) {
        LocalDateTime fechaLimite = LocalDateTime.now().minusDays(diasAntiguedad);
        return notificacionRepository.eliminarNotificacionesAntiguasLeidas(fechaLimite);
    }

    // ========== Métodos de utilidad para crear notificaciones específicas ==========

    @Override
    public void notificarSolicitudAprobada(Long idUsuario, Long idSolicitud, String tituloProyecto) {
        crear(idUsuario,
              "Solicitud Aprobada",
              "Su solicitud de proyecto \"" + tituloProyecto + "\" ha sido aprobada. El proyecto ha sido creado exitosamente.",
              Notificacion.TIPO_SUCCESS,
              "/proyectos/" + idSolicitud,
              idSolicitud,
              Notificacion.REF_SOLICITUD);
    }

    @Override
    public void notificarSolicitudRechazada(Long idUsuario, Long idSolicitud, String observaciones) {
        String mensaje = "Su solicitud de proyecto ha sido rechazada.";
        if (observaciones != null && !observaciones.isEmpty()) {
            mensaje += " Motivo: " + observaciones;
        }
        crear(idUsuario,
              "Solicitud Rechazada",
              mensaje,
              Notificacion.TIPO_ERROR,
              "/solicitudes/" + idSolicitud,
              idSolicitud,
              Notificacion.REF_SOLICITUD);
    }

    @Override
    public void notificarSolicitudConObservaciones(Long idUsuario, Long idSolicitud, String observaciones) {
        crear(idUsuario,
              "Solicitud con Observaciones",
              "Su solicitud de proyecto tiene observaciones que requieren su atención: " + observaciones,
              Notificacion.TIPO_WARNING,
              "/solicitudes/" + idSolicitud,
              idSolicitud,
              Notificacion.REF_SOLICITUD);
    }

    @Override
    public void notificarPostulacionAceptada(Long idEstudiante, Long idPostulacion, String nombreProyecto) {
        crear(idEstudiante,
              "¡Postulación Aceptada!",
              "Felicidades, su postulación al proyecto \"" + nombreProyecto + "\" ha sido aceptada. Ya puede comenzar sus horas sociales.",
              Notificacion.TIPO_SUCCESS,
              "/estudiante/mis-proyectos",
              idPostulacion,
              Notificacion.REF_POSTULACION);
    }

    @Override
    public void notificarPostulacionRechazada(Long idEstudiante, Long idPostulacion, String nombreProyecto, String observaciones) {
        String mensaje = "Su postulación al proyecto \"" + nombreProyecto + "\" no fue aceptada.";
        if (observaciones != null && !observaciones.isEmpty()) {
            mensaje += " Observaciones: " + observaciones;
        }
        crear(idEstudiante,
              "Postulación No Aceptada",
              mensaje,
              Notificacion.TIPO_ERROR,
              "/estudiante/proyectos",
              idPostulacion,
              Notificacion.REF_POSTULACION);
    }

    @Override
    public void notificarNuevaPostulacion(Long idAdmin, Long idPostulacion, String nombreEstudiante, String nombreProyecto) {
        crear(idAdmin,
              "Nueva Postulación",
              "El estudiante " + nombreEstudiante + " se ha postulado al proyecto \"" + nombreProyecto + "\".",
              Notificacion.TIPO_INFO,
              "/admin/proyectos/postulaciones",
              idPostulacion,
              Notificacion.REF_POSTULACION);
    }

    @Override
    public void notificarAsignacionRevisor(Long idRevisor, Long idSolicitud, String tituloSolicitud) {
        crear(idRevisor,
              "Nueva Solicitud Asignada",
              "Se le ha asignado la revisión de la solicitud: \"" + tituloSolicitud + "\".",
              Notificacion.TIPO_ALERT,
              "/admin/solicitudes/" + idSolicitud,
              idSolicitud,
              Notificacion.REF_SOLICITUD);
    }

    // ========== Métodos privados de utilidad ==========

    /**
     * Convierte una entidad Notificacion a DTO.
     */
    private NotificacionDTO toDTO(Notificacion notificacion) {
        return NotificacionDTO.builder()
                .idNotificacion(notificacion.getIdNotificacion())
                .idUsuario(notificacion.getIdUsuario())
                .titulo(notificacion.getTitulo())
                .mensaje(notificacion.getMensaje())
                .tipo(notificacion.getTipo())
                .leida(notificacion.getLeida())
                .fechaCreacion(notificacion.getFechaCreacion())
                .fechaLectura(notificacion.getFechaLectura())
                .enlace(notificacion.getEnlace())
                .idReferencia(notificacion.getIdReferencia())
                .tipoReferencia(notificacion.getTipoReferencia())
                .tiempoTranscurrido(calcularTiempoTranscurrido(notificacion.getFechaCreacion()))
                .build();
    }

    /**
     * Calcula el tiempo transcurrido desde la fecha dada hasta ahora.
     */
    private String calcularTiempoTranscurrido(LocalDateTime fecha) {
        if (fecha == null) return "";
        
        LocalDateTime ahora = LocalDateTime.now();
        long minutos = ChronoUnit.MINUTES.between(fecha, ahora);
        
        if (minutos < 1) {
            return "Ahora mismo";
        } else if (minutos < 60) {
            return "Hace " + minutos + (minutos == 1 ? " minuto" : " minutos");
        }
        
        long horas = ChronoUnit.HOURS.between(fecha, ahora);
        if (horas < 24) {
            return "Hace " + horas + (horas == 1 ? " hora" : " horas");
        }
        
        long dias = ChronoUnit.DAYS.between(fecha, ahora);
        if (dias < 7) {
            return "Hace " + dias + (dias == 1 ? " día" : " días");
        }
        
        long semanas = dias / 7;
        if (semanas < 4) {
            return "Hace " + semanas + (semanas == 1 ? " semana" : " semanas");
        }
        
        long meses = ChronoUnit.MONTHS.between(fecha, ahora);
        if (meses < 12) {
            return "Hace " + meses + (meses == 1 ? " mes" : " meses");
        }
        
        long anos = ChronoUnit.YEARS.between(fecha, ahora);
        return "Hace " + anos + (anos == 1 ? " año" : " años");
    }
}
