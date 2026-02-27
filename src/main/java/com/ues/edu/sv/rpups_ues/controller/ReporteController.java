package com.ues.edu.sv.rpups_ues.controller;

import com.ues.edu.sv.rpups_ues.service.ReporteExcelService;
import com.ues.edu.sv.rpups_ues.service.UsuarioService;
import com.ues.edu.sv.rpups_ues.model.entity.Usuario;
import com.ues.edu.sv.rpups_ues.model.repository.ProyectoRepository;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Controlador REST para generación de reportes en Excel.
 * Endpoints para exportar datos del sistema a formato .xlsx
 */
@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    private final ReporteExcelService reporteExcelService;
    private final UsuarioService usuarioService;
    private final ProyectoRepository proyectoRepository;

    private static final String EXCEL_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    public ReporteController(ReporteExcelService reporteExcelService,
                            UsuarioService usuarioService,
                            ProyectoRepository proyectoRepository) {
        this.reporteExcelService = reporteExcelService;
        this.usuarioService = usuarioService;
        this.proyectoRepository = proyectoRepository;
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

    /**
     * Genera reporte Excel de proyectos por tutor con filtrado por fechas.
     * - SUP: Solo puede ver sus propios proyectos
     * - COORD: Puede ver sus proyectos y los de SUP de su mismo departamento
     * - ADMIN: Puede ver proyectos de cualquier tutor
     */
    @GetMapping("/proyectos/excel/tutor")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'COORD', 'SUP')")
    public ResponseEntity<byte[]> exportarProyectosPorTutor(
            @RequestParam(required = false) Long idTutor,
            @RequestParam(required = false) String fechaInicio,
            @RequestParam(required = false) String fechaFin) {
        
        // Obtener usuario autenticado
        Long idUsuarioAutenticado = getUsuarioAutenticadoId();
        Usuario usuarioAutenticado = usuarioService.findById(idUsuarioAutenticado)
                .orElseThrow(() -> new RuntimeException("Usuario autenticado no encontrado"));
        
        String rolUsuario = usuarioAutenticado.getCodigoRol();
        Long idTutorFinal;
        
        // Validar permisos según rol
        if ("SUP".equals(rolUsuario)) {
            // SUP solo puede ver sus propios proyectos
            idTutorFinal = idUsuarioAutenticado;
        } else if ("COORD".equals(rolUsuario)) {
            if (idTutor == null) {
                // Si no se especifica tutor, mostrar proyectos del COORD
                idTutorFinal = idUsuarioAutenticado;
            } else {
                // Validar que el tutor solicitado sea el mismo COORD o un SUP de su departamento
                Usuario tutorSolicitado = usuarioService.findById(idTutor)
                        .orElseThrow(() -> new RuntimeException("Tutor no encontrado"));
                
                if (idTutor.equals(idUsuarioAutenticado)) {
                    // El COORD puede ver sus propios proyectos
                    idTutorFinal = idTutor;
                } else if ("SUP".equals(tutorSolicitado.getCodigoRol()) &&
                          usuarioAutenticado.getIdDeptoCarrera() != null &&
                          usuarioAutenticado.getIdDeptoCarrera().equals(tutorSolicitado.getIdDeptoCarrera())) {
                    // El COORD puede ver proyectos de SUP del mismo departamento
                    idTutorFinal = idTutor;
                } else {
                    throw new RuntimeException("No tiene permisos para ver proyectos de este tutor");
                }
            }
        } else if ("ADMIN".equals(rolUsuario)) {
            // ADMIN puede ver proyectos de cualquier tutor
            if (idTutor == null) {
                throw new RuntimeException("Debe especificar un tutor para generar el reporte");
            }
            idTutorFinal = idTutor;
        } else {
            throw new RuntimeException("Rol no autorizado para generar reportes");
        }
        
        // Obtener nombre del tutor para el título
        Usuario tutor = usuarioService.findById(idTutorFinal)
                .orElseThrow(() -> new RuntimeException("Tutor no encontrado"));
        String nombreTutor = tutor.getNombres() + " " + tutor.getApellidos();
        
        // Parsear fechas si se proporcionan
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fechaInicioDate = fechaInicio != null && !fechaInicio.isEmpty() 
                ? LocalDate.parse(fechaInicio, formatter) 
                : null;
        LocalDate fechaFinDate = fechaFin != null && !fechaFin.isEmpty() 
                ? LocalDate.parse(fechaFin, formatter) 
                : null;
        
        // Generar reporte
        byte[] excelData = reporteExcelService.generarExcelProyectosPorTutor(
                idTutorFinal, nombreTutor, fechaInicioDate, fechaFinDate);
        
        // Crear nombre de archivo
        String filename = "proyectos_tutor_" + idTutorFinal;
        if (fechaInicioDate != null || fechaFinDate != null) {
            filename += "_" + (fechaInicio != null ? fechaInicio : "inicio") + 
                       "_" + (fechaFin != null ? fechaFin : "presente");
        }
        filename += ".xlsx";
        
        return crearRespuestaExcel(excelData, filename);
    }

    /**
     * Endpoint de diagnóstico para verificar proyectos con idAdministrador asignado.
     * Útil para debugging del reporte por tutor.
     */
    @GetMapping("/diagnostico/proyectos-tutor")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'COORD', 'SUP')")
    public ResponseEntity<Map<String, Object>> diagnosticoProyectosTutor(@RequestParam Long idTutor) {
        Map<String, Object> diagnostico = new HashMap<>();
        
        // Verificar usuario
        Usuario tutor = usuarioService.findById(idTutor).orElse(null);
        if (tutor == null) {
            diagnostico.put("error", "Tutor no encontrado");
            return ResponseEntity.badRequest().body(diagnostico);
        }
        
        diagnostico.put("tutor", tutor.getNombres() + " " + tutor.getApellidos());
        diagnostico.put("idTutor", idTutor);
        diagnostico.put("rol", tutor.getCodigoRol());
        
        // Buscar proyectos del tutor (sin filtro de fechas)
        var proyectos = proyectoRepository.findProyectosPorTutorYFechas(idTutor, null, null);
        diagnostico.put("totalProyectos", proyectos.size());
        
        if (!proyectos.isEmpty()) {
            diagnostico.put("ejemploProyecto", Map.of(
                "id", proyectos.get(0).getIdProyecto(),
                "titulo", proyectos.get(0).getTitulo(),
                "fechaCreacion", proyectos.get(0).getFechaCreacion(),
                "idAdministrador", proyectos.get(0).getIdAdministrador()
            ));
        }
        
        // Contar todos los proyectos en la base de datos
        long totalProyectosDB = proyectoRepository.count();
        diagnostico.put("totalProyectosEnDB", totalProyectosDB);
        
        return ResponseEntity.ok(diagnostico);
    }

    // ========== MÉTODOS DE UTILIDAD ==========

    /**
     * Obtiene el ID del usuario autenticado desde el contexto de seguridad.
     */
    private Long getUsuarioAutenticadoId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Usuario no autenticado");
        }
        return Long.parseLong(authentication.getName());
    }

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
