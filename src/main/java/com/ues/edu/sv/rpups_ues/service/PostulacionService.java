package com.ues.edu.sv.rpups_ues.service;

import com.ues.edu.sv.rpups_ues.model.entity.Postulacion;

import java.util.List;
import java.util.Optional;

public interface PostulacionService {

    List<Postulacion> findAll();

    Optional<Postulacion> findById(Long idPostulacion);

    List<Postulacion> findByEstado(String codigoEstado);

    Optional<Postulacion> findByEstudianteAndProyecto(Long idEstudiante, Long idProyecto);

    Optional<Postulacion> findByEstudianteAndProyectoAndEstado(Long idEstudiante, Long idProyecto, String codigoEstado);

    Postulacion save(Postulacion postulacion);

    void deleteById(Long idPostulacion);
}