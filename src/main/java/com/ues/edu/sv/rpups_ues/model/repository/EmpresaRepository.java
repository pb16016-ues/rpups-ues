package com.ues.edu.sv.rpups_ues.model.repository;

import com.ues.edu.sv.rpups_ues.model.entity.Empresa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    List<Empresa> findByNombreComercialContainingIgnoreCase(String nombreComercial);

    List<Empresa> findByNombreLegalContainingIgnoreCase(String nombreLegal);

    List<Empresa> findByIdRubro(Long idRubro);

    List<Empresa> findByIdUserCreador(Long idUserCreador);

    // Paginación con filtro de búsqueda
    @Query("SELECT e FROM Empresa e WHERE " +
           "LOWER(e.nombreComercial) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
           "LOWER(e.nombreLegal) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
           "LOWER(e.contactoNombre) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
           "LOWER(e.contactoEmail) LIKE LOWER(CONCAT('%', :filter, '%'))")
    Page<Empresa> findByFilter(@Param("filter") String filter, Pageable pageable);

    // Paginación simple
    Page<Empresa> findAll(Pageable pageable);

    // Filtrar solo activas
    Page<Empresa> findByEstadoActivo(Boolean estadoActivo, Pageable pageable);

    // Filtro con estado activo
    @Query("SELECT e FROM Empresa e WHERE e.estadoActivo = :activo AND (" +
           "LOWER(e.nombreComercial) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
           "LOWER(e.nombreLegal) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
           "LOWER(e.contactoNombre) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
           "LOWER(e.contactoEmail) LIKE LOWER(CONCAT('%', :filter, '%')))")
    Page<Empresa> findByFilterAndEstadoActivo(@Param("filter") String filter, @Param("activo") Boolean activo, Pageable pageable);
}
