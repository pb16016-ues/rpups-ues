package com.ues.edu.sv.rpups_ues.service;

import com.ues.edu.sv.rpups_ues.model.entity.Modalidad;

import java.util.List;
import java.util.Optional;

public interface ModalidadService {

    List<Modalidad> findAll();

    Optional<Modalidad> findByCodigoModalidad(String codigoModalidad);

    Optional<Modalidad> findByNombre(String nombre);

    Modalidad save(Modalidad modalidad);

    void deleteByCodigoModalidad(String codigoModalidad);

    boolean existsByNombre(String nombre);
}