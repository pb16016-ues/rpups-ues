package com.ues.edu.sv.rpups_ues.controller;

import com.ues.edu.sv.rpups_ues.model.entity.Postulacion;
import com.ues.edu.sv.rpups_ues.service.PostulacionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.annotation.Secured;

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
    @Secured({ "ADMIN", "COORD", "SUP" })
    public ResponseEntity<List<Postulacion>> getAllPostulaciones() {
        List<Postulacion> postulaciones = postulacionService.findAll();
        return ResponseEntity.ok(postulaciones);
    }

    @GetMapping("/proyecto/{id}")
    @Secured({ "EMP", "ADMIN", "COORD", "SUP" })
    public ResponseEntity<List<Postulacion>> getPostulacionesByProyecto(@PathVariable Long id) {
        List<Postulacion> postulaciones = postulacionService.findByProyecto(id);
        return ResponseEntity.ok(postulaciones);
    }

    @GetMapping("/{id}")
    @Secured({ "ESTUD", "EMP", "ADMIN", "COORD", "SUP" })
    public ResponseEntity<Postulacion> getPostulacionById(@PathVariable Long id) {
        Optional<Postulacion> postulacion = postulacionService.findById(id);
        return postulacion.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/estudiante/{idEstudiante}/proyecto/{idProyecto}")
    @Secured({ "ESTUD", "ADMIN", "COORD", "SUP" })
    public ResponseEntity<Postulacion> getPostulacionByEstudianteAndProyecto(
            @PathVariable Long idEstudiante, @PathVariable Long idProyecto) {
        Optional<Postulacion> postulacion = postulacionService.findByEstudianteAndProyecto(idEstudiante, idProyecto);
        return postulacion.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/estudiante/{idEstudiante}")
    @Secured({ "ESTUD", "ADMIN", "COORD", "SUP" })
    public ResponseEntity<List<Postulacion>> getPostulacionesByEstudiante(@PathVariable Long idEstudiante) {
        List<Postulacion> postulaciones = postulacionService.findByEstudiante(idEstudiante);
        return ResponseEntity.ok(postulaciones);
    }

    @PostMapping
    @Secured({ "ESTUD" })
    public ResponseEntity<Postulacion> createPostulacion(@RequestBody Postulacion postulacion) {
        try {
            Postulacion savedPostulacion = postulacionService.save(postulacion);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPostulacion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @DeleteMapping("/{id}")
    @Secured({ "ESTUD", "ADMIN", "COORD", "SUP" })
    public ResponseEntity<Void> deletePostulacion(@PathVariable Long id) {
        if (!postulacionService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        postulacionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}