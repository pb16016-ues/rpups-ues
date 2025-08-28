package com.ues.edu.sv.rpups_ues.controller;

import com.ues.edu.sv.rpups_ues.model.entity.Empresa;
import com.ues.edu.sv.rpups_ues.service.EmpresaService;

import jakarta.annotation.security.PermitAll;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/empresas")
public class EmpresaController {

    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    @GetMapping
    @PermitAll
    public ResponseEntity<List<Empresa>> getAllEmpresas() {
        List<Empresa> empresas = empresaService.findAll();
        return ResponseEntity.ok(empresas);
    }

    @GetMapping("/{id}")
    @PermitAll
    public ResponseEntity<Empresa> getEmpresaById(@PathVariable Long id) {
        Optional<Empresa> empresa = empresaService.findById(id);
        return empresa.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/nombre-comercial/{nombreComercial}")
    @PermitAll
    public ResponseEntity<List<Empresa>> getEmpresasByNombreComercial(@PathVariable String nombreComercial) {
        List<Empresa> empresas = empresaService.findByNombreComercial(nombreComercial);
        return ResponseEntity.ok(empresas);
    }

    @GetMapping("/nombre-legal/{nombreLegal}")
    @PermitAll
    public ResponseEntity<List<Empresa>> getEmpresasByNombreLegal(@PathVariable String nombreLegal) {
        List<Empresa> empresas = empresaService.findByNombreLegal(nombreLegal);
        return ResponseEntity.ok(empresas);
    }

    @GetMapping("/rubro/{idRubro}")
    @PermitAll
    public ResponseEntity<List<Empresa>> getEmpresasByRubro(@PathVariable Long idRubro) {
        List<Empresa> empresas = empresaService.findByRubroIdRubro(idRubro);
        return ResponseEntity.ok(empresas);
    }

    @GetMapping("/user-creador/{idUsuario}")
    @PermitAll
    public ResponseEntity<List<Empresa>> getEmpresasByUserCreador(@PathVariable Long idUsuario) {
        List<Empresa> empresas = empresaService.findByUserCreador(idUsuario);
        return ResponseEntity.ok(empresas);
    }

    @PostMapping
    @PermitAll
    public ResponseEntity<Empresa> createEmpresa(@RequestBody Empresa empresa) {
        Empresa savedEmpresa = empresaService.save(empresa);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEmpresa);
    }

    @PutMapping("/{id}")
    @PermitAll
    public ResponseEntity<Empresa> updateEmpresa(@PathVariable Long id, @RequestBody Empresa empresa) {
        if (!empresaService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        empresa.setIdEmpresa(id);
        Empresa updatedEmpresa = empresaService.save(empresa);
        return ResponseEntity.ok(updatedEmpresa);
    }

    @DeleteMapping("/{id}")
    @Secured({ "ADMIN", "COORD", "SUP" })
    public ResponseEntity<Void> deleteEmpresa(@PathVariable Long id) {
        if (!empresaService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        empresaService.desactiveById(id);
        return ResponseEntity.noContent().build();
    }
}