package com.ues.edu.sv.rpups_ues.controller;

import com.ues.edu.sv.rpups_ues.model.entity.Empresa;
import com.ues.edu.sv.rpups_ues.service.EmpresaService;

import jakarta.annotation.security.PermitAll;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    @Secured({ "EMP", "ADMIN", "COORD", "SUP","ESTUD" })
    public ResponseEntity<List<Empresa>> getAllEmpresas() {
        List<Empresa> empresas = empresaService.findAll();
        return ResponseEntity.ok(empresas);
    }

    @GetMapping("/paginated")
    @Secured({ "ADMIN", "COORD", "SUP","ESTUD" })
    public ResponseEntity<Page<Empresa>> getEmpresasPaginated(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "filter", defaultValue = "") String filter) {
        Page<Empresa> empresas = empresaService.findByFilter(filter, PageRequest.of(page, size));
        return ResponseEntity.ok(empresas);
    }

    @GetMapping("/paginated/activas")
    @Secured({ "ADMIN", "COORD", "SUP","ESTUD" })
    public ResponseEntity<Page<Empresa>> getEmpresasActivasPaginated(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "filter", defaultValue = "") String filter) {
        Page<Empresa> empresas = empresaService.findByFilterAndEstadoActivo(filter, true, PageRequest.of(page, size));
        return ResponseEntity.ok(empresas);
    }

    @GetMapping("/{id}")
    @Secured({ "EMP", "ADMIN", "COORD", "SUP" })
    public ResponseEntity<Empresa> getEmpresaById(@PathVariable Long id) {
        Optional<Empresa> empresa = empresaService.findById(id);
        return empresa.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user-creador/{idUsuario}")
    @Secured({ "EMP", "ADMIN", "COORD", "SUP","ESTUD" })
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
    @Secured({ "EMP", "ADMIN", "COORD", "SUP" })
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