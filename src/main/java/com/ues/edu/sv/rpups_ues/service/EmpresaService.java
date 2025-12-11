package com.ues.edu.sv.rpups_ues.service;

import com.ues.edu.sv.rpups_ues.model.entity.Empresa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface EmpresaService {

    List<Empresa> findAll();

    Page<Empresa> findAll(Pageable pageable);

    Page<Empresa> findByFilter(String filter, Pageable pageable);

    Page<Empresa> findByFilterAndEstadoActivo(String filter, Boolean activo, Pageable pageable);

    Optional<Empresa> findById(Long idEmpresa);

    List<Empresa> findByNombreComercial(String nombreComercial);

    List<Empresa> findByNombreLegal(String nombreLegal);

    List<Empresa> findByRubroIdRubro(Long idRubro);

    List<Empresa> findByUserCreador(Long idUsuario);

    Empresa save(Empresa empresa);

    void desactiveById(Long idEmpresa);
}