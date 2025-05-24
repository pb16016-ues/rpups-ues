package com.ues.edu.sv.rpups_ues.service.impl;

import com.ues.edu.sv.rpups_ues.model.entity.Proyecto;
import com.ues.edu.sv.rpups_ues.model.repository.ProyectoRepository;
import com.ues.edu.sv.rpups_ues.service.ProyectoService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProyectoServiceImpl implements ProyectoService {

    private final ProyectoRepository proyectoRepository;

    public ProyectoServiceImpl(ProyectoRepository proyectoRepository) {
        this.proyectoRepository = proyectoRepository;
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

}
