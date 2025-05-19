package com.ues.edu.sv.rpups_ues.model.repository;

import com.ues.edu.sv.rpups_ues.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);

    List<Usuario> findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCase(String nombres, String apellidos);

    Optional<Usuario> findByCarnet(String carnet);

    Optional<Usuario> findByCorreoInstitucional(String correoInstitucional);

    Optional<Usuario> findByCorreoPersonal(String correoPersonal);

    @Query("SELECT u FROM Usuario u " +
            "WHERE (:filter IS NULL " +
            "OR LOWER(u.nombres) LIKE LOWER(CONCAT('%', :filter, '%')) " +
            "OR LOWER(u.apellidos) LIKE LOWER(CONCAT('%', :filter, '%')) " +
            "OR LOWER(u.carnet) LIKE LOWER(CONCAT('%', :filter, '%')) " +
            "OR LOWER(u.correoInstitucional) LIKE LOWER(CONCAT('%', :filter, '%')) " +
            "OR LOWER(u.correoPersonal) LIKE LOWER(CONCAT('%', :filter, '%')))")
    Page<Usuario> searchByAnyField(
            @Param("filter") String filter,
            Pageable pageable);

    boolean existsByCorreoInstitucional(String correoInstitucional);

    boolean existsByCorreoPersonal(String correoPersonal);

    boolean existsByCarnet(String carnet);

    boolean existsByUsername(String username);
}