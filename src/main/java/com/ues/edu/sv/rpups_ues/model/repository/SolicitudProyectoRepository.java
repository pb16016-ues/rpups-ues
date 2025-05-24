package com.ues.edu.sv.rpups_ues.model.repository;

import com.ues.edu.sv.rpups_ues.model.entity.SolicitudProyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface SolicitudProyectoRepository extends JpaRepository<SolicitudProyecto, Long> {

        List<SolicitudProyecto> findByTituloContainingIgnoreCase(String titulo);

        List<SolicitudProyecto> findByEstadoCodigoEstado(String codigoEstado);

        List<SolicitudProyecto> findByEmpresaIdEmpresa(Long idEmpresa);

        List<SolicitudProyecto> findByCarreraCodigo(String codigoCarrera);

        List<SolicitudProyecto> findByModalidadCodigoModalidad(String codigoModalidad);

        List<SolicitudProyecto> findByAdminRevisorIdUsuario(Long idUsuario);

        List<SolicitudProyecto> findByUserCreadorIdUsuario(Long idUsuario);

        List<SolicitudProyecto> findByEmpresaIdEmpresaAndEstadoCodigoEstado(Long idEmpresa, String codigoEstado);

        List<SolicitudProyecto> findByCarreraCodigoAndEstadoCodigoEstado(String codigoCarrera, String codigoEstado);

        List<SolicitudProyecto> findByModalidadCodigoModalidadAndEstadoCodigoEstado(String codigoModalidad,
                        String codigoEstado);

        @Query("SELECT s FROM SolicitudProyecto s " +
                        "WHERE (:filter IS NULL " +
                        "OR LOWER(s.titulo) LIKE LOWER(CONCAT('%', :filter, '%')) " +
                        "OR LOWER(s.carrera.nombre) LIKE LOWER(CONCAT('%', :filter, '%')) " +
                        "OR LOWER(s.modalidad.nombre) LIKE LOWER(CONCAT('%', :filter, '%')) " +
                        "OR LOWER(s.estado.nombre) LIKE LOWER(CONCAT('%', :filter, '%')))")
        Page<SolicitudProyecto> searchByAnyField(
                        @Param("filter") String filter,
                        Pageable pageable);

        @Query("SELECT s FROM SolicitudProyecto s " +
                        "WHERE (:filter IS NULL " +
                        "OR LOWER(s.titulo) LIKE LOWER(CONCAT('%', :filter, '%')) " +
                        "OR LOWER(s.carrera.nombre) LIKE LOWER(CONCAT('%', :filter, '%')) " +
                        "OR LOWER(s.modalidad.nombre) LIKE LOWER(CONCAT('%', :filter, '%')) " +
                        "OR LOWER(s.estado.nombre) LIKE LOWER(CONCAT('%', :filter, '%'))) " +
                        "AND (:idUserCreador IS NULL OR s.idUserCreacion.idUsuario = :idUserCreador)")
        Page<SolicitudProyecto> searchByAnyFieldAndUser(
                        @Param("filter") String filter,
                        @Param("idUserCreador") Long idUserCreador,
                        Pageable pageable);

        Page<SolicitudProyecto> findByEstadoCodigoEstado(String codigoEstado, Pageable pageable);
}