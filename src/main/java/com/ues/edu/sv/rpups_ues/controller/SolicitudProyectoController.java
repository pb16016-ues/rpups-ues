package com.ues.edu.sv.rpups_ues.controller;

import com.ues.edu.sv.rpups_ues.model.entity.SolicitudProyecto;
import com.ues.edu.sv.rpups_ues.service.SolicitudProyectoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/solicitudes-proyectos")
public class SolicitudProyectoController {

    private final SolicitudProyectoService solicitudProyectoService;

    public SolicitudProyectoController(SolicitudProyectoService solicitudProyectoService) {
        this.solicitudProyectoService = solicitudProyectoService;
    }

    @GetMapping
    public ResponseEntity<List<SolicitudProyecto>> getAllSolicitudes() {
        List<SolicitudProyecto> solicitudes = solicitudProyectoService.findAll();
        return ResponseEntity.ok(solicitudes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudProyecto> getSolicitudById(@PathVariable Long id) {
        Optional<SolicitudProyecto> solicitud = solicitudProyectoService.findById(id);
        return solicitud.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/titulo/{titulo}")
    public ResponseEntity<List<SolicitudProyecto>> getSolicitudesByTitulo(@PathVariable String titulo) {
        List<SolicitudProyecto> solicitudes = solicitudProyectoService.findByTitulo(titulo);
        return ResponseEntity.ok(solicitudes);
    }

    @GetMapping("/estado/{codigoEstado}")
    public ResponseEntity<List<SolicitudProyecto>> getSolicitudesByEstado(@PathVariable String codigoEstado) {
        List<SolicitudProyecto> solicitudes = solicitudProyectoService.findByEstado(codigoEstado);
        return ResponseEntity.ok(solicitudes);
    }

    @GetMapping("/empresa/{idEmpresa}")
    public ResponseEntity<List<SolicitudProyecto>> getSolicitudesByEmpresa(@PathVariable Long idEmpresa) {
        List<SolicitudProyecto> solicitudes = solicitudProyectoService.findByEmpresa(idEmpresa);
        return ResponseEntity.ok(solicitudes);
    }

    @GetMapping("/carrera/{codigoCarrera}")
    public ResponseEntity<List<SolicitudProyecto>> getSolicitudesByCarrera(@PathVariable String codigoCarrera) {
        List<SolicitudProyecto> solicitudes = solicitudProyectoService.findByCarrera(codigoCarrera);
        return ResponseEntity.ok(solicitudes);
    }

    @GetMapping("/modalidad/{codigoModalidad}")
    public ResponseEntity<List<SolicitudProyecto>> getSolicitudesByModalidad(@PathVariable String codigoModalidad) {
        List<SolicitudProyecto> solicitudes = solicitudProyectoService.findByModalidad(codigoModalidad);
        return ResponseEntity.ok(solicitudes);
    }

    @GetMapping("/admin-revisor/{idUsuario}")
    public ResponseEntity<List<SolicitudProyecto>> getSolicitudesByAdministradorRevisor(@PathVariable Long idUsuario) {
        List<SolicitudProyecto> solicitudes = solicitudProyectoService.findByAdministradorRevisor(idUsuario);
        return ResponseEntity.ok(solicitudes);
    }

    @GetMapping("/empresa/{idEmpresa}/estado/{codigoEstado}")
    public ResponseEntity<List<SolicitudProyecto>> getProyectosByEmpresaAndEstado(
            @PathVariable Long idEmpresa, @PathVariable String codigoEstado) {
        List<SolicitudProyecto> solicitudes = solicitudProyectoService
                .findByEmpresaIdEmpresaAndEstadoCodigoEstado(idEmpresa, codigoEstado);
        return ResponseEntity.ok(solicitudes);
    }

    @GetMapping("/carrera/{codigoCarrera}/estado/{codigoEstado}")
    public ResponseEntity<List<SolicitudProyecto>> getProyectosByCarreraAndEstado(
            @PathVariable String codigoCarrera, @PathVariable String codigoEstado) {
        List<SolicitudProyecto> solicitudes = solicitudProyectoService.findByCarreraCodigoAndEstadoCodigoEstado(
                codigoCarrera,
                codigoEstado);
        return ResponseEntity.ok(solicitudes);
    }

    @GetMapping("/modalidad/{codigoModalidad}/estado/{codigoEstado}")
    public ResponseEntity<List<SolicitudProyecto>> getProyectosByModalidadAndEstado(
            @PathVariable String codigoModalidad, @PathVariable String codigoEstado) {
        List<SolicitudProyecto> solicitudes = solicitudProyectoService
                .findByModalidadCodigoModalidadAndEstadoCodigoEstado(codigoModalidad,
                        codigoEstado);
        return ResponseEntity.ok(solicitudes);
    }

    @PostMapping
    public ResponseEntity<SolicitudProyecto> createSolicitud(@RequestBody SolicitudProyecto solicitudProyecto) {
        SolicitudProyecto savedSolicitud = solicitudProyectoService.save(solicitudProyecto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSolicitud);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SolicitudProyecto> updateSolicitud(@PathVariable Long id,
            @RequestBody SolicitudProyecto solicitudProyecto) {
        if (!solicitudProyectoService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        solicitudProyecto.setIdSolicitud(id);
        SolicitudProyecto updatedSolicitud = solicitudProyectoService.save(solicitudProyecto);
        return ResponseEntity.ok(updatedSolicitud);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSolicitud(@PathVariable Long id) {
        if (!solicitudProyectoService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        solicitudProyectoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}