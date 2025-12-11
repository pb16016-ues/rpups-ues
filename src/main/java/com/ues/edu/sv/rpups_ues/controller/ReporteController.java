package com.ues.edu.sv.rpups_ues.controller;

import com.ues.edu.sv.rpups_ues.service.ReporteExcelService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para generación de reportes en Excel.
 * Endpoints para exportar datos del sistema a formato .xlsx
 */
@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    private final ReporteExcelService reporteExcelService;

    private static final String EXCEL_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    public ReporteController(ReporteExcelService reporteExcelService) {
        this.reporteExcelService = reporteExcelService;
    }

    // ========== REPORTES DE PROYECTOS ==========

    /**
     * Genera reporte Excel de todos los proyectos.
     */
    @GetMapping("/proyectos/excel")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'COORD')")
    public ResponseEntity<byte[]> exportarProyectosTodos() {
        byte[] excelData = reporteExcelService.generarExcelProyectosTodos();
        return crearRespuestaExcel(excelData, "proyectos_todos.xlsx");
    }

    /**
     * Genera reporte Excel de proyectos filtrados por estado.
     */
    @GetMapping("/proyectos/excel/estado")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'COORD')")
    public ResponseEntity<byte[]> exportarProyectosPorEstado(
            @RequestParam("codEstado") String codigoEstado,
            @RequestParam(value = "nombreEstado", defaultValue = "") String nombreEstado) {
        
        byte[] excelData = reporteExcelService.generarExcelProyectosPorEstado(codigoEstado, nombreEstado);
        String filename = "proyectos_estado_" + codigoEstado.toLowerCase() + ".xlsx";
        return crearRespuestaExcel(excelData, filename);
    }

    /**
     * Genera reporte Excel de proyectos filtrados por carrera.
     */
    @GetMapping("/proyectos/excel/carrera")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'COORD')")
    public ResponseEntity<byte[]> exportarProyectosPorCarrera(
            @RequestParam("codCarrera") String codigoCarrera,
            @RequestParam(value = "nombreCarrera", defaultValue = "") String nombreCarrera) {
        
        byte[] excelData = reporteExcelService.generarExcelProyectosPorCarrera(codigoCarrera, nombreCarrera);
        String filename = "proyectos_carrera_" + codigoCarrera.toLowerCase() + ".xlsx";
        return crearRespuestaExcel(excelData, filename);
    }

    /**
     * Genera reporte Excel de proyectos filtrados por empresa.
     */
    @GetMapping("/proyectos/excel/empresa")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'COORD')")
    public ResponseEntity<byte[]> exportarProyectosPorEmpresa(
            @RequestParam("idEmpresa") Long idEmpresa,
            @RequestParam(value = "nombreEmpresa", defaultValue = "") String nombreEmpresa) {
        
        byte[] excelData = reporteExcelService.generarExcelProyectosPorEmpresa(idEmpresa, nombreEmpresa);
        String filename = "proyectos_empresa_" + idEmpresa + ".xlsx";
        return crearRespuestaExcel(excelData, filename);
    }

    // ========== REPORTES DE SOLICITUDES ==========

    /**
     * Genera reporte Excel de todas las solicitudes.
     */
    @GetMapping("/solicitudes/excel")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'COORD')")
    public ResponseEntity<byte[]> exportarSolicitudesTodas() {
        byte[] excelData = reporteExcelService.generarExcelSolicitudesTodas();
        return crearRespuestaExcel(excelData, "solicitudes_todas.xlsx");
    }

    /**
     * Genera reporte Excel de solicitudes filtradas por estado.
     */
    @GetMapping("/solicitudes/excel/estado")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'COORD')")
    public ResponseEntity<byte[]> exportarSolicitudesPorEstado(
            @RequestParam("codEstado") String codigoEstado,
            @RequestParam(value = "nombreEstado", defaultValue = "") String nombreEstado) {
        
        byte[] excelData = reporteExcelService.generarExcelSolicitudesPorEstado(codigoEstado, nombreEstado);
        String filename = "solicitudes_estado_" + codigoEstado.toLowerCase() + ".xlsx";
        return crearRespuestaExcel(excelData, filename);
    }

    /**
     * Genera reporte Excel de solicitudes filtradas por carrera.
     */
    @GetMapping("/solicitudes/excel/carrera")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'COORD')")
    public ResponseEntity<byte[]> exportarSolicitudesPorCarrera(
            @RequestParam("codCarrera") String codigoCarrera,
            @RequestParam(value = "nombreCarrera", defaultValue = "") String nombreCarrera) {
        
        byte[] excelData = reporteExcelService.generarExcelSolicitudesPorCarrera(codigoCarrera, nombreCarrera);
        String filename = "solicitudes_carrera_" + codigoCarrera.toLowerCase() + ".xlsx";
        return crearRespuestaExcel(excelData, filename);
    }

    /**
     * Genera reporte Excel de solicitudes filtradas por empresa.
     */
    @GetMapping("/solicitudes/excel/empresa")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'COORD')")
    public ResponseEntity<byte[]> exportarSolicitudesPorEmpresa(
            @RequestParam("idEmpresa") Long idEmpresa,
            @RequestParam(value = "nombreEmpresa", defaultValue = "") String nombreEmpresa) {
        
        byte[] excelData = reporteExcelService.generarExcelSolicitudesPorEmpresa(idEmpresa, nombreEmpresa);
        String filename = "solicitudes_empresa_" + idEmpresa + ".xlsx";
        return crearRespuestaExcel(excelData, filename);
    }

    // ========== REPORTES DE POSTULACIONES ==========

    /**
     * Genera reporte Excel de postulaciones de un proyecto.
     */
    @GetMapping("/postulaciones/excel/proyecto/{idProyecto}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'COORD')")
    public ResponseEntity<byte[]> exportarPostulacionesPorProyecto(
            @PathVariable Long idProyecto,
            @RequestParam(value = "nombreProyecto", defaultValue = "") String nombreProyecto) {
        
        byte[] excelData = reporteExcelService.generarExcelPostulacionesPorProyecto(idProyecto, nombreProyecto);
        String filename = "postulaciones_proyecto_" + idProyecto + ".xlsx";
        return crearRespuestaExcel(excelData, filename);
    }

    /**
     * Genera reporte Excel de postulaciones filtradas por estado.
     */
    @GetMapping("/postulaciones/excel/estado")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'COORD')")
    public ResponseEntity<byte[]> exportarPostulacionesPorEstado(
            @RequestParam("codEstado") String codigoEstado,
            @RequestParam(value = "nombreEstado", defaultValue = "") String nombreEstado) {
        
        byte[] excelData = reporteExcelService.generarExcelPostulacionesPorEstado(codigoEstado, nombreEstado);
        String filename = "postulaciones_estado_" + codigoEstado.toLowerCase() + ".xlsx";
        return crearRespuestaExcel(excelData, filename);
    }

    /**
     * Genera reporte Excel de postulaciones de un estudiante.
     */
    @GetMapping("/postulaciones/excel/estudiante/{idEstudiante}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'COORD', 'ESTUD')")
    public ResponseEntity<byte[]> exportarPostulacionesPorEstudiante(
            @PathVariable Long idEstudiante,
            @RequestParam(value = "nombreEstudiante", defaultValue = "") String nombreEstudiante) {
        
        byte[] excelData = reporteExcelService.generarExcelPostulacionesPorEstudiante(idEstudiante, nombreEstudiante);
        String filename = "postulaciones_estudiante_" + idEstudiante + ".xlsx";
        return crearRespuestaExcel(excelData, filename);
    }

    // ========== REPORTES DE ESTADÍSTICAS ==========

    /**
     * Genera reporte Excel con estadísticas generales del sistema.
     */
    @GetMapping("/estadisticas/excel")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'COORD')")
    public ResponseEntity<byte[]> exportarEstadisticasGenerales() {
        byte[] excelData = reporteExcelService.generarExcelEstadisticasGenerales();
        return crearRespuestaExcel(excelData, "estadisticas_generales.xlsx");
    }

    /**
     * Genera reporte Excel de estudiantes asignados a proyectos.
     */
    @GetMapping("/estudiantes-asignados/excel")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'COORD')")
    public ResponseEntity<byte[]> exportarEstudiantesAsignados() {
        byte[] excelData = reporteExcelService.generarExcelEstudiantesAsignados();
        return crearRespuestaExcel(excelData, "estudiantes_asignados.xlsx");
    }

    // ========== MÉTODOS DE UTILIDAD ==========

    /**
     * Crea una respuesta HTTP con el archivo Excel.
     */
    private ResponseEntity<byte[]> crearRespuestaExcel(byte[] data, String filename) {
        if (data == null || data.length == 0) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body("No hay datos para generar el reporte".getBytes());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(EXCEL_CONTENT_TYPE));
        headers.setContentDisposition(
                org.springframework.http.ContentDisposition.attachment()
                        .filename(filename)
                        .build()
        );
        headers.setContentLength(data.length);

        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }
}
