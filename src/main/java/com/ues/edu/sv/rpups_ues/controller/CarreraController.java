package com.ues.edu.sv.rpups_ues.controller;

import com.ues.edu.sv.rpups_ues.model.entity.Carrera;
import com.ues.edu.sv.rpups_ues.service.CarreraService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.security.PermitAll;

import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/carreras")
public class CarreraController {

    private final CarreraService carreraService;

    public CarreraController(CarreraService carreraService) {
        this.carreraService = carreraService;
    }

    @GetMapping
    @PermitAll
    public ResponseEntity<List<Carrera>> getAllCarreras() {
        List<Carrera> carreras = carreraService.findAll();
        return ResponseEntity.ok(carreras);
    }

    @GetMapping("/{codigo}")
    @PermitAll
    public ResponseEntity<Carrera> getCarreraByCodigo(@PathVariable String codigo) {
        Optional<Carrera> carrera = carreraService.findByCodigo(codigo);
        return carrera.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/nombre/{nombre}")
    @PermitAll
    public ResponseEntity<Carrera> getCarreraByNombre(@PathVariable String nombre) {
        Optional<Carrera> carrera = carreraService.findByNombre(nombre);
        return carrera.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Secured({ "ADMIN", "COOR", "SUP" })
    public ResponseEntity<Carrera> createCarrera(@RequestBody Carrera carrera) {
        if (carreraService.existsByNombre(carrera.getNombre())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Carrera savedCarrera = carreraService.save(carrera);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCarrera);
    }

    @PutMapping("/{codigo}")
    @Secured({ "ADMIN", "COOR", "SUP" })
    public ResponseEntity<Carrera> updateCarrera(@PathVariable String codigo, @RequestBody Carrera carrera) {
        if (!carreraService.findByCodigo(codigo).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        carrera.setCodigo(codigo);
        Carrera updatedCarrera = carreraService.save(carrera);
        return ResponseEntity.ok(updatedCarrera);
    }

    @DeleteMapping("/{codigo}")
    @Secured({ "ADMIN", "COOR", "SUP" })
    public ResponseEntity<Void> deleteCarrera(@PathVariable String codigo) {
        if (!carreraService.findByCodigo(codigo).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        carreraService.deleteByCodigo(codigo);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists/{nombre}")
    @PermitAll
    public ResponseEntity<Boolean> existsByNombre(@PathVariable String nombre) {
        boolean exists = carreraService.existsByNombre(nombre);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/by-depto/{idDeptoCarrera}")
    @PermitAll
    public ResponseEntity<List<Carrera>> getCarrerasByDepto(@PathVariable Long idDeptoCarrera) {
        return ResponseEntity.status(HttpStatus.OK).body(carreraService.findAllByDepartamento(idDeptoCarrera));
    }
    
}