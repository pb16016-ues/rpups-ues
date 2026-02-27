package com.ues.edu.sv.rpups_ues.service;

import java.util.List;

import com.ues.edu.sv.rpups_ues.model.entity.Postulacion;
import com.ues.edu.sv.rpups_ues.model.entity.Proyecto;
import com.ues.edu.sv.rpups_ues.model.entity.SolicitudProyecto;

/**
 * Servicio para generar reportes en formato Excel (.xlsx).
 */
public interface ReporteExcelService {

    // ========== REPORTES DE PROYECTOS ==========

    /**
     * Genera reporte Excel de proyectos filtrados por estado.
     */
    byte[] generarExcelProyectosPorEstado(String codigoEstado, String nombreEstado);

    /**
     * Genera reporte Excel de proyectos filtrados por carrera.
     */
    byte[] generarExcelProyectosPorCarrera(String codigoCarrera, String nombreCarrera);

    /**
     * Genera reporte Excel de proyectos filtrados por empresa.
     */
    byte[] generarExcelProyectosPorEmpresa(Long idEmpresa, String nombreEmpresa);

    /**
     * Genera reporte Excel de todos los proyectos.
     */
    byte[] generarExcelProyectosTodos();

    /**
     * Genera reporte Excel de proyectos desde una lista.
     */
    byte[] generarExcelProyectos(List<Proyecto> proyectos, String titulo);

    /**
     * Genera reporte Excel de proyectos creados por un tutor/administrador en un rango de fechas.
     * Solo incluye proyectos aprobados (estado APRO).
     * 
     * @param idAdministrador ID del tutor/administrador
     * @param nombreTutor Nombre del tutor para incluir en el título
     * @param fechaInicio Fecha inicio del periodo (opcional, puede ser null)
     * @param fechaFin Fecha fin del periodo (opcional, puede ser null)
     * @return Archivo Excel con el reporte
     */
    byte[] generarExcelProyectosPorTutor(Long idAdministrador, String nombreTutor, 
                                          java.time.LocalDate fechaInicio, 
                                          java.time.LocalDate fechaFin);

    // ========== REPORTES DE SOLICITUDES ==========

    /**
     * Genera reporte Excel de solicitudes filtradas por estado.
     */
    byte[] generarExcelSolicitudesPorEstado(String codigoEstado, String nombreEstado);

    /**
     * Genera reporte Excel de solicitudes filtradas por carrera.
     */
    byte[] generarExcelSolicitudesPorCarrera(String codigoCarrera, String nombreCarrera);

    /**
     * Genera reporte Excel de solicitudes filtradas por empresa.
     */
    byte[] generarExcelSolicitudesPorEmpresa(Long idEmpresa, String nombreEmpresa);

    /**
     * Genera reporte Excel de todas las solicitudes.
     */
    byte[] generarExcelSolicitudesTodas();

    /**
     * Genera reporte Excel de solicitudes desde una lista.
     */
    byte[] generarExcelSolicitudes(List<SolicitudProyecto> solicitudes, String titulo);

    // ========== REPORTES DE POSTULACIONES ==========

    /**
     * Genera reporte Excel de postulaciones de un proyecto.
     */
    byte[] generarExcelPostulacionesPorProyecto(Long idProyecto, String nombreProyecto);

    /**
     * Genera reporte Excel de postulaciones filtradas por estado.
     */
    byte[] generarExcelPostulacionesPorEstado(String codigoEstado, String nombreEstado);

    /**
     * Genera reporte Excel de postulaciones de un estudiante.
     */
    byte[] generarExcelPostulacionesPorEstudiante(Long idEstudiante, String nombreEstudiante);

    /**
     * Genera reporte Excel de postulaciones desde una lista.
     */
    byte[] generarExcelPostulaciones(List<Postulacion> postulaciones, String titulo);

    // ========== REPORTES DE ESTADÍSTICAS ==========

    /**
     * Genera reporte Excel con resumen estadístico general del sistema.
     */
    byte[] generarExcelEstadisticasGenerales();

    /**
     * Genera reporte Excel de estudiantes asignados a proyectos activos.
     */
    byte[] generarExcelEstudiantesAsignados();
}
