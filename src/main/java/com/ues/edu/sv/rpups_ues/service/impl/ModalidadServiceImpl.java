package com.ues.edu.sv.rpups_ues.service.impl;

import com.ues.edu.sv.rpups_ues.model.entity.Modalidad;
import com.ues.edu.sv.rpups_ues.model.repository.ModalidadRepository;
import com.ues.edu.sv.rpups_ues.service.ModalidadService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ModalidadServiceImpl implements ModalidadService {

    private final ModalidadRepository modalidadRepository;

    public ModalidadServiceImpl(ModalidadRepository modalidadRepository) {
        this.modalidadRepository = modalidadRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Modalidad> findAll() {
        return modalidadRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Modalidad> findByCodigoModalidad(String codigoModalidad) {
        return modalidadRepository.findById(codigoModalidad);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Modalidad> findByNombre(String nombre) {
        return modalidadRepository.findByNombre(nombre);
    }

    @Override
    @Transactional
    public Modalidad save(Modalidad modalidad) {
        return modalidadRepository.save(modalidad);
    }

    @Override
    @Transactional
    public void deleteByCodigoModalidad(String codigoModalidad) {
        modalidadRepository.deleteById(codigoModalidad);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNombre(String nombre) {
        return modalidadRepository.existsByNombre(nombre);
    }
}