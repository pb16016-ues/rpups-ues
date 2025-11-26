# UsuarioController

## Información General

| Propiedad | Valor |
|-----------|-------|
| **Ruta base** | `/api/usuarios` |
| **Archivo** | `controller/UsuarioController.java` |
| **Dependencias** | `UsuarioService`, `EmailService` |

---

## Endpoints

### Consultas

| Método | Path | Seguridad | Descripción |
|--------|------|-----------|-------------|
| GET | `/` | ADMIN, COORD, SUP | Listar usuarios (paginado) |
| GET | `/{id}` | PermitAll ⚠️ | Obtener por ID |
| GET | `/username/{username}` | PermitAll ⚠️ | Obtener por username |
| GET | `/carnet/{carnet}` | ADMIN, COORD, SUP | Obtener por carnet |
| GET | `/correo-institucional/{correo}` | ADMIN, COORD, SUP | Obtener por correo institucional |
| GET | `/correo-personal/{correo}` | ADMIN, COORD, SUP | Obtener por correo personal |
| GET | `/search-user/{searchTerm}` | ADMIN, COORD, SUP, EMP, EST | Buscar por nombre/apellido |
| GET | `/search-filters` | ADMIN, COORD, SUP, EMP, EST | Búsqueda con filtros |
| GET | `/administradores` | ADMIN, COORD, SUP | Listar administradores |
| GET | `/administradores-by-carrera` | ADMIN, COORD, SUP | Administradores por depto |

### Verificaciones

| Método | Path | Seguridad | Descripción |
|--------|------|-----------|-------------|
| GET | `/exists/correo/{correo}` | ADMIN, COORD, SUP | ¿Existe correo? |
| GET | `/exists/carnet/{carnet}` | ADMIN, COORD, SUP | ¿Existe carnet? |
| GET | `/exists/username/{username}` | ADMIN, COORD, SUP | ¿Existe username? |

### Creación de Usuarios

| Método | Path | Seguridad | Descripción |
|--------|------|-----------|-------------|
| POST | `/register` | PermitAll | Registro de estudiante |
| POST | `/administrativo` | COORD | Crear usuario administrativo |
| POST | `/repres-empresa` | PermitAll | Registro de representante empresa |

### Modificación

| Método | Path | Seguridad | Descripción |
|--------|------|-----------|-------------|
| PUT | `/change-password` | PermitAll | Cambiar contraseña (con validación) |
| PUT | `/{idUsuario}` | PermitAll ⚠️ | Editar usuario |
| DELETE | `/{id}` | ADMIN, COORD | Eliminar usuario |

---

## Detalle de Endpoints Principales

### GET / - Listar Usuarios

**Parámetros**:
- `page` (int, default: 0): Número de página
- `size` (int, default: 10): Tamaño de página

**Respuesta**: `Page<Usuario>`

---

### POST /register - Registro de Estudiante

**Validaciones previas**:
1. Correo institucional no existe
2. Correo personal no existe (si se proporciona)
3. Carnet no existe
4. Username no existe

**Flujo**:
1. Valida unicidad de campos
2. Llama a `usuarioService.registerUsuario()`
3. El servicio encripta la contraseña y asigna rol ESTUD

**Request Body**:
```json
{
  "username": "jperez",
  "password": "123456",
  "nombres": "Juan",
  "apellidos": "Pérez",
  "carnet": "PE21001",
  "correoInstitucional": "jp21001@ues.edu.sv",
  "correoPersonal": "juan@gmail.com",
  "telefono": "7890-1234",
  "carrera": { "codigo": "LIS" }
}
```

---

### POST /administrativo - Crear Usuario Administrativo

**Seguridad**: Solo COORD

**Parámetros**:
- `withPasswordDefault` (Boolean): Si es true, genera contraseña temporal

**Flujo**:
1. Valida unicidad
2. Si `withPasswordDefault=true`:
   - Genera contraseña temporal aleatoria
   - Envía por correo electrónico
3. Guarda usuario con rol asignado en el body

---

### POST /repres-empresa - Registro de Representante

**Validaciones**: Igual que `/register` pero sin carnet

**Flujo**:
1. Valida unicidad
2. Llama a `usuarioService.registerRepresentanteEmpresa()`
3. El servicio asigna rol EMP automáticamente

---

### GET /search-filters - Búsqueda con Filtros

**Parámetros**:
- `filter` (String): Término de búsqueda (nombre, apellido, carnet)
- `idDeptoCarrera` (Long): Filtrar por departamento académico
- `page`, `size`: Paginación

**Validación especial**: Si `filter` contiene solo caracteres especiales, retorna página vacía.

---

### PUT /change-password - Cambiar Contraseña

**Request Body** (`ChangePasswordDTO`):
```json
{
  "currentPassword": "actual123",
  "newPassword": "nueva456",
  "confirmPassword": "nueva456"
}
```

**Validaciones en servicio**:
1. `currentPassword` coincide con la actual
2. `newPassword` == `confirmPassword`
3. Nueva contraseña cumple requisitos

---

## Problemas Identificados

### Seguridad Crítica

| ID | Endpoint | Problema | Impacto |
|----|----------|----------|---------|
| U-01 | `GET /{id}` | @PermitAll | Cualquiera puede ver datos de cualquier usuario |
| U-02 | `GET /username/{username}` | @PermitAll | Exposición de datos por username |
| U-03 | `PUT /{idUsuario}` | @PermitAll | Cualquiera puede editar cualquier usuario |

### Lógica

| ID | Problema | Ubicación |
|----|----------|-----------|
| U-04 | No valida que usuario solo edite su propio perfil | `PUT /{idUsuario}` |
| U-05 | Rol "EST" vs "ESTUD" inconsistente | Línea 83 |
| U-06 | No hay validación @Valid en registro | `POST /register` |

### Mejoras Sugeridas

1. **Proteger endpoints de consulta**:
   - `GET /{id}` → Solo el propio usuario o ADMIN/COORD
   - `GET /username/{username}` → ADMIN, COORD, SUP

2. **Validar propiedad en edición**:
   ```java
   @PutMapping("/{idUsuario}")
   @PreAuthorize("@securityService.isOwnerOrAdmin(#idUsuario, principal)")
   ```

3. **Agregar @Valid**:
   ```java
   public ResponseEntity<Usuario> createUsuario(@Valid @RequestBody Usuario usuario)
   ```

4. **Unificar roles**: Cambiar "EST" a "ESTUD" en todas las anotaciones.

---

## DTOs Utilizados

### UsuarioDTO (para edición)

Campos permitidos en actualización:
- nombres
- apellidos
- telefono
- correoPersonal
- carrera

### ChangePasswordDTO

| Campo | Tipo | Validación |
|-------|------|------------|
| currentPassword | String | @NotBlank |
| newPassword | String | @NotBlank, @Size(min=6) |
| confirmPassword | String | @NotBlank |

---

## Flujo de Autenticación Relacionado

1. Usuario se registra → `POST /register`
2. Usuario hace login → `POST /api/auth/login` (AuthController)
3. Recibe JWT token
4. Usa token en header `Authorization: Bearer {token}`
5. Puede cambiar contraseña → `PUT /change-password`
