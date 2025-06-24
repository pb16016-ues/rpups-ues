package com.ues.edu.sv.rpups_ues.service;

import com.ues.edu.sv.rpups_ues.model.entity.DepartamentoCarrera;

import java.util.List;
import java.util.Optional;

public interface DepartamentoCarreraService {

    List<DepartamentoCarrera> findAll();

    Optional<DepartamentoCarrera> findById(Long idDepartamentoCarrera);

    Optional<DepartamentoCarrera> findByNombre(String nombre);

    DepartamentoCarrera save(DepartamentoCarrera carrera);

    void deleteById(Long idDepartamentoCarrera);

    boolean existsByNombre(String nombre);
}