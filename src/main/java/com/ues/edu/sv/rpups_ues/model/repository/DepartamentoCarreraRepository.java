package com.ues.edu.sv.rpups_ues.model.repository;

import com.ues.edu.sv.rpups_ues.model.entity.DepartamentoCarrera;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartamentoCarreraRepository extends JpaRepository<DepartamentoCarrera, Long> {

    Optional<DepartamentoCarrera> findByNombre(String nombre);

    boolean existsByNombre(String nombre);
}