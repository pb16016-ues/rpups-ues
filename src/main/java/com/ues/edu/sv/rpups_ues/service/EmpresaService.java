package com.ues.edu.sv.rpups_ues.service;

import com.ues.edu.sv.rpups_ues.model.entity.Empresa;

import java.util.List;
import java.util.Optional;

public interface EmpresaService {

    List<Empresa> findAll();

    Optional<Empresa> findById(Long idEmpresa);

    List<Empresa> findByNombreComercial(String nombreComercial);

    List<Empresa> findByNombreLegal(String nombreLegal);

    List<Empresa> findByRubroIdRubro(Long idRubro);

    Empresa save(Empresa empresa);

    void deleteById(Long idEmpresa);
}