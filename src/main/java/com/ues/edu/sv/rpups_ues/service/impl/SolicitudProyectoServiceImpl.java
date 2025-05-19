package com.ues.edu.sv.rpups_ues.service.impl;

import com.ues.edu.sv.rpups_ues.model.entity.SolicitudProyecto;
import com.ues.edu.sv.rpups_ues.model.repository.SolicitudProyectoRepository;
import com.ues.edu.sv.rpups_ues.service.SolicitudProyectoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Service
public class SolicitudProyectoServiceImpl implements SolicitudProyectoService {

    private final SolicitudProyectoRepository solicitudProyectoRepository;

    public SolicitudProyectoServiceImpl(SolicitudProyectoRepository solicitudProyectoRepository) {
        this.solicitudProyectoRepository = solicitudProyectoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SolicitudProyecto> findAll() {
        return solicitudProyectoRepository.findAll();
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
        return solicitudProyectoRepository.findByAdministradorIdUsuario(idUsuario);
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
    @Transactional
    public SolicitudProyecto save(SolicitudProyecto solicitudProyecto) {
        return solicitudProyectoRepository.save(solicitudProyecto);
    }

    @Override
    @Transactional
    public void deleteById(Long idSolicitud) {
        solicitudProyectoRepository.deleteById(idSolicitud);
    }
}