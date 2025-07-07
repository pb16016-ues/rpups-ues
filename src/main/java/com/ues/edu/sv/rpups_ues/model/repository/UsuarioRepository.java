package com.ues.edu.sv.rpups_ues.model.repository;

import com.ues.edu.sv.rpups_ues.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

        Optional<Usuario> findByUsernameAndEstadoActivoTrue(String username);

        Optional<Usuario> findByUsernameOrCorreoInstitucionalAndEstadoActivoTrue(String username,
                        String correoInstitucional);

        Page<Usuario> findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCaseAndEstadoActivoTrue(
                        String nombres, String apellidos,
                        Pageable pageable);

        Optional<Usuario> findByCarnet(String carnet);

        Optional<Usuario> findByCorreoInstitucionalAndEstadoActivoTrue(String correoInstitucional);

        Optional<Usuario> findByCorreoPersonalAndEstadoActivoTrue(String correoPersonal);

        @Query("SELECT u FROM Usuario u " +
                        "WHERE u.estado_activo = true " +
                        "AND (:idDeptoCarrera IS NULL OR u.id_depto_carrera = :idDeptoCarrera) " +
                        "AND (" +
                        "(:filter IS NULL) " +
                        "OR LOWER(u.nombres) LIKE LOWER(CONCAT('%', :filter, '%')) " +
                        "OR LOWER(u.apellidos) LIKE LOWER(CONCAT('%', :filter, '%')) " +
                        "OR LOWER(u.carnet) LIKE LOWER(CONCAT('%', :filter, '%')) " +
                        "OR LOWER(u.correoInstitucional) LIKE LOWER(CONCAT('%', :filter, '%')) " +
                        "OR LOWER(u.correoPersonal) LIKE LOWER(CONCAT('%', :filter, '%'))" +
                        ")")
        Page<Usuario> searchByAnyField(
                        @Param("filter") String filter,
                        @Param("idDeptoCarrera") Long idDeptoCarrera,
                        Pageable pageable);

        boolean existsByCorreoInstitucional(String correoInstitucional);

        boolean existsByCorreoPersonalAndEstadoActivoTrue(String correoPersonal);

        boolean existsByCarnetAndEstadoActivoTrue(String carnet);

        boolean existsByUsername(String username);

        @Query("SELECT u FROM Usuario u WHERE u.rol.codigo IN ('ADMIN', 'COOR', 'SUP') AND u.estado_activo = true")
        Page<Usuario> findUsuariosAdministradores(Pageable pageable);

        @Query("SELECT u FROM Usuario u WHERE u.rol.codigo IN ('ADMIN', 'COOR', 'SUP') " +
                        "AND (:idDeptoCarrera IS NULL OR u.id_depto_carrera = :idDeptoCarrera) " +
                        "AND u.estado_activo = true")
        Page<Usuario> findUsuariosAdministradoresByDepartamento(@Param("idDeptoCarrera") Long idDeptoCarrera,
                        Pageable pageable);

}