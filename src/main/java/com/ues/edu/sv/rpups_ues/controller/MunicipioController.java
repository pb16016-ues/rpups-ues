package com.ues.edu.sv.rpups_ues.controller;

import com.ues.edu.sv.rpups_ues.model.entity.Municipio;
import com.ues.edu.sv.rpups_ues.service.MunicipioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.annotation.Secured;
import jakarta.annotation.security.PermitAll;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/municipios")
public class MunicipioController {

    private final MunicipioService municipioService;

    public MunicipioController(MunicipioService municipioService) {
        this.municipioService = municipioService;
    }

    @GetMapping
    @PermitAll
    public ResponseEntity<List<Municipio>> getAllMunicipios() {
        List<Municipio> municipios = municipioService.findAll();
        return ResponseEntity.ok(municipios);
    }

    @GetMapping("/{codigo}")
    @PermitAll
    public ResponseEntity<Municipio> getMunicipioByCodigo(@PathVariable String codigo) {
        Optional<Municipio> municipio = municipioService.findByCodigo(codigo);
        return municipio.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/departamento/{codigoDepartamento}")
    @PermitAll
    public ResponseEntity<List<Municipio>> getMunicipiosByCodigoDepartamento(@PathVariable String codigoDepartamento) {
        List<Municipio> municipios = municipioService.findByCodigoDepartamento(codigoDepartamento);
        return ResponseEntity.ok(municipios);
    }

    @GetMapping("/nombre/{nombre}")
    @PermitAll
    public ResponseEntity<Municipio> getMunicipioByNombre(@PathVariable String nombre) {
        Optional<Municipio> municipio = municipioService.findByNombre(nombre);
        return municipio.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Secured({ "ADMIN", "COOR", "SUP" })
    public ResponseEntity<Municipio> createMunicipio(@RequestBody Municipio municipio) {
        if (municipioService.existsByNombre(municipio.getNombre())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Municipio savedMunicipio = municipioService.save(municipio);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMunicipio);
    }

    @PutMapping("/{codigo}")
    @Secured({ "ADMIN", "COOR", "SUP" })
    public ResponseEntity<Municipio> updateMunicipio(@PathVariable String codigo, @RequestBody Municipio municipio) {
        if (!municipioService.findByCodigo(codigo).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        municipio.setCodigo(codigo);
        Municipio updatedMunicipio = municipioService.save(municipio);
        return ResponseEntity.ok(updatedMunicipio);
    }

    @DeleteMapping("/{codigo}")
    @Secured({ "ADMIN", "COOR", "SUP" })
    public ResponseEntity<Void> deleteMunicipio(@PathVariable String codigo) {
        if (!municipioService.findByCodigo(codigo).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        municipioService.deleteByCodigo(codigo);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists/{nombre}")
    @PermitAll
    public ResponseEntity<Boolean> existsByNombre(@PathVariable String nombre) {
        boolean exists = municipioService.existsByNombre(nombre);
        return ResponseEntity.ok(exists);
    }
}