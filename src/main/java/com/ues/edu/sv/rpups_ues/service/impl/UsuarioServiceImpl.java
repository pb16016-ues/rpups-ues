package com.ues.edu.sv.rpups_ues.service.impl;

import com.ues.edu.sv.rpups_ues.exceptions.UniqueValidationException;
import com.ues.edu.sv.rpups_ues.model.DTO.ChangePasswordDTO;
import com.ues.edu.sv.rpups_ues.model.DTO.UsuarioDTO;
import com.ues.edu.sv.rpups_ues.model.entity.Rol;
import com.ues.edu.sv.rpups_ues.model.entity.Usuario;
import com.ues.edu.sv.rpups_ues.model.repository.UsuarioRepository;
import com.ues.edu.sv.rpups_ues.service.UsuarioService;
import com.ues.edu.sv.rpups_ues.utils.JavaUtils;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Usuario> findAll(Pageable pageable) {
        return usuarioRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findById(Long idUsuario) {
        return usuarioRepository.findById(idUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findByUsername(String username) {
        return usuarioRepository.findByUsernameAndEstadoActivoTrue(username);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findByCarnet(String carnet) {
        return usuarioRepository.findByCarnet(carnet);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findByCorreoInstitucional(String correoInstitucional) {
        return usuarioRepository.findByCorreoInstitucionalAndEstadoActivoTrue(correoInstitucional);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findByCorreoPersonal(String correoPersonal) {
        return usuarioRepository.findByCorreoPersonalAndEstadoActivoTrue(correoPersonal);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Usuario> findByNombresOrApellidos(String searchTerm, Pageable pageable) {
        return usuarioRepository.findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCaseAndEstadoActivoTrue(
                searchTerm,
                searchTerm, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Usuario> findUsuarioByFiltros(String filter, Long idDeptoCarrera, Pageable pageable) {
        return usuarioRepository.searchByAnyField(filter, idDeptoCarrera, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByCorreo(String correo) {
        return usuarioRepository.existsByCorreoInstitucional(correo)
                || usuarioRepository.existsByCorreoPersonalAndEstadoActivoTrue(correo);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByCarnet(String carnet) {
        return usuarioRepository.existsByCarnetAndEstadoActivoTrue(carnet);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return usuarioRepository.existsByUsername(username);
    }

    @Override
    @Transactional
    public Usuario createUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El argumento usuario no puede ser nulo");
        }
        if (usuarioRepository.existsByCorreoInstitucional(usuario.getCorreoInstitucional())) {
            throw new UniqueValidationException("El correo institucional ingresado ya está registrado.");
        }
        if (usuario.getCorreoPersonal() != null
                && usuarioRepository.existsByCorreoPersonalAndEstadoActivoTrue(usuario.getCorreoPersonal())) {
            throw new UniqueValidationException("El correo personal ingresado ya está registrado.");
        }
        if (usuario.getCarnet() != null && !usuario.getCarnet().isEmpty()) {

            String carnet = usuario.getCarnet().toUpperCase();

            if (usuarioRepository.existsByCarnetAndEstadoActivoTrue(carnet)) {
                throw new UniqueValidationException("El carnet ingresado ya está registrado.");
            }

            String carnetFormat = JavaUtils.validarFormatearCarnet(carnet);
            usuario.setCarnet(carnetFormat);
        }
        if (usuarioRepository.existsByUsername(usuario.getUsername())) {
            throw new UniqueValidationException("Ya existe un usuario con el username ingresado");
        }

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional()
    public Usuario registerUsuario(Usuario usuario) {
        Rol rol = new Rol("ESTUD");
        usuario.setRol(rol);
        usuario.setEstadoActivo(true);
        return createUsuario(usuario);
    }

    @Override
    @Transactional()
    public Usuario registerAdministrativo(Usuario usuario) {
        usuario.setEstadoActivo(true);
        return createUsuario(usuario);
    }

    @Override
    @Transactional()
    public Usuario registerRepresentanteEmpresa(Usuario usuario) {
        Rol rol = new Rol("EMP");
        usuario.setRol(rol);
        usuario.setEstadoActivo(true);
        return createUsuario(usuario);
    }

    @Override
    @Transactional()
    public Usuario changePassword(ChangePasswordDTO changePasswordDTO, Principal principal) {
        Long id = Long.parseLong(principal.getName());
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrada con el ID proporcionado: " + id));
        if (!passwordEncoder.matches(changePasswordDTO.getOldPassword(), usuario.getPassword())) {
            throw new IllegalArgumentException("Introduzca correctamente su contraseña actual");
        }
        usuario.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public Usuario editUsuario(Long idUsuario, UsuarioDTO usuarioDTO) {
        if (idUsuario == null || usuarioDTO == null) {
            throw new IllegalArgumentException("Los argumentos idUsuario y usuarioDTO no pueden ser nulos");
        }
        Usuario usuarioDB = usuarioRepository.findById(idUsuario).orElseThrow(
                () -> new EntityNotFoundException("Usuario no encontrado con el ID proporcionado: " + idUsuario));

        if (usuarioDTO.getNombres() != null)
            usuarioDB.setNombres(usuarioDTO.getNombres());
        if (usuarioDTO.getApellidos() != null)
            usuarioDB.setApellidos(usuarioDTO.getApellidos());

        if (usuarioDTO.getCarnet() != null && !usuarioDTO.getCarnet().isEmpty()) {

            String carnet = usuarioDTO.getCarnet().toUpperCase();

            if (usuarioRepository.existsByCarnetAndEstadoActivoTrue(carnet)
                    && !usuarioDB.getCarnet().equals(usuarioDTO.getCarnet())) {
                throw new UniqueValidationException("Ya existe un usuario con el carnet ingresado");
            }

            String carnetFormat = JavaUtils.validarFormatearCarnet(carnet);
            usuarioDB.setCarnet(carnetFormat);
        }

        if (usuarioDTO.getCorreoInstitucional() != null) {
            if (usuarioRepository.existsByCorreoInstitucional(usuarioDTO.getCorreoInstitucional())
                    && !usuarioDB.getCorreoInstitucional().equals(usuarioDTO.getCorreoInstitucional())) {
                throw new UniqueValidationException("Ya existe un usuario con el correo institucional ingresado");
            }
            usuarioDB.setCorreoInstitucional(usuarioDTO.getCorreoInstitucional());
        }
        if (usuarioDTO.getCorreoPersonal() != null) {
            if (usuarioRepository.existsByCorreoPersonalAndEstadoActivoTrue(usuarioDTO.getCorreoPersonal())
                    && !usuarioDB.getCorreoPersonal().equals(usuarioDTO.getCorreoPersonal())) {
                throw new UniqueValidationException("Ya existe un usuario con el correo personal ingresado");
            }
            usuarioDB.setCorreoPersonal(usuarioDTO.getCorreoPersonal());
        }
        if (usuarioDTO.getTelefono() != null)
            usuarioDB.setTelefono(usuarioDTO.getTelefono());
        if (usuarioDTO.getUsername() != null) {
            if (usuarioRepository.existsByUsername(usuarioDTO.getUsername())
                    && !usuarioDB.getUsername().equals(usuarioDTO.getUsername())) {
                throw new UniqueValidationException("Ya existe un usuario con el username ingresado");
            }
            usuarioDB.setUsername(usuarioDTO.getUsername());
        }
        if (usuarioDTO.getRol() != null)
            usuarioDB.setRol(usuarioDTO.getRol());

        if (usuarioDTO.getDepartamentoCarrera() != null)
            usuarioDB.setDepartamentoCarrera(usuarioDTO.getDepartamentoCarrera());

        return usuarioRepository.save(usuarioDB);
    }

    @Override
    @Transactional
    public Usuario editPasswordUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El argumento usuario no puede ser nulo");
        }

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public Usuario deleteById(Long idUsuario) {
        if (idUsuario == null) {
            throw new IllegalArgumentException("El argumento idUsuario no puede ser nulo");
        }
        Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow(
                () -> new EntityNotFoundException("Usuario no encontrada con el ID proporcionado: " + idUsuario));
        usuario.setEstadoActivo(false);
        usuarioRepository.save(usuario);
        return usuario;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Usuario> findUsuariosAdministradores(Pageable pageable) {
        return usuarioRepository.findUsuariosAdministradores(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Usuario> findUsuariosAdministradoresByDepartamento(Long idDepartamento, Pageable pageable) {
        return usuarioRepository.findUsuariosAdministradoresByDepartamento(idDepartamento, pageable);
    }

}