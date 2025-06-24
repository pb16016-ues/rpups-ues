package com.ues.edu.sv.rpups_ues.service;

import com.ues.edu.sv.rpups_ues.model.DTO.ChangePasswordDTO;
import com.ues.edu.sv.rpups_ues.model.DTO.UsuarioDTO;
import com.ues.edu.sv.rpups_ues.model.entity.Usuario;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UsuarioService {

    Page<Usuario> findAll(Pageable pageable);

    Optional<Usuario> findById(Long idUsuario);

    Optional<Usuario> findByUsername(String username);

    Page<Usuario> findByNombresOrApellidos(String searchTerm, Pageable pageable);

    Optional<Usuario> findByCarnet(String carnet);

    Optional<Usuario> findByCorreoInstitucional(String correoInstitucional);

    Optional<Usuario> findByCorreoPersonal(String correoPersonal);

    Page<Usuario> findUsuarioByFiltros(String filter, Pageable pageable);

    Usuario createUsuario(Usuario usuario);

    Usuario changePassword(ChangePasswordDTO changePasswordDTO, Principal principal);

    Usuario registerUsuario(Usuario usuario);

    Usuario registerAdministrativo(Usuario usuario);

    Usuario registerRepresentanteEmpresa(Usuario usuario);

    Usuario editUsuario(Long idUsuario, UsuarioDTO usuario);

    Usuario editPasswordUsuario(Usuario usuario);

    Usuario deleteById(Long idUsuario);

    boolean existsByCorreo(String correo);

    boolean existsByCarnet(String carnet);

    boolean existsByUsername(String username);

    List<Usuario> findUsuariosAdministradores();
}