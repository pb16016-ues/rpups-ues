package com.ues.edu.sv.rpups_ues.service;

import com.ues.edu.sv.rpups_ues.model.entity.SolicitudProyecto;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SolicitudProyectoService {

        Page<SolicitudProyecto> findAll(Pageable pageable);

        Optional<SolicitudProyecto> findById(Long idSolicitud);

        List<SolicitudProyecto> findByTitulo(String titulo);

        List<SolicitudProyecto> findByEstado(String codigoEstado);

        List<SolicitudProyecto> findByEmpresa(Long idEmpresa);

        List<SolicitudProyecto> findByCarrera(String codigoCarrera);

        List<SolicitudProyecto> findByModalidad(String codigoModalidad);

        List<SolicitudProyecto> findByAdministradorRevisor(Long idUsuario);

        List<SolicitudProyecto> findByEmpresaIdEmpresaAndEstadoCodigoEstado(Long idEmpresa, String codigoEstado);

        List<SolicitudProyecto> findByCarreraCodigoAndEstadoCodigoEstado(String codigoCarrera, String codigoEstado);

        List<SolicitudProyecto> findByModalidadCodigoModalidadAndEstadoCodigoEstado(String codigoModalidad,
                        String codigoEstado);

        Page<SolicitudProyecto> findSolicitudByFiltros(String filter, Pageable pageable);

        SolicitudProyecto save(SolicitudProyecto solicitudProyecto);

        void deleteById(Long idSolicitud);
}