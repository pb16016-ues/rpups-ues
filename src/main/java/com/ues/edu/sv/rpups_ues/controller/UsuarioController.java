package com.ues.edu.sv.rpups_ues.controller;

import com.ues.edu.sv.rpups_ues.model.DTO.ChangePasswordDTO;
import com.ues.edu.sv.rpups_ues.model.DTO.UsuarioDTO;
import com.ues.edu.sv.rpups_ues.model.entity.Usuario;
import com.ues.edu.sv.rpups_ues.service.EmailService;
import com.ues.edu.sv.rpups_ues.service.UsuarioService;

import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final EmailService emailService;

    public UsuarioController(UsuarioService usuarioService, EmailService emailService) {
        this.usuarioService = usuarioService;
        this.emailService = emailService;
    }

    @GetMapping
    @Secured({ "ADMIN", "COOR", "SUP" })
    public ResponseEntity<Page<Usuario>> getAllUsuarios(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size) {
        return new ResponseEntity<>(usuarioService.findAll(PageRequest.of(page, size)),
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PermitAll
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.findById(id);
        return usuario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/username/{username}")
    // @Secured({ "ADMIN", "COOR", "SUP" })
    @PermitAll
    public ResponseEntity<Usuario> getUsuarioByUsername(@PathVariable String username) {
        Optional<Usuario> usuario = usuarioService.findByUsername(username);
        return usuario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/carnet/{carnet}")
    @Secured({ "ADMIN", "COOR", "SUP" })
    public ResponseEntity<Usuario> getUsuarioByCarnet(@PathVariable String carnet) {
        Optional<Usuario> usuario = usuarioService.findByCarnet(carnet);
        return usuario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/correo-institucional/{correoInstitucional}")
    @Secured({ "ADMIN", "COOR", "SUP" })
    public ResponseEntity<Usuario> getUsuarioByCorreoInstitucional(@PathVariable String correoInstitucional) {
        Optional<Usuario> usuario = usuarioService.findByCorreoInstitucional(correoInstitucional);
        return usuario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/correo-personal/{correoPersonal}")
    @Secured({ "ADMIN", "COOR", "SUP" })
    public ResponseEntity<Usuario> getUsuarioByCorreoPersonal(@PathVariable String correoPersonal) {
        Optional<Usuario> usuario = usuarioService.findByCorreoPersonal(correoPersonal);
        return usuario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/search-user/{searchTerm}")
    @Secured({ "ADMIN", "COOR", "SUP", "EMP", "EST" })
    public ResponseEntity<Page<Usuario>> getUsuariosByNombresOrApellidos(@PathVariable String searchTerm,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size) {
        return new ResponseEntity<>(usuarioService.findByNombresOrApellidos(searchTerm, PageRequest.of(page, size)),
                HttpStatus.OK);
    }

    @GetMapping("/search-filters")
    @Secured({ "ADMIN", "COOR", "SUP", "EMP", "EST" })
    public ResponseEntity<Page<Usuario>> getUsuariosByFiltros(
            @RequestParam(name = "filter", defaultValue = "", required = false) String filter,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size) {

        if (filter != null && filter.trim().matches("^[\\W_]+$")) {
            return ResponseEntity.ok(Page.empty(PageRequest.of(page, size)));
        }

        return new ResponseEntity<>(usuarioService.findUsuarioByFiltros(filter, PageRequest.of(page, size)),
                HttpStatus.OK);
    }

    // Crear usuario general
    @PostMapping("/register")
    @PermitAll
    public ResponseEntity<Usuario> createUsuario(@RequestBody Usuario usuario) {
        if (usuarioService.existsByCorreo(usuario.getCorreoInstitucional()) ||
                (usuario.getCorreoPersonal() != null
                        && usuarioService.existsByCorreo(usuario.getCorreoPersonal()))
                ||
                usuarioService.existsByCarnet(usuario.getCarnet()) ||
                usuarioService.existsByUsername(usuario.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Usuario savedUsuario = usuarioService.registerUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUsuario);
    }

    @PostMapping("/administrativo")
    @Secured({ "ADMIN" })
    public ResponseEntity<Usuario> createUsuarioAdministrativo(@RequestBody Usuario usuario,
            Boolean withPasswordDefault) {
        if (usuarioService.existsByCorreo(usuario.getCorreoInstitucional()) ||
                (usuario.getCorreoPersonal() != null
                        && usuarioService.existsByCorreo(usuario.getCorreoPersonal()))
                ||

                usuarioService.existsByUsername(usuario.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        String passwordTemporal = null;

        System.out.println("Creating administrative user: " + usuario);
        if (withPasswordDefault != null && withPasswordDefault) {
            passwordTemporal = emailService.generateTemporaryPassword();
            usuario.setPassword(passwordTemporal);
        }
        Usuario savedUsuario = usuarioService.registerAdministrativo(usuario);

        // Enviar el correo electrónico
        if (withPasswordDefault != null && withPasswordDefault) {
            emailService.sendPasswordInitialEmail(savedUsuario.getCorreoInstitucional(), passwordTemporal);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUsuario);
    }

    // Crear representante de empresa (ruta diferente)
    @PostMapping("/repres-empresa")
    @PermitAll
    public ResponseEntity<Usuario> createRepresentanteEmpresa(@RequestBody Usuario usuario) {
        if (usuarioService.existsByCorreo(usuario.getCorreoInstitucional()) ||
                (usuario.getCorreoPersonal() != null
                        && usuarioService.existsByCorreo(usuario.getCorreoPersonal()))
                ||
                usuarioService.existsByUsername(usuario.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Usuario savedUsuario = usuarioService.registerRepresentanteEmpresa(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUsuario);
    }

    @PutMapping("/change-password")
    @PermitAll
    ResponseEntity<Usuario> changePassword(
            @Valid @RequestBody ChangePasswordDTO changePasswordDTO, Principal principal) {
        return new ResponseEntity<>(usuarioService.changePassword(changePasswordDTO, principal), HttpStatus.OK);
    }

    @PutMapping("/{idUsuario}")
    @PermitAll
    ResponseEntity<Usuario> editUsuario(@PathVariable Long idUsuario,
            @Valid @RequestBody UsuarioDTO usuario) {
        return new ResponseEntity<>(usuarioService.editUsuario(idUsuario, usuario), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Secured({ "ADMIN", "COOR" })
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        if (!usuarioService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        usuarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists/correo/{correo}")
    @Secured({ "ADMIN", "COOR", "SUP" })
    public ResponseEntity<Boolean> existsByCorreoInstitucional(@PathVariable String correo) {
        boolean exists = usuarioService.existsByCorreo(correo);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/exists/carnet/{carnet}")
    @Secured({ "ADMIN", "COOR", "SUP" })
    public ResponseEntity<Boolean> existsByCarnet(@PathVariable String carnet) {
        boolean exists = usuarioService.existsByCarnet(carnet);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/exists/username/{username}")
    @Secured({ "ADMIN", "COOR", "SUP" })
    public ResponseEntity<Boolean> existsByUsername(@PathVariable String username) {
        boolean exists = usuarioService.existsByUsername(username);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/administradores")
    @Secured({ "ADMIN", "SUP" })
    public ResponseEntity<List<Usuario>> getUsuariosAdministradores() {
        List<Usuario> administradores = usuarioService.findUsuariosAdministradores();
        return ResponseEntity.ok(administradores);
    }
}