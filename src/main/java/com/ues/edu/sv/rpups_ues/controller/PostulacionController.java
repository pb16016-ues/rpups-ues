package com.ues.edu.sv.rpups_ues.controller;

import com.ues.edu.sv.rpups_ues.model.entity.Postulacion;
import com.ues.edu.sv.rpups_ues.model.entity.Proyecto;
import com.ues.edu.sv.rpups_ues.model.DTO.CambioEstadoPostulacionDTO;
import com.ues.edu.sv.rpups_ues.model.DTO.CuposProyectoDTO;
import com.ues.edu.sv.rpups_ues.model.DTO.Message;
import com.ues.edu.sv.rpups_ues.service.PostulacionService;
import com.ues.edu.sv.rpups_ues.service.ProyectoService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/postulaciones")
public class PostulacionController {

    private final PostulacionService postulacionService;
    private final ProyectoService proyectoService;

    public PostulacionController(PostulacionService postulacionService, ProyectoService proyectoService) {
        this.postulacionService = postulacionService;
        this.proyectoService = proyectoService;
    }

    @GetMapping("/proyecto/{id}")
    @Secured({ "EMP", "ADMIN", "COORD", "SUP" })
    public ResponseEntity<List<Postulacion>> getPostulacionesByProyecto(@PathVariable Long id) {
        List<Postulacion> postulaciones = postulacionService.findByProyecto(id);
        return ResponseEntity.ok(postulaciones);
    }

    @GetMapping("/proyecto/{id}/estado/{codigoEstado}")
    @Secured({ "EMP", "ADMIN", "COORD", "SUP" })
    public ResponseEntity<List<Postulacion>> getPostulacionesByProyectoAndEstado(
            @PathVariable Long id, @PathVariable String codigoEstado) {
        List<Postulacion> postulaciones = postulacionService.findByProyectoAndEstado(id, codigoEstado);
        return ResponseEntity.ok(postulaciones);
    }

    @GetMapping("/proyecto/{id}/pendientes")
    @Secured({ "EMP", "ADMIN", "COORD", "SUP" })
    public ResponseEntity<List<Postulacion>> getPostulacionesPendientesByProyecto(@PathVariable Long id) {
        List<Postulacion> postulaciones = postulacionService.findByProyectoAndEstado(id, "PEND");
        return ResponseEntity.ok(postulaciones);
    }

    @GetMapping("/proyecto/{id}/aceptadas")
    @Secured({ "EMP", "ADMIN", "COORD", "SUP" })
    public ResponseEntity<List<Postulacion>> getPostulacionesAceptadasByProyecto(@PathVariable Long id) {
        List<Postulacion> postulaciones = postulacionService.findByProyectoAndEstado(id, "APRO");
        return ResponseEntity.ok(postulaciones);
    }

    @GetMapping("/proyecto/{id}/cupos")
    @Secured({ "ESTUD", "EMP", "ADMIN", "COORD", "SUP" })
    public ResponseEntity<?> getCuposProyecto(@PathVariable Long id) {
        try {
            Optional<Proyecto> optProyecto = proyectoService.findById(id);
            if (optProyecto.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new Message("No existe el proyecto con ID: " + id));
            }

            Proyecto proyecto = optProyecto.get();
            Long aceptados = postulacionService.countAceptadosByProyecto(id);
            Long pendientes = postulacionService.countByProyectoAndEstado(id, "PEND");
            int cuposDisponibles = postulacionService.getCuposDisponibles(id);

            CuposProyectoDTO cupos = new CuposProyectoDTO();
            cupos.setIdProyecto(id);
            cupos.setTituloProyecto(proyecto.getTitulo());
            cupos.setMaxEstudiantes(proyecto.getMaxEstudiantes());
            cupos.setEstudiantesAceptados(aceptados);
            cupos.setCuposDisponibles(cuposDisponibles);
            cupos.setPostulacionesPendientes(pendientes);

            return ResponseEntity.ok(cupos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Message("Error al obtener información de cupos: " + e.getMessage()));
        }
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
    public ResponseEntity<?> createPostulacion(@RequestBody Postulacion postulacion) {
        try {
            Postulacion savedPostulacion = postulacionService.save(postulacion);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPostulacion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new Message(e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Message(e.getMessage()));
        }
    }

    @PutMapping("/{id}/estado")
    @Secured({ "ADMIN", "COORD" })
    public ResponseEntity<?> cambiarEstadoPostulacion(
            @PathVariable Long id,
            @Valid @RequestBody CambioEstadoPostulacionDTO dto) {
        try {
            // Obtener el ID del usuario autenticado
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Long idAdmin = Long.parseLong(authentication.getName());

            Postulacion postulacion = postulacionService.cambiarEstado(
                    id,
                    dto.getNuevoEstado(),
                    idAdmin,
                    dto.getObservaciones());

            return ResponseEntity.ok(postulacion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Message(e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new Message(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Message("Error al cambiar estado de la postulación: " + e.getMessage()));
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