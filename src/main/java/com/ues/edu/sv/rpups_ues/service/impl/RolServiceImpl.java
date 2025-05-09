package com.ues.edu.sv.rpups_ues.service.impl;

import com.ues.edu.sv.rpups_ues.model.entity.Rol;
import com.ues.edu.sv.rpups_ues.model.repository.RolRepository;
import com.ues.edu.sv.rpups_ues.service.RolService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RolServiceImpl implements RolService {

    private final RolRepository rolRepository;

    public RolServiceImpl(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Rol> findAll() {
        return rolRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Rol> findByCodigo(String codigo) {
        return rolRepository.findById(codigo);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Rol> findByNombre(String nombre) {
        return Optional.ofNullable(rolRepository.findByNombre(nombre));
    }

    @Override
    @Transactional
    public Rol save(Rol rol) {
        return rolRepository.save(rol);
    }

    @Override
    @Transactional
    public void deleteByCodigo(String codigo) {
        rolRepository.deleteById(codigo);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNombre(String nombre) {
        return rolRepository.existsByNombre(nombre);
    }
}