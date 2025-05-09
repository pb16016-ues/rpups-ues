package com.ues.edu.sv.rpups_ues.model.repository;

import com.ues.edu.sv.rpups_ues.model.entity.SolicitudProyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitudProyectoRepository extends JpaRepository<SolicitudProyecto, Long> {

    List<SolicitudProyecto> findByTituloContainingIgnoreCase(String titulo);

    List<SolicitudProyecto> findByEstadoCodigoEstado(String codigoEstado);

    List<SolicitudProyecto> findByEmpresaIdEmpresa(Long idEmpresa);

    List<SolicitudProyecto> findByCarreraCodigo(String codigoCarrera);

    List<SolicitudProyecto> findByModalidadCodigoModalidad(String codigoModalidad);

    List<SolicitudProyecto> findByAdministradorIdUsuario(Long idUsuario);

    List<SolicitudProyecto> findByEmpresaIdEmpresaAndEstadoCodigoEstado(Long idEmpresa, String codigoEstado);

    List<SolicitudProyecto> findByCarreraCodigoAndEstadoCodigoEstado(String codigoCarrera, String codigoEstado);

    List<SolicitudProyecto> findByModalidadCodigoModalidadAndEstadoCodigoEstado(String codigoModalidad,
            String codigoEstado);
}