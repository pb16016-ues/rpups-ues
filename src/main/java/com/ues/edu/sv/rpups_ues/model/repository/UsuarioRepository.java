package com.ues.edu.sv.rpups_ues.model.repository;

import com.ues.edu.sv.rpups_ues.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);

    List<Usuario> findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCase(String nombres, String apellidos);

    Optional<Usuario> findByCarnet(String carnet);

    Optional<Usuario> findByCorreoInstitucional(String correoInstitucional);

    Optional<Usuario> findByCorreoPersonal(String correoPersonal);

    boolean existsByCorreoInstitucional(String correoInstitucional);

    boolean existsByCorreoPersonal(String correoPersonal);

    boolean existsByCarnet(String carnet);

    boolean existsByUsername(String username);
}