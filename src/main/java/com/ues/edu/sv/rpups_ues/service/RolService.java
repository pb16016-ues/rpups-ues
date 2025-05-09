package com.ues.edu.sv.rpups_ues.service;

import com.ues.edu.sv.rpups_ues.model.entity.Rol;

import java.util.List;
import java.util.Optional;

public interface RolService {

    List<Rol> findAll();

    Optional<Rol> findByCodigo(String codigo);

    Optional<Rol> findByNombre(String nombre);

    Rol save(Rol rol);

    void deleteByCodigo(String codigo);

    boolean existsByNombre(String nombre);
}
