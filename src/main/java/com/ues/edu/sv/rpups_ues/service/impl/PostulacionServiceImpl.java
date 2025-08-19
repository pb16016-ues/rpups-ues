package com.ues.edu.sv.rpups_ues.service.impl;

import com.ues.edu.sv.rpups_ues.model.entity.Postulacion;
import com.ues.edu.sv.rpups_ues.model.repository.PostulacionRepository;
import com.ues.edu.sv.rpups_ues.service.PostulacionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PostulacionServiceImpl implements PostulacionService {

    private final PostulacionRepository postulacionRepository;

    public PostulacionServiceImpl(PostulacionRepository postulacionRepository) {
        this.postulacionRepository = postulacionRepository;
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

        Optional<Postulacion> existingPostulacion = postulacionRepository
                .findByIdEstudianteAndIdProyecto(
                        postulacion.getIdEstudiante(),
                        postulacion.getIdProyecto());

        if (existingPostulacion.isPresent()) {
            throw new IllegalArgumentException(
                    "Ya existe una postulación para este estudiante asociada a este proyecto.");
        }
        postulacion.setFechaPostulacion(null);

        return postulacionRepository.save(postulacion);
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