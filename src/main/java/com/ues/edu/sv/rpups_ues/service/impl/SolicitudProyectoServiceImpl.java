package com.ues.edu.sv.rpups_ues.service.impl;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.ues.edu.sv.rpups_ues.model.entity.SolicitudProyecto;
import com.ues.edu.sv.rpups_ues.model.entity.Estado;
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
import java.time.LocalDateTime;

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
        return solicitudProyectoRepository.findByCodigoEstado(codigoEstado);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SolicitudProyecto> findByEstadoRevision(Pageable pageable) {
        String codigoEstado = "REV";
        return solicitudProyectoRepository.findByCodigoEstado(codigoEstado, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SolicitudProyecto> findByEmpresa(Long idEmpresa, Pageable pageable) {
        return solicitudProyectoRepository.findByIdEmpresa(idEmpresa, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SolicitudProyecto> findByCarrera(String codigoCarrera) {
        return solicitudProyectoRepository.findByCodigoCarrera(codigoCarrera);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SolicitudProyecto> findByModalidad(String codigoModalidad) {
        return solicitudProyectoRepository.findByCodigoModalidad(codigoModalidad);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SolicitudProyecto> findByAdministradorRevisorAndCodigoEstadoRevision(Long idUsuario) {
        return solicitudProyectoRepository.findByIdAdminRevisorAndCodigoEstado(idUsuario, "REV");
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SolicitudProyecto> findByUserCreador(Long idUsuario, Pageable pageable) {
        return solicitudProyectoRepository.findByIdUserCreador(idUsuario, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SolicitudProyecto> findByEmpresaIdEmpresaAndEstadoCodigoEstado(Long idEmpresa, String codigoEstado) {
        return solicitudProyectoRepository.findByIdEmpresaAndCodigoEstado(idEmpresa, codigoEstado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SolicitudProyecto> findByCarreraCodigoAndEstadoCodigoEstado(String codigoCarrera, String codigoEstado) {
        return solicitudProyectoRepository.findByCodigoCarreraAndCodigoEstado(codigoCarrera, codigoEstado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SolicitudProyecto> findByModalidadCodigoModalidadAndEstadoCodigoEstado(String codigoModalidad,
            String codigoEstado) {
        return solicitudProyectoRepository.findByCodigoModalidadAndCodigoEstado(codigoModalidad,
                codigoEstado);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SolicitudProyecto> findSolicitudByFiltros(String filter, Long idDeptoCarrera, Pageable pageable) {
        return solicitudProyectoRepository.searchByAnyField(filter, idDeptoCarrera, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SolicitudProyecto> findSolicitudByFiltrosWithUserCreador(String filter, Long idUserCreador,
            Long idDeptoCarrera,
            Pageable pageable) {
        return solicitudProyectoRepository.searchByAnyFieldAndUser(filter, idUserCreador, idDeptoCarrera, pageable);
    }

    @Override
    @Transactional
    public SolicitudProyecto save(SolicitudProyecto solicitudProyecto) {
        solicitudProyecto.setFechaCreacion(null);
        solicitudProyecto.setFechaRevision(null);
        solicitudProyecto.setObservaciones(null);
        solicitudProyecto.setAdminRevisor(null);

        Estado estadoNew = new Estado("PEND");
        solicitudProyecto.setEstado(estadoNew);

        if (solicitudProyecto.getFechaFin() != null && solicitudProyecto.getFechaInicio() != null) {
            if (!solicitudProyecto.getFechaFin().isAfter(solicitudProyecto.getFechaInicio())) {
                throw new IllegalArgumentException(
                        "La fecha estimada de finalización del proyecto debe ser posterior a la fecha estimada de inicio.");
            }
        }

        return solicitudProyectoRepository.save(solicitudProyecto);
    }

    @Override
    @Transactional
    public SolicitudProyecto updateAdmin(SolicitudProyecto solicitudProyectoBD, SolicitudProyecto solicitudProyecto) {
        solicitudProyecto.setFechaCreacion(solicitudProyectoBD.getFechaCreacion());

        if (solicitudProyecto.getEstado() == null && solicitudProyectoBD.getEstado() == null) {
            throw new IllegalArgumentException(
                    "La solicitud de proyecto debe contener un estado especificado.");
        }

        if (solicitudProyecto.getFechaRevision() != null && solicitudProyectoBD.getFechaCreacion() != null) {
            if (!solicitudProyecto.getFechaRevision().isAfter(solicitudProyectoBD.getFechaCreacion())) {
                throw new IllegalArgumentException(
                        "La fecha de revisión debe ser posterior a la fecha de creación de la solicitud.");
            }
        }
        if (solicitudProyectoBD.getFechaCreacion() == null) {
            solicitudProyecto.setFechaCreacion(LocalDateTime.now());
        }

        if (solicitudProyecto.getFechaFin() != null && solicitudProyecto.getFechaInicio() != null) {
            if (!solicitudProyecto.getFechaFin().isAfter(solicitudProyecto.getFechaInicio())) {
                throw new IllegalArgumentException(
                        "La fecha estimada de finalización del proyecto debe ser posterior a la fecha estimada de inicio.");
            }
        }

        if ((solicitudProyecto.getCodigoEstado().equals("RECH")
                || solicitudProyecto.getCodigoEstado().equals("OBS"))
                && (!solicitudProyecto.getCodigoEstado().equals("APRO"))
                && (solicitudProyecto.getObservaciones() == null || solicitudProyecto.getObservaciones().isEmpty())
                && solicitudProyectoBD.getObservaciones() == null) {
            throw new IllegalArgumentException(
                    "La solicitud de proyecto debe contener al menos un argumento en Observaciones");
        }

        return solicitudProyectoRepository.save(solicitudProyecto);
    }

    @Override
    @Transactional
    public SolicitudProyecto updateExterno(SolicitudProyecto solicitudProyectoBD, SolicitudProyecto solicitudProyecto) {
        solicitudProyecto.setFechaCreacion(solicitudProyectoBD.getFechaCreacion());

        if (solicitudProyecto.getEstado() == null && solicitudProyectoBD.getEstado() == null) {
            throw new IllegalArgumentException(
                    "La solicitud de proyecto debe contener un estado especificado.");
        }

        if (solicitudProyecto.getFechaRevision() != null && solicitudProyectoBD.getFechaCreacion() != null) {
            if (!solicitudProyecto.getFechaRevision().isAfter(solicitudProyectoBD.getFechaCreacion())) {
                throw new IllegalArgumentException(
                        "La fecha de revisión debe ser posterior a la fecha de creación de la solicitud.");
            }
        }
        if (solicitudProyectoBD.getFechaCreacion() == null) {
            solicitudProyecto.setFechaCreacion(LocalDateTime.now());
        }

        if (solicitudProyecto.getFechaFin() != null && solicitudProyecto.getFechaInicio() != null) {
            if (!solicitudProyecto.getFechaFin().isAfter(solicitudProyecto.getFechaInicio())) {
                throw new IllegalArgumentException(
                        "La fecha estimada de finalización del proyecto debe ser posterior a la fecha estimada de inicio.");
            }
        }

        if ((solicitudProyecto.getCodigoEstado().equals("RECH")
                || solicitudProyecto.getCodigoEstado().equals("OBS"))
                && (!solicitudProyecto.getCodigoEstado().equals("APRO"))
                && (solicitudProyecto.getObservaciones() == null || solicitudProyecto.getObservaciones().isEmpty())
                && solicitudProyectoBD.getObservaciones() == null) {
            throw new IllegalArgumentException(
                    "La solicitud de proyecto debe contener al menos un argumento en Observaciones");
        }

        return solicitudProyectoRepository.save(solicitudProyecto);
    }

    @Override
    @Transactional
    public void deleteById(Long idSolicitud) {
        if (idSolicitud == null) {
            throw new IllegalArgumentException("El ID del proyecto no puede ser nulo");
        }
        SolicitudProyecto solicitudProyecto = solicitudProyectoRepository.findById(idSolicitud)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Proyecto no encontrado con el ID proporcionado: " + idSolicitud));

        solicitudProyecto.setCodigoEstado("ELI");
        solicitudProyectoRepository.save(solicitudProyecto);
    }

    @Override
    public byte[] generarReportePorEstado(String codigoEstado, String nombreEstado) {

        List<SolicitudProyecto> solicitudes = solicitudProyectoRepository.findByCodigoEstado(codigoEstado);

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

        List<SolicitudProyecto> solicitudes = solicitudProyectoRepository.findByCodigoCarrera(codigoCarrera);

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

        List<SolicitudProyecto> solicitudes = solicitudProyectoRepository.findByIdEmpresa(idEmpresa);

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

    @Override
    public byte[] generarReportePorDeptoCarreraYCarrera(Long idDeptoCarrera, String nombreDeptoCarrera,
            String codigoCarrera, String nombreCarrera) {
        List<SolicitudProyecto> solicitudes = solicitudProyectoRepository
                .findByIdDeptoCarreraAndCodigoCarrera(idDeptoCarrera, codigoCarrera);

        Context context = new Context();
        context.setVariable("solicitudes", solicitudes);
        context.setVariable("departamentoCarrera", nombreDeptoCarrera);
        context.setVariable("carrera", nombreCarrera);

        String htmlContent = templateEngine.process("solicitudes-proyectos/reporte_solicitudes_by_depto_carrera",
                context);

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