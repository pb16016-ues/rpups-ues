package com.ues.edu.sv.rpups_ues.service.impl;

import com.ues.edu.sv.rpups_ues.model.entity.Postulacion;
import com.ues.edu.sv.rpups_ues.model.entity.Proyecto;
import com.ues.edu.sv.rpups_ues.model.repository.PostulacionRepository;
import com.ues.edu.sv.rpups_ues.model.repository.ProyectoRepository;
import com.ues.edu.sv.rpups_ues.service.NotificacionService;
import com.ues.edu.sv.rpups_ues.service.PostulacionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostulacionServiceImpl implements PostulacionService {

    private final PostulacionRepository postulacionRepository;
    private final ProyectoRepository proyectoRepository;
    private final NotificacionService notificacionService;

    public PostulacionServiceImpl(PostulacionRepository postulacionRepository, 
                                   ProyectoRepository proyectoRepository,
                                   NotificacionService notificacionService) {
        this.postulacionRepository = postulacionRepository;
        this.proyectoRepository = proyectoRepository;
        this.notificacionService = notificacionService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Postulacion> findAll() {
        return postulacionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Postulacion> findByProyecto(Long idProyecto) {
        return postulacionRepository.findByIdProyecto(idProyecto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Postulacion> findByProyectoAndEstado(Long idProyecto, String codigoEstado) {
        return postulacionRepository.findByIdProyectoAndCodigoEstado(idProyecto, codigoEstado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Postulacion> findByEstado(String codigoEstado) {
        return postulacionRepository.findByCodigoEstado(codigoEstado);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Postulacion> findById(Long idPostulacion) {
        return postulacionRepository.findById(idPostulacion);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Postulacion> findByEstudianteAndProyecto(Long idEstudiante, Long idProyecto) {
        return postulacionRepository.findByIdEstudianteAndIdProyecto(idEstudiante, idProyecto);
    }

    @Override
    @Transactional
    public Postulacion save(Postulacion postulacion) {
        // Verificar si ya existe una postulación para este estudiante en este proyecto
        Optional<Postulacion> existingPostulacion = postulacionRepository
                .findByIdEstudianteAndIdProyecto(
                        postulacion.getIdEstudiante(),
                        postulacion.getIdProyecto());

        if (existingPostulacion.isPresent()) {
            throw new IllegalArgumentException(
                    "Ya existe una postulación para este estudiante asociada a este proyecto.");
        }

        // Validar que el proyecto tenga cupos disponibles
        if (!hayCuposDisponibles(postulacion.getIdProyecto())) {
            throw new IllegalStateException(
                    "El proyecto no tiene cupos disponibles para más estudiantes.");
        }

        // Establecer valores por defecto
        postulacion.setFechaPostulacion(null); // Se establece por default en BD
        postulacion.setCodigoEstado("PEND");

        return postulacionRepository.save(postulacion);
    }

    @Override
    @Transactional
    public Postulacion cambiarEstado(Long idPostulacion, String nuevoEstado, Long idAdmin, String observaciones) {
        Optional<Postulacion> optPostulacion = postulacionRepository.findById(idPostulacion);
        
        if (optPostulacion.isEmpty()) {
            throw new IllegalArgumentException("No existe la postulación con ID: " + idPostulacion);
        }

        Postulacion postulacion = optPostulacion.get();
        
        // Validar estados permitidos
        if (!nuevoEstado.equals("APRO") && !nuevoEstado.equals("RECH") && !nuevoEstado.equals("PEND")) {
            throw new IllegalArgumentException("Estado no válido. Estados permitidos: APRO, RECH, PEND");
        }

        // Si se va a aprobar, validar que haya cupos disponibles
        if (nuevoEstado.equals("APRO") && !postulacion.getCodigoEstado().equals("APRO")) {
            if (!hayCuposDisponibles(postulacion.getIdProyecto())) {
                throw new IllegalStateException(
                        "No hay cupos disponibles en el proyecto para aprobar esta postulación.");
            }
        }

        postulacion.setCodigoEstado(nuevoEstado);
        postulacion.setFechaCambioEstado(LocalDateTime.now());
        postulacion.setIdAdminCambioEstado(idAdmin);
        postulacion.setObservaciones(observaciones);

        Postulacion savedPostulacion = postulacionRepository.save(postulacion);

        // Crear notificación para el estudiante
        try {
            String nombreProyecto = postulacion.getProyecto() != null 
                    ? postulacion.getProyecto().getTitulo() 
                    : "Proyecto #" + postulacion.getIdProyecto();

            if (nuevoEstado.equals("APRO")) {
                notificacionService.notificarPostulacionAceptada(
                        postulacion.getIdEstudiante(),
                        postulacion.getIdPostulacion(),
                        nombreProyecto);
            } else if (nuevoEstado.equals("RECH")) {
                notificacionService.notificarPostulacionRechazada(
                        postulacion.getIdEstudiante(),
                        postulacion.getIdPostulacion(),
                        nombreProyecto,
                        observaciones);
            }
        } catch (Exception e) {
            // No fallar si no se puede crear la notificación
            System.err.println("Error al crear notificación de postulación: " + e.getMessage());
        }

        return savedPostulacion;
    }

    @Override
    @Transactional(readOnly = true)
    public Long countAceptadosByProyecto(Long idProyecto) {
        return postulacionRepository.countAceptadosByProyecto(idProyecto);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countByProyectoAndEstado(Long idProyecto, String codigoEstado) {
        return postulacionRepository.countByProyectoAndEstado(idProyecto, codigoEstado);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hayCuposDisponibles(Long idProyecto) {
        return getCuposDisponibles(idProyecto) > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public int getCuposDisponibles(Long idProyecto) {
        Optional<Proyecto> optProyecto = proyectoRepository.findById(idProyecto);
        if (optProyecto.isEmpty()) {
            throw new IllegalArgumentException("No existe el proyecto con ID: " + idProyecto);
        }
        
        Proyecto proyecto = optProyecto.get();
        Long aceptados = countAceptadosByProyecto(idProyecto);
        
        return proyecto.getMaxEstudiantes() - aceptados.intValue();
    }

    @Override
    @Transactional
    public void deleteById(Long idPostulacion) {
        postulacionRepository.deleteById(idPostulacion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Postulacion> findByEstudiante(Long idEstudiante) {
        return postulacionRepository.findByIdEstudiante(idEstudiante);
    }
}