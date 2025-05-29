package com.ues.edu.sv.rpups_ues.model.repository;

import com.ues.edu.sv.rpups_ues.model.entity.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {

        List<Proyecto> findByTituloContainingIgnoreCase(String titulo);

        List<Proyecto> findByEstadoCodigoEstado(String codigoEstado);

        Page<Proyecto> findByEmpresaIdEmpresa(Long idEmpresa, Pageable pageable);

        List<Proyecto> findByEmpresaIdEmpresa(Long idEmpresa);

        List<Proyecto> findByCarreraCodigo(String codigoCarrera);

        List<Proyecto> findByModalidadCodigoModalidad(String codigoModalidad);

        List<Proyecto> findByAdministradorIdUsuario(Long idUsuario);

        List<Proyecto> findByEmpresaIdEmpresaAndEstadoCodigoEstado(Long idEmpresa, String codigoEstado);

        List<Proyecto> findByCarreraCodigoAndEstadoCodigoEstado(String codigoCarrera, String codigoEstado);

        List<Proyecto> findByModalidadCodigoModalidadAndEstadoCodigoEstado(String codigoModalidad, String codigoEstado);

        @Query("SELECT p FROM Proyecto p " +
                        "WHERE (:filter IS NULL " +
                        "OR LOWER(p.titulo) LIKE LOWER(CONCAT('%', :filter, '%')) " +
                        "OR LOWER(p.carrera.nombre) LIKE LOWER(CONCAT('%', :filter, '%')) " +
                        "OR LOWER(p.modalidad.nombre) LIKE LOWER(CONCAT('%', :filter, '%')) " +
                        "OR LOWER(p.estado.nombre) LIKE LOWER(CONCAT('%', :filter, '%')))")
        Page<Proyecto> searchByAnyField(
                        @Param("filter") String filter,
                        Pageable pageable);

        @Query("SELECT p FROM Proyecto p " +
                        "WHERE (p.estado.codigoEstado = 'DIS') " +
                        "AND (" +
                        ":filter IS NULL " +
                        "OR LOWER(p.titulo) LIKE LOWER(CONCAT('%', :filter, '%')) " +
                        "OR LOWER(p.carrera.nombre) LIKE LOWER(CONCAT('%', :filter, '%')) " +
                        "OR LOWER(p.modalidad.nombre) LIKE LOWER(CONCAT('%', :filter, '%')) " +
                        ")")
        Page<Proyecto> searchByAnyFieldDisponible(
                        @Param("filter") String filter,
                        Pageable pageable);
}