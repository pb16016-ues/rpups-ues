package com.ues.edu.sv.rpups_ues.service;

import com.ues.edu.sv.rpups_ues.model.DTO.ChangePasswordDTO;
import com.ues.edu.sv.rpups_ues.model.DTO.UsuarioDTO;
import com.ues.edu.sv.rpups_ues.model.entity.Usuario;

import java.security.Principal;
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

    /**
     * Busca usuarios con filtros opcionales.
     * @param filter Texto a buscar en nombres, apellidos, carnet, correos
     * @param idDeptoCarrera ID del departamento (opcional)
     * @param codigoRol Código del rol para filtrar (opcional)
     * @param pageable Paginación
     * @return Página de usuarios que coinciden con los filtros
     */
    Page<Usuario> findUsuarioByFiltros(String filter, Long idDeptoCarrera, String codigoRol, Pageable pageable);

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

    Page<Usuario> findUsuariosAdministradores(Pageable pageable);

    Page<Usuario> findUsuariosAdministradoresByDepartamento(Long idDeptoCarrera, Pageable pageable);
}