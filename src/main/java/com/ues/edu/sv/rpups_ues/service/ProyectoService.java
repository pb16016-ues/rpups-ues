package com.ues.edu.sv.rpups_ues.service;

import com.ues.edu.sv.rpups_ues.model.entity.Proyecto;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProyectoService {

    Page<Proyecto> findAll(Pageable pageable);

    Optional<Proyecto> findById(Long idProyecto);

    List<Proyecto> findByTitulo(String titulo);

    Page<Proyecto> findByTitulo(String titulo, Pageable pageable);

    List<Proyecto> findByEstado(String codigoEstado);

    Page<Proyecto> findByEstado(String codigoEstado, Pageable pageable);

    Page<Proyecto> findByEmpresa(Long idEmpresa, Pageable pageable);

    List<Proyecto> findByCarrera(String codigoCarrera);

    Page<Proyecto> findByCarrera(String codigoCarrera, Pageable pageable);

    List<Proyecto> findByModalidad(String codigoModalidad);

    Page<Proyecto> findByModalidad(String codigoModalidad, Pageable pageable);

    List<Proyecto> findByAdministradorAprobador(Long idUsuario);

    Page<Proyecto> findByAdministradorAprobador(Long idUsuario, Pageable pageable);

    List<Proyecto> findByEmpresaIdEmpresaAndEstadoCodigoEstado(Long idEmpresa, String codigoEstado);

    List<Proyecto> findByCarreraCodigoAndEstadoCodigoEstado(String codigoCarrera, String codigoEstado);

    List<Proyecto> findByModalidadCodigoModalidadAndEstadoCodigoEstado(String codigoModalidad, String codigoEstado);

    Page<Proyecto> findProyectoByFiltros(String filter, Long idDeptoCarrera, Pageable pageable);

    Page<Proyecto> findProyectoByFiltrosWithEstadoDisponible(String filter, Long idDeptoCarrera, Pageable pageable);

    /**
     * Busca proyectos disponibles para el banco público con filtros avanzados.
     * 
     * @param filter Texto de búsqueda (título o empresa)
     * @param codigoCarrera Código de carrera (opcional)
     * @param codigoModalidad Código de modalidad (opcional)
     * @param pageable Paginación
     * @return Página de proyectos disponibles
     */
    Page<Proyecto> findProyectosDisponiblesPublicos(String filter, String codigoCarrera, 
            String codigoModalidad, Pageable pageable);

    Proyecto save(Proyecto proyecto);

    void deleteById(Long idProyecto);

    byte[] generarReportePorEstado(String codigoEstado, String nombreEstado);

    byte[] generarReportePorCarrera(String codigoCarrera, String nombreCarrera);

    byte[] generarReportePorEmpresa(Long idEmpresa, String nombreEmpresa);

    byte[] generarReportePorDeptoCarreraYCarrera(Long idDeptoCarrera, String nombreDeptoCarrera,
            String codigoCarrera, String nombreCarrera);

    /**
     * Genera un reporte PDF con los proyectos disponibles (estado DIS).
     * Este método es público y no requiere autenticación.
     * 
     * @param codigoCarrera Código de carrera para filtrar (opcional)
     * @param codigoModalidad Código de modalidad para filtrar (opcional)
     * @param busqueda Texto de búsqueda (opcional)
     * @return byte[] con el contenido del PDF
     */
    byte[] generarReporteProyectosDisponibles(String codigoCarrera, String nombreCarrera,
            String codigoModalidad, String nombreModalidad, String busqueda);

    boolean existsByTituloIgnoreCase(String titulo);

    /**
     * Clona un proyecto existente asignando nuevos estudiantes
     * @param idProyectoOriginal ID del proyecto a clonar
     * @param idsEstudiantes Lista de IDs de estudiantes a asignar al proyecto clonado
     * @param idAdministrador ID del usuario que realiza la clonación
     * @return Proyecto clonado con los estudiantes asignados
     * @throws IllegalArgumentException si el proyecto no existe o los estudiantes no son válidos
     */
    Proyecto clonarProyecto(Long idProyectoOriginal, List<Long> idsEstudiantes, Long idAdministrador);
}