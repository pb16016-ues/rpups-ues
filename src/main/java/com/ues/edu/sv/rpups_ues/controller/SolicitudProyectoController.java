package com.ues.edu.sv.rpups_ues.controller;

import com.ues.edu.sv.rpups_ues.model.entity.SolicitudProyecto;
import com.ues.edu.sv.rpups_ues.service.EmailService;
import com.ues.edu.sv.rpups_ues.service.SolicitudProyectoService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.annotation.Secured;
import jakarta.annotation.security.PermitAll;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/solicitudes-proyectos")
public class SolicitudProyectoController {

    private final SolicitudProyectoService solicitudProyectoService;
    private final EmailService emailService;

    public SolicitudProyectoController(SolicitudProyectoService solicitudProyectoService, EmailService emailService) {
        this.solicitudProyectoService = solicitudProyectoService;
        this.emailService = emailService;
    }

    @GetMapping
    @PermitAll
    public ResponseEntity<Page<SolicitudProyecto>> getAllSolicitudes(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size) {
        return new ResponseEntity<>(solicitudProyectoService.findAll(PageRequest.of(page, size)),
                HttpStatus.OK);
    }

    @GetMapping
    @Secured({ "ADMIN", "COOR", "SUP" })
    public ResponseEntity<Page<SolicitudProyecto>> getSolicitudesByEstadoRevision(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size) {
        return new ResponseEntity<>(solicitudProyectoService.findByEstadoRevision(PageRequest.of(page, size)),
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PermitAll
    public ResponseEntity<SolicitudProyecto> getSolicitudById(@PathVariable Long id) {
        Optional<SolicitudProyecto> solicitud = solicitudProyectoService.findById(id);
        return solicitud.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/titulo/{titulo}")
    @PermitAll
    public ResponseEntity<List<SolicitudProyecto>> getSolicitudesByTitulo(@PathVariable String titulo) {
        List<SolicitudProyecto> solicitudes = solicitudProyectoService.findByTitulo(titulo);
        return ResponseEntity.ok(solicitudes);
    }

    @GetMapping("/estado/{codigoEstado}")
    @PermitAll
    public ResponseEntity<List<SolicitudProyecto>> getSolicitudesByEstado(@PathVariable String codigoEstado) {
        List<SolicitudProyecto> solicitudes = solicitudProyectoService.findByEstado(codigoEstado);
        return ResponseEntity.ok(solicitudes);
    }

    @GetMapping("/empresa/{idEmpresa}")
    @PermitAll
    public ResponseEntity<List<SolicitudProyecto>> getSolicitudesByEmpresa(@PathVariable Long idEmpresa) {
        List<SolicitudProyecto> solicitudes = solicitudProyectoService.findByEmpresa(idEmpresa);
        return ResponseEntity.ok(solicitudes);
    }

    @GetMapping("/carrera/{codigoCarrera}")
    @PermitAll
    public ResponseEntity<List<SolicitudProyecto>> getSolicitudesByCarrera(@PathVariable String codigoCarrera) {
        List<SolicitudProyecto> solicitudes = solicitudProyectoService.findByCarrera(codigoCarrera);
        return ResponseEntity.ok(solicitudes);
    }

    @GetMapping("/modalidad/{codigoModalidad}")
    @PermitAll
    public ResponseEntity<List<SolicitudProyecto>> getSolicitudesByModalidad(@PathVariable String codigoModalidad) {
        List<SolicitudProyecto> solicitudes = solicitudProyectoService.findByModalidad(codigoModalidad);
        return ResponseEntity.ok(solicitudes);
    }

    @GetMapping("/admin-revisor/{idUsuario}")
    @PermitAll
    public ResponseEntity<List<SolicitudProyecto>> getSolicitudesByAdministradorRevisor(@PathVariable Long idUsuario) {
        List<SolicitudProyecto> solicitudes = solicitudProyectoService.findByAdministradorRevisor(idUsuario);
        return ResponseEntity.ok(solicitudes);
    }

    @GetMapping("/user-creador/{idUsuario}")
    @PermitAll
    public ResponseEntity<List<SolicitudProyecto>> getSolicitudesByUserCreador(@PathVariable Long idUsuario) {
        List<SolicitudProyecto> solicitudes = solicitudProyectoService.findByUserCreador(idUsuario);
        return ResponseEntity.ok(solicitudes);
    }

    @GetMapping("/empresa/{idEmpresa}/estado/{codigoEstado}")
    @PermitAll
    public ResponseEntity<List<SolicitudProyecto>> getSolicitudesByEmpresaAndEstado(
            @PathVariable Long idEmpresa, @PathVariable String codigoEstado) {
        List<SolicitudProyecto> solicitudes = solicitudProyectoService
                .findByEmpresaIdEmpresaAndEstadoCodigoEstado(idEmpresa, codigoEstado);
        return ResponseEntity.ok(solicitudes);
    }

    @GetMapping("/carrera/{codigoCarrera}/estado/{codigoEstado}")
    @PermitAll
    public ResponseEntity<List<SolicitudProyecto>> getSolicitudesByCarreraAndEstado(
            @PathVariable String codigoCarrera, @PathVariable String codigoEstado) {
        List<SolicitudProyecto> solicitudes = solicitudProyectoService.findByCarreraCodigoAndEstadoCodigoEstado(
                codigoCarrera,
                codigoEstado);
        return ResponseEntity.ok(solicitudes);
    }

    @GetMapping("/modalidad/{codigoModalidad}/estado/{codigoEstado}")
    @PermitAll
    public ResponseEntity<List<SolicitudProyecto>> getSolicitudesByModalidadAndEstado(
            @PathVariable String codigoModalidad, @PathVariable String codigoEstado) {
        List<SolicitudProyecto> solicitudes = solicitudProyectoService
                .findByModalidadCodigoModalidadAndEstadoCodigoEstado(codigoModalidad,
                        codigoEstado);
        return ResponseEntity.ok(solicitudes);
    }

    @GetMapping("/search-filters")
    @Secured({ "ADMIN", "COOR", "SUP" })
    public ResponseEntity<Page<SolicitudProyecto>> getSolicitudesByFiltros(
            @RequestParam(name = "filter", defaultValue = "", required = false) String filter,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size) {

        if (filter != null && filter.trim().matches("^[\\W_]+$")) {
            return ResponseEntity.ok(Page.empty(PageRequest.of(page, size)));
        }
        return new ResponseEntity<>(solicitudProyectoService.findSolicitudByFiltros(filter, PageRequest.of(page, size)),
                HttpStatus.OK);
    }

    @GetMapping("/user-search-filters")
    @PermitAll
    public ResponseEntity<Page<SolicitudProyecto>> getSolicitudesByFiltrosWithUser(
            @RequestParam(name = "idUser", defaultValue = "", required = false) Long idUserCreador,
            @RequestParam(name = "filter", defaultValue = "", required = false) String filter,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size) {

        if (filter != null && filter.trim().matches("^[\\W_]+$")) {
            return ResponseEntity.ok(Page.empty(PageRequest.of(page, size)));
        }
        return new ResponseEntity<>(
                solicitudProyectoService.findSolicitudByFiltrosWithUserCreador(filter, idUserCreador,
                        PageRequest.of(page, size)),
                HttpStatus.OK);
    }

    @PostMapping
    @PermitAll
    public ResponseEntity<SolicitudProyecto> createSolicitud(@RequestBody SolicitudProyecto solicitudProyecto) {
        SolicitudProyecto savedSolicitud = solicitudProyectoService.save(solicitudProyecto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSolicitud);
    }

    @PutMapping("/{id}")
    @PermitAll
    public ResponseEntity<SolicitudProyecto> updateSolicitud(@PathVariable Long id,
            @RequestBody SolicitudProyecto solicitudProyecto) {
        if (!solicitudProyectoService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        if (solicitudProyecto.getEstado() == null) {
            return ResponseEntity.badRequest().build();
        } else if (solicitudProyecto.getEstado().getNombre() == null
                || solicitudProyecto.getEstado().getNombre().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        solicitudProyecto.setIdSolicitud(id);
        SolicitudProyecto updatedSolicitud = solicitudProyectoService.save(solicitudProyecto);

        if (updatedSolicitud.getEstado().getNombre().equalsIgnoreCase("Aprobado")
                || updatedSolicitud.getEstado().getNombre().equalsIgnoreCase("Rechazado")
                || updatedSolicitud.getEstado().getNombre().equalsIgnoreCase("En Observación")) {

            // Enviar el correo institucional
            emailService.sendNotificationSolicitudProyectoEmail(
                    updatedSolicitud.getUserCreador().getCorreoInstitucional(),
                    updatedSolicitud.getEstado().getNombre(),
                    updatedSolicitud.getObservaciones());

            // Enviar el correo personal
            if (updatedSolicitud.getUserCreador().getCorreoPersonal() != null) {
                emailService.sendNotificationSolicitudProyectoEmail(
                        updatedSolicitud.getUserCreador().getCorreoPersonal(),
                        updatedSolicitud.getEstado().getNombre(),
                        updatedSolicitud.getObservaciones());
            }

            // Enviar el correo a la empresa
            emailService.sendNotificationSolicitudProyectoEmail(
                    updatedSolicitud.getEmpresa().getContactoEmail(),
                    updatedSolicitud.getEstado().getNombre(),
                    updatedSolicitud.getObservaciones());
        }

        return ResponseEntity.ok(updatedSolicitud);
    }

    @DeleteMapping("/{id}")
    @Secured({ "ADMIN", "COOR", "SUP" })
    public ResponseEntity<Void> deleteSolicitud(@PathVariable Long id) {
        if (!solicitudProyectoService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        solicitudProyectoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}