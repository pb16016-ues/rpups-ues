package com.ues.edu.sv.rpups_ues.controller;

import com.ues.edu.sv.rpups_ues.model.entity.Modalidad;
import com.ues.edu.sv.rpups_ues.service.ModalidadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.annotation.Secured;
import jakarta.annotation.security.PermitAll;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/modalidades")
public class ModalidadController {

    private final ModalidadService modalidadService;

    public ModalidadController(ModalidadService modalidadService) {
        this.modalidadService = modalidadService;
    }

    @GetMapping
    @PermitAll
    public ResponseEntity<List<Modalidad>> getAllModalidades() {
        List<Modalidad> modalidades = modalidadService.findAll();
        return ResponseEntity.ok(modalidades);
    }

    @GetMapping("/{codigoModalidad}")
    @PermitAll
    public ResponseEntity<Modalidad> getModalidadByCodigo(@PathVariable String codigoModalidad) {
        Optional<Modalidad> modalidad = modalidadService.findByCodigoModalidad(codigoModalidad);
        return modalidad.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/nombre/{nombre}")
    @PermitAll
    public ResponseEntity<Modalidad> getModalidadByNombre(@PathVariable String nombre) {
        Optional<Modalidad> modalidad = modalidadService.findByNombre(nombre);
        return modalidad.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Secured({ "ADMIN", "COOR", "SUP" })
    public ResponseEntity<Modalidad> createModalidad(@RequestBody Modalidad modalidad) {
        if (modalidadService.existsByNombre(modalidad.getNombre())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Modalidad savedModalidad = modalidadService.save(modalidad);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedModalidad);
    }

    @PutMapping("/{codigoModalidad}")
    @Secured({ "ADMIN", "COOR", "SUP" })
    public ResponseEntity<Modalidad> updateModalidad(@PathVariable String codigoModalidad,
            @RequestBody Modalidad modalidad) {
        if (!modalidadService.findByCodigoModalidad(codigoModalidad).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        modalidad.setCodigoModalidad(codigoModalidad);
        Modalidad updatedModalidad = modalidadService.save(modalidad);
        return ResponseEntity.ok(updatedModalidad);
    }

    @DeleteMapping("/{codigoModalidad}")
    @Secured({ "ADMIN", "COOR", "SUP" })
    public ResponseEntity<Void> deleteModalidad(@PathVariable String codigoModalidad) {
        if (!modalidadService.findByCodigoModalidad(codigoModalidad).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        modalidadService.deleteByCodigoModalidad(codigoModalidad);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists/{nombre}")
    @PermitAll
    public ResponseEntity<Boolean> existsByNombre(@PathVariable String nombre) {
        boolean exists = modalidadService.existsByNombre(nombre);
        return ResponseEntity.ok(exists);
    }
}