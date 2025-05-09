package com.ues.edu.sv.rpups_ues.model.repository;

import com.ues.edu.sv.rpups_ues.model.entity.Postulacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface PostulacionRepository extends JpaRepository<Postulacion, Long> {

    Optional<Postulacion> findByEstudianteIdUsuarioAndProyectoIdProyecto(Long idEstudiante, Long idProyecto);

    List<Postulacion> findByEstadoCodigoEstado(String codigoEstado);

    Optional<Postulacion> findByEstudianteIdUsuarioAndProyectoIdProyectoAndEstadoCodigoEstado(Long idEstudiante,
            Long idProyecto, String codigoEstado);
}