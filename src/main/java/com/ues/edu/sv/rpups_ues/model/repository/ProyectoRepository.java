package com.ues.edu.sv.rpups_ues.model.repository;

import com.ues.edu.sv.rpups_ues.model.entity.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {

    List<Proyecto> findByTituloContainingIgnoreCase(String titulo);

    List<Proyecto> findByEstadoCodigoEstado(String codigoEstado);

    List<Proyecto> findByEmpresaIdEmpresa(Long idEmpresa);

    List<Proyecto> findByCarreraCodigo(String codigoCarrera);

    List<Proyecto> findByModalidadCodigoModalidad(String codigoModalidad);

    List<Proyecto> findByAdministradorIdUsuario(Long idUsuario);

    List<Proyecto> findByEmpresaIdEmpresaAndEstadoCodigoEstado(Long idEmpresa, String codigoEstado);

    List<Proyecto> findByCarreraCodigoAndEstadoCodigoEstado(String codigoCarrera, String codigoEstado);

    List<Proyecto> findByModalidadCodigoModalidadAndEstadoCodigoEstado(String codigoModalidad, String codigoEstado);
}