package com.ues.edu.sv.rpups_ues.service.impl;

import com.ues.edu.sv.rpups_ues.model.entity.Departamento;
import com.ues.edu.sv.rpups_ues.model.repository.DepartamentoRepository;
import com.ues.edu.sv.rpups_ues.service.DepartamentoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DepartamentoServiceImpl implements DepartamentoService {

    private final DepartamentoRepository departamentoRepository;

    public DepartamentoServiceImpl(DepartamentoRepository departamentoRepository) {
        this.departamentoRepository = departamentoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Departamento> findAll() {
        return departamentoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Departamento> findByCodigo(String codigo) {
        return departamentoRepository.findById(codigo);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Departamento> findByNombre(String nombre) {
        return Optional.ofNullable(departamentoRepository.findByNombre(nombre));
    }

    @Override
    @Transactional
    public Departamento save(Departamento departamento) {
        return departamentoRepository.save(departamento);
    }

    @Override
    @Transactional
    public void deleteByCodigo(String codigo) {
        departamentoRepository.deleteById(codigo);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNombre(String nombre) {
        return departamentoRepository.existsByNombre(nombre);
    }
}