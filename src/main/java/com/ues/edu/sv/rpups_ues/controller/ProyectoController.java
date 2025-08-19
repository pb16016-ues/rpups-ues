package com.ues.edu.sv.rpups_ues.controller;

import com.ues.edu.sv.rpups_ues.model.entity.Proyecto;
import com.ues.edu.sv.rpups_ues.model.entity.Estado;
import com.ues.edu.sv.rpups_ues.model.entity.Carrera;
import com.ues.edu.sv.rpups_ues.model.entity.DepartamentoCarrera;
import com.ues.edu.sv.rpups_ues.model.entity.Empresa;
import com.ues.edu.sv.rpups_ues.service.CarreraService;
import com.ues.edu.sv.rpups_ues.service.DepartamentoCarreraService;
import com.ues.edu.sv.rpups_ues.service.EstadoService;
import com.ues.edu.sv.rpups_ues.service.ProyectoService;
import com.ues.edu.sv.rpups_ues.service.EmpresaService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ContentDisposition;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.annotation.Secured;
import jakarta.annotation.security.PermitAll;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/proyectos")
public class ProyectoController {

    private final ProyectoService proyectoService;
    private final EstadoService estadoService;
    private final CarreraService carreraService;
    private final DepartamentoCarreraService deptoCarreraService;
    private final EmpresaService empresaService;

    public ProyectoController(ProyectoService proyectoService, EstadoService estadoService,
            CarreraService carreraService, DepartamentoCarreraService deptoCarreraService,
            EmpresaService empresaService) {
        this.proyectoService = proyectoService;
        this.estadoService = estadoService;
        this.carreraService = carreraService;
        this.deptoCarreraService = deptoCarreraService;
        this.empresaService = empresaService;
    }

    @GetMapping
    @PermitAll
    public ResponseEntity<Page<Proyecto>> getAllProyectos(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size) {
        return new ResponseEntity<>(proyectoService.findAll(PageRequest.of(page, size)),
                HttpStatus.OK);
    }

