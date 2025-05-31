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
        return proyectoRepository.findByEstadoCodigoEstado(codigoEstado);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Proyecto> findByEmpresa(Long idEmpresa, Pageable pageable) {
        return proyectoRepository.findByEmpresaIdEmpresa(idEmpresa, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proyecto> findByCarrera(String codigoCarrera) {
        return proyectoRepository.findByCarreraCodigo(codigoCarrera);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proyecto> findByModalidad(String codigoModalidad) {
        return proyectoRepository.findByModalidadCodigoModalidad(codigoModalidad);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proyecto> findByAdministradorAprobador(Long idUsuario) {
        return proyectoRepository.findByAdministradorIdUsuario(idUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proyecto> findByEmpresaIdEmpresaAndEstadoCodigoEstado(Long idEmpresa, String codigoEstado) {
        return proyectoRepository.findByEmpresaIdEmpresaAndEstadoCodigoEstado(idEmpresa, codigoEstado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proyecto> findByCarreraCodigoAndEstadoCodigoEstado(String codigoCarrera, String codigoEstado) {
        return proyectoRepository.findByCarreraCodigoAndEstadoCodigoEstado(codigoCarrera, codigoEstado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proyecto> findByModalidadCodigoModalidadAndEstadoCodigoEstado(String codigoModalidad,
            String codigoEstado) {
        return proyectoRepository.findByModalidadCodigoModalidadAndEstadoCodigoEstado(codigoModalidad, codigoEstado);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Proyecto> findProyectoByFiltros(String filter, Pageable pageable) {
        return proyectoRepository.searchByAnyField(filter, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Proyecto> findProyectoByFiltrosWithEstadoDisponible(String filter, Pageable pageable) {
        return proyectoRepository.searchByAnyFieldDisponible(filter, pageable);
    }

    @Override
    @Transactional
    public Proyecto save(Proyecto proyecto) {
        return proyectoRepository.save(proyecto);
    }

    @Override
    @Transactional
    public void deleteById(Long idProyecto) {
        proyectoRepository.deleteById(idProyecto);
    }

    @Override
    public byte[] generarReportePorEstado(String codigoEstado, String nombreEstado) {

        List<Proyecto> proyectos = proyectoRepository.findByEstadoCodigoEstado(codigoEstado);

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

        List<Proyecto> proyectos = proyectoRepository.findByCarreraCodigo(codigoCarrera);

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

        List<Proyecto> proyectos = proyectoRepository.findByEmpresaIdEmpresa(idEmpresa);

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
}
