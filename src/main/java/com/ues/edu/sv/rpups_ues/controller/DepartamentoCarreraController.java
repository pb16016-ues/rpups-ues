package com.ues.edu.sv.rpups_ues.controller;

import com.ues.edu.sv.rpups_ues.model.entity.DepartamentoCarrera;
import com.ues.edu.sv.rpups_ues.service.DepartamentoCarreraService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.security.PermitAll;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/deptos-carreras")
public class DepartamentoCarreraController {

    private final DepartamentoCarreraService departamentoCarreraService;

    public DepartamentoCarreraController(DepartamentoCarreraService departamentoCarreraService) {
        this.departamentoCarreraService = departamentoCarreraService;
    }

    @GetMapping
    @PermitAll
    public ResponseEntity<List<DepartamentoCarrera>> getAllDepartamentosCarreras() {
        List<DepartamentoCarrera> departamentosCarreras = departamentoCarreraService.findAll();
        return ResponseEntity.ok(departamentosCarreras);
    }

    @GetMapping("/{idDepartamentoCarrera}")
    @PermitAll
    public ResponseEntity<DepartamentoCarrera> getDepartamentosCarrerasByCodigo(
            @PathVariable Long idDepartamentoCarrera) {
        Optional<DepartamentoCarrera> departamentoCarrera = departamentoCarreraService.findById(idDepartamentoCarrera);
        return departamentoCarrera.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/nombre/{nombre}")
    @PermitAll
    public ResponseEntity<DepartamentoCarrera> getDepartamentoCarreraByNombre(@PathVariable String nombre) {
        Optional<DepartamentoCarrera> departamentoCarrera = departamentoCarreraService.findByNombre(nombre);
        return departamentoCarrera.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Secured({ "ADMIN", "COOR", "SUP" })
    public ResponseEntity<DepartamentoCarrera> createDepartamentoCarrera(
            @RequestBody DepartamentoCarrera departamentoCarrera) {
        if (departamentoCarreraService.existsByNombre(departamentoCarrera.getNombre())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        DepartamentoCarrera savedDepartamentoCarrera = departamentoCarreraService.save(departamentoCarrera);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDepartamentoCarrera);
    }

    @PutMapping("/{idDepartamentoCarrera}")
    @Secured({ "ADMIN", "COOR", "SUP" })
    public ResponseEntity<DepartamentoCarrera> updateDepartamentoCarrera(@PathVariable Long idDepartamentoCarrera,
            @RequestBody DepartamentoCarrera departamentoCarrera) {
        if (!departamentoCarreraService.findById(idDepartamentoCarrera).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        departamentoCarrera.setIdDepartamentoCarrera(idDepartamentoCarrera);
        DepartamentoCarrera updatedDepartamentoCarrera = departamentoCarreraService.save(departamentoCarrera);
        return ResponseEntity.ok(updatedDepartamentoCarrera);
    }

    @DeleteMapping("/{idDepartamentoCarrera}")
    @Secured({ "ADMIN", "COOR", "SUP" })
    public ResponseEntity<Void> deleteCarrera(@PathVariable Long idDepartamentoCarrera) {
        if (!departamentoCarreraService.findById(idDepartamentoCarrera).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        departamentoCarreraService.deleteById(idDepartamentoCarrera);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists/{nombre}")
    @PermitAll
    public ResponseEntity<Boolean> existsByNombre(@PathVariable String nombre) {
        boolean exists = departamentoCarreraService.existsByNombre(nombre);
        return ResponseEntity.ok(exists);
    }
}