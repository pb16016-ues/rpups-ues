package com.ues.edu.sv.rpups_ues.controller;

import com.ues.edu.sv.rpups_ues.model.entity.Departamento;
import com.ues.edu.sv.rpups_ues.service.DepartamentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/departamentos")
public class DepartamentoController {

    private final DepartamentoService departamentoService;

    public DepartamentoController(DepartamentoService departamentoService) {
        this.departamentoService = departamentoService;
    }

    @GetMapping
    public ResponseEntity<List<Departamento>> getAllDepartamentos() {
        List<Departamento> departamentos = departamentoService.findAll();
        return ResponseEntity.ok(departamentos);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Departamento> getDepartamentoByCodigo(@PathVariable String codigo) {
        Optional<Departamento> departamento = departamentoService.findByCodigo(codigo);
        return departamento.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Departamento> getDepartamentoByNombre(@PathVariable String nombre) {
        Optional<Departamento> departamento = departamentoService.findByNombre(nombre);
        return departamento.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Departamento> createDepartamento(@RequestBody Departamento departamento) {
        if (departamentoService.existsByNombre(departamento.getNombre())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Departamento savedDepartamento = departamentoService.save(departamento);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDepartamento);
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<Departamento> updateDepartamento(@PathVariable String codigo,
            @RequestBody Departamento departamento) {
        if (!departamentoService.findByCodigo(codigo).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        departamento.setCodigo(codigo);
        Departamento updatedDepartamento = departamentoService.save(departamento);
        return ResponseEntity.ok(updatedDepartamento);
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> deleteDepartamento(@PathVariable String codigo) {
        if (!departamentoService.findByCodigo(codigo).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        departamentoService.deleteByCodigo(codigo);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists/{nombre}")
    public ResponseEntity<Boolean> existsByNombre(@PathVariable String nombre) {
        boolean exists = departamentoService.existsByNombre(nombre);
        return ResponseEntity.ok(exists);
    }
}