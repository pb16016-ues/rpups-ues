package com.ues.edu.sv.rpups_ues.service;

import com.ues.edu.sv.rpups_ues.model.entity.Municipio;
import java.util.List;
import java.util.Optional;

public interface MunicipioService {

    List<Municipio> findAll();

    Optional<Municipio> findByCodigo(String codigo);

    List<Municipio> findByCodigoDepartamento(String codigoDepartamento);

    Optional<Municipio> findByNombre(String nombre);

    Municipio save(Municipio municipio);

    void deleteByCodigo(String codigo);

    boolean existsByNombre(String nombre);
}