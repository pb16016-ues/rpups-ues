package com.ues.edu.sv.rpups_ues.service.impl;

import com.ues.edu.sv.rpups_ues.model.entity.DepartamentoCarrera;
import com.ues.edu.sv.rpups_ues.model.repository.DepartamentoCarreraRepository;
import com.ues.edu.sv.rpups_ues.service.DepartamentoCarreraService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartamentoCarreraServiceImpl implements DepartamentoCarreraService {

    private final DepartamentoCarreraRepository departamentoCarreraRepository;

    public DepartamentoCarreraServiceImpl(DepartamentoCarreraRepository departamentoCarreraRepository) {
        this.departamentoCarreraRepository = departamentoCarreraRepository;
    }

    @Override
    public List<DepartamentoCarrera> findAll() {
        return departamentoCarreraRepository.findAll();
    }

    @Override
    public Optional<DepartamentoCarrera> findById(Long idDepartamentoCarrera) {
        return departamentoCarreraRepository.findById(idDepartamentoCarrera);
    }

    @Override
    public Optional<DepartamentoCarrera> findByNombre(String nombre) {
        return departamentoCarreraRepository.findByNombre(nombre);
    }

    @Override
    public DepartamentoCarrera save(DepartamentoCarrera departamentoCarrera) {
        return departamentoCarreraRepository.save(departamentoCarrera);
    }

    @Override
    public void deleteById(Long idDepartamentoCarrera) {
        departamentoCarreraRepository.deleteById(idDepartamentoCarrera);
    }

    @Override
    public boolean existsByNombre(String nombre) {
        return departamentoCarreraRepository.existsByNombre(nombre);
    }
}