package com.ues.edu.sv.rpups_ues.service;

import com.ues.edu.sv.rpups_ues.model.entity.Estado;
import java.util.List;
import java.util.Optional;

public interface EstadoService {

    List<Estado> findAll();

    Optional<Estado> findByCodigoEstado(String codigoEstado);

    Optional<Estado> findByNombre(String nombre);

    Estado save(Estado estado);

    void deleteByCodigoEstado(String codigoEstado);

    boolean existsByNombre(String nombre);
}