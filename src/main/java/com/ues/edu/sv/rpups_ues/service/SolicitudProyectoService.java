package com.ues.edu.sv.rpups_ues.service;

import com.ues.edu.sv.rpups_ues.model.DTO.AprobacionSolicitudResponse;
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

        Page<SolicitudProyecto> findByUserCreador(Long idUsuario, Pageable pageable);

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

        byte[] generarReportePorDeptoCarreraYCarrera(Long idDeptoCarrera, String nombreDeptoCarrera,
                        String codigoCarrera, String nombreCarrera);

        /**
         * Aprueba una solicitud de proyecto y crea automáticamente el proyecto correspondiente.
         * @param idSolicitud ID de la solicitud a aprobar
         * @param idAdmin ID del administrador que aprueba
         * @param observaciones Observaciones opcionales
         * @param codigoEstadoProyecto Código de estado inicial del proyecto (por defecto DISP)
         * @return DTO con la solicitud aprobada y el proyecto creado
         */
        AprobacionSolicitudResponse aprobarYCrearProyecto(Long idSolicitud, Long idAdmin, String observaciones, String codigoEstadoProyecto);

        /**
         * Verifica si ya existe un proyecto creado a partir de una solicitud.
         * @param idSolicitud ID de la solicitud
         * @return true si ya existe un proyecto, false en caso contrario
         */
        boolean existeProyectoParaSolicitud(Long idSolicitud);

        /**
         * Cuenta las solicitudes por código de estado.
         * Optimizado para badges/notificaciones sin cargar datos completos.
         * @param codigoEstado Código del estado (PEND, REV, APRO, RECH, OBS)
         * @return Número de solicitudes con ese estado
         */
        long countByEstado(String codigoEstado);

        /**
         * Obtiene solicitudes sin asignar (sin admin revisor).
         * Para la pestaña "Sin Asignar".
         */
        List<SolicitudProyecto> findUnassigned();

        /**
         * Cuenta solicitudes sin asignar.
         */
        long countUnassigned();

        /**
         * Obtiene solicitudes sin asignar para COORD (filtradas por departamento de carrera).
         * Para la pestaña "Sin Asignar" del COORD.
         */
        List<SolicitudProyecto> findUnassignedCoord(Long idDeptoCarrera);

        /**
         * Cuenta solicitudes sin asignar para COORD.
         */
        long countUnassignedCoord(Long idDeptoCarrera);

        /**
         * Obtiene la bandeja de entrada de un admin (asignadas y no cerradas).
         * Para la pestaña "Bandeja de entrada".
         */
        List<SolicitudProyecto> findBandejaEntrada(Long idAdmin);

        /**
         * Cuenta la bandeja de entrada de un admin.
         */
        long countBandejaEntrada(Long idAdmin);

        /**
         * Obtiene la bandeja de entrada para COORD (asignadas al COORD o SUP del mismo depto carrera).
         * Para la pestaña "Bandeja de entrada" del COORD.
         */
        List<SolicitudProyecto> findBandejaEntradaCoord(Long idDeptoCarrera);

        /**
         * Cuenta la bandeja de entrada para COORD.
         */
        long countBandejaEntradaCoord(Long idDeptoCarrera);

        /**
         * Obtiene la bandeja de entrada para ADMIN (todas las solicitudes no cerradas).
         * Para modo lectura del ADMIN.
         */
        List<SolicitudProyecto> findBandejaEntradaAdmin();

        /**
         * Cuenta la bandeja de entrada para ADMIN.
         */
        long countBandejaEntradaAdmin();

        /**
         * Obtiene todas las solicitudes asignadas a un admin.
         * Para la pestaña "Solicitudes".
         */
        List<SolicitudProyecto> findByAdminRevisor(Long idAdmin);
}