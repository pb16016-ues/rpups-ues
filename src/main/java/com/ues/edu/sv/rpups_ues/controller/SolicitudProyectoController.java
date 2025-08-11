package com.ues.edu.sv.rpups_ues.controller;

import com.ues.edu.sv.rpups_ues.model.entity.Carrera;
import com.ues.edu.sv.rpups_ues.model.entity.Empresa;
import com.ues.edu.sv.rpups_ues.model.entity.Estado;
import com.ues.edu.sv.rpups_ues.model.entity.SolicitudProyecto;
import com.ues.edu.sv.rpups_ues.service.CarreraService;
import com.ues.edu.sv.rpups_ues.service.EmailService;
import com.ues.edu.sv.rpups_ues.service.EmpresaService;
import com.ues.edu.sv.rpups_ues.service.EstadoService;
import com.ues.edu.sv.rpups_ues.service.SolicitudProyectoService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.annotation.Secured;
import jakarta.annotation.security.PermitAll;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/solicitudes-proyectos")
public class SolicitudProyectoController {

    private final SolicitudProyectoService solicitudProyectoService;
    private final EmailService emailService;
    private final EstadoService estadoService;
    private final CarreraService carreraService;
    private final EmpresaService empresaService;

    public SolicitudProyectoController(SolicitudProyectoService solicitudProyectoService, EmailService emailService,
            EstadoService estadoService, CarreraService carreraService, EmpresaService empresaService) {
        this.solicitudProyectoService = solicitudProyectoService;
        this.emailService = emailService;
        this.estadoService = estadoService;
        this.carreraService = carreraService;
        this.empresaService = empresaService;
    }

    @GetMapping
    @PermitAll
    public ResponseEntity<Page<SolicitudProyecto>> getAllSolicitudes(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size) {
        return new ResponseEntity<>(solicitudProyectoService.findAll(PageRequest.of(page, size)),
                HttpStatus.OK);
    }

    @GetMapping("/estado-revision")
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
    public ResponseEntity<Page<SolicitudProyecto>> getSolicitudesByEmpresa(@PathVariable Long idEmpresa,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size) {
        Page<SolicitudProyecto> solicitudes = solicitudProyectoService.findByEmpresa(idEmpresa,
                PageRequest.of(page, size));
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
    public ResponseEntity<List<SolicitudProyecto>> getSolicitudesByAdministradorRevisorAndCodigoEstadoRevision(
            @PathVariable Long idUsuario) {
        List<SolicitudProyecto> solicitudes = solicitudProyectoService
                .findByAdministradorRevisorAndCodigoEstadoRevision(idUsuario);
        return ResponseEntity.ok(solicitudes);
    }

    @GetMapping("/user-creador/{idUsuario}")
    @PermitAll
    public ResponseEntity<Page<SolicitudProyecto>> getSolicitudesByUserCreador(
            @PathVariable Long idUsuario,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size) {
        Page<SolicitudProyecto> result = solicitudProyectoService.findByUserCreador(idUsuario, PageRequest.of(page, size));
        return new ResponseEntity<>(result, HttpStatus.OK);
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
            @RequestParam(name = "idDepto", defaultValue = "", required = false) Long idDeptoCarrera,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size) {

        if (filter != null && filter.trim().matches("^[\\W_]+$")) {
            return ResponseEntity.ok(Page.empty(PageRequest.of(page, size)));
        }
        return new ResponseEntity<>(
                solicitudProyectoService.findSolicitudByFiltros(filter, idDeptoCarrera, PageRequest.of(page, size)),
                HttpStatus.OK);
    }

    @GetMapping("/user-search-filters")
    @PermitAll
    public ResponseEntity<Page<SolicitudProyecto>> getSolicitudesByFiltrosWithUser(
            @RequestParam(name = "idUser", defaultValue = "", required = false) Long idUserCreador,
            @RequestParam(name = "idDepto", defaultValue = "", required = false) Long idDeptoCarrera,
            @RequestParam(name = "filter", defaultValue = "", required = false) String filter,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size) {

        if (filter != null && filter.trim().matches("^[\\W_]+$")) {
            return ResponseEntity.ok(Page.empty(PageRequest.of(page, size)));
        }
        return new ResponseEntity<>(
                solicitudProyectoService.findSolicitudByFiltrosWithUserCreador(filter, idUserCreador, idDeptoCarrera,
                        PageRequest.of(page, size)),
                HttpStatus.OK);
    }

    @PostMapping
    @PermitAll
    public ResponseEntity<SolicitudProyecto> createSolicitud(@RequestBody SolicitudProyecto solicitudProyecto) {
        SolicitudProyecto savedSolicitud = solicitudProyectoService.save(solicitudProyecto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSolicitud);
    }

    @PutMapping("/admin/{id}")
    @PermitAll
    public ResponseEntity<?> updateSolicitudAdmin(@PathVariable Long id,
            @RequestBody SolicitudProyecto solicitudProyecto) {

        SolicitudProyecto solicitudProyectoBD = null;
        Map<String, Object> response = new HashMap<>();

        if (!solicitudProyectoService.findById(id).isPresent()) {
            response.put("Mensaje", "No fue posible encontrar la entidad con el ID proporcionado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            Optional<SolicitudProyecto> optSolicitudProyectoBD = solicitudProyectoService.findById(id);
            solicitudProyectoBD = optSolicitudProyectoBD.get();
        }

        if (solicitudProyectoBD == null) {
            response.put("Mensaje", "No fue posible encontrar la entidad con el ID proporcionado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        try {
            solicitudProyecto.setIdSolicitud(id);
            SolicitudProyecto updatedSolicitud = solicitudProyectoService.updateAdmin(solicitudProyectoBD,
                    solicitudProyecto);

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

                    if (!updatedSolicitud.getUserCreador().getCorreoInstitucional()
                            .equals(updatedSolicitud.getUserCreador().getCorreoPersonal())) {
                        emailService.sendNotificationSolicitudProyectoEmail(
                                updatedSolicitud.getUserCreador().getCorreoPersonal(),
                                updatedSolicitud.getEstado().getNombre(),
                                updatedSolicitud.getObservaciones());
                    }
                }

                if (!updatedSolicitud.getEmpresa().getContactoEmail()
                        .equals(updatedSolicitud.getUserCreador().getCorreoInstitucional())
                        && !updatedSolicitud.getEmpresa().getContactoEmail()
                                .equals(updatedSolicitud.getUserCreador().getCorreoPersonal())) {
                    // Enviar el correo a la empresa
                    emailService.sendNotificationSolicitudProyectoEmail(
                            updatedSolicitud.getEmpresa().getContactoEmail(),
                            updatedSolicitud.getEstado().getNombre(),
                            updatedSolicitud.getObservaciones());
                }
            }

            return ResponseEntity.ok(updatedSolicitud);

        } catch (IllegalArgumentException e) {
            response.put("mensaje", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/externo/{id}")
    @PermitAll
    public ResponseEntity<?> updateSolicitudExterno(@PathVariable Long id,
            @RequestBody SolicitudProyecto solicitudProyecto) {

        SolicitudProyecto solicitudProyectoBD = null;
        Map<String, Object> response = new HashMap<>();

        if (!solicitudProyectoService.findById(id).isPresent()) {
            response.put("Mensaje", "No fue posible encontrar la entidad con el ID proporcionado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            Optional<SolicitudProyecto> optSolicitudProyectoBD = solicitudProyectoService.findById(id);
            solicitudProyectoBD = optSolicitudProyectoBD.get();
        }

        if (solicitudProyectoBD == null) {
            response.put("Mensaje", "No fue posible encontrar la entidad con el ID proporcionado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        try {
            solicitudProyecto.setIdSolicitud(id);
            SolicitudProyecto updatedSolicitud = solicitudProyectoService.updateExterno(solicitudProyectoBD,
                    solicitudProyecto);

            if (updatedSolicitud.getEstado().getNombre().equalsIgnoreCase("En Revisión")
                    || updatedSolicitud.getEstado().getNombre().equalsIgnoreCase("Pendiente")) {

                String datos = updatedSolicitud.getTitulo() + "|" + updatedSolicitud.getUserCreador().getNombres() + " "
                        + updatedSolicitud.getUserCreador().getApellidos() + "|"
                        + updatedSolicitud.getUserCreador().getCorreoInstitucional() + "|"
                        + updatedSolicitud.getObservaciones();

                // Enviar el correo institucional
                emailService.sendNotificationSolicitudProyectoEmail(
                        updatedSolicitud.getAdminRevisor().getCorreoInstitucional(),
                        updatedSolicitud.getEstado().getNombre(),
                        datos);
            }

            return ResponseEntity.ok(updatedSolicitud);

        } catch (IllegalArgumentException e) {
            response.put("mensaje", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
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

    @GetMapping("/report-estado")
    @Secured({ "ADMIN", "COOR", "SUP" })
    public ResponseEntity<byte[]> proyectosByEstadosGenerarReportePDF(@RequestParam("codEstado") String codigoEstado) {

        if (codigoEstado == null || codigoEstado.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("El código de estado es requerido.".getBytes());
        }

        Optional<Estado> estado = estadoService.findByCodigoEstado(codigoEstado);
        if (!estado.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(("Estado con código " + codigoEstado + " no encontrado.").getBytes());
        }
        String nombreEstado = estado.get().getNombre();

        if (nombreEstado == null || nombreEstado.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("El estado con código " + codigoEstado + " no tiene nombre válido.").getBytes());
        }

        byte[] pdfReport = solicitudProyectoService.generarReportePorEstado(codigoEstado, nombreEstado);

        if (pdfReport == null || pdfReport.length == 0) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(("No se pudo generar el reporte para el estado " + nombreEstado).getBytes());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);

        String safeNombreEstado = nombreEstado.replaceAll("[^a-zA-Z0-9\\-_]", "_");
        headers.setContentDisposition(ContentDisposition
                .builder("attachment")
                .filename("Reporte de proyectos por estado " + safeNombreEstado + ".pdf")
                .build());

        return new ResponseEntity<>(pdfReport, headers, HttpStatus.OK);
    }

    @GetMapping("/report-carrera")
    @Secured({ "ADMIN", "COOR", "SUP" })
    public ResponseEntity<byte[]> proyectosByCarrerasGenerarReportePDF(
            @RequestParam("codCarrera") String codigoCarrera) {

        if (codigoCarrera == null || codigoCarrera.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("El código de carrera es requerido.".getBytes());
        }

        Optional<Carrera> carrera = carreraService.findByCodigo(codigoCarrera);
        if (!carrera.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(("Carrera con código " + codigoCarrera + " no encontrada.").getBytes());
        }
        String nombreCarrera = carrera.get().getNombre();

        if (nombreCarrera == null || nombreCarrera.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("La carrera con código " + codigoCarrera + " no tiene nombre válido.").getBytes());
        }

        byte[] pdfReport = solicitudProyectoService.generarReportePorCarrera(codigoCarrera, nombreCarrera);

        if (pdfReport == null || pdfReport.length == 0) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(("No se pudo generar el reporte para la carrera " + nombreCarrera).getBytes());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);

        String safeNombreCarrera = nombreCarrera.replaceAll("[^a-zA-Z0-9\\-_]", "_");
        headers.setContentDisposition(ContentDisposition
                .builder("attachment")
                .filename("Reporte de proyectos por carrera " + safeNombreCarrera + ".pdf")
                .build());

        return new ResponseEntity<>(pdfReport, headers, HttpStatus.OK);
    }

    @GetMapping("/report-empresa")
    @Secured({ "ADMIN", "COOR", "SUP" })
    public ResponseEntity<byte[]> proyectosByEmpresasGenerarReportePDF(
            @RequestParam("idEmpresa") Long idEmpresa) {

        if (idEmpresa == null) {
            return ResponseEntity.badRequest()
                    .body("El ID de la empresa es requerido.".getBytes());
        }

        Optional<Empresa> empresa = empresaService.findById(idEmpresa);
        if (!empresa.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(("Empresa con ID " + idEmpresa + " no encontrada.").getBytes());
        }

        String nombreComercial = empresa.get().getNombreComercial();
        String nombreLegal = empresa.get().getNombreLegal();

        if (nombreComercial == null || nombreComercial.trim().isEmpty() ||
                nombreLegal == null || nombreLegal.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("La empresa con ID " + idEmpresa + " no tiene nombre comercial o legal válido.").getBytes());
        }

        String nombreEmpresa = nombreComercial + " (" + nombreLegal + ")";

        byte[] pdfReport = solicitudProyectoService.generarReportePorEmpresa(idEmpresa, nombreEmpresa);

        if (pdfReport == null || pdfReport.length == 0) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(("No se pudo generar el reporte para la empresa " + nombreEmpresa).getBytes());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);

        String safeNombreEmpresa = nombreEmpresa.replaceAll("[^a-zA-Z0-9\\-_]", "_");
        headers.setContentDisposition(ContentDisposition
                .builder("attachment")
                .filename("Reporte de proyectos por empresa " + safeNombreEmpresa + ".pdf")
                .build());

        return new ResponseEntity<>(pdfReport, headers, HttpStatus.OK);
    }
}