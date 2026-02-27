package com.ues.edu.sv.rpups_ues.model.repository;

import com.ues.edu.sv.rpups_ues.model.entity.Proyecto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {

        List<Proyecto> findByTituloContainingIgnoreCase(String titulo);

        Page<Proyecto> findByTituloContainingIgnoreCase(String titulo, Pageable pageable);

        List<Proyecto> findByCodigoEstado(String codigoEstado);

        Page<Proyecto> findByCodigoEstado(String codigoEstado, Pageable pageable);

        Page<Proyecto> findByIdEmpresa(Long idEmpresa, Pageable pageable);

        List<Proyecto> findByIdEmpresa(Long idEmpresa);

        List<Proyecto> findByCodigoCarrera(String codigoCarrera);

        Page<Proyecto> findByCodigoCarrera(String codigoCarrera, Pageable pageable);

        List<Proyecto> findByCodigoModalidad(String codigoModalidad);

        Page<Proyecto> findByCodigoModalidad(String codigoModalidad, Pageable pageable);

        List<Proyecto> findByIdAdministrador(Long idUsuario);

        Page<Proyecto> findByIdAdministrador(Long idUsuario, Pageable pageable);

        List<Proyecto> findByIdEmpresaAndCodigoEstado(Long idEmpresa, String codigoEstado);

        List<Proyecto> findByCodigoCarreraAndCodigoEstado(String codigoCarrera, String codigoEstado);

        List<Proyecto> findByCodigoModalidadAndCodigoEstado(String codigoModalidad, String codigoEstado);

        @Query("SELECT p FROM Proyecto p " +
                        "WHERE (:filter IS NULL " +
                        "OR LOWER(p.titulo) LIKE LOWER(CONCAT('%', :filter, '%')) " +
                        "OR LOWER(p.carrera.nombre) LIKE LOWER(CONCAT('%', :filter, '%')) " +
                        "OR LOWER(p.modalidad.nombre) LIKE LOWER(CONCAT('%', :filter, '%')) " +
                        "OR LOWER(p.estado.nombre) LIKE LOWER(CONCAT('%', :filter, '%'))) " +
                        "AND (:idDeptoCarrera IS NULL OR p.carrera.departamentoCarrera.idDepartamentoCarrera = :idDeptoCarrera)")
        Page<Proyecto> searchByAnyField(
                        @Param("filter") String filter,
                        @Param("idDeptoCarrera") Long idDeptoCarrera,
                        Pageable pageable);

        @Query("SELECT p FROM Proyecto p " +
                        "WHERE (p.estado.codigoEstado = 'DIS') " +
                        "AND (" +
                        ":filter IS NULL " +
                        "OR LOWER(p.titulo) LIKE LOWER(CONCAT('%', :filter, '%')) " +
                        "OR LOWER(p.carrera.nombre) LIKE LOWER(CONCAT('%', :filter, '%')) " +
                        "OR LOWER(p.modalidad.nombre) LIKE LOWER(CONCAT('%', :filter, '%'))) " +
                        "AND (:idDeptoCarrera IS NULL OR p.carrera.departamentoCarrera.idDepartamentoCarrera = :idDeptoCarrera)")
        Page<Proyecto> searchByAnyFieldDisponible(
                        @Param("filter") String filter,
                        @Param("idDeptoCarrera") Long idDeptoCarrera,
                        Pageable pageable);

        @Query("SELECT p FROM Proyecto p " +
                        "WHERE ((:idDeptoCarrera IS NULL OR p.carrera.departamentoCarrera.idDepartamentoCarrera = :idDeptoCarrera) "
                        +
                        "AND (:codigoCarrera IS NULL OR p.codigoCarrera = :codigoCarrera) " +
                        "AND (:codigoEstado IS NULL OR p.codigoEstado = :codigoEstado) " +
                        "AND (:idEmpresa IS NULL OR p.idEmpresa = :idEmpresa))")
        List<Proyecto> findByAnyField(
                        @Param("idDeptoCarrera") Long idDeptoCarrera,
                        @Param("codigoCarrera") String codigoCarrera,
                        @Param("codigoEstado") String codigoEstado,
                        @Param("idEmpresa") Long idEmpresa);

        @Query("SELECT p FROM Proyecto p " +
                        "WHERE p.carrera.departamentoCarrera.idDepartamentoCarrera = :idDeptoCarrera " +
                        "AND (:codigoCarrera IS NULL OR p.carrera.codigo = :codigoCarrera)")
        List<Proyecto> findByIdDeptoCarreraAndCodigoCarrera(
                        @Param("idDeptoCarrera") Long idDeptoCarrera,
                        @Param("codigoCarrera") String codigoCarrera);

        @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Proyecto p WHERE LOWER(p.titulo) = LOWER(:titulo)")
        boolean existsByTituloIgnoreCase(@Param("titulo") String titulo);

        /**
         * Verifica si existe un proyecto creado a partir de una solicitud específica.
         */
        boolean existsByIdSolicitudOrigen(Long idSolicitudOrigen);

        /**
         * Busca un proyecto por su solicitud de origen.
         */
        Proyecto findByIdSolicitudOrigen(Long idSolicitudOrigen);

        /**
         * Cuenta proyectos por código de estado.
         */
        long countByCodigoEstado(String codigoEstado);

        /**
         * Busca proyectos creados por un tutor/administrador en un rango de fechas.
         * Usado para reportes de actividad por tutor.
         * 
         * @param idAdministrador ID del tutor/administrador
         * @param fechaInicio Fecha inicio del rango (opcional)
         * @param fechaFin Fecha fin del rango (opcional)
         * @return Lista de proyectos del tutor en el rango de fechas
         */
        @Query("SELECT p FROM Proyecto p " +
                        "WHERE p.idAdministrador = :idAdministrador " +
                        "AND (:fechaInicio IS NULL OR p.fechaCreacion >= :fechaInicio) " +
                        "AND (:fechaFin IS NULL OR p.fechaCreacion <= :fechaFin) " +
                        "ORDER BY p.fechaCreacion DESC")
        List<Proyecto> findProyectosPorTutorYFechas(
                        @Param("idAdministrador") Long idAdministrador,
                        @Param("fechaInicio") java.time.LocalDateTime fechaInicio,
                        @Param("fechaFin") java.time.LocalDateTime fechaFin);

        /**
         * Busca proyectos disponibles (estado DIS) con filtros opcionales.
         * Este método es usado para el reporte público de proyectos disponibles.
         */
        @Query("SELECT p FROM Proyecto p " +
                        "WHERE p.estado.codigoEstado = 'DIS' " +
                        "AND (:codigoCarrera IS NULL OR p.carrera.codigo = :codigoCarrera) " +
                        "AND (:codigoModalidad IS NULL OR p.modalidad.codigoModalidad = :codigoModalidad) " +
                        "AND (:busqueda IS NULL OR :busqueda = '' " +
                        "     OR LOWER(p.titulo) LIKE LOWER(CONCAT('%', :busqueda, '%')) " +
                        "     OR LOWER(p.empresa.nombreComercial) LIKE LOWER(CONCAT('%', :busqueda, '%'))) " +
                        "ORDER BY p.fechaInicio DESC")
        List<Proyecto> findProyectosDisponiblesConFiltros(
                        @Param("codigoCarrera") String codigoCarrera,
                        @Param("codigoModalidad") String codigoModalidad,
                        @Param("busqueda") String busqueda);

        /**
         * Busca proyectos disponibles (estado DIS) para el banco público con paginación.
         * Usado por el endpoint /public/search
         */
        @Query("SELECT p FROM Proyecto p " +
                        "WHERE p.estado.codigoEstado = 'DIS' " +
                        "AND (:codigoCarrera IS NULL OR :codigoCarrera = '' OR p.carrera.codigo = :codigoCarrera) " +
                        "AND (:codigoModalidad IS NULL OR :codigoModalidad = '' OR p.modalidad.codigoModalidad = :codigoModalidad) " +
                        "AND (:filter IS NULL OR :filter = '' " +
                        "     OR LOWER(p.titulo) LIKE LOWER(CONCAT('%', :filter, '%')) " +
                        "     OR LOWER(p.empresa.nombreComercial) LIKE LOWER(CONCAT('%', :filter, '%'))) " +
                        "ORDER BY p.fechaInicio DESC")
        Page<Proyecto> searchProyectosDisponiblesPublicos(
                        @Param("filter") String filter,
                        @Param("codigoCarrera") String codigoCarrera,
                        @Param("codigoModalidad") String codigoModalidad,
                        Pageable pageable);
}