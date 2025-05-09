package com.ues.edu.sv.rpups_ues.service.impl;

import com.ues.edu.sv.rpups_ues.model.entity.Estado;
import com.ues.edu.sv.rpups_ues.model.repository.EstadoRepository;
import com.ues.edu.sv.rpups_ues.service.EstadoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EstadoServiceImpl implements EstadoService {

    private final EstadoRepository estadoRepository;

    public EstadoServiceImpl(EstadoRepository estadoRepository) {
        this.estadoRepository = estadoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Estado> findAll() {
        return estadoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Estado> findByCodigoEstado(String codigoEstado) {
        return estadoRepository.findById(codigoEstado);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Estado> findByNombre(String nombre) {
        return Optional.ofNullable(estadoRepository.findByNombre(nombre));
    }

    @Override
    @Transactional
    public Estado save(Estado estado) {
        return estadoRepository.save(estado);
    }

    @Override
    @Transactional
    public void deleteByCodigoEstado(String codigoEstado) {
        estadoRepository.deleteById(codigoEstado);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNombre(String nombre) {
        return estadoRepository.existsByNombre(nombre);
    }
}