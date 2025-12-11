package com.ues.edu.sv.rpups_ues.model.repository;

import com.ues.edu.sv.rpups_ues.model.entity.Notificacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para la entidad Notificacion.
 * Proporciona métodos para gestionar las notificaciones de los usuarios.
 */
@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    /**
     * Obtiene todas las notificaciones de un usuario ordenadas por fecha descendente.
     */
    List<Notificacion> findByIdUsuarioOrderByFechaCreacionDesc(Long idUsuario);

    /**
     * Obtiene las notificaciones de un usuario con paginación.
     */
    Page<Notificacion> findByIdUsuarioOrderByFechaCreacionDesc(Long idUsuario, Pageable pageable);

    /**
     * Obtiene las notificaciones no leídas de un usuario.
     */
    List<Notificacion> findByIdUsuarioAndLeidaFalseOrderByFechaCreacionDesc(Long idUsuario);

    /**
     * Obtiene las notificaciones no leídas de un usuario con paginación.
     */
    Page<Notificacion> findByIdUsuarioAndLeidaFalseOrderByFechaCreacionDesc(Long idUsuario, Pageable pageable);

    /**
     * Cuenta las notificaciones no leídas de un usuario.
     */
    long countByIdUsuarioAndLeidaFalse(Long idUsuario);

    /**
     * Obtiene las notificaciones de un usuario por tipo.
     */
    List<Notificacion> findByIdUsuarioAndTipoOrderByFechaCreacionDesc(Long idUsuario, String tipo);

    /**
     * Marca todas las notificaciones de un usuario como leídas.
     */
    @Modifying
    @Query("UPDATE Notificacion n SET n.leida = true, n.fechaLectura = CURRENT_TIMESTAMP WHERE n.idUsuario = :idUsuario AND n.leida = false")
    int marcarTodasComoLeidas(@Param("idUsuario") Long idUsuario);

    /**
     * Elimina notificaciones antiguas (más de X días).
     */
    @Modifying
    @Query("DELETE FROM Notificacion n WHERE n.leida = true AND n.fechaCreacion < :fecha")
    int eliminarNotificacionesAntiguasLeidas(@Param("fecha") java.time.LocalDateTime fecha);

    /**
     * Obtiene notificaciones por tipo de referencia.
     */
    List<Notificacion> findByIdUsuarioAndTipoReferenciaOrderByFechaCreacionDesc(Long idUsuario, String tipoReferencia);

    /**
     * Verifica si existe una notificación para una referencia específica.
     */
    boolean existsByIdUsuarioAndIdReferenciaAndTipoReferencia(Long idUsuario, Long idReferencia, String tipoReferencia);

    /**
     * Obtiene las últimas N notificaciones de un usuario.
     */
    @Query("SELECT n FROM Notificacion n WHERE n.idUsuario = :idUsuario ORDER BY n.fechaCreacion DESC")
    List<Notificacion> findTopByIdUsuario(@Param("idUsuario") Long idUsuario, Pageable pageable);
}
