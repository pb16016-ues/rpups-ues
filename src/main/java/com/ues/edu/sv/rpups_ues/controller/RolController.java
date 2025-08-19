package com.ues.edu.sv.rpups_ues.controller;

import com.ues.edu.sv.rpups_ues.model.entity.Rol;
import com.ues.edu.sv.rpups_ues.service.RolService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.annotation.Secured;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
public class RolController {

    private final RolService rolService;

    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    @GetMapping
    @Secured({ "ADMIN", "COORD" })
    public ResponseEntity<List<Rol>> getAllRoles() {
        List<Rol> roles = rolService.findAll();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{codigo}")
    @Secured({ "ADMIN", "COORD" })
    public ResponseEntity<Rol> getRolByCodigo(@PathVariable String codigo) {
        Optional<Rol> rol = rolService.findByCodigo(codigo);
        return rol.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/nombre/{nombre}")
    @Secured({ "ADMIN", "COORD" })
    public ResponseEntity<Rol> getRolByNombre(@PathVariable String nombre) {
        Optional<Rol> rol = rolService.findByNombre(nombre);
        return rol.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Secured({ "ADMIN" })
    public ResponseEntity<Rol> createRol(@RequestBody Rol rol) {
        if (rolService.existsByNombre(rol.getNombre())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Rol savedRol = rolService.save(rol);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRol);
    }

    @PutMapping("/{codigo}")
    @Secured({ "ADMIN" })
    public ResponseEntity<Rol> updateRol(@PathVariable String codigo, @RequestBody Rol rol) {
        if (!rolService.findByCodigo(codigo).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        rol.setCodigo(codigo);
        Rol updatedRol = rolService.save(rol);
        return ResponseEntity.ok(updatedRol);
    }

    @DeleteMapping("/{codigo}")
    @Secured({ "ADMIN" })
    public ResponseEntity<Void> deleteRol(@PathVariable String codigo) {
        if (!rolService.findByCodigo(codigo).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        rolService.deleteByCodigo(codigo);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists/{nombre}")
    @Secured({ "ADMIN", "COORD" })
    public ResponseEntity<Boolean> existsByNombre(@PathVariable String nombre) {
        boolean exists = rolService.existsByNombre(nombre);
        return ResponseEntity.ok(exists);
    }
}