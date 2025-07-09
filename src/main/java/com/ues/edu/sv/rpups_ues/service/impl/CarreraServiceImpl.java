package com.ues.edu.sv.rpups_ues.service.impl;

import com.ues.edu.sv.rpups_ues.model.entity.Carrera;
import com.ues.edu.sv.rpups_ues.model.repository.CarreraRepository;
import com.ues.edu.sv.rpups_ues.service.CarreraService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarreraServiceImpl implements CarreraService {

    private final CarreraRepository carreraRepository;

    public CarreraServiceImpl(CarreraRepository carreraRepository) {
        this.carreraRepository = carreraRepository;
    }

    @Override
    public List<Carrera> findAll() {
        return carreraRepository.findAll();
    }

    @Override
    public Optional<Carrera> findByCodigo(String codigo) {
        return carreraRepository.findById(codigo);
    }

    @Override
    public Optional<Carrera> findByNombre(String nombre) {
        return carreraRepository.findByNombre(nombre);
    }

    @Override
    public Carrera save(Carrera carrera) {
        return carreraRepository.save(carrera);
    }

    @Override
    public void deleteByCodigo(String codigo) {
        carreraRepository.deleteById(codigo);
    }

    @Override
    public boolean existsByNombre(String nombre) {
        return carreraRepository.existsByNombre(nombre);
    }

    @Override
    public List<Carrera> findAllByDepartamento(Long idDepartamentoCarrera) {
        return carreraRepository.findByIdDepartamentoCarrera(idDepartamentoCarrera);
    }
}