package com.ues.edu.sv.rpups_ues.controller;

import com.ues.edu.sv.rpups_ues.model.entity.Usuario;
import com.ues.edu.sv.rpups_ues.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> getAllUsuarios() {
        List<Usuario> usuarios = usuarioService.findAll();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.findById(id);
        return usuario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<Usuario> getUsuarioByUsername(@PathVariable String username) {
        Optional<Usuario> usuario = usuarioService.findByUsername(username);
        return usuario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/carnet/{carnet}")
    public ResponseEntity<Usuario> getUsuarioByCarnet(@PathVariable String carnet) {
        Optional<Usuario> usuario = usuarioService.findByCarnet(carnet);
        return usuario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/correo-institucional/{correoInstitucional}")
    public ResponseEntity<Usuario> getUsuarioByCorreoInstitucional(@PathVariable String correoInstitucional) {
        Optional<Usuario> usuario = usuarioService.findByCorreoInstitucional(correoInstitucional);
        return usuario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/correo-personal/{correoPersonal}")
    public ResponseEntity<Usuario> getUsuarioByCorreoPersonal(@PathVariable String correoPersonal) {
        Optional<Usuario> usuario = usuarioService.findByCorreoPersonal(correoPersonal);
        return usuario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar-user/{searchTerm}")
    public ResponseEntity<List<Usuario>> getUsuariosByNombresOrApellidos(@PathVariable String searchTerm) {
        List<Usuario> usuarios = usuarioService.findByNombresOrApellidos(searchTerm);
        return ResponseEntity.ok(usuarios);
    }

    @PostMapping
    public ResponseEntity<Usuario> createUsuario(@RequestBody Usuario usuario) {
        if (usuarioService.existsByCorreoInstitucional(usuario.getCorreoInstitucional()) ||
                (usuario.getCorreoPersonal() != null
                        && usuarioService.existsByCorreoPersonal(usuario.getCorreoPersonal()))
                ||
                usuarioService.existsByCarnet(usuario.getCarnet()) ||
                usuarioService.existsByUsername(usuario.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Usuario savedUsuario = usuarioService.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUsuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        if (!usuarioService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        usuario.setIdUsuario(id);
        Usuario updatedUsuario = usuarioService.save(usuario);
        return ResponseEntity.ok(updatedUsuario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        if (!usuarioService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        usuarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists/correo-institucional/{correoInstitucional}")
    public ResponseEntity<Boolean> existsByCorreoInstitucional(@PathVariable String correoInstitucional) {
        boolean exists = usuarioService.existsByCorreoInstitucional(correoInstitucional);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/exists/correo-personal/{correoPersonal}")
    public ResponseEntity<Boolean> existsByCorreoPersonal(@PathVariable String correoPersonal) {
        boolean exists = usuarioService.existsByCorreoPersonal(correoPersonal);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/exists/carnet/{carnet}")
    public ResponseEntity<Boolean> existsByCarnet(@PathVariable String carnet) {
        boolean exists = usuarioService.existsByCarnet(carnet);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/exists/username/{username}")
    public ResponseEntity<Boolean> existsByUsername(@PathVariable String username) {
        boolean exists = usuarioService.existsByUsername(username);
        return ResponseEntity.ok(exists);
    }
}