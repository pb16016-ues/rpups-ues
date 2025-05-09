package com.ues.edu.sv.rpups_ues.service;

import com.ues.edu.sv.rpups_ues.model.entity.Carrera;

import java.util.List;
import java.util.Optional;

public interface CarreraService {

    List<Carrera> findAll();

    Optional<Carrera> findByCodigo(String codigo);

    Optional<Carrera> findByNombre(String nombre);

    Carrera save(Carrera carrera);

    void deleteByCodigo(String codigo);

    boolean existsByNombre(String nombre);
}