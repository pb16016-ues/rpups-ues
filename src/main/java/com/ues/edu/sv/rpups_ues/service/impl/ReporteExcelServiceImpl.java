package com.ues.edu.sv.rpups_ues.service.impl;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ues.edu.sv.rpups_ues.model.entity.Postulacion;
import com.ues.edu.sv.rpups_ues.model.entity.Proyecto;
import com.ues.edu.sv.rpups_ues.model.entity.SolicitudProyecto;
import com.ues.edu.sv.rpups_ues.model.repository.PostulacionRepository;
import com.ues.edu.sv.rpups_ues.model.repository.ProyectoRepository;
import com.ues.edu.sv.rpups_ues.model.repository.SolicitudProyectoRepository;
import com.ues.edu.sv.rpups_ues.service.ReporteExcelService;

/**
 * Implementación del servicio de reportes Excel usando Apache POI.
 */
@Service
@Transactional(readOnly = true)
public class ReporteExcelServiceImpl implements ReporteExcelService {

    private final ProyectoRepository proyectoRepository;
    private final SolicitudProyectoRepository solicitudProyectoRepository;
    private final PostulacionRepository postulacionRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public ReporteExcelServiceImpl(ProyectoRepository proyectoRepository,
                                   SolicitudProyectoRepository solicitudProyectoRepository,
                                   PostulacionRepository postulacionRepository) {
        this.proyectoRepository = proyectoRepository;
        this.solicitudProyectoRepository = solicitudProyectoRepository;
        this.postulacionRepository = postulacionRepository;
    }

    // ========== REPORTES DE PROYECTOS ==========

    @Override
    public byte[] generarExcelProyectosPorEstado(String codigoEstado, String nombreEstado) {
        List<Proyecto> proyectos = proyectoRepository.findByCodigoEstado(codigoEstado);
        return generarExcelProyectos(proyectos, "Proyectos - Estado: " + nombreEstado);
    }

    @Override
    public byte[] generarExcelProyectosPorCarrera(String codigoCarrera, String nombreCarrera) {
        List<Proyecto> proyectos = proyectoRepository.findByCodigoCarrera(codigoCarrera);
        return generarExcelProyectos(proyectos, "Proyectos - Carrera: " + nombreCarrera);
    }

    @Override
    public byte[] generarExcelProyectosPorEmpresa(Long idEmpresa, String nombreEmpresa) {
        List<Proyecto> proyectos = proyectoRepository.findByIdEmpresa(idEmpresa);
        return generarExcelProyectos(proyectos, "Proyectos - Empresa: " + nombreEmpresa);
    }

    @Override
    public byte[] generarExcelProyectosTodos() {
        List<Proyecto> proyectos = proyectoRepository.findAll();
        return generarExcelProyectos(proyectos, "Todos los Proyectos");
    }

    @Override
    public byte[] generarExcelProyectosPorTutor(Long idAdministrador, String nombreTutor, 
                                                 java.time.LocalDate fechaInicio, 
                                                 java.time.LocalDate fechaFin) {
        // Convertir fechas LocalDate a LocalDateTime para la consulta
        LocalDateTime fechaInicioTime = fechaInicio != null ? fechaInicio.atStartOfDay() : null;
        LocalDateTime fechaFinTime = fechaFin != null ? fechaFin.atTime(23, 59, 59) : null;
        
        // Obtener proyectos del tutor en el rango de fechas
        List<Proyecto> proyectos = proyectoRepository.findProyectosPorTutorYFechas(
            idAdministrador, fechaInicioTime, fechaFinTime);
        
        // Construir título del reporte
        StringBuilder titulo = new StringBuilder("Proyectos del Tutor: ");
        titulo.append(nombreTutor);
        
        if (fechaInicio != null || fechaFin != null) {
            titulo.append(" | Periodo: ");
            if (fechaInicio != null) {
                titulo.append(fechaInicio.format(DATE_FORMATTER));
            } else {
                titulo.append("Inicio");
            }
            titulo.append(" - ");
            if (fechaFin != null) {
                titulo.append(fechaFin.format(DATE_FORMATTER));
            } else {
                titulo.append("Presente");
            }
        }
        
        return generarExcelProyectos(proyectos, titulo.toString());
    }

