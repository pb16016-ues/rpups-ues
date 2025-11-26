# Entidad: Usuario

## Descripción

Representa a todos los usuarios del sistema: estudiantes, representantes de empresa y personal administrativo (admin, coordinador, supervisor).

---

## Campos

| Campo | Tipo | Nullable | Descripción |
|-------|------|----------|-------------|
| `idUsuario` | Long | No | Identificador único (auto-generado) |
| `nombres` | String | No | Nombres del usuario |
| `apellidos` | String | No | Apellidos del usuario |
| `carnet` | String | Sí | Carnet universitario (solo estudiantes) |
| `correoInstitucional` | String | No | Correo @ues.edu.sv (único) |
| `username` | String | No | Nombre de usuario para login (único) |
| `password` | String | No | Contraseña (encriptada con BCrypt) |
| `activo` | Boolean | No | Estado del usuario (soft delete) |
| `rol` | Rol | No | Rol asignado (FK a Rol) |
| `departamentoCarrera` | DepartamentoCarrera | Sí | Departamento académico (FK) |

---

## Relaciones

| Tipo | Entidad Relacionada | Descripción |
|------|---------------------|-------------|
| ManyToOne | `Rol` | Cada usuario tiene un rol |
| ManyToOne | `DepartamentoCarrera` | Departamento del estudiante/admin |
| OneToMany | `SolicitudProyecto` | Solicitudes creadas por el usuario |
| OneToMany | `Proyecto` | Proyectos aprobados por el usuario |
| OneToMany | `Postulacion` | Postulaciones del estudiante |
| OneToMany | `Empresa` | Empresas creadas por representante |

---

## Validaciones

| Campo | Validación | Mensaje |
|-------|------------|---------|
| nombres | @NotBlank | "El nombre es requerido" |
| nombres | @Size(max=100) | "Máximo 100 caracteres" |
| apellidos | @NotBlank | "Los apellidos son requeridos" |
| apellidos | @Size(max=100) | "Máximo 100 caracteres" |
| carnet | @Pattern | (Comentado actualmente) |
| correoInstitucional | @NotBlank, @Email | "Correo válido requerido" |
| username | @NotBlank | "Username requerido" |
| password | @NotBlank | "Password requerido" |

---

## Mapeo JPA

```java
@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;
    
    @ManyToOne
    @JoinColumn(name = "codigo_rol")
    private Rol rol;
    
    @ManyToOne
    @JoinColumn(name = "id_departamento_carrera")
    private DepartamentoCarrera departamentoCarrera;
}
```

---

## Roles de Usuario

| Código | Nombre | Permisos |
|--------|--------|----------|
| `ADMIN` | Administrador | Acceso total |
| `COORD` | Coordinador | Gestión de usuarios admin, solicitudes, proyectos |
| `SUP` | Supervisor | Revisión de solicitudes asignadas |
| `ESTUD` | Estudiante | Ver proyectos, postularse, crear solicitudes |
| `EMP` | Empresa | Crear empresa, crear solicitudes |

---

## Endpoints Relacionados

- `POST /api/usuarios/register` - Registrar estudiante
- `POST /api/usuarios/administrativo` - Crear admin/coord/sup
- `POST /api/usuarios/representante/register` - Registrar representante
- `GET /api/usuarios/{id}` - Obtener usuario
- `PUT /api/usuarios/{id}` - Actualizar usuario
- `PUT /api/usuarios/{id}/change-password` - Cambiar contraseña
- `DELETE /api/usuarios/{id}` - Desactivar usuario

---

## Notas Importantes

1. **Carnet**: Solo aplica para estudiantes. La validación de formato está comentada actualmente.

2. **Soft Delete**: Se usa el campo `activo` en lugar de eliminar físicamente.

3. **Password**: Se encripta con BCrypt antes de guardar. Nunca se retorna en las respuestas.

4. **DepartamentoCarrera**: Para estudiantes indica su carrera, para admin/coord indica su departamento de trabajo.

---

## Problemas Identificados

- **ERROR-B10**: Validación de carnet comentada
- Endpoint `PUT /api/usuarios/{id}` usa `@PermitAll` (debería verificar que sea el mismo usuario o admin)
