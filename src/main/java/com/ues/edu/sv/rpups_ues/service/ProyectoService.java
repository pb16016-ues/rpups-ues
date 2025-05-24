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

    List<Proyecto> findByEstado(String codigoEstado);

    Page<Proyecto> findByEmpresa(Long idEmpresa, Pageable pageable);

    List<Proyecto> findByCarrera(String codigoCarrera);

    List<Proyecto> findByModalidad(String codigoModalidad);

    List<Proyecto> findByAdministradorAprobador(Long idUsuario);

    List<Proyecto> findByEmpresaIdEmpresaAndEstadoCodigoEstado(Long idEmpresa, String codigoEstado);

    List<Proyecto> findByCarreraCodigoAndEstadoCodigoEstado(String codigoCarrera, String codigoEstado);

    List<Proyecto> findByModalidadCodigoModalidadAndEstadoCodigoEstado(String codigoModalidad, String codigoEstado);

    Page<Proyecto> findProyectoByFiltros(String filter, Pageable pageable);

    Page<Proyecto> findProyectoByFiltrosWithEstadoDisponible(String filter, Pageable pageable);

    Proyecto save(Proyecto proyecto);

    void deleteById(Long idProyecto);
}