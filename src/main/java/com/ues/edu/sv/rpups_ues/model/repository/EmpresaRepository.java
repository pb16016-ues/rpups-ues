package com.ues.edu.sv.rpups_ues.model.repository;

import com.ues.edu.sv.rpups_ues.model.entity.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    List<Empresa> findByNombreComercialContainingIgnoreCase(String nombreComercial);

    List<Empresa> findByNombreLegalContainingIgnoreCase(String nombreLegal);

    List<Empresa> findByRubroIdRubro(Long idRubro);

    List<Empresa> findByUserCreadorIdUsuario(Long idUsuario);
}
