package com.ues.edu.sv.rpups_ues.controller;

import com.ues.edu.sv.rpups_ues.model.entity.Carrera;
import com.ues.edu.sv.rpups_ues.service.CarreraService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/carreras")
public class CarreraController {

    private final CarreraService carreraService;

    public CarreraController(CarreraService carreraService) {
        this.carreraService = carreraService;
    }

    @GetMapping
    public ResponseEntity<List<Carrera>> getAllCarreras() {
        List<Carrera> carreras = carreraService.findAll();
        return ResponseEntity.ok(carreras);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Carrera> getCarreraByCodigo(@PathVariable String codigo) {
        Optional<Carrera> carrera = carreraService.findByCodigo(codigo);
        return carrera.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Carrera> getCarreraByNombre(@PathVariable String nombre) {
        Optional<Carrera> carrera = carreraService.findByNombre(nombre);
        return carrera.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Carrera> createCarrera(@RequestBody Carrera carrera) {
        if (carreraService.existsByNombre(carrera.getNombre())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Carrera savedCarrera = carreraService.save(carrera);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCarrera);
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<Carrera> updateCarrera(@PathVariable String codigo, @RequestBody Carrera carrera) {
        if (!carreraService.findByCodigo(codigo).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        carrera.setCodigo(codigo);
        Carrera updatedCarrera = carreraService.save(carrera);
        return ResponseEntity.ok(updatedCarrera);
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> deleteCarrera(@PathVariable String codigo) {
        if (!carreraService.findByCodigo(codigo).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        carreraService.deleteByCodigo(codigo);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists/{nombre}")
    public ResponseEntity<Boolean> existsByNombre(@PathVariable String nombre) {
        boolean exists = carreraService.existsByNombre(nombre);
        return ResponseEntity.ok(exists);
    }
}