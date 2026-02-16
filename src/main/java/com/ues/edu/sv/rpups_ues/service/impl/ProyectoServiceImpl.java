package com.ues.edu.sv.rpups_ues.service.impl;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.ues.edu.sv.rpups_ues.model.entity.Proyecto;
import com.ues.edu.sv.rpups_ues.model.entity.Postulacion;
import com.ues.edu.sv.rpups_ues.model.entity.Usuario;
import com.ues.edu.sv.rpups_ues.model.repository.ProyectoRepository;
import com.ues.edu.sv.rpups_ues.model.repository.PostulacionRepository;
import com.ues.edu.sv.rpups_ues.model.repository.UsuarioRepository;
import com.ues.edu.sv.rpups_ues.service.ProyectoService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.io.ByteArrayOutputStream;

@Service
public class ProyectoServiceImpl implements ProyectoService {

    private final ProyectoRepository proyectoRepository;
    private final PostulacionRepository postulacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final SpringTemplateEngine templateEngine;

    public ProyectoServiceImpl(ProyectoRepository proyectoRepository, 
                              PostulacionRepository postulacionRepository,
                              UsuarioRepository usuarioRepository,
                              SpringTemplateEngine templateEngine) {
        this.proyectoRepository = proyectoRepository;
        this.postulacionRepository = postulacionRepository;
        this.usuarioRepository = usuarioRepository;
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
    public Page<Proyecto> findByTitulo(String titulo, Pageable pageable) {
        return proyectoRepository.findByTituloContainingIgnoreCase(titulo, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proyecto> findByEstado(String codigoEstado) {
        return proyectoRepository.findByCodigoEstado(codigoEstado);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Proyecto> findByEstado(String codigoEstado, Pageable pageable) {
        return proyectoRepository.findByCodigoEstado(codigoEstado, pageable);
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
    public Page<Proyecto> findByCarrera(String codigoCarrera, Pageable pageable) {
        return proyectoRepository.findByCodigoCarrera(codigoCarrera, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proyecto> findByModalidad(String codigoModalidad) {
        return proyectoRepository.findByCodigoModalidad(codigoModalidad);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Proyecto> findByModalidad(String codigoModalidad, Pageable pageable) {
        return proyectoRepository.findByCodigoModalidad(codigoModalidad, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proyecto> findByAdministradorAprobador(Long idUsuario) {
        return proyectoRepository.findByIdAdministrador(idUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Proyecto> findByAdministradorAprobador(Long idUsuario, Pageable pageable) {
        return proyectoRepository.findByIdAdministrador(idUsuario, pageable);
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
    @Transactional(readOnly = true)
    public Page<Proyecto> findProyectosDisponiblesPublicos(String filter, String codigoCarrera,
            String codigoModalidad, Pageable pageable) {
        return proyectoRepository.searchProyectosDisponiblesPublicos(
                filter, codigoCarrera, codigoModalidad, pageable);
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

    @Override
    public boolean existsByTituloIgnoreCase(String titulo) {
        return proyectoRepository.existsByTituloIgnoreCase(titulo);
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] generarReporteProyectosDisponibles(String codigoCarrera, String nombreCarrera,
            String codigoModalidad, String nombreModalidad, String busqueda) {
        
        // Obtener proyectos disponibles con filtros opcionales
        List<Proyecto> proyectos = proyectoRepository.findProyectosDisponiblesConFiltros(
                codigoCarrera, codigoModalidad, busqueda);

        Context context = new Context();
        context.setVariable("proyectos", proyectos);
        
        // Crear objeto de filtros para mostrar en el reporte
        java.util.Map<String, String> filtros = new java.util.HashMap<>();
        if (nombreCarrera != null && !nombreCarrera.isEmpty()) {
            filtros.put("carrera", nombreCarrera);
        }
        if (nombreModalidad != null && !nombreModalidad.isEmpty()) {
            filtros.put("modalidad", nombreModalidad);
        }
        if (busqueda != null && !busqueda.trim().isEmpty()) {
            filtros.put("busqueda", busqueda);
        }
        context.setVariable("filtros", filtros.isEmpty() ? null : filtros);

        String htmlContent = templateEngine.process("proyectos/reporte_proyectos_disponibles", context);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(htmlContent, null);
            builder.toStream(outputStream);
            builder.run();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el reporte PDF de proyectos disponibles", e);
        }
    }

    @Override
    @Transactional
    public Proyecto clonarProyecto(Long idProyectoOriginal, List<Long> idsEstudiantes, Long idAdministrador) {
        // Validar que el proyecto original existe
        Proyecto proyectoOriginal = proyectoRepository.findById(idProyectoOriginal)
            .orElseThrow(() -> new IllegalArgumentException(
                "Proyecto con ID " + idProyectoOriginal + " no encontrado"));

        // Validar número de estudiantes
        if (idsEstudiantes == null || idsEstudiantes.isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar al menos un estudiante");
        }

        if (idsEstudiantes.size() > proyectoOriginal.getMaxEstudiantes()) {
            throw new IllegalArgumentException(
                "El número de estudiantes seleccionados (" + idsEstudiantes.size() + 
                ") excede el máximo permitido (" + proyectoOriginal.getMaxEstudiantes() + ")");
        }

        // Validar que todos los estudiantes existen y pertenecen al mismo departamento
        List<Usuario> estudiantes = new ArrayList<>();
        for (Long idEstudiante : idsEstudiantes) {
            Usuario estudiante = usuarioRepository.findById(idEstudiante)
                .orElseThrow(() -> new IllegalArgumentException(
                    "Estudiante con ID " + idEstudiante + " no encontrado"));

            // Verificar que es estudiante
            if (!"ESTUD".equals(estudiante.getCodigoRol())) {
                throw new IllegalArgumentException(
                    "El usuario con ID " + idEstudiante + " no es un estudiante");
            }

            estudiantes.add(estudiante);
        }

        // Crear el proyecto clonado
        Proyecto proyectoClonado = new Proyecto();
        proyectoClonado.setTitulo(proyectoOriginal.getTitulo() + " (Copia)");
        proyectoClonado.setDescripcion(proyectoOriginal.getDescripcion());
        proyectoClonado.setRequisitos(proyectoOriginal.getRequisitos());
        proyectoClonado.setFechaInicio(proyectoOriginal.getFechaInicio());
        proyectoClonado.setFechaFin(proyectoOriginal.getFechaFin());
        proyectoClonado.setDuracion(proyectoOriginal.getDuracion());
        proyectoClonado.setMaxEstudiantes(proyectoOriginal.getMaxEstudiantes());
        proyectoClonado.setDireccionDetallada(proyectoOriginal.getDireccionDetallada());
        proyectoClonado.setFechaCreacion(LocalDateTime.now());
        
        // Copiar relaciones
        proyectoClonado.setIdEmpresa(proyectoOriginal.getIdEmpresa());
        proyectoClonado.setCodigoDepartamento(proyectoOriginal.getCodigoDepartamento());
        proyectoClonado.setCodigoMunicipio(proyectoOriginal.getCodigoMunicipio());
        proyectoClonado.setCodigoCarrera(proyectoOriginal.getCodigoCarrera());
        proyectoClonado.setCodigoModalidad(proyectoOriginal.getCodigoModalidad());
        proyectoClonado.setIdAdministrador(idAdministrador);
        
        // Establecer estado según número de estudiantes asignados
        if (idsEstudiantes.size() >= proyectoOriginal.getMaxEstudiantes()) {
            proyectoClonado.setCodigoEstado("CERR"); // Cerrado (lleno)
        } else {
            proyectoClonado.setCodigoEstado("DIS"); // Disponible (aún acepta estudiantes)
        }
        
        proyectoClonado.setIdSolicitudOrigen(proyectoOriginal.getIdSolicitudOrigen());

        // Guardar el proyecto clonado
        Proyecto proyectoGuardado = proyectoRepository.save(proyectoClonado);

        // Crear postulaciones aceptadas para cada estudiante
        for (Usuario estudiante : estudiantes) {
            Postulacion postulacion = new Postulacion();
            postulacion.setIdEstudiante(estudiante.getIdUsuario());
            postulacion.setIdProyecto(proyectoGuardado.getIdProyecto());
            postulacion.setCodigoEstado("APRO"); // Aprobado
            postulacion.setFechaPostulacion(LocalDateTime.now());
            postulacion.setFechaCambioEstado(LocalDateTime.now());
            postulacion.setIdAdminCambioEstado(idAdministrador);
            postulacion.setObservaciones("Asignado automáticamente al clonar proyecto");
            
            postulacionRepository.save(postulacion);
        }

        return proyectoGuardado;
    }
}