    @GetMapping("/public")
    @PermitAll
    public ResponseEntity<Page<Proyecto>> getAllProyectosPublic(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size) {
        return new ResponseEntity<>(proyectoService.findAll(PageRequest.of(page, size)),
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PermitAll
    public ResponseEntity<Proyecto> getProyectoById(@PathVariable Long id) {
        Optional<Proyecto> proyecto = proyectoService.findById(id);
        return proyecto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/titulo/{titulo}")
    @PermitAll
    public ResponseEntity<List<Proyecto>> getProyectosByTitulo(@PathVariable String titulo) {
        List<Proyecto> proyectos = proyectoService.findByTitulo(titulo);
        return ResponseEntity.ok(proyectos);
    }

    @GetMapping("/estado/{codigoEstado}")
    @PermitAll
    public ResponseEntity<List<Proyecto>> getProyectosByEstado(@PathVariable String codigoEstado) {
        List<Proyecto> proyectos = proyectoService.findByEstado(codigoEstado);
        return ResponseEntity.ok(proyectos);
    }

    @GetMapping("/empresa/{idEmpresa}")
    @PermitAll
    public ResponseEntity<Page<Proyecto>> getProyectosByEmpresa(@PathVariable Long idEmpresa,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size) {
        Page<Proyecto> proyectos = proyectoService.findByEmpresa(idEmpresa, PageRequest.of(page, size));
        return ResponseEntity.ok(proyectos);
    }

    @GetMapping("/carrera/{codigoCarrera}")
    @PermitAll
    public ResponseEntity<List<Proyecto>> getProyectosByCarrera(@PathVariable String codigoCarrera) {
        List<Proyecto> proyectos = proyectoService.findByCarrera(codigoCarrera);
        return ResponseEntity.ok(proyectos);
    }

    @GetMapping("/modalidad/{codigoModalidad}")
    @PermitAll
    public ResponseEntity<List<Proyecto>> getProyectosByModalidad(@PathVariable String codigoModalidad) {
        List<Proyecto> proyectos = proyectoService.findByModalidad(codigoModalidad);
        return ResponseEntity.ok(proyectos);
    }

    @GetMapping("/admin-aprobador/{idUsuario}")
    @PermitAll
    public ResponseEntity<List<Proyecto>> getProyectosByAdministradorAprobador(@PathVariable Long idUsuario) {
        List<Proyecto> proyectos = proyectoService.findByAdministradorAprobador(idUsuario);
        return ResponseEntity.ok(proyectos);
    }

    @GetMapping("/empresa/{idEmpresa}/estado/{codigoEstado}")
    @PermitAll
    public ResponseEntity<List<Proyecto>> getProyectosByEmpresaAndEstado(
            @PathVariable Long idEmpresa, @PathVariable String codigoEstado) {
        List<Proyecto> proyectos = proyectoService.findByEmpresaIdEmpresaAndEstadoCodigoEstado(idEmpresa, codigoEstado);
        return ResponseEntity.ok(proyectos);
    }

    @GetMapping("/carrera/{codigoCarrera}/estado/{codigoEstado}")
    @PermitAll
    public ResponseEntity<List<Proyecto>> getProyectosByCarreraAndEstado(
            @PathVariable String codigoCarrera, @PathVariable String codigoEstado) {
        List<Proyecto> proyectos = proyectoService.findByCarreraCodigoAndEstadoCodigoEstado(codigoCarrera,
                codigoEstado);
        return ResponseEntity.ok(proyectos);
    }

    @GetMapping("/modalidad/{codigoModalidad}/estado/{codigoEstado}")
    @PermitAll
    public ResponseEntity<List<Proyecto>> getProyectosByModalidadAndEstado(
            @PathVariable String codigoModalidad, @PathVariable String codigoEstado) {
        List<Proyecto> proyectos = proyectoService.findByModalidadCodigoModalidadAndEstadoCodigoEstado(codigoModalidad,
                codigoEstado);
        return ResponseEntity.ok(proyectos);
    }

    @GetMapping("/search-filters")
    @Secured({ "ADMIN", "COOR", "SUP" })
    public ResponseEntity<Page<Proyecto>> getProyectosByFiltros(
            @RequestParam(name = "filter", defaultValue = "", required = false) String filter,
            @RequestParam(name = "idDepto", defaultValue = "", required = false) Long idDeptoCarrera,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size) {

        if (filter != null && filter.trim().matches("^[\\W_]+$")) {
            return ResponseEntity.ok(Page.empty(PageRequest.of(page, size)));
        }
        return new ResponseEntity<>(
                proyectoService.findProyectoByFiltros(filter, idDeptoCarrera, PageRequest.of(page, size)),
                HttpStatus.OK);
    }

    @GetMapping("/disp-search-filters")
    @PermitAll
    public ResponseEntity<Page<Proyecto>> getProyectosByFiltrosWithEstadoDisponible(
            @RequestParam(name = "filter", defaultValue = "", required = false) String filter,
            @RequestParam(name = "idDepto", defaultValue = "", required = false) Long idDeptoCarrera,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size) {

        if (filter != null && filter.trim().matches("^[\\W_]+$")) {
            return ResponseEntity.ok(Page.empty(PageRequest.of(page, size)));
        }
        return new ResponseEntity<>(
                proyectoService.findProyectoByFiltrosWithEstadoDisponible(filter, idDeptoCarrera,
                        PageRequest.of(page, size)),
                HttpStatus.OK);
    }

    @PostMapping
    @Secured({ "ADMIN", "COOR", "SUP" })
    public ResponseEntity<Proyecto> createProyecto(@RequestBody Proyecto proyecto) {
        Proyecto savedProyecto = proyectoService.save(proyecto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProyecto);
    }

    @PutMapping("/{id}")
    @Secured({ "ADMIN", "COOR", "SUP" })
    public ResponseEntity<Proyecto> updateProyecto(@PathVariable Long id, @RequestBody Proyecto proyecto) {
        if (!proyectoService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        proyecto.setIdProyecto(id);
        Proyecto updatedProyecto = proyectoService.save(proyecto);
        return ResponseEntity.ok(updatedProyecto);
    }

    @DeleteMapping("/{id}")
    @Secured({ "ADMIN", "COOR", "SUP" })
    public ResponseEntity<Void> deleteProyecto(@PathVariable Long id) {
        if (!proyectoService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        proyectoService.deleteById(id);
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

        byte[] pdfReport = proyectoService.generarReportePorEstado(codigoEstado, nombreEstado);

        if (pdfReport == null || pdfReport.length == 0) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(("No se pudo generar el reporte para el estado " + nombreEstado).getBytes());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition
                .builder("attachment")
                .filename("Reporte de proyectos por estado " + nombreEstado + ".pdf")
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

        byte[] pdfReport = proyectoService.generarReportePorCarrera(codigoCarrera, nombreCarrera);

        if (pdfReport == null || pdfReport.length == 0) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(("No se pudo generar el reporte para la carrera " + nombreCarrera).getBytes());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition
                .builder("attachment")
                .filename("Reporte de proyectos por carrera " + nombreCarrera + ".pdf")
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
        byte[] pdfReport = proyectoService.generarReportePorEmpresa(idEmpresa, nombreEmpresa);

        if (pdfReport == null || pdfReport.length == 0) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(("No se pudo generar el reporte para la empresa " + nombreEmpresa).getBytes());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.inline()
                .filename("Reporte de proyectos por empresa " + nombreEmpresa + ".pdf").build());

        return new ResponseEntity<>(pdfReport, headers, HttpStatus.OK);
    }

    @GetMapping("/report-depto-carrera")
    @Secured({ "ADMIN", "COOR", "SUP" })
    public ResponseEntity<byte[]> proyectosByDeptosCarreraGenerarReportePDF(
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

        byte[] pdfReport = proyectoService.generarReportePorDeptoCarreraYCarrera(
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