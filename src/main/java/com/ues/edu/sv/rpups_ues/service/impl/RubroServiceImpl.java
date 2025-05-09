package com.ues.edu.sv.rpups_ues.service.impl;

import com.ues.edu.sv.rpups_ues.model.entity.Rubro;
import com.ues.edu.sv.rpups_ues.model.repository.RubroRepository;
import com.ues.edu.sv.rpups_ues.service.RubroService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RubroServiceImpl implements RubroService {

    private final RubroRepository rubroRepository;

    public RubroServiceImpl(RubroRepository rubroRepository) {
        this.rubroRepository = rubroRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Rubro> findAll() {
        return rubroRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Rubro> findById(Long idRubro) {
        return rubroRepository.findById(idRubro);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Rubro> findByNombre(String nombre) {
        return rubroRepository.findByNombre(nombre);
    }

    @Override
    @Transactional
    public Rubro save(Rubro rubro) {
        return rubroRepository.save(rubro);
    }

    @Override
    @Transactional
    public void deleteById(Long idRubro) {
        rubroRepository.deleteById(idRubro);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNombre(String nombre) {
        return rubroRepository.existsByNombre(nombre);
    }
}