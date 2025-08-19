package com.ues.edu.sv.rpups_ues.model.repository;

import com.ues.edu.sv.rpups_ues.model.entity.SolicitudProyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface SolicitudProyectoRepository extends JpaRepository<SolicitudProyecto, Long> {

        List<SolicitudProyecto> findByTituloContainingIgnoreCase(String titulo);

        List<SolicitudProyecto> findByCodigoEstado(String codigoEstado);

        Page<SolicitudProyecto> findByIdEmpresa(Long idEmpresa, Pageable pageable);

        List<SolicitudProyecto> findByIdEmpresa(Long idEmpresa);

        List<SolicitudProyecto> findByCodigoCarrera(String codigoCarrera);

        List<SolicitudProyecto> findByCodigoModalidad(String codigoModalidad);

        List<SolicitudProyecto> findByIdAdminRevisorAndCodigoEstado(Long idAdminRevisor, String codigoEstado);

        List<SolicitudProyecto> findByIdUserCreador(Long idUserCreador);

        List<SolicitudProyecto> findByIdEmpresaAndCodigoEstado(Long idEmpresa, String codigoEstado);

        List<SolicitudProyecto> findByCodigoCarreraAndCodigoEstado(String codigoCarrera, String codigoEstado);

        List<SolicitudProyecto> findByCodigoModalidadAndCodigoEstado(String codigoModalidad,
                        String codigoEstado);

        @Query("SELECT s FROM SolicitudProyecto s " +
                        "WHERE (:filter IS NULL " +
                        "OR LOWER(s.titulo) LIKE LOWER(CONCAT('%', :filter, '%')) " +
                        "OR LOWER(s.carrera.nombre) LIKE LOWER(CONCAT('%', :filter, '%')) " +
                        "OR LOWER(s.modalidad.nombre) LIKE LOWER(CONCAT('%', :filter, '%')) " +
                        "OR LOWER(s.estado.nombre) LIKE LOWER(CONCAT('%', :filter, '%'))) " +
                        "AND (:idDeptoCarrera IS NULL OR s.carrera.departamentoCarrera.idDepartamentoCarrera = :idDeptoCarrera)")
        Page<SolicitudProyecto> searchByAnyField(
                        @Param("filter") String filter,
                        @Param("idDeptoCarrera") Long idDeptoCarrera,
                        Pageable pageable);

        @Query("SELECT s FROM SolicitudProyecto s " +
                        "WHERE (:filter IS NULL " +
                        "OR LOWER(s.titulo) LIKE LOWER(CONCAT('%', :filter, '%')) " +
                        "OR LOWER(s.carrera.nombre) LIKE LOWER(CONCAT('%', :filter, '%')) " +
                        "OR LOWER(s.modalidad.nombre) LIKE LOWER(CONCAT('%', :filter, '%')) " +
                        "OR LOWER(s.estado.nombre) LIKE LOWER(CONCAT('%', :filter, '%'))) " +
                        "AND (:idUserCreador IS NULL OR s.userCreador.idUsuario = :idUserCreador)" +
                        "AND (:idDeptoCarrera IS NULL OR s.carrera.departamentoCarrera.idDepartamentoCarrera = :idDeptoCarrera)")
        Page<SolicitudProyecto> searchByAnyFieldAndUser(
                        @Param("filter") String filter,
                        @Param("idUserCreador") Long idUserCreador,
                        @Param("idDeptoCarrera") Long idDeptoCarrera,
                        Pageable pageable);

        @Query("SELECT s FROM SolicitudProyecto s " +
                        "WHERE ((:idDeptoCarrera IS NULL OR s.carrera.departamentoCarrera.idDepartamentoCarrera = :idDeptoCarrera) "
                        +
                        "AND (:codigoCarrera IS NULL OR s.codigoCarrera = :codigoCarrera) " +
                        "AND (:codigoEstado IS NULL OR s.codigoEstado = :codigoEstado) " +
                        "AND (:idEmpresa IS NULL OR s.idEmpresa = :idEmpresa))")
        List<SolicitudProyecto> findByAnyField(
                        @Param("idDeptoCarrera") Long idDeptoCarrera,
                        @Param("codigoCarrera") String codigoCarrera,
                        @Param("codigoEstado") String codigoEstado,
                        @Param("idEmpresa") Long idEmpresa);

        @Query("SELECT s FROM SolicitudProyecto s " +
                        "WHERE s.carrera.departamentoCarrera.idDepartamentoCarrera = :idDeptoCarrera " +
                        "AND (:codigoCarrera IS NULL OR s.carrera.codigo = :codigoCarrera)")
        List<SolicitudProyecto> findByIdDeptoCarreraAndCodigoCarrera(
                        @Param("idDeptoCarrera") Long idDeptoCarrera,
                        @Param("codigoCarrera") String codigoCarrera);

        Page<SolicitudProyecto> findByCodigoEstado(String codigoEstado, Pageable pageable);
}