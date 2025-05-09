package com.ues.edu.sv.rpups_ues.controller;

import com.ues.edu.sv.rpups_ues.model.entity.Postulacion;
import com.ues.edu.sv.rpups_ues.service.PostulacionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/postulaciones")
public class PostulacionController {

    private final PostulacionService postulacionService;

    public PostulacionController(PostulacionService postulacionService) {
        this.postulacionService = postulacionService;
    }

    @GetMapping
    public ResponseEntity<List<Postulacion>> getAllPostulaciones() {
        List<Postulacion> postulaciones = postulacionService.findAll();
        return ResponseEntity.ok(postulaciones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Postulacion> getPostulacionById(@PathVariable Long id) {
        Optional<Postulacion> postulacion = postulacionService.findById(id);
        return postulacion.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/estado/{codigoEstado}")
    public ResponseEntity<List<Postulacion>> getPostulacionesByEstado(@PathVariable String codigoEstado) {
        List<Postulacion> postulaciones = postulacionService.findByEstado(codigoEstado);
        return ResponseEntity.ok(postulaciones);
    }

    @GetMapping("/estudiante/{idEstudiante}/proyecto/{idProyecto}")
    public ResponseEntity<Postulacion> getPostulacionByEstudianteAndProyecto(
            @PathVariable Long idEstudiante, @PathVariable Long idProyecto) {
        Optional<Postulacion> postulacion = postulacionService.findByEstudianteAndProyecto(idEstudiante, idProyecto);
        return postulacion.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/estudiante/{idEstudiante}/proyecto/{idProyecto}/estado/{codigoEstado}")
    public ResponseEntity<Postulacion> getPostulacionByEstudianteAndProyectoAndEstado(
            @PathVariable Long idEstudiante, @PathVariable Long idProyecto, @PathVariable String codigoEstado) {
        Optional<Postulacion> postulacion = postulacionService.findByEstudianteAndProyectoAndEstado(
                idEstudiante, idProyecto, codigoEstado);
        return postulacion.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Postulacion> createPostulacion(@RequestBody Postulacion postulacion) {
        try {
            Postulacion savedPostulacion = postulacionService.save(postulacion);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPostulacion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePostulacion(@PathVariable Long id) {
        if (!postulacionService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        postulacionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}