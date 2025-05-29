package com.ues.edu.sv.rpups_ues.controller;

import com.ues.edu.sv.rpups_ues.model.entity.Proyecto;
import com.ues.edu.sv.rpups_ues.model.entity.Estado;
import com.ues.edu.sv.rpups_ues.model.entity.Carrera;
import com.ues.edu.sv.rpups_ues.model.entity.Empresa;
import com.ues.edu.sv.rpups_ues.service.CarreraService;
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
    private final EmpresaService empresaService;

    public ProyectoController(ProyectoService proyectoService, EstadoService estadoService,
            CarreraService carreraService, EmpresaService empresaService) {
        this.proyectoService = proyectoService;
        this.estadoService = estadoService;
        this.carreraService = carreraService;
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
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size) {

        if (filter != null && filter.trim().matches("^[\\W_]+$")) {
            return ResponseEntity.ok(Page.empty(PageRequest.of(page, size)));
        }
        return new ResponseEntity<>(proyectoService.findProyectoByFiltros(filter, PageRequest.of(page, size)),
                HttpStatus.OK);
    }

    @GetMapping("/disp-search-filters")
    @PermitAll
    public ResponseEntity<Page<Proyecto>> getProyectosByFiltrosWithEstadoDisponible(
            @RequestParam(name = "filter", defaultValue = "", required = false) String filter,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size) {

        if (filter != null && filter.trim().matches("^[\\W_]+$")) {
            return ResponseEntity.ok(Page.empty(PageRequest.of(page, size)));
        }
        return new ResponseEntity<>(
                proyectoService.findProyectoByFiltrosWithEstadoDisponible(filter, PageRequest.of(page, size)),
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
    @PermitAll
    public ResponseEntity<byte[]> proyectosByEstadosGenerarReportePDF(@RequestParam("codEstado") String codigoEstado) {

        Optional<Estado> estado = estadoService.findByCodigoEstado(codigoEstado);
        if (!estado.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(("Estado con código " + codigoEstado + " no encontrado.").getBytes());
        }
        String nombreEstado = estado.get().getNombre();

        byte[] pdfReport = proyectoService.generarReportePorEstado(codigoEstado, nombreEstado);

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
    @PermitAll
    public ResponseEntity<byte[]> proyectosByCarrerasGenerarReportePDF(
            @RequestParam("codCarrera") String codigoCarrera) {

        Optional<Carrera> carrera = carreraService.findByCodigo(codigoCarrera);
        if (!carrera.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(("Carrera con código " + codigoCarrera + " no encontrada.").getBytes());
        }
        String nombreCarrera = carrera.get().getNombre();

        byte[] pdfReport = proyectoService.generarReportePorCarrera(codigoCarrera, nombreCarrera);

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
    @PermitAll
    public ResponseEntity<byte[]> proyectosByEmpresasGenerarReportePDF(
            @RequestParam("idEmpresa") Long idEmpresa) {

        Optional<Empresa> empresa = empresaService.findById(idEmpresa);
        if (!empresa.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(("Empresa con ID " + idEmpresa + " no encontrada.").getBytes());
        }
        String nombreEmpresa = empresa.get().getNombreComercial() + " (" + empresa.get().getNombreLegal() + ")";

        byte[] pdfReport = proyectoService.generarReportePorEmpresa(idEmpresa, nombreEmpresa);

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