# Controladores Backend

## Descripción General

Los controladores REST del sistema RPUPS-UES manejan las peticiones HTTP y delegan la lógica de negocio a los servicios correspondientes. Utilizan Spring Security para control de acceso basado en roles.

---

## Índice de Controladores

### Controladores Principales

| Controlador | Ruta Base | Descripción |
|-------------|-----------|-------------|
| [UsuarioController](./UsuarioController.md) | `/api/usuarios` | Gestión de usuarios y autenticación |
| [ProyectoController](./ProyectoController.md) | `/api/proyectos` | Gestión de proyectos aprobados |
| [SolicitudProyectoController](./SolicitudProyectoController.md) | `/api/solicitudes-proyectos` | Gestión de solicitudes de proyecto |
| [PostulacionController](./PostulacionController.md) | `/api/postulaciones` | Postulaciones de estudiantes |
| [EmpresaController](./EmpresaController.md) | `/api/empresas` | Gestión de empresas |

### Controladores de Catálogos

| Controlador | Ruta Base | Descripción |
|-------------|-----------|-------------|
| CarreraController | `/api/carreras` | CRUD de carreras |
| ModalidadController | `/api/modalidades` | CRUD de modalidades |
| EstadoController | `/api/estados` | CRUD de estados |
| DepartamentoController | `/api/departamentos` | CRUD de departamentos geográficos |
| DepartamentoCarreraController | `/api/deptos-carreras` | CRUD de departamentos académicos |
| MunicipioController | `/api/municipios` | CRUD de municipios |
| RubroController | `/api/rubros` | CRUD de rubros empresariales |
| RolController | `/api/roles` | CRUD de roles |

### Controladores Auxiliares

| Controlador | Ruta Base | Descripción |
|-------------|-----------|-------------|
| PasswordResetController | `/api/password` | Recuperación de contraseña |
| AuthController | `/api/auth` | Login y generación de JWT |

---

## Patrones Comunes

### Anotaciones de Seguridad

```java
@PermitAll                           // Público
@Secured({"ADMIN"})                  // Solo administrador
@Secured({"ADMIN", "COORD"})         // Admin o coordinador
@Secured({"ADMIN", "COORD", "SUP"})  // Staff administrativo
```

### Estructura de Respuestas

- **200 OK**: Operación exitosa
- **201 CREATED**: Recurso creado
- **204 NO CONTENT**: Eliminación exitosa
- **400 BAD REQUEST**: Datos inválidos
- **404 NOT FOUND**: Recurso no encontrado
- **409 CONFLICT**: Conflicto (duplicado)

### Paginación

Los endpoints con listas grandes usan paginación:

```
GET /api/usuarios?page=0&size=10
```

Retorna objeto `Page<T>` con:
- `content`: Lista de elementos
- `totalElements`: Total de registros
- `totalPages`: Total de páginas
- `number`: Página actual

---

## Problemas Identificados

### Seguridad

1. **Endpoints con @PermitAll que deberían estar protegidos**:
   - `PUT /api/usuarios/{id}` - Cualquiera puede editar usuarios
   - `POST /api/empresas` - Cualquiera puede crear empresas
   - `PUT /api/empresas/{id}` - Cualquiera puede modificar empresas
   - `POST /api/solicitudes-proyectos` - Cualquiera puede crear solicitudes
   - `PUT /api/solicitudes-proyectos/admin/{id}` - Cualquiera puede aprobar/rechazar

2. **Inconsistencias en nombres de roles**:
   - Algunos usan `"ESTUD"`, otros `"ESTUD"`
   - Debe unificarse según la tabla `roles`

### Diseño

3. **Código duplicado en reportes**:
   - `ProyectoController` y `SolicitudProyectoController` tienen lógica similar para generar PDFs
   - Debería extraerse a un servicio común

4. **Falta validación de entrada**:
   - No se usa `@Valid` consistentemente en `@RequestBody`
   - No hay validación de permisos por propiedad (usuario solo puede editar sus propios datos)

---

## Convenciones de Nomenclatura

| Operación | Prefijo del Método | Ejemplo |
|-----------|-------------------|---------|
| Listar todos | `getAll*` | `getAllUsuarios()` |
| Obtener por ID | `get*ById` | `getUsuarioById()` |
| Buscar por campo | `get*By{Campo}` | `getUsuarioByUsername()` |
| Crear | `create*` | `createUsuario()` |
| Actualizar | `update*` | `updateUsuario()` |
| Eliminar | `delete*` | `deleteUsuario()` |
| Verificar existencia | `existsBy*` | `existsByUsername()` |
| Generar reporte | `*GenerarReportePDF` | `proyectosByEstadosGenerarReportePDF()` |
