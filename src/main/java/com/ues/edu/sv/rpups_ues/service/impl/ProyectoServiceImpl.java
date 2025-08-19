package com.ues.edu.sv.rpups_ues.service.impl;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.ues.edu.sv.rpups_ues.model.entity.Proyecto;
import com.ues.edu.sv.rpups_ues.model.repository.ProyectoRepository;
import com.ues.edu.sv.rpups_ues.service.ProyectoService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;
import java.util.Optional;
import java.io.ByteArrayOutputStream;

@Service
public class ProyectoServiceImpl implements ProyectoService {

    private final ProyectoRepository proyectoRepository;
    private final SpringTemplateEngine templateEngine;

    public ProyectoServiceImpl(ProyectoRepository proyectoRepository, SpringTemplateEngine templateEngine) {
        this.proyectoRepository = proyectoRepository;
        this.templateEngine = templateEngine;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Proyecto> findAll(Pageable pageable) {
        return proyectoRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Proyecto> findById(Long idProyecto) {
        return proyectoRepository.findById(idProyecto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proyecto> findByTitulo(String titulo) {
        return proyectoRepository.findByTituloContainingIgnoreCase(titulo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proyecto> findByEstado(String codigoEstado) {
        return proyectoRepository.findByCodigoEstado(codigoEstado);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Proyecto> findByEmpresa(Long idEmpresa, Pageable pageable) {
        return proyectoRepository.findByIdEmpresa(idEmpresa, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proyecto> findByCarrera(String codigoCarrera) {
        return proyectoRepository.findByCodigoCarrera(codigoCarrera);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proyecto> findByModalidad(String codigoModalidad) {
        return proyectoRepository.findByCodigoModalidad(codigoModalidad);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proyecto> findByAdministradorAprobador(Long idUsuario) {
        return proyectoRepository.findByIdAdministrador(idUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proyecto> findByEmpresaIdEmpresaAndEstadoCodigoEstado(Long idEmpresa, String codigoEstado) {
        return proyectoRepository.findByIdEmpresaAndCodigoEstado(idEmpresa, codigoEstado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proyecto> findByCarreraCodigoAndEstadoCodigoEstado(String codigoCarrera, String codigoEstado) {
        return proyectoRepository.findByCodigoCarreraAndCodigoEstado(codigoCarrera, codigoEstado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proyecto> findByModalidadCodigoModalidadAndEstadoCodigoEstado(String codigoModalidad,
            String codigoEstado) {
        return proyectoRepository.findByCodigoModalidadAndCodigoEstado(codigoModalidad, codigoEstado);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Proyecto> findProyectoByFiltros(String filter, Long idDeptoCarrera, Pageable pageable) {
        return proyectoRepository.searchByAnyField(filter, idDeptoCarrera, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Proyecto> findProyectoByFiltrosWithEstadoDisponible(String filter, Long idDeptoCarrera,
            Pageable pageable) {
        return proyectoRepository.searchByAnyFieldDisponible(filter, idDeptoCarrera, pageable);
    }

    @Override
    @Transactional
    public Proyecto save(Proyecto proyecto) {
        return proyectoRepository.save(proyecto);
    }

    @Override
    @Transactional
    public void deleteById(Long idProyecto) {
        if (idProyecto == null) {
            throw new IllegalArgumentException("El ID del proyecto no puede ser nulo");
        }
        Proyecto proyecto = proyectoRepository.findById(idProyecto)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Proyecto no encontrado con el ID proporcionado: " + idProyecto));

        proyecto.setCodigoEstado("ELI");
        proyectoRepository.save(proyecto);
    }

    @Override
    public byte[] generarReportePorEstado(String codigoEstado, String nombreEstado) {

        List<Proyecto> proyectos = proyectoRepository.findByCodigoEstado(codigoEstado);

        Context context = new Context();
        context.setVariable("proyectos", proyectos);
        context.setVariable("estado", nombreEstado);

        String htmlContent = templateEngine.process("proyectos/reporte_proyectos_by_estado", context);

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

        List<Proyecto> proyectos = proyectoRepository.findByCodigoCarrera(codigoCarrera);

        Context context = new Context();
        context.setVariable("proyectos", proyectos);
        context.setVariable("carrera", nombreCarrera);

        String htmlContent = templateEngine.process("proyectos/reporte_proyectos_by_carrera", context);

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

        List<Proyecto> proyectos = proyectoRepository.findByIdEmpresa(idEmpresa);

        Context context = new Context();
        context.setVariable("proyectos", proyectos);
        context.setVariable("empresa", nombreEmpresa);

        String htmlContent = templateEngine.process("proyectos/reporte_proyectos_by_empresa", context);

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
        List<Proyecto> proyectos = proyectoRepository
                .findByIdDeptoCarreraAndCodigoCarrera(idDeptoCarrera, codigoCarrera);

        Context context = new Context();
        context.setVariable("proyectos", proyectos);
        context.setVariable("departamentoCarrera", nombreDeptoCarrera);
        context.setVariable("carrera", nombreCarrera);

        String htmlContent = templateEngine.process("proyectos/reporte_proyectos_by_depto_carrera",
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
