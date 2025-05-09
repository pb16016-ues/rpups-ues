package com.ues.edu.sv.rpups_ues.controller;

import com.ues.edu.sv.rpups_ues.model.entity.Estado;
import com.ues.edu.sv.rpups_ues.service.EstadoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/estados")
public class EstadoController {

    private final EstadoService estadoService;

    public EstadoController(EstadoService estadoService) {
        this.estadoService = estadoService;
    }

    @GetMapping
    public ResponseEntity<List<Estado>> getAllEstados() {
        List<Estado> estados = estadoService.findAll();
        return ResponseEntity.ok(estados);
    }

    @GetMapping("/{codigoEstado}")
    public ResponseEntity<Estado> getEstadoByCodigo(@PathVariable String codigoEstado) {
        Optional<Estado> estado = estadoService.findByCodigoEstado(codigoEstado);
        return estado.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Estado> getEstadoByNombre(@PathVariable String nombre) {
        Optional<Estado> estado = estadoService.findByNombre(nombre);
        return estado.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/exists/{nombre}")
    public ResponseEntity<Boolean> existsByNombre(@PathVariable String nombre) {
        boolean exists = estadoService.existsByNombre(nombre);
        return ResponseEntity.ok(exists);
    }

    @PostMapping
    public ResponseEntity<Estado> createEstado(@RequestBody Estado estado) {
        if (estadoService.existsByNombre(estado.getNombre())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Estado savedEstado = estadoService.save(estado);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEstado);
    }

    @PutMapping("/{codigoEstado}")
    public ResponseEntity<Estado> updateEstado(@PathVariable String codigoEstado, @RequestBody Estado estado) {
        if (!estadoService.findByCodigoEstado(codigoEstado).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        estado.setCodigoEstado(codigoEstado);
        Estado updatedEstado = estadoService.save(estado);
        return ResponseEntity.ok(updatedEstado);
    }

    @DeleteMapping("/{codigoEstado}")
    public ResponseEntity<Void> deleteEstado(@PathVariable String codigoEstado) {
        if (!estadoService.findByCodigoEstado(codigoEstado).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        estadoService.deleteByCodigoEstado(codigoEstado);
        return ResponseEntity.noContent().build();
    }
}