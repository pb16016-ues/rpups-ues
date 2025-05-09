package com.ues.edu.sv.rpups_ues.controller;

import com.ues.edu.sv.rpups_ues.model.entity.Rubro;
import com.ues.edu.sv.rpups_ues.service.RubroService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rubros")
public class RubroController {

    private final RubroService rubroService;

    public RubroController(RubroService rubroService) {
        this.rubroService = rubroService;
    }

    @GetMapping
    public ResponseEntity<List<Rubro>> getAllRubros() {
        List<Rubro> rubros = rubroService.findAll();
        return ResponseEntity.ok(rubros);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rubro> getRubroById(@PathVariable Long id) {
        Optional<Rubro> rubro = rubroService.findById(id);
        return rubro.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Rubro> getRubroByNombre(@PathVariable String nombre) {
        Optional<Rubro> rubro = rubroService.findByNombre(nombre);
        return rubro.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Rubro> createRubro(@RequestBody Rubro rubro) {
        if (rubroService.existsByNombre(rubro.getNombre())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Rubro savedRubro = rubroService.save(rubro);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRubro);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rubro> updateRubro(@PathVariable Long id, @RequestBody Rubro rubro) {
        if (!rubroService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        rubro.setIdRubro(id);
        Rubro updatedRubro = rubroService.save(rubro);
        return ResponseEntity.ok(updatedRubro);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRubro(@PathVariable Long id) {
        if (!rubroService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        rubroService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists/{nombre}")
    public ResponseEntity<Boolean> existsByNombre(@PathVariable String nombre) {
        boolean exists = rubroService.existsByNombre(nombre);
        return ResponseEntity.ok(exists);
    }
}