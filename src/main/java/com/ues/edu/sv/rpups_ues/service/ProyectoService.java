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

    Proyecto save(Proyecto proyecto);

    void deleteById(Long idProyecto);

    byte[] generarReportePorEstado(String codigoEstado, String nombreEstado);

    byte[] generarReportePorCarrera(String codigoCarrera, String nombreCarrera);

    byte[] generarReportePorEmpresa(Long idEmpresa, String nombreEmpresa);

    byte[] generarReportePorDeptoCarreraYCarrera(Long idDeptoCarrera, String nombreDeptoCarrera,
            String codigoCarrera, String nombreCarrera);

    boolean existsByTituloIgnoreCase(String titulo);
}