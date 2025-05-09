package com.ues.edu.sv.rpups_ues.model.repository;

import com.ues.edu.sv.rpups_ues.model.entity.Rubro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RubroRepository extends JpaRepository<Rubro, Long> {

    Optional<Rubro> findByNombre(String nombre);

    boolean existsByNombre(String nombre);
}