package com.ues.edu.sv.rpups_ues.model.repository;

import com.ues.edu.sv.rpups_ues.model.entity.Postulacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface PostulacionRepository extends JpaRepository<Postulacion, Long> {

    Optional<Postulacion> findByIdEstudianteAndIdProyecto(Long idEstudiante, Long idProyecto);

    List<Postulacion> findByIdEstudiante(Long idEstudiante);

    List<Postulacion> findByIdProyecto(Long idProyecto);

    List<Postulacion> findByIdProyectoAndCodigoEstado(Long idProyecto, String codigoEstado);

    List<Postulacion> findByCodigoEstado(String codigoEstado);

    @Query("SELECT COUNT(p) FROM Postulacion p WHERE p.idProyecto = :idProyecto AND p.codigoEstado = :codigoEstado")
    Long countByProyectoAndEstado(@Param("idProyecto") Long idProyecto, @Param("codigoEstado") String codigoEstado);

    @Query("SELECT COUNT(p) FROM Postulacion p WHERE p.idProyecto = :idProyecto AND p.codigoEstado = 'APRO'")
    Long countAceptadosByProyecto(@Param("idProyecto") Long idProyecto);

    /**
     * Cuenta postulaciones por código de estado.
     */
    long countByCodigoEstado(String codigoEstado);
}