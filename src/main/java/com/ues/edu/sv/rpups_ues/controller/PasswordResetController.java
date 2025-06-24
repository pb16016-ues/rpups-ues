package com.ues.edu.sv.rpups_ues.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.security.PermitAll;
import com.ues.edu.sv.rpups_ues.model.DTO.Message;
import com.ues.edu.sv.rpups_ues.model.entity.Usuario;
import com.ues.edu.sv.rpups_ues.service.EmailService;
import com.ues.edu.sv.rpups_ues.service.UsuarioService;

@RestController
@RequestMapping("/api/v1/password-reset")
public class PasswordResetController {

    private final UsuarioService usuarioService;
    private final EmailService emailService;

    public PasswordResetController(UsuarioService usuarioService, EmailService emailService) {
        this.usuarioService = usuarioService;
        this.emailService = emailService;
    }

    @GetMapping("/request")
    @PermitAll()
    public ResponseEntity<?> requestPasswordReset(
            @RequestParam(name = "email", defaultValue = "", required = true) String email) {

        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body("El correo electrónico no puede estar vacío");
        }
        if (usuarioService.existsByCorreo(email)) {
            Usuario usuario = usuarioService.findByCorreoInstitucional(email).orElse(null);

            if (usuario == null) {
                usuario = usuarioService.findByCorreoPersonal(email).orElse(null);
                if (usuario == null) {
                    return ResponseEntity.badRequest().body("Usuario no existe o no ha podido ser encontrado " + email);
                }
            }

            String passwordTemporal = emailService.generateTemporaryPassword();
            usuario.setPassword(passwordTemporal);
            usuarioService.editPasswordUsuario(usuario);

            // Enviar el correo electrónico
            emailService.sendPasswordResetEmail(usuario.getCorreoInstitucional(), passwordTemporal);
            return ResponseEntity
                    .ok(new Message("Contraseña restablecida con éxito, puede verificar en su correo electrónico"));

        } else {
            return ResponseEntity.badRequest().body("El correo electrónico no esta registrado en el sistema");
        }
    }

}
