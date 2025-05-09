package com.ues.edu.sv.rpups_ues.service;

import com.ues.edu.sv.rpups_ues.model.entity.Rubro;

import java.util.List;
import java.util.Optional;

public interface RubroService {

    List<Rubro> findAll();

    Optional<Rubro> findById(Long idRubro);

    Optional<Rubro> findByNombre(String nombre);

    Rubro save(Rubro rubro);

    void deleteById(Long idRubro);

    boolean existsByNombre(String nombre);
}