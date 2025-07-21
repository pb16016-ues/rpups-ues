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

        Page<SolicitudProyecto> findByEmpresa(Long idEmpresa, Pageable pageable);

        List<SolicitudProyecto> findByCarrera(String codigoCarrera);

        List<SolicitudProyecto> findByModalidad(String codigoModalidad);

        List<SolicitudProyecto> findByAdministradorRevisorAndCodigoEstadoRevision(Long idAdminRevisor);

        List<SolicitudProyecto> findByUserCreador(Long idUsuario);

        List<SolicitudProyecto> findByEmpresaIdEmpresaAndEstadoCodigoEstado(Long idEmpresa, String codigoEstado);

        List<SolicitudProyecto> findByCarreraCodigoAndEstadoCodigoEstado(String codigoCarrera, String codigoEstado);

        List<SolicitudProyecto> findByModalidadCodigoModalidadAndEstadoCodigoEstado(String codigoModalidad,
                        String codigoEstado);

        Page<SolicitudProyecto> findSolicitudByFiltros(String filter, Long idDeptoCarrera, Pageable pageable);

        Page<SolicitudProyecto> findByEstadoRevision(Pageable pageable);

        Page<SolicitudProyecto> findSolicitudByFiltrosWithUserCreador(String filter, Long idUserCreador,
                        Long idDeptoCarrera,
                        Pageable pageable);

        SolicitudProyecto save(SolicitudProyecto solicitudProyecto);

        SolicitudProyecto updateAdmin(SolicitudProyecto solicitudProyectoBD, SolicitudProyecto solicitudProyecto);

        SolicitudProyecto updateExterno(SolicitudProyecto solicitudProyectoBD, SolicitudProyecto solicitudProyecto);

        void deleteById(Long idSolicitud);

        byte[] generarReportePorEstado(String codigoEstado, String nombreEstado);

        byte[] generarReportePorCarrera(String codigoCarrera, String nombreCarrera);

        byte[] generarReportePorEmpresa(Long idEmpresa, String nombreEmpresa);
}