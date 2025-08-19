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

        List<Proyecto> findByCodigoEstado(String codigoEstado);

        Page<Proyecto> findByIdEmpresa(Long idEmpresa, Pageable pageable);

        List<Proyecto> findByIdEmpresa(Long idEmpresa);

        List<Proyecto> findByCodigoCarrera(String codigoCarrera);

        List<Proyecto> findByCodigoModalidad(String codigoModalidad);

        List<Proyecto> findByIdAdministrador(Long idUsuario);

        List<Proyecto> findByIdEmpresaAndCodigoEstado(Long idEmpresa, String codigoEstado);

        List<Proyecto> findByCodigoCarreraAndCodigoEstado(String codigoCarrera, String codigoEstado);

        List<Proyecto> findByCodigoModalidadAndCodigoEstado(String codigoModalidad, String codigoEstado);

        @Query("SELECT p FROM Proyecto p " +
                        "WHERE (:filter IS NULL " +
                        "OR LOWER(p.titulo) LIKE LOWER(CONCAT('%', :filter, '%')) " +
                        "OR LOWER(p.carrera.nombre) LIKE LOWER(CONCAT('%', :filter, '%')) " +
                        "OR LOWER(p.modalidad.nombre) LIKE LOWER(CONCAT('%', :filter, '%')) " +
                        "OR LOWER(p.estado.nombre) LIKE LOWER(CONCAT('%', :filter, '%'))) " +
                        "AND (:idDeptoCarrera IS NULL OR p.carrera.departamentoCarrera.idDepartamentoCarrera = :idDeptoCarrera)")
        Page<Proyecto> searchByAnyField(
                        @Param("filter") String filter,
                        @Param("idDeptoCarrera") Long idDeptoCarrera,
                        Pageable pageable);

        @Query("SELECT p FROM Proyecto p " +
                        "WHERE (p.estado.codigoEstado = 'DIS') " +
                        "AND (" +
                        ":filter IS NULL " +
                        "OR LOWER(p.titulo) LIKE LOWER(CONCAT('%', :filter, '%')) " +
                        "OR LOWER(p.carrera.nombre) LIKE LOWER(CONCAT('%', :filter, '%')) " +
                        "OR LOWER(p.modalidad.nombre) LIKE LOWER(CONCAT('%', :filter, '%'))) " +
                        "AND (:idDeptoCarrera IS NULL OR p.carrera.departamentoCarrera.idDepartamentoCarrera = :idDeptoCarrera)")
        Page<Proyecto> searchByAnyFieldDisponible(
                        @Param("filter") String filter,
                        @Param("idDeptoCarrera") Long idDeptoCarrera,
                        Pageable pageable);

        @Query("SELECT p FROM Proyecto p " +
                        "WHERE ((:idDeptoCarrera IS NULL OR p.carrera.departamentoCarrera.idDepartamentoCarrera = :idDeptoCarrera) "
                        +
                        "AND (:codigoCarrera IS NULL OR p.codigoCarrera = :codigoCarrera) " +
                        "AND (:codigoEstado IS NULL OR p.codigoEstado = :codigoEstado) " +
                        "AND (:idEmpresa IS NULL OR p.idEmpresa = :idEmpresa))")
        List<Proyecto> findByAnyField(
                        @Param("idDeptoCarrera") Long idDeptoCarrera,
                        @Param("codigoCarrera") String codigoCarrera,
                        @Param("codigoEstado") String codigoEstado,
                        @Param("idEmpresa") Long idEmpresa);

        @Query("SELECT p FROM Proyecto p " +
                        "WHERE p.carrera.departamentoCarrera.idDepartamentoCarrera = :idDeptoCarrera " +
                        "AND (:codigoCarrera IS NULL OR p.carrera.codigo = :codigoCarrera)")
        List<Proyecto> findByIdDeptoCarreraAndCodigoCarrera(
                        @Param("idDeptoCarrera") Long idDeptoCarrera,
                        @Param("codigoCarrera") String codigoCarrera);
}