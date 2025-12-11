package com.ues.edu.sv.rpups_ues.controller;

import com.ues.edu.sv.rpups_ues.model.DTO.NotificacionDTO;
import com.ues.edu.sv.rpups_ues.model.DTO.NotificacionesResumenDTO;
import com.ues.edu.sv.rpups_ues.model.entity.Notificacion;
import com.ues.edu.sv.rpups_ues.service.NotificacionService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para gestionar las notificaciones de los usuarios.
 */
@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    /**
     * Obtiene el ID del usuario autenticado actualmente.
     */
    private Long getUsuarioAutenticadoId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Long.parseLong(authentication.getName());
    }

    /**
     * Obtiene el resumen de notificaciones del usuario autenticado.
     * Incluye el conteo de no leídas y las últimas N notificaciones.
     * 
     * @param limite Número de notificaciones a retornar (default: 10)
     */
    @GetMapping("/resumen")
    @Secured({"ADMIN", "COORD", "SUP", "ESTUD", "EMP"})
    public ResponseEntity<NotificacionesResumenDTO> getResumen(
            @RequestParam(name = "limite", defaultValue = "10") int limite) {
        Long idUsuario = getUsuarioAutenticadoId();
        NotificacionesResumenDTO resumen = notificacionService.getResumen(idUsuario, limite);
        return ResponseEntity.ok(resumen);
    }

    /**
     * Obtiene todas las notificaciones del usuario autenticado con paginación.
     */
    @GetMapping
    @Secured({"ADMIN", "COORD", "SUP", "ESTUD", "EMP"})
    public ResponseEntity<Page<NotificacionDTO>> getAllNotificaciones(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size) {
        Long idUsuario = getUsuarioAutenticadoId();
        Page<NotificacionDTO> notificaciones = notificacionService.findByUsuario(idUsuario, PageRequest.of(page, size));
        return ResponseEntity.ok(notificaciones);
    }

    /**
     * Obtiene solo las notificaciones no leídas del usuario autenticado.
     */
    @GetMapping("/no-leidas")
    @Secured({"ADMIN", "COORD", "SUP", "ESTUD", "EMP"})
    public ResponseEntity<List<NotificacionDTO>> getNoLeidas() {
        Long idUsuario = getUsuarioAutenticadoId();
        List<NotificacionDTO> notificaciones = notificacionService.findNoLeidasByUsuario(idUsuario);
        return ResponseEntity.ok(notificaciones);
    }

    /**
     * Obtiene el conteo de notificaciones no leídas.
     */
    @GetMapping("/conteo-no-leidas")
    @Secured({"ADMIN", "COORD", "SUP", "ESTUD", "EMP"})
    public ResponseEntity<Map<String, Long>> getConteoNoLeidas() {
        Long idUsuario = getUsuarioAutenticadoId();
        long conteo = notificacionService.contarNoLeidas(idUsuario);
        Map<String, Long> response = new HashMap<>();
        response.put("totalNoLeidas", conteo);
        return ResponseEntity.ok(response);
    }

    /**
     * Marca una notificación específica como leída.
     */
    @PutMapping("/{idNotificacion}/leer")
    @Secured({"ADMIN", "COORD", "SUP", "ESTUD", "EMP"})
    public ResponseEntity<?> marcarComoLeida(@PathVariable Long idNotificacion) {
        Long idUsuario = getUsuarioAutenticadoId();
        
        // Verificar que la notificación pertenece al usuario
        Notificacion notificacion = notificacionService.findById(idNotificacion)
                .orElse(null);
        
        if (notificacion == null) {
            return ResponseEntity.notFound().build();
        }
        
        if (!notificacion.getIdUsuario().equals(idUsuario)) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "No tiene permiso para acceder a esta notificación");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        }
        
        Notificacion actualizada = notificacionService.marcarComoLeida(idNotificacion);
        return ResponseEntity.ok(actualizada);
    }

    /**
     * Marca todas las notificaciones del usuario como leídas.
     */
    @PutMapping("/leer-todas")
    @Secured({"ADMIN", "COORD", "SUP", "ESTUD", "EMP"})
    public ResponseEntity<Map<String, Object>> marcarTodasComoLeidas() {
        Long idUsuario = getUsuarioAutenticadoId();
        int actualizadas = notificacionService.marcarTodasComoLeidas(idUsuario);
        
        Map<String, Object> response = new HashMap<>();
        response.put("notificacionesMarcadas", actualizadas);
        response.put("mensaje", actualizadas + " notificaciones marcadas como leídas");
        return ResponseEntity.ok(response);
    }

    /**
     * Elimina una notificación específica.
     */
    @DeleteMapping("/{idNotificacion}")
    @Secured({"ADMIN", "COORD", "SUP", "ESTUD", "EMP"})
    public ResponseEntity<?> eliminarNotificacion(@PathVariable Long idNotificacion) {
        Long idUsuario = getUsuarioAutenticadoId();
        
        // Verificar que la notificación pertenece al usuario
        Notificacion notificacion = notificacionService.findById(idNotificacion)
                .orElse(null);
        
        if (notificacion == null) {
            return ResponseEntity.notFound().build();
        }
        
        if (!notificacion.getIdUsuario().equals(idUsuario)) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "No tiene permiso para eliminar esta notificación");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        }
        
        notificacionService.eliminar(idNotificacion);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint administrativo para limpiar notificaciones antiguas.
     * Solo accesible por administradores.
     */
    @DeleteMapping("/limpiar-antiguas")
    @Secured({"ADMIN"})
    public ResponseEntity<Map<String, Object>> limpiarNotificacionesAntiguas(
            @RequestParam(name = "dias", defaultValue = "30") int diasAntiguedad) {
        int eliminadas = notificacionService.eliminarAntiguasLeidas(diasAntiguedad);
        
        Map<String, Object> response = new HashMap<>();
        response.put("notificacionesEliminadas", eliminadas);
        response.put("mensaje", eliminadas + " notificaciones antiguas eliminadas");
        return ResponseEntity.ok(response);
    }
}
