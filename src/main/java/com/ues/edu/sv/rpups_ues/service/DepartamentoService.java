package com.ues.edu.sv.rpups_ues.service;

import com.ues.edu.sv.rpups_ues.model.entity.Departamento;
import java.util.List;
import java.util.Optional;

public interface DepartamentoService {

    List<Departamento> findAll();

    Optional<Departamento> findByCodigo(String codigo);

    Optional<Departamento> findByNombre(String nombre);

    Departamento save(Departamento departamento);

    void deleteByCodigo(String codigo);

    boolean existsByNombre(String nombre);
}