    @Override
    public byte[] generarExcelProyectos(List<Proyecto> proyectos, String titulo) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Proyectos");

            // Estilos
            CellStyle headerStyle = crearEstiloEncabezado(workbook);
            CellStyle titleStyle = crearEstiloTitulo(workbook);
            CellStyle dataStyle = crearEstiloDatos(workbook);
            CellStyle dateStyle = crearEstiloFecha(workbook);

            int rowNum = 0;

            // Título del reporte
            Row titleRow = sheet.createRow(rowNum++);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue(titulo);
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 9));

            // Fecha de generación
            Row dateRow = sheet.createRow(rowNum++);
            Cell dateCell = dateRow.createCell(0);
            dateCell.setCellValue("Generado: " + LocalDateTime.now().format(DATETIME_FORMATTER));
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 9));

            rowNum++; // Fila vacía

            // Encabezados
            String[] headers = {"ID", "Título", "Empresa", "Carrera", "Estado", "Modalidad", 
                               "Fecha Inicio", "Fecha Fin", "Max. Estudiantes", "Dirección"};
            Row headerRow = sheet.createRow(rowNum++);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Datos
            for (Proyecto proyecto : proyectos) {
                Row row = sheet.createRow(rowNum++);
                
                crearCelda(row, 0, proyecto.getIdProyecto(), dataStyle);
                crearCelda(row, 1, proyecto.getTitulo(), dataStyle);
                crearCelda(row, 2, proyecto.getEmpresa() != null ? proyecto.getEmpresa().getNombreComercial() : "N/A", dataStyle);
                crearCelda(row, 3, proyecto.getCarrera() != null ? proyecto.getCarrera().getNombre() : "N/A", dataStyle);
                crearCelda(row, 4, proyecto.getEstado() != null ? proyecto.getEstado().getNombre() : proyecto.getCodigoEstado(), dataStyle);
                crearCelda(row, 5, proyecto.getModalidad() != null ? proyecto.getModalidad().getNombre() : "N/A", dataStyle);
                crearCelda(row, 6, formatearFecha(proyecto.getFechaInicio()), dateStyle);
                crearCelda(row, 7, formatearFecha(proyecto.getFechaFin()), dateStyle);
                crearCelda(row, 8, proyecto.getMaxEstudiantes(), dataStyle);
                crearCelda(row, 9, proyecto.getDireccionDetallada(), dataStyle);
            }

            // Fila de total
            rowNum++;
            Row totalRow = sheet.createRow(rowNum);
            Cell totalLabelCell = totalRow.createCell(0);
            totalLabelCell.setCellValue("Total de proyectos:");
            totalLabelCell.setCellStyle(headerStyle);
            Cell totalValueCell = totalRow.createCell(1);
            totalValueCell.setCellValue(proyectos.size());
            totalValueCell.setCellStyle(headerStyle);

            // Ajustar ancho de columnas
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
                // Limitar ancho máximo
                if (sheet.getColumnWidth(i) > 15000) {
                    sheet.setColumnWidth(i, 15000);
                }
            }

            return workbookToBytes(workbook);
        } catch (Exception e) {
            throw new RuntimeException("Error al generar reporte Excel de proyectos", e);
        }
    }

    // ========== REPORTES DE SOLICITUDES ==========

    @Override
    public byte[] generarExcelSolicitudesPorEstado(String codigoEstado, String nombreEstado) {
        List<SolicitudProyecto> solicitudes = solicitudProyectoRepository.findByCodigoEstado(codigoEstado);
        return generarExcelSolicitudes(solicitudes, "Solicitudes - Estado: " + nombreEstado);
    }

    @Override
    public byte[] generarExcelSolicitudesPorCarrera(String codigoCarrera, String nombreCarrera) {
        List<SolicitudProyecto> solicitudes = solicitudProyectoRepository.findByCodigoCarrera(codigoCarrera);
        return generarExcelSolicitudes(solicitudes, "Solicitudes - Carrera: " + nombreCarrera);
    }

    @Override
    public byte[] generarExcelSolicitudesPorEmpresa(Long idEmpresa, String nombreEmpresa) {
        List<SolicitudProyecto> solicitudes = solicitudProyectoRepository.findByIdEmpresa(idEmpresa);
        return generarExcelSolicitudes(solicitudes, "Solicitudes - Empresa: " + nombreEmpresa);
    }

    @Override
    public byte[] generarExcelSolicitudesTodas() {
        List<SolicitudProyecto> solicitudes = solicitudProyectoRepository.findAll();
        return generarExcelSolicitudes(solicitudes, "Todas las Solicitudes de Proyecto");
    }

    @Override
    public byte[] generarExcelSolicitudes(List<SolicitudProyecto> solicitudes, String titulo) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Solicitudes");

            CellStyle headerStyle = crearEstiloEncabezado(workbook);
            CellStyle titleStyle = crearEstiloTitulo(workbook);
            CellStyle dataStyle = crearEstiloDatos(workbook);
            CellStyle dateStyle = crearEstiloFecha(workbook);

            int rowNum = 0;

            // Título
            Row titleRow = sheet.createRow(rowNum++);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue(titulo);
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 9));

            // Fecha de generación
            Row dateRow = sheet.createRow(rowNum++);
            Cell dateCell = dateRow.createCell(0);
            dateCell.setCellValue("Generado: " + LocalDateTime.now().format(DATETIME_FORMATTER));
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 9));

            rowNum++;

            // Encabezados
            String[] headers = {"ID", "Título", "Empresa", "Carrera", "Estado", "Fecha Creación", 
                               "Fecha Revisión", "Revisor", "Max. Estudiantes (1-5)", "Observaciones"};
            Row headerRow = sheet.createRow(rowNum++);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Datos
            for (SolicitudProyecto solicitud : solicitudes) {
                Row row = sheet.createRow(rowNum++);
                
                crearCelda(row, 0, solicitud.getIdSolicitud(), dataStyle);
                crearCelda(row, 1, solicitud.getTitulo(), dataStyle);
                crearCelda(row, 2, solicitud.getEmpresa() != null ? solicitud.getEmpresa().getNombreComercial() : "N/A", dataStyle);
                crearCelda(row, 3, solicitud.getCarrera() != null ? solicitud.getCarrera().getNombre() : "N/A", dataStyle);
                crearCelda(row, 4, solicitud.getEstado() != null ? solicitud.getEstado().getNombre() : solicitud.getCodigoEstado(), dataStyle);
                crearCelda(row, 5, formatearFechaHora(solicitud.getFechaCreacion()), dateStyle);
                crearCelda(row, 6, formatearFechaHora(solicitud.getFechaRevision()), dateStyle);
                crearCelda(row, 7, solicitud.getAdminRevisor() != null ? 
                           solicitud.getAdminRevisor().getNombres() + " " + solicitud.getAdminRevisor().getApellidos() : "Sin asignar", dataStyle);
                crearCelda(row, 8, solicitud.getMaxEstudiantes(), dataStyle);
                crearCelda(row, 9, solicitud.getObservaciones() != null ? solicitud.getObservaciones() : "", dataStyle);
            }

            // Total
            rowNum++;
            Row totalRow = sheet.createRow(rowNum);
            Cell totalLabelCell = totalRow.createCell(0);
            totalLabelCell.setCellValue("Total de solicitudes:");
            totalLabelCell.setCellStyle(headerStyle);
            Cell totalValueCell = totalRow.createCell(1);
            totalValueCell.setCellValue(solicitudes.size());
            totalValueCell.setCellStyle(headerStyle);

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
                if (sheet.getColumnWidth(i) > 15000) {
                    sheet.setColumnWidth(i, 15000);
                }
            }

            return workbookToBytes(workbook);
        } catch (Exception e) {
            throw new RuntimeException("Error al generar reporte Excel de solicitudes", e);
        }
    }

    // ========== REPORTES DE POSTULACIONES ==========

    @Override
    public byte[] generarExcelPostulacionesPorProyecto(Long idProyecto, String nombreProyecto) {
        List<Postulacion> postulaciones = postulacionRepository.findByIdProyecto(idProyecto);
        return generarExcelPostulaciones(postulaciones, "Postulaciones - Proyecto: " + nombreProyecto);
    }

    @Override
    public byte[] generarExcelPostulacionesPorEstado(String codigoEstado, String nombreEstado) {
        List<Postulacion> postulaciones = postulacionRepository.findByCodigoEstado(codigoEstado);
        return generarExcelPostulaciones(postulaciones, "Postulaciones - Estado: " + nombreEstado);
    }

    @Override
    public byte[] generarExcelPostulacionesPorEstudiante(Long idEstudiante, String nombreEstudiante) {
        List<Postulacion> postulaciones = postulacionRepository.findByIdEstudiante(idEstudiante);
        return generarExcelPostulaciones(postulaciones, "Postulaciones - Estudiante: " + nombreEstudiante);
    }

    @Override
    public byte[] generarExcelPostulaciones(List<Postulacion> postulaciones, String titulo) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Postulaciones");

            CellStyle headerStyle = crearEstiloEncabezado(workbook);
            CellStyle titleStyle = crearEstiloTitulo(workbook);
            CellStyle dataStyle = crearEstiloDatos(workbook);
            CellStyle dateStyle = crearEstiloFecha(workbook);

            int rowNum = 0;

            // Título
            Row titleRow = sheet.createRow(rowNum++);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue(titulo);
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));

            // Fecha de generación
            Row dateRow = sheet.createRow(rowNum++);
            Cell dateCell = dateRow.createCell(0);
            dateCell.setCellValue("Generado: " + LocalDateTime.now().format(DATETIME_FORMATTER));
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 8));

            rowNum++;

            // Encabezados
            String[] headers = {"ID", "Estudiante", "Carnet", "Proyecto", "Estado", 
                               "Fecha Postulación", "Fecha Cambio Estado", "Observaciones"};
            Row headerRow = sheet.createRow(rowNum++);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Datos
            for (Postulacion postulacion : postulaciones) {
                Row row = sheet.createRow(rowNum++);
                
                crearCelda(row, 0, postulacion.getIdPostulacion(), dataStyle);
                
                String nombreEstudiante = "N/A";
                String carnet = "N/A";
                if (postulacion.getEstudiante() != null) {
                    nombreEstudiante = postulacion.getEstudiante().getNombres() + " " + postulacion.getEstudiante().getApellidos();
                    carnet = postulacion.getEstudiante().getCarnet() != null ? postulacion.getEstudiante().getCarnet() : "N/A";
                }
                crearCelda(row, 1, nombreEstudiante, dataStyle);
                crearCelda(row, 2, carnet, dataStyle);
                
                String nombreProyecto = "N/A";
                if (postulacion.getProyecto() != null) {
                    nombreProyecto = postulacion.getProyecto().getTitulo();
                }
                crearCelda(row, 3, nombreProyecto, dataStyle);
                
                crearCelda(row, 4, postulacion.getEstado() != null ? postulacion.getEstado().getNombre() : postulacion.getCodigoEstado(), dataStyle);
                crearCelda(row, 5, formatearFechaHora(postulacion.getFechaPostulacion()), dateStyle);
                crearCelda(row, 6, formatearFechaHora(postulacion.getFechaCambioEstado()), dateStyle);
                crearCelda(row, 7, postulacion.getObservaciones() != null ? postulacion.getObservaciones() : "", dataStyle);
            }

            // Resumen por estado
            rowNum += 2;
            long pendientes = postulaciones.stream().filter(p -> "PEND".equals(p.getCodigoEstado())).count();
            long aprobadas = postulaciones.stream().filter(p -> "APRO".equals(p.getCodigoEstado())).count();
            long rechazadas = postulaciones.stream().filter(p -> "RECH".equals(p.getCodigoEstado())).count();

            Row resumenTitleRow = sheet.createRow(rowNum++);
            Cell resumenTitleCell = resumenTitleRow.createCell(0);
            resumenTitleCell.setCellValue("Resumen:");
            resumenTitleCell.setCellStyle(headerStyle);

            Row totalRow = sheet.createRow(rowNum++);
            crearCelda(totalRow, 0, "Total:", headerStyle);
            crearCelda(totalRow, 1, postulaciones.size(), dataStyle);

            Row pendRow = sheet.createRow(rowNum++);
            crearCelda(pendRow, 0, "Pendientes:", dataStyle);
            crearCelda(pendRow, 1, pendientes, dataStyle);

            Row aproRow = sheet.createRow(rowNum++);
            crearCelda(aproRow, 0, "Aprobadas:", dataStyle);
            crearCelda(aproRow, 1, aprobadas, dataStyle);

            Row rechRow = sheet.createRow(rowNum);
            crearCelda(rechRow, 0, "Rechazadas:", dataStyle);
            crearCelda(rechRow, 1, rechazadas, dataStyle);

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
                if (sheet.getColumnWidth(i) > 15000) {
                    sheet.setColumnWidth(i, 15000);
                }
            }

            return workbookToBytes(workbook);
        } catch (Exception e) {
            throw new RuntimeException("Error al generar reporte Excel de postulaciones", e);
        }
    }

    // ========== REPORTES DE ESTADÍSTICAS ==========

    @Override
    public byte[] generarExcelEstadisticasGenerales() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Estadísticas Generales");

            CellStyle headerStyle = crearEstiloEncabezado(workbook);
            CellStyle titleStyle = crearEstiloTitulo(workbook);
            CellStyle dataStyle = crearEstiloDatos(workbook);

            int rowNum = 0;

            // Título
            Row titleRow = sheet.createRow(rowNum++);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("Estadísticas Generales del Sistema RPUPS-UES");
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));

            // Fecha
            Row dateRow = sheet.createRow(rowNum++);
            Cell dateCell = dateRow.createCell(0);
            dateCell.setCellValue("Generado: " + LocalDateTime.now().format(DATETIME_FORMATTER));
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 3));

            rowNum += 2;

            // === PROYECTOS ===
            Row proyectosHeader = sheet.createRow(rowNum++);
            Cell proyectosHeaderCell = proyectosHeader.createCell(0);
            proyectosHeaderCell.setCellValue("PROYECTOS");
            proyectosHeaderCell.setCellStyle(headerStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, 1));

            long totalProyectos = proyectoRepository.count();
            long proyectosDisponibles = proyectoRepository.countByCodigoEstado("DIS");
            long proyectosEnProceso = proyectoRepository.countByCodigoEstado("PRO");
            long proyectosFinalizados = proyectoRepository.countByCodigoEstado("FIN");

            Row totalProyRow = sheet.createRow(rowNum++);
            crearCelda(totalProyRow, 0, "Total de proyectos:", dataStyle);
            crearCelda(totalProyRow, 1, totalProyectos, dataStyle);

            Row dispRow = sheet.createRow(rowNum++);
            crearCelda(dispRow, 0, "Disponibles:", dataStyle);
            crearCelda(dispRow, 1, proyectosDisponibles, dataStyle);

            Row procRow = sheet.createRow(rowNum++);
            crearCelda(procRow, 0, "En proceso:", dataStyle);
            crearCelda(procRow, 1, proyectosEnProceso, dataStyle);

            Row finRow = sheet.createRow(rowNum++);
            crearCelda(finRow, 0, "Finalizados:", dataStyle);
            crearCelda(finRow, 1, proyectosFinalizados, dataStyle);

            rowNum += 2;

            // === SOLICITUDES ===
            Row solicitudesHeader = sheet.createRow(rowNum++);
            Cell solicitudesHeaderCell = solicitudesHeader.createCell(0);
            solicitudesHeaderCell.setCellValue("SOLICITUDES DE PROYECTO");
            solicitudesHeaderCell.setCellStyle(headerStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, 1));

            long totalSolicitudes = solicitudProyectoRepository.count();
            long solicitudesPendientes = solicitudProyectoRepository.countByCodigoEstado("PEND");
            long solicitudesAprobadas = solicitudProyectoRepository.countByCodigoEstado("APRO");
            long solicitudesRechazadas = solicitudProyectoRepository.countByCodigoEstado("RECH");
            long solicitudesObservadas = solicitudProyectoRepository.countByCodigoEstado("OBS");

            Row totalSolRow = sheet.createRow(rowNum++);
            crearCelda(totalSolRow, 0, "Total de solicitudes:", dataStyle);
            crearCelda(totalSolRow, 1, totalSolicitudes, dataStyle);

            Row pendSolRow = sheet.createRow(rowNum++);
            crearCelda(pendSolRow, 0, "Pendientes:", dataStyle);
            crearCelda(pendSolRow, 1, solicitudesPendientes, dataStyle);

            Row aproSolRow = sheet.createRow(rowNum++);
            crearCelda(aproSolRow, 0, "Aprobadas:", dataStyle);
            crearCelda(aproSolRow, 1, solicitudesAprobadas, dataStyle);

            Row rechSolRow = sheet.createRow(rowNum++);
            crearCelda(rechSolRow, 0, "Rechazadas:", dataStyle);
            crearCelda(rechSolRow, 1, solicitudesRechazadas, dataStyle);

            Row obsSolRow = sheet.createRow(rowNum++);
            crearCelda(obsSolRow, 0, "Con observaciones:", dataStyle);
            crearCelda(obsSolRow, 1, solicitudesObservadas, dataStyle);

            rowNum += 2;

            // === POSTULACIONES ===
            Row postulacionesHeader = sheet.createRow(rowNum++);
            Cell postulacionesHeaderCell = postulacionesHeader.createCell(0);
            postulacionesHeaderCell.setCellValue("POSTULACIONES");
            postulacionesHeaderCell.setCellStyle(headerStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, 1));

            long totalPostulaciones = postulacionRepository.count();
            long postulacionesPendientes = postulacionRepository.countByCodigoEstado("PEND");
            long postulacionesAprobadas = postulacionRepository.countByCodigoEstado("APRO");
            long postulacionesRechazadas = postulacionRepository.countByCodigoEstado("RECH");

            Row totalPostRow = sheet.createRow(rowNum++);
            crearCelda(totalPostRow, 0, "Total de postulaciones:", dataStyle);
            crearCelda(totalPostRow, 1, totalPostulaciones, dataStyle);

            Row pendPostRow = sheet.createRow(rowNum++);
            crearCelda(pendPostRow, 0, "Pendientes:", dataStyle);
            crearCelda(pendPostRow, 1, postulacionesPendientes, dataStyle);

            Row aproPostRow = sheet.createRow(rowNum++);
            crearCelda(aproPostRow, 0, "Aprobadas:", dataStyle);
            crearCelda(aproPostRow, 1, postulacionesAprobadas, dataStyle);

            Row rechPostRow = sheet.createRow(rowNum);
            crearCelda(rechPostRow, 0, "Rechazadas:", dataStyle);
            crearCelda(rechPostRow, 1, postulacionesRechazadas, dataStyle);

            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);

            return workbookToBytes(workbook);
        } catch (Exception e) {
            throw new RuntimeException("Error al generar reporte de estadísticas generales", e);
        }
    }

    @Override
    public byte[] generarExcelEstudiantesAsignados() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Estudiantes Asignados");

            CellStyle headerStyle = crearEstiloEncabezado(workbook);
            CellStyle titleStyle = crearEstiloTitulo(workbook);
            CellStyle dataStyle = crearEstiloDatos(workbook);
            CellStyle dateStyle = crearEstiloFecha(workbook);

            int rowNum = 0;

            // Título
            Row titleRow = sheet.createRow(rowNum++);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("Estudiantes Asignados a Proyectos");
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));

            // Fecha
            Row dateRow = sheet.createRow(rowNum++);
            Cell dateCell = dateRow.createCell(0);
            dateCell.setCellValue("Generado: " + LocalDateTime.now().format(DATETIME_FORMATTER));
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 7));

            rowNum++;

            // Encabezados
            String[] headers = {"Estudiante", "Carnet", "Correo", "Teléfono", "Proyecto", "Empresa", "Fecha Asignación", "Estado Proyecto"};
            Row headerRow = sheet.createRow(rowNum++);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Obtener postulaciones aprobadas con proyectos activos
            List<Postulacion> asignados = postulacionRepository.findByCodigoEstado("APRO");

            for (Postulacion postulacion : asignados) {
                if (postulacion.getProyecto() == null) continue;
                
                Row row = sheet.createRow(rowNum++);
                
                String nombreEstudiante = "N/A";
                String carnet = "N/A";
                String correo = "N/A";
                String telefono = "N/A";
                
                if (postulacion.getEstudiante() != null) {
                    nombreEstudiante = postulacion.getEstudiante().getNombres() + " " + postulacion.getEstudiante().getApellidos();
                    carnet = postulacion.getEstudiante().getCarnet() != null ? postulacion.getEstudiante().getCarnet() : "N/A";
                    correo = postulacion.getEstudiante().getCorreoInstitucional() != null ? postulacion.getEstudiante().getCorreoInstitucional() : "N/A";
                    telefono = postulacion.getEstudiante().getTelefono() != null ? postulacion.getEstudiante().getTelefono() : "N/A";
                }
                
                crearCelda(row, 0, nombreEstudiante, dataStyle);
                crearCelda(row, 1, carnet, dataStyle);
                crearCelda(row, 2, correo, dataStyle);
                crearCelda(row, 3, telefono, dataStyle);
                crearCelda(row, 4, postulacion.getProyecto().getTitulo(), dataStyle);
                crearCelda(row, 5, postulacion.getProyecto().getEmpresa() != null ? 
                           postulacion.getProyecto().getEmpresa().getNombreComercial() : "N/A", dataStyle);
                crearCelda(row, 6, formatearFechaHora(postulacion.getFechaCambioEstado()), dateStyle);
                crearCelda(row, 7, postulacion.getProyecto().getEstado() != null ? 
                           postulacion.getProyecto().getEstado().getNombre() : "N/A", dataStyle);
            }

            // Total
            rowNum++;
            Row totalRow = sheet.createRow(rowNum);
            Cell totalLabelCell = totalRow.createCell(0);
            totalLabelCell.setCellValue("Total estudiantes asignados:");
            totalLabelCell.setCellStyle(headerStyle);
            Cell totalValueCell = totalRow.createCell(1);
            totalValueCell.setCellValue(asignados.size());
            totalValueCell.setCellStyle(headerStyle);

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
                if (sheet.getColumnWidth(i) > 15000) {
                    sheet.setColumnWidth(i, 15000);
                }
            }

            return workbookToBytes(workbook);
        } catch (Exception e) {
            throw new RuntimeException("Error al generar reporte de estudiantes asignados", e);
        }
    }

    // ========== MÉTODOS PRIVADOS DE UTILIDAD ==========

    private CellStyle crearEstiloTitulo(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 14);
        font.setColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private CellStyle crearEstiloEncabezado(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private CellStyle crearEstiloDatos(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setWrapText(true);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    private CellStyle crearEstiloFecha(Workbook workbook) {
        CellStyle style = crearEstiloDatos(workbook);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private void crearCelda(Row row, int column, Object value, CellStyle style) {
        Cell cell = row.createCell(column);
        if (value == null) {
            cell.setCellValue("");
        } else if (value instanceof Number) {
            cell.setCellValue(((Number) value).doubleValue());
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue(value.toString());
        }
        cell.setCellStyle(style);
    }

    private String formatearFecha(java.time.LocalDate fecha) {
        if (fecha == null) return "N/A";
        return fecha.format(DATE_FORMATTER);
    }

    private String formatearFechaHora(LocalDateTime fecha) {
        if (fecha == null) return "N/A";
        return fecha.format(DATETIME_FORMATTER);
    }

    private byte[] workbookToBytes(Workbook workbook) throws java.io.IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        return outputStream.toByteArray();
    }
}
