package com.ues.edu.sv.rpups_ues.service.impl;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.ues.edu.sv.rpups_ues.model.entity.SolicitudProyecto;
import com.ues.edu.sv.rpups_ues.model.repository.SolicitudProyectoRepository;
import com.ues.edu.sv.rpups_ues.service.SolicitudProyectoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Optional;

@Service
public class SolicitudProyectoServiceImpl implements SolicitudProyectoService {

    private final SolicitudProyectoRepository solicitudProyectoRepository;
    private final SpringTemplateEngine templateEngine;

    public SolicitudProyectoServiceImpl(SolicitudProyectoRepository solicitudProyectoRepository,
            SpringTemplateEngine templateEngine) {
        this.solicitudProyectoRepository = solicitudProyectoRepository;
        this.templateEngine = templateEngine;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SolicitudProyecto> findAll(Pageable pageable) {
        return solicitudProyectoRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SolicitudProyecto> findById(Long idSolicitud) {
        return solicitudProyectoRepository.findById(idSolicitud);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SolicitudProyecto> findByTitulo(String titulo) {
        return solicitudProyectoRepository.findByTituloContainingIgnoreCase(titulo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SolicitudProyecto> findByEstado(String codigoEstado) {
        return solicitudProyectoRepository.findByEstadoCodigoEstado(codigoEstado);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SolicitudProyecto> findByEstadoRevision(Pageable pageable) {
        String codigoEstado = "REV";
        return solicitudProyectoRepository.findByEstadoCodigoEstado(codigoEstado, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SolicitudProyecto> findByEmpresa(Long idEmpresa) {
        return solicitudProyectoRepository.findByEmpresaIdEmpresa(idEmpresa);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SolicitudProyecto> findByCarrera(String codigoCarrera) {
        return solicitudProyectoRepository.findByCarreraCodigo(codigoCarrera);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SolicitudProyecto> findByModalidad(String codigoModalidad) {
        return solicitudProyectoRepository.findByModalidadCodigoModalidad(codigoModalidad);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SolicitudProyecto> findByAdministradorRevisor(Long idUsuario) {
        return solicitudProyectoRepository.findByAdminRevisorIdUsuario(idUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SolicitudProyecto> findByUserCreador(Long idUsuario) {
        return solicitudProyectoRepository.findByUserCreadorIdUsuario(idUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SolicitudProyecto> findByEmpresaIdEmpresaAndEstadoCodigoEstado(Long idEmpresa, String codigoEstado) {
        return solicitudProyectoRepository.findByEmpresaIdEmpresaAndEstadoCodigoEstado(idEmpresa, codigoEstado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SolicitudProyecto> findByCarreraCodigoAndEstadoCodigoEstado(String codigoCarrera, String codigoEstado) {
        return solicitudProyectoRepository.findByCarreraCodigoAndEstadoCodigoEstado(codigoCarrera, codigoEstado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SolicitudProyecto> findByModalidadCodigoModalidadAndEstadoCodigoEstado(String codigoModalidad,
            String codigoEstado) {
        return solicitudProyectoRepository.findByModalidadCodigoModalidadAndEstadoCodigoEstado(codigoModalidad,
                codigoEstado);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SolicitudProyecto> findSolicitudByFiltros(String filter, Pageable pageable) {
        return solicitudProyectoRepository.searchByAnyField(filter, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SolicitudProyecto> findSolicitudByFiltrosWithUserCreador(String filter, Long idUserCreador,
            Pageable pageable) {
        return solicitudProyectoRepository.searchByAnyFieldAndUser(filter, idUserCreador, pageable);
    }

    @Override
    @Transactional
    public SolicitudProyecto save(SolicitudProyecto solicitudProyecto) {
        return solicitudProyectoRepository.save(solicitudProyecto);
    }

    @Override
    @Transactional
    public void deleteById(Long idSolicitud) {
        solicitudProyectoRepository.deleteById(idSolicitud);
    }

    @Override
    public byte[] generarReportePorEstado(String codigoEstado, String nombreEstado) {

        List<SolicitudProyecto> solicitudes = solicitudProyectoRepository.findByEstadoCodigoEstado(codigoEstado);

        Context context = new Context();
        context.setVariable("solicitudes", solicitudes);
        context.setVariable("estado", nombreEstado);

        String htmlContent = templateEngine.process("solicitudes-proyectos/reporte_solicitudes_by_estado", context);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(htmlContent, null);
            builder.toStream(outputStream);
            builder.run();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el reporte PDF", e);
        }
    }

    @Override
    public byte[] generarReportePorCarrera(String codigoCarrera, String nombreCarrera) {

        List<SolicitudProyecto> solicitudes = solicitudProyectoRepository.findByCarreraCodigo(codigoCarrera);

        Context context = new Context();
        context.setVariable("solicitudes", solicitudes);
        context.setVariable("carrera", nombreCarrera);

        String htmlContent = templateEngine.process("solicitudes-proyectos/reporte_solicitudes_by_carrera", context);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(htmlContent, null);
            builder.toStream(outputStream);
            builder.run();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el reporte PDF", e);
        }
    }

    @Override
    public byte[] generarReportePorEmpresa(Long idEmpresa, String nombreEmpresa) {

        List<SolicitudProyecto> solicitudes = solicitudProyectoRepository.findByEmpresaIdEmpresa(idEmpresa);

        Context context = new Context();
        context.setVariable("solicitudes", solicitudes);
        context.setVariable("empresa", nombreEmpresa);

        String htmlContent = templateEngine.process("solicitudes-proyectos/reporte_solicitudes_by_empresa", context);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(htmlContent, null);
            builder.toStream(outputStream);
            builder.run();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el reporte PDF", e);
        }
    }
}