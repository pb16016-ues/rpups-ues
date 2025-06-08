package com.ues.edu.sv.rpups_ues.service;

import com.ues.edu.sv.rpups_ues.model.entity.Postulacion;

import java.util.List;
import java.util.Optional;

public interface PostulacionService {

    List<Postulacion> findAll();

    Optional<Postulacion> findById(Long idPostulacion);

    Optional<Postulacion> findByEstudianteAndProyecto(Long idEstudiante, Long idProyecto);

    List<Postulacion> findByProyecto(Long idProyecto);

    Postulacion save(Postulacion postulacion);

    void deleteById(Long idPostulacion);
}