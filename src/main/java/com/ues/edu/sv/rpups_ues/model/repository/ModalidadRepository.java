package com.ues.edu.sv.rpups_ues.model.repository;

import com.ues.edu.sv.rpups_ues.model.entity.Modalidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ModalidadRepository extends JpaRepository<Modalidad, String> {

    Optional<Modalidad> findByNombre(String nombre);

    boolean existsByNombre(String nombre);
}