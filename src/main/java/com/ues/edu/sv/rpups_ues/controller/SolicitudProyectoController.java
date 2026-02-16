package com.ues.edu.sv.rpups_ues.controller;

import com.ues.edu.sv.rpups_ues.model.entity.Carrera;
import com.ues.edu.sv.rpups_ues.model.entity.DepartamentoCarrera;
import com.ues.edu.sv.rpups_ues.model.entity.Empresa;
import com.ues.edu.sv.rpups_ues.model.entity.Estado;
import com.ues.edu.sv.rpups_ues.model.entity.SolicitudProyecto;
import com.ues.edu.sv.rpups_ues.model.DTO.AprobacionSolicitudDTO;
import com.ues.edu.sv.rpups_ues.model.DTO.AprobacionSolicitudResponse;
import com.ues.edu.sv.rpups_ues.service.CarreraService;
import com.ues.edu.sv.rpups_ues.service.DepartamentoCarreraService;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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
    private final DepartamentoCarreraService deptoCarreraService;
    private final EmpresaService empresaService;

    public SolicitudProyectoController(SolicitudProyectoService solicitudProyectoService, EmailService emailService,
            EstadoService estadoService, CarreraService carreraService, DepartamentoCarreraService deptoCarreraService,
            EmpresaService empresaService) {
        this.solicitudProyectoService = solicitudProyectoService;
        this.emailService = emailService;
        this.estadoService = estadoService;
        this.carreraService = carreraService;
        this.deptoCarreraService = deptoCarreraService;
        this.empresaService = empresaService;
    }

    @GetMapping
    @Secured({ "ADMIN", "COORD", "SUP" })
    public ResponseEntity<Page<SolicitudProyecto>> getAllSolicitudes(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size) {
        return new ResponseEntity<>(solicitudProyectoService.findAll(PageRequest.of(page, size)),
                HttpStatus.OK);
    }

    @GetMapping("/estado-revision")
    @Secured({ "ADMIN", "COORD", "SUP" })
    public ResponseEntity<Page<SolicitudProyecto>> getSolicitudesByEstadoRevision(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size) {
        return new ResponseEntity<>(solicitudProyectoService.findByEstadoRevision(PageRequest.of(page, size)),
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Secured({ "ESTUD", "EMP", "ADMIN", "COORD", "SUP" })
    public ResponseEntity<SolicitudProyecto> getSolicitudById(@PathVariable Long id) {
        Optional<SolicitudProyecto> solicitud = solicitudProyectoService.findById(id);
        return solicitud.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/estado/{codigoEstado}")
    @Secured({ "ADMIN", "COORD", "SUP" })
    public ResponseEntity<List<SolicitudProyecto>> getSolicitudesByEstado(@PathVariable String codigoEstado) {
        List<SolicitudProyecto> solicitudes = solicitudProyectoService.findByEstado(codigoEstado);
        return ResponseEntity.ok(solicitudes);
    }

    /**
     * Endpoint optimizado para obtener solo el conteo de solicitudes por estado.
     * Ideal para badges y notificaciones en el TabMenu.
     */
    @GetMapping("/count/estado/{codigoEstado}")
    @Secured({ "ADMIN", "COORD", "SUP" })
    public ResponseEntity<Map<String, Object>> getCountByEstado(@PathVariable String codigoEstado) {
        long count = solicitudProyectoService.countByEstado(codigoEstado);
        Map<String, Object> response = new HashMap<>();
        response.put("codigoEstado", codigoEstado);
        response.put("count", count);
        return ResponseEntity.ok(response);
    }

    /**
     * Solicitudes sin asignar (sin admin revisor).
     * Para la pestaña "Sin Asignar".
     */
    @GetMapping("/sin-asignar")
    @Secured({ "ADMIN" })
    public ResponseEntity<List<SolicitudProyecto>> getSolicitudesSinAsignar() {
        List<SolicitudProyecto> solicitudes = solicitudProyectoService.findUnassigned();
        return ResponseEntity.ok(solicitudes);
    }

    /**
     * Conteo de solicitudes sin asignar.
     */
    @GetMapping("/count/sin-asignar")
    @Secured({ "ADMIN" })
    public ResponseEntity<Map<String, Object>> getCountSinAsignar() {
        long count = solicitudProyectoService.countUnassigned();
        Map<String, Object> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }

    /**
     * Solicitudes sin asignar filtradas por departamento de carrera (COORD).
     */
    @GetMapping("/sin-asignar/coord/{idDeptoCarrera}")
    @Secured({ "COORD" })
    public ResponseEntity<List<SolicitudProyecto>> getSolicitudesSinAsignarCoord(@PathVariable Long idDeptoCarrera) {
        List<SolicitudProyecto> solicitudes = solicitudProyectoService.findUnassignedCoord(idDeptoCarrera);
        return ResponseEntity.ok(solicitudes);
    }

    /**
     * Conteo de solicitudes sin asignar filtradas por departamento de carrera (COORD).
     */
    @GetMapping("/count/sin-asignar/coord/{idDeptoCarrera}")
    @Secured({ "COORD" })
    public ResponseEntity<Map<String, Object>> getCountSinAsignarCoord(@PathVariable Long idDeptoCarrera) {
        long count = solicitudProyectoService.countUnassignedCoord(idDeptoCarrera);
        Map<String, Object> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }

    /**
     * Bandeja de entrada: solicitudes asignadas al admin que NO están cerradas (APRO o RECH).
     * Para la pestaña "Bandeja de entrada".
     */
    @GetMapping("/bandeja-entrada/{idAdmin}")
    @Secured({ "ADMIN", "COORD", "SUP" })
    public ResponseEntity<List<SolicitudProyecto>> getBandejaEntrada(@PathVariable Long idAdmin) {
        List<SolicitudProyecto> solicitudes = solicitudProyectoService.findBandejaEntrada(idAdmin);
        return ResponseEntity.ok(solicitudes);
    }

    /**
     * Conteo de bandeja de entrada de un admin.
     */
    @GetMapping("/count/bandeja-entrada/{idAdmin}")
    @Secured({ "ADMIN", "COORD", "SUP" })
    public ResponseEntity<Map<String, Object>> getCountBandejaEntrada(@PathVariable Long idAdmin) {
        long count = solicitudProyectoService.countBandejaEntrada(idAdmin);
        Map<String, Object> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }

    /**
     * Bandeja de entrada para COORD: solicitudes asignadas al COORD o a SUP del mismo departamento de carrera.
     * Filtra por departamento de carrera.
     */
    @GetMapping("/bandeja-entrada/coord/{idDeptoCarrera}")
    @Secured({ "COORD" })
    public ResponseEntity<List<SolicitudProyecto>> getBandejaEntradaCoord(@PathVariable Long idDeptoCarrera) {
        List<SolicitudProyecto> solicitudes = solicitudProyectoService.findBandejaEntradaCoord(idDeptoCarrera);
        return ResponseEntity.ok(solicitudes);
    }

    /**
     * Conteo de bandeja de entrada para COORD por departamento de carrera.
     */
    @GetMapping("/count/bandeja-entrada/coord/{idDeptoCarrera}")
    @Secured({ "COORD" })
    public ResponseEntity<Map<String, Object>> getCountBandejaEntradaCoord(@PathVariable Long idDeptoCarrera) {
        long count = solicitudProyectoService.countBandejaEntradaCoord(idDeptoCarrera);
        Map<String, Object> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }

    /**
     * Bandeja de entrada para ADMIN: TODAS las solicitudes no cerradas (modo lectura).
     */
    @GetMapping("/bandeja-entrada/admin")
    @Secured({ "ADMIN" })
    public ResponseEntity<List<SolicitudProyecto>> getBandejaEntradaAdmin() {
        List<SolicitudProyecto> solicitudes = solicitudProyectoService.findBandejaEntradaAdmin();
        return ResponseEntity.ok(solicitudes);
    }

    /**
     * Conteo de bandeja de entrada para ADMIN.
     */
    @GetMapping("/count/bandeja-entrada/admin")
    @Secured({ "ADMIN" })
    public ResponseEntity<Map<String, Object>> getCountBandejaEntradaAdmin() {
        long count = solicitudProyectoService.countBandejaEntradaAdmin();
        Map<String, Object> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }

    /**
     * Todas las solicitudes asignadas a un admin (sin importar estado).
     * Para la pestaña "Solicitudes".
     */
    @GetMapping("/mis-solicitudes/{idAdmin}")
    @Secured({ "ADMIN", "COORD", "SUP" })
    public ResponseEntity<List<SolicitudProyecto>> getMisSolicitudes(@PathVariable Long idAdmin) {
        List<SolicitudProyecto> solicitudes = solicitudProyectoService.findByAdminRevisor(idAdmin);
        return ResponseEntity.ok(solicitudes);
    }

    @GetMapping("/empresa/{idEmpresa}")
    @Secured({ "EMP", "ADMIN", "COORD", "SUP" })
    public ResponseEntity<Page<SolicitudProyecto>> getSolicitudesByEmpresa(@PathVariable Long idEmpresa,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size) {
        Page<SolicitudProyecto> solicitudes = solicitudProyectoService.findByEmpresa(idEmpresa,
                PageRequest.of(page, size));
        return ResponseEntity.ok(solicitudes);
    }

    @GetMapping("/admin-revisor/{idUsuario}")
    @Secured({ "ADMIN", "COORD", "SUP" })
    public ResponseEntity<List<SolicitudProyecto>> getSolicitudesByAdministradorRevisorAndCodigoEstadoRevision(
            @PathVariable Long idUsuario) {
        List<SolicitudProyecto> solicitudes = solicitudProyectoService
                .findByAdministradorRevisorAndCodigoEstadoRevision(idUsuario);
        return ResponseEntity.ok(solicitudes);
    }

    @GetMapping("/user-creador/{idUsuario}")
    @Secured({ "ESTUD", "EMP", "ADMIN", "COORD", "SUP" })
    public ResponseEntity<Page<SolicitudProyecto>> getSolicitudesByUserCreador(
            @PathVariable Long idUsuario,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size) {
        Page<SolicitudProyecto> result = solicitudProyectoService.findByUserCreador(idUsuario,
                PageRequest.of(page, size));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/search-filters")
    @Secured({ "ADMIN", "COORD", "SUP" })
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
    @Secured({ "ESTUD", "EMP", "ADMIN", "COORD", "SUP" })
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
    @Secured({ "ESTUD", "EMP" })
    public ResponseEntity<SolicitudProyecto> createSolicitud(@RequestBody SolicitudProyecto solicitudProyecto) {
        SolicitudProyecto savedSolicitud = solicitudProyectoService.save(solicitudProyecto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSolicitud);
    }

    @PutMapping("/{idSolicitud}/revision")
    @Secured({ "ADMIN", "COORD", "SUP", "EMP" })
    public ResponseEntity<?> updateSolicitudAdmin(@PathVariable Long idSolicitud,
            @RequestBody SolicitudProyecto solicitudProyecto) {

        SolicitudProyecto solicitudProyectoBD = null;
        Map<String, Object> response = new HashMap<>();

        if (!solicitudProyectoService.findById(idSolicitud).isPresent()) {
            response.put("mensaje", "No fue posible encontrar la entidad con el ID proporcionado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            Optional<SolicitudProyecto> optSolicitudProyectoBD = solicitudProyectoService.findById(idSolicitud);
            solicitudProyectoBD = optSolicitudProyectoBD.get();
        }

        if (solicitudProyectoBD == null) {
            response.put("mensaje", "No fue posible encontrar la entidad con el ID proporcionado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        try {
            solicitudProyecto.setIdSolicitud(idSolicitud);
            SolicitudProyecto updatedSolicitud = solicitudProyectoService.updateAdmin(solicitudProyectoBD,
                    solicitudProyecto);

            // Intentar enviar notificación por email (no bloquear si falla)
            try {
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
            } catch (Exception emailException) {
                // Loggear el error pero no fallar la operación principal
                System.err.println("Error al enviar notificación por email: " + emailException.getMessage());
            }

            return ResponseEntity.ok(updatedSolicitud);

        } catch (IllegalArgumentException e) {
            response.put("mensaje", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{idSolicitud}/correccion")
    @Secured({ "ESTUD", "EMP" })
    public ResponseEntity<?> updateSolicitudExterno(@PathVariable Long idSolicitud,
            @RequestBody SolicitudProyecto solicitudProyecto) {

        SolicitudProyecto solicitudProyectoBD = null;
        Map<String, Object> response = new HashMap<>();

        if (!solicitudProyectoService.findById(idSolicitud).isPresent()) {
            response.put("Mensaje", "No fue posible encontrar la entidad con el ID proporcionado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            Optional<SolicitudProyecto> optSolicitudProyectoBD = solicitudProyectoService.findById(idSolicitud);
            solicitudProyectoBD = optSolicitudProyectoBD.get();
        }

        if (solicitudProyectoBD == null) {
            response.put("Mensaje", "No fue posible encontrar la entidad con el ID proporcionado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        try {
            solicitudProyecto.setIdSolicitud(idSolicitud);
            SolicitudProyecto updatedSolicitud = solicitudProyectoService.updateExterno(solicitudProyectoBD,
                    solicitudProyecto);

            // Verificar que el estado existe antes de intentar acceder a sus propiedades
            String nombreEstado = null;
            if (updatedSolicitud.getEstado() != null) {
                nombreEstado = updatedSolicitud.getEstado().getNombre();
            } else if (updatedSolicitud.getCodigoEstado() != null) {
                // Si el estado no está cargado, obtenerlo por código
                Estado estado = estadoService.findByCodigoEstado(updatedSolicitud.getCodigoEstado()).orElse(null);
                if (estado != null) {
                    nombreEstado = estado.getNombre();
                    updatedSolicitud.setEstado(estado);
                }
            }

            if (nombreEstado != null && 
                (nombreEstado.equalsIgnoreCase("En Revisión") || nombreEstado.equalsIgnoreCase("Pendiente"))) {

                String datos = updatedSolicitud.getTitulo() + "|" + updatedSolicitud.getUserCreador().getNombres() + " "
                        + updatedSolicitud.getUserCreador().getApellidos() + "|"
                        + updatedSolicitud.getUserCreador().getCorreoInstitucional() + "|"
                        + updatedSolicitud.getObservaciones();

                // Enviar el correo institucional (envuelto en try-catch para no bloquear)
                try {
                    if (updatedSolicitud.getAdminRevisor() != null) {
                        emailService.sendNotificationSolicitudProyectoEmail(
                                updatedSolicitud.getAdminRevisor().getCorreoInstitucional(),
                                nombreEstado,
                                datos);
                    }
                } catch (Exception emailEx) {
                    System.err.println("Error al enviar correo de notificación: " + emailEx.getMessage());
                }
            }

            return ResponseEntity.ok(updatedSolicitud);

        } catch (IllegalArgumentException e) {
            response.put("mensaje", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Elimina una solicitud de proyecto.
     * Solo se permite eliminar solicitudes en estado PENDIENTE (PEND) u OBSERVACIÓN (OBS).
     * 
     * @param id ID de la solicitud a eliminar
     * @return ResponseEntity vacío si se eliminó correctamente
     */
    @DeleteMapping("/{id}")
    @Secured({ "ADMIN", "COORD", "SUP", "EMP", "ESTUD" })
    public ResponseEntity<?> deleteSolicitud(@PathVariable Long id) {
        var solicitudOpt = solicitudProyectoService.findById(id);
        if (!solicitudOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        SolicitudProyecto solicitud = solicitudOpt.get();
        String codigoEstado = solicitud.getCodigoEstado();
        
        // Solo permitir eliminar en estado PEND u OBS
        if (codigoEstado == null || (!codigoEstado.equals("PEND") && !codigoEstado.equals("OBS"))) {
            return ResponseEntity.badRequest()
                .body(java.util.Map.of("message", "Solo se pueden eliminar solicitudes en estado Pendiente u Observación"));
        }
        
        solicitudProyectoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Aprueba una solicitud de proyecto y crea automáticamente el proyecto correspondiente.
     * Este endpoint realiza dos operaciones en una transacción:
     * 1. Cambia el estado de la solicitud a APROBADO
     * 2. Crea un nuevo proyecto con los datos de la solicitud
     * 
     * @param idSolicitud ID de la solicitud a aprobar
     * @param dto DTO con observaciones opcionales y estado inicial del proyecto
     * @return La solicitud aprobada junto con el proyecto creado
     */
    @PostMapping("/{idSolicitud}/aprobar")
    @Secured({ "ADMIN", "COORD", "SUP" })
    public ResponseEntity<?> aprobarSolicitudYCrearProyecto(
            @PathVariable Long idSolicitud,
            @RequestBody(required = false) AprobacionSolicitudDTO dto) {

        Map<String, Object> response = new HashMap<>();

        try {
            // Obtener el ID del usuario autenticado
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Long idAdmin = Long.parseLong(authentication.getName());

            String observaciones = dto != null ? dto.getObservaciones() : null;
            String codigoEstadoProyecto = dto != null && dto.getCodigoEstadoProyecto() != null 
                    ? dto.getCodigoEstadoProyecto() 
                    : "DIS";

            AprobacionSolicitudResponse resultado = solicitudProyectoService.aprobarYCrearProyecto(
                    idSolicitud, idAdmin, observaciones, codigoEstadoProyecto);

            // Intentar enviar notificación por email (no bloquear si falla)
            try {
                SolicitudProyecto solicitudAprobada = resultado.getSolicitud();
                
                // Notificar al creador de la solicitud
                emailService.sendNotificationSolicitudProyectoEmail(
                        solicitudAprobada.getUserCreador().getCorreoInstitucional(),
                        "Aprobado",
                        "Su solicitud de proyecto ha sido aprobada y el proyecto ha sido creado. " +
                        (observaciones != null ? "Observaciones: " + observaciones : ""));

                // Notificar al correo personal si existe y es diferente
                if (solicitudAprobada.getUserCreador().getCorreoPersonal() != null
                        && !solicitudAprobada.getUserCreador().getCorreoInstitucional()
                                .equals(solicitudAprobada.getUserCreador().getCorreoPersonal())) {
                    emailService.sendNotificationSolicitudProyectoEmail(
                            solicitudAprobada.getUserCreador().getCorreoPersonal(),
                            "Aprobado",
                            "Su solicitud de proyecto ha sido aprobada y el proyecto ha sido creado.");
                }

                // Notificar a la empresa si el correo es diferente
                if (solicitudAprobada.getEmpresa() != null 
                        && solicitudAprobada.getEmpresa().getContactoEmail() != null
                        && !solicitudAprobada.getEmpresa().getContactoEmail()
                                .equals(solicitudAprobada.getUserCreador().getCorreoInstitucional())
                        && !solicitudAprobada.getEmpresa().getContactoEmail()
                                .equals(solicitudAprobada.getUserCreador().getCorreoPersonal())) {
                    emailService.sendNotificationSolicitudProyectoEmail(
                            solicitudAprobada.getEmpresa().getContactoEmail(),
                            "Aprobado",
                            "La solicitud de proyecto ha sido aprobada y el proyecto ha sido creado.");
                }
            } catch (Exception emailException) {
                // Loggear el error pero no fallar la operación principal
                System.err.println("Error al enviar notificación por email: " + emailException.getMessage());
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(resultado);

        } catch (IllegalArgumentException e) {
            response.put("mensaje", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (IllegalStateException e) {
            response.put("mensaje", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (jakarta.validation.ConstraintViolationException e) {
            String errorMessage = e.getConstraintViolations().stream()
                    .map(violation -> violation.getMessage())
                    .collect(java.util.stream.Collectors.joining(". "));
            response.put("mensaje", errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (jakarta.persistence.RollbackException e) {
            // Extraer el mensaje de validación de la causa si existe
            Throwable cause = e.getCause();
            if (cause instanceof jakarta.validation.ConstraintViolationException) {
                jakarta.validation.ConstraintViolationException cve = (jakarta.validation.ConstraintViolationException) cause;
                String errorMessage = cve.getConstraintViolations().stream()
                        .map(violation -> violation.getMessage())
                        .collect(java.util.stream.Collectors.joining(". "));
                response.put("mensaje", errorMessage);
            } else {
                response.put("mensaje", "Error de validación al crear el proyecto");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("mensaje", "Error al aprobar la solicitud: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Verifica si ya existe un proyecto creado a partir de una solicitud específica.
     */
    @GetMapping("/{idSolicitud}/tiene-proyecto")
    @Secured({ "ADMIN", "COORD", "SUP" })
    public ResponseEntity<Map<String, Object>> verificarProyectoExistente(@PathVariable Long idSolicitud) {
        Map<String, Object> response = new HashMap<>();
        boolean existe = solicitudProyectoService.existeProyectoParaSolicitud(idSolicitud);
        response.put("existeProyecto", existe);
        response.put("idSolicitud", idSolicitud);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/report-estado")
    @Secured({ "ADMIN", "COORD", "SUP" })
    public ResponseEntity<byte[]> solicitudesProyectoByEstadosGenerarReportePDF(
            @RequestParam("codEstado") String codigoEstado) {

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
                .filename("Reporte de solicitudes de proyecto por estado " + safeNombreEstado + ".pdf")
                .build());

        return new ResponseEntity<>(pdfReport, headers, HttpStatus.OK);
    }

    @GetMapping("/report-carrera")
    @Secured({ "ADMIN", "COORD", "SUP" })
    public ResponseEntity<byte[]> solicitudesProyectoByCarrerasGenerarReportePDF(
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
                .filename("Reporte de solicitudes de proyecto por carrera " + safeNombreCarrera + ".pdf")
                .build());

        return new ResponseEntity<>(pdfReport, headers, HttpStatus.OK);
    }

    @GetMapping("/report-empresa")
    @Secured({ "ADMIN", "COORD", "SUP" })
    public ResponseEntity<byte[]> solicitudesProyectoByEmpresasGenerarReportePDF(
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
                .filename("Reporte de solicitudes de proyecto por empresa " + safeNombreEmpresa + ".pdf")
                .build());

        return new ResponseEntity<>(pdfReport, headers, HttpStatus.OK);
    }

    @GetMapping("/report-depto-carrera")
    @Secured({ "ADMIN", "COORD", "SUP" })
    public ResponseEntity<byte[]> solicitudesProyectoByDeptosCarreraGenerarReportePDF(
            @RequestParam("idDeptoCarrera") Long idDeptoCarrera,
            @RequestParam(value = "codCarrera", required = false) String codigoCarrera) {

        if (idDeptoCarrera == null || idDeptoCarrera <= 0) {
            return ResponseEntity.badRequest()
                    .body("El id de departamento de carrera es requerido.".getBytes());
        }

        Optional<DepartamentoCarrera> deptoCarrera = deptoCarreraService.findById(idDeptoCarrera);
        if (!deptoCarrera.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(("Departamento Carrera con id " + idDeptoCarrera + " no encontrada.").getBytes());
        }
        String nombreDeptoCarrera = deptoCarrera.get().getNombre();

        String nombreCarrera = null;
        if (codigoCarrera != null && !codigoCarrera.trim().isEmpty()) {
            Optional<Carrera> carrera = carreraService.findByCodigo(codigoCarrera);
            if (!carrera.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(("Carrera con código " + codigoCarrera + " no encontrada.").getBytes());
            }
            nombreCarrera = carrera.get().getNombre();
            if (nombreCarrera == null || nombreCarrera.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(("La carrera con código " + codigoCarrera + " no tiene nombre válido.").getBytes());
            }
        }

        byte[] pdfReport = solicitudProyectoService.generarReportePorDeptoCarreraYCarrera(
                idDeptoCarrera,
                nombreDeptoCarrera,
                codigoCarrera,
                nombreCarrera);

        if (pdfReport == null || pdfReport.length == 0) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(("No se pudo generar el reporte para el departamento de carrera " + nombreDeptoCarrera)
                            .getBytes());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);

        String fileName = "Reporte de solicitudes de proyecto por departamento " + nombreDeptoCarrera;
        if (nombreCarrera != null) {
            fileName += " - carrera " + nombreCarrera;
        }
        String safeFileName = fileName.replaceAll("[^a-zA-Z0-9\\-_]", "_") + ".pdf";

        headers.setContentDisposition(ContentDisposition
                .builder("attachment")
                .filename(safeFileName)
                .build());

        return new ResponseEntity<>(pdfReport, headers, HttpStatus.OK);
    }
}