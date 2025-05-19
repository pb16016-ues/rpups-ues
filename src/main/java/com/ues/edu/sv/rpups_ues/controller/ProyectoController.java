package com.ues.edu.sv.rpups_ues.controller;

import com.ues.edu.sv.rpups_ues.model.entity.Proyecto;
import com.ues.edu.sv.rpups_ues.service.ProyectoService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/proyectos")
public class ProyectoController {

    private final ProyectoService proyectoService;

    public ProyectoController(ProyectoService proyectoService) {
        this.proyectoService = proyectoService;
    }

    @GetMapping
    public ResponseEntity<Page<Proyecto>> getAllProyectos(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size) {
        return new ResponseEntity<>(proyectoService.findAll(PageRequest.of(page, size)),
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proyecto> getProyectoById(@PathVariable Long id) {
        Optional<Proyecto> proyecto = proyectoService.findById(id);
        return proyecto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/titulo/{titulo}")
    public ResponseEntity<List<Proyecto>> getProyectosByTitulo(@PathVariable String titulo) {
        List<Proyecto> proyectos = proyectoService.findByTitulo(titulo);
        return ResponseEntity.ok(proyectos);
    }

    @GetMapping("/estado/{codigoEstado}")
    public ResponseEntity<List<Proyecto>> getProyectosByEstado(@PathVariable String codigoEstado) {
        List<Proyecto> proyectos = proyectoService.findByEstado(codigoEstado);
        return ResponseEntity.ok(proyectos);
    }

    @GetMapping("/empresa/{idEmpresa}")
    public ResponseEntity<List<Proyecto>> getProyectosByEmpresa(@PathVariable Long idEmpresa) {
        List<Proyecto> proyectos = proyectoService.findByEmpresa(idEmpresa);
        return ResponseEntity.ok(proyectos);
    }

    @GetMapping("/carrera/{codigoCarrera}")
    public ResponseEntity<List<Proyecto>> getProyectosByCarrera(@PathVariable String codigoCarrera) {
        List<Proyecto> proyectos = proyectoService.findByCarrera(codigoCarrera);
        return ResponseEntity.ok(proyectos);
    }

    @GetMapping("/modalidad/{codigoModalidad}")
    public ResponseEntity<List<Proyecto>> getProyectosByModalidad(@PathVariable String codigoModalidad) {
        List<Proyecto> proyectos = proyectoService.findByModalidad(codigoModalidad);
        return ResponseEntity.ok(proyectos);
    }

    @GetMapping("/admin-aprobador/{idUsuario}")
    public ResponseEntity<List<Proyecto>> getProyectosByAdministradorAprobador(@PathVariable Long idUsuario) {
        List<Proyecto> proyectos = proyectoService.findByAdministradorAprobador(idUsuario);
        return ResponseEntity.ok(proyectos);
    }

    @GetMapping("/empresa/{idEmpresa}/estado/{codigoEstado}")
    public ResponseEntity<List<Proyecto>> getProyectosByEmpresaAndEstado(
            @PathVariable Long idEmpresa, @PathVariable String codigoEstado) {
        List<Proyecto> proyectos = proyectoService.findByEmpresaIdEmpresaAndEstadoCodigoEstado(idEmpresa, codigoEstado);
        return ResponseEntity.ok(proyectos);
    }

    @GetMapping("/carrera/{codigoCarrera}/estado/{codigoEstado}")
    public ResponseEntity<List<Proyecto>> getProyectosByCarreraAndEstado(
            @PathVariable String codigoCarrera, @PathVariable String codigoEstado) {
        List<Proyecto> proyectos = proyectoService.findByCarreraCodigoAndEstadoCodigoEstado(codigoCarrera,
                codigoEstado);
        return ResponseEntity.ok(proyectos);
    }

    @GetMapping("/modalidad/{codigoModalidad}/estado/{codigoEstado}")
    public ResponseEntity<List<Proyecto>> getProyectosByModalidadAndEstado(
            @PathVariable String codigoModalidad, @PathVariable String codigoEstado) {
        List<Proyecto> proyectos = proyectoService.findByModalidadCodigoModalidadAndEstadoCodigoEstado(codigoModalidad,
                codigoEstado);
        return ResponseEntity.ok(proyectos);
    }

    @GetMapping("/search-filters")
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

    @PostMapping
    public ResponseEntity<Proyecto> createProyecto(@RequestBody Proyecto proyecto) {
        Proyecto savedProyecto = proyectoService.save(proyecto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProyecto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Proyecto> updateProyecto(@PathVariable Long id, @RequestBody Proyecto proyecto) {
        if (!proyectoService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        proyecto.setIdProyecto(id);
        Proyecto updatedProyecto = proyectoService.save(proyecto);
        return ResponseEntity.ok(updatedProyecto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProyecto(@PathVariable Long id) {
        if (!proyectoService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        proyectoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}