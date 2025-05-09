package com.ues.edu.sv.rpups_ues.service.impl;

import com.ues.edu.sv.rpups_ues.model.entity.Municipio;
import com.ues.edu.sv.rpups_ues.model.repository.MunicipioRepository;
import com.ues.edu.sv.rpups_ues.service.MunicipioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MunicipioServiceImpl implements MunicipioService {

    private final MunicipioRepository municipioRepository;

    public MunicipioServiceImpl(MunicipioRepository municipioRepository) {
        this.municipioRepository = municipioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Municipio> findAll() {
        return municipioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Municipio> findByCodigo(String codigo) {
        return municipioRepository.findById(codigo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Municipio> findByCodigoDepartamento(String codigoDepartamento) {
        return municipioRepository.findByCodigoDepartamento(codigoDepartamento);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Municipio> findByNombre(String nombre) {
        return municipioRepository.findAll().stream()
                .filter(municipio -> municipio.getNombre().equalsIgnoreCase(nombre))
                .findFirst();
    }

    @Override
    @Transactional
    public Municipio save(Municipio municipio) {
        return municipioRepository.save(municipio);
    }

    @Override
    @Transactional
    public void deleteByCodigo(String codigo) {
        municipioRepository.deleteById(codigo);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNombre(String nombre) {
        return municipioRepository.existsByNombre(nombre);
    }
}