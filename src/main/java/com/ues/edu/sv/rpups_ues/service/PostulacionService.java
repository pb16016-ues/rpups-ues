package com.ues.edu.sv.rpups_ues.service;

import com.ues.edu.sv.rpups_ues.model.entity.Postulacion;

import java.util.List;
import java.util.Optional;

public interface PostulacionService {

    List<Postulacion> findAll();

    Optional<Postulacion> findById(Long idPostulacion);

    Optional<Postulacion> findByEstudianteAndProyecto(Long idEstudiante, Long idProyecto);

    List<Postulacion> findByProyecto(Long idProyecto);
    
    List<Postulacion> findByProyectoAndEstado(Long idProyecto, String codigoEstado);

    List<Postulacion> findByEstado(String codigoEstado);
    
    Postulacion save(Postulacion postulacion);
    
    void deleteById(Long idPostulacion);
    
    List<Postulacion> findByEstudiante(Long idEstudiante);

    Postulacion cambiarEstado(Long idPostulacion, String nuevoEstado, Long idAdmin, String observaciones);

    Long countAceptadosByProyecto(Long idProyecto);

    Long countByProyectoAndEstado(Long idProyecto, String codigoEstado);

    boolean hayCuposDisponibles(Long idProyecto);

    int getCuposDisponibles(Long idProyecto);
}