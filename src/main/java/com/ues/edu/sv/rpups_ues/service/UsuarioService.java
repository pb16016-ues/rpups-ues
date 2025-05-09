package com.ues.edu.sv.rpups_ues.service;

import com.ues.edu.sv.rpups_ues.model.DTO.ChangePasswordDTO;
import com.ues.edu.sv.rpups_ues.model.DTO.UsuarioDTO;
import com.ues.edu.sv.rpups_ues.model.entity.Usuario;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    List<Usuario> findAll();

    Optional<Usuario> findById(Long idUsuario);

    Optional<Usuario> findByUsername(String username);

    List<Usuario> findByNombresOrApellidos(String searchTerm);

    Optional<Usuario> findByCarnet(String carnet);

    Optional<Usuario> findByCorreoInstitucional(String correoInstitucional);

    Optional<Usuario> findByCorreoPersonal(String correoPersonal);

    Usuario save(Usuario usuario);

    Usuario createUsuario(Usuario usuario);

    Usuario changePassword(ChangePasswordDTO changePasswordDTO, Principal principal);

    Usuario registerUsuario(Usuario usuario);

    Usuario editUsuario(Long idUsuario, UsuarioDTO usuario);

    Usuario editPasswordUsuario(Usuario usuario);

    Usuario deleteById(Long idUsuario);

    boolean existsByCorreoInstitucional(String correoInstitucional);

    boolean existsByCorreoPersonal(String correoPersonal);

    boolean existsByCarnet(String carnet);

    boolean existsByUsername(String username);
}