# EmpresaController

## Información General

| Propiedad | Valor |
|-----------|-------|
| **Ruta base** | `/api/empresas` |
| **Archivo** | `controller/EmpresaController.java` |
| **Dependencias** | `EmpresaService` |

---

## Endpoints

| Método | Path | Seguridad | Descripción |
|--------|------|-----------|-------------|
| GET | `/` | PermitAll | Listar todas las empresas |
| GET | `/{id}` | PermitAll | Obtener por ID |
| GET | `/nombre-comercial/{nombre}` | PermitAll | Buscar por nombre comercial |
| GET | `/nombre-legal/{nombre}` | PermitAll | Buscar por nombre legal |
| GET | `/rubro/{idRubro}` | PermitAll | Filtrar por rubro |
| GET | `/user-creador/{idUsuario}` | PermitAll | Por usuario creador |
| POST | `/` | PermitAll ⚠️ | Crear empresa |
| PUT | `/{id}` | PermitAll ⚠️ | Actualizar empresa |
| DELETE | `/{id}` | ADMIN, COORD, SUP | Desactivar empresa |

---

## Detalle de Endpoints

### GET / - Listar Empresas

**Retorna**: `List<Empresa>` (sin paginación)

**Problema**: Lista completa sin paginación.

---

### GET /nombre-comercial/{nombreComercial}

**Propósito**: Buscar empresas por nombre comercial.

**Retorna**: `List<Empresa>` (puede haber coincidencias parciales según implementación del servicio).

---

### GET /user-creador/{idUsuario}

**Propósito**: Obtener empresas registradas por un representante específico.

**Uso**: Panel de empresa → Ver mis empresas registradas.

---

### POST / - Crear Empresa

**Seguridad**: PermitAll ⚠️ (PROBLEMA CRÍTICO)

**Request Body**:
```json
{
  "nombreComercial": "Tech Solutions",
  "nombreLegal": "Tech Solutions S.A. de C.V.",
  "direccion": "Av. Principal #123, Santa Ana",
  "telefono": "2440-0000",
  "correo": "info@techsolutions.com",
  "contactoNombre": "María García",
  "contactoTelefono": "7890-1234",
  "contactoCorreo": "maria@techsolutions.com",
  "rubro": { "idRubro": 1 },
  "departamento": { "codigo": "SA" },
  "municipio": { "codigo": "0201" },
  "userCreador": { "idUsuario": 5 }
}
```

**Problema**: Cualquier usuario (incluso no autenticado) puede crear empresas.

---

### PUT /{id} - Actualizar Empresa

**Seguridad**: PermitAll ⚠️ (PROBLEMA CRÍTICO)

**Problema**: Cualquier usuario puede modificar cualquier empresa.

---

### DELETE /{id} - Desactivar Empresa

**Seguridad**: ADMIN, COORD, SUP ✓

**Comportamiento**: Soft delete - llama a `desactiveById()` que establece `activo = false`.

**Nota**: No elimina físicamente, solo desactiva.

---

## Problemas Identificados

### Seguridad Crítica ⚠️

| ID | Endpoint | Problema | Impacto |
|----|----------|----------|---------|
| E-01 | `POST /` | @PermitAll | Cualquiera puede crear empresas |
| E-02 | `PUT /{id}` | @PermitAll | Cualquiera puede modificar empresas |
| E-03 | Todos los GET | @PermitAll + sin paginación | Exposición masiva de datos |

### Lógica

| ID | Problema | Ubicación |
|----|----------|-----------|
| E-04 | Sin paginación | Todos los endpoints |
| E-05 | Sin validación de unicidad | `POST /` |
| E-06 | Sin validación de propiedad | `PUT /{id}` |
| E-07 | Retorna List en búsquedas que deberían ser únicas | `/nombre-comercial/`, `/nombre-legal/` |

### Funcionalidad Faltante

| ID | Funcionalidad | Descripción |
|----|---------------|-------------|
| E-08 | Verificar existencia | Falta `/exists/nombre-comercial`, `/exists/nombre-legal` |
| E-09 | Búsqueda con filtros | Falta `/search-filters` |
| E-10 | Reactivar empresa | Falta endpoint para reactivar empresas desactivadas |

---

## Mejoras Sugeridas

### 1. Proteger Creación

```java
@PostMapping
@Secured({"EMP"})  // Solo representantes de empresa
public ResponseEntity<?> createEmpresa(@Valid @RequestBody Empresa empresa, Principal principal) {
    // Validar que userCreador sea el usuario autenticado
    if (!empresa.getUserCreador().getIdUsuario().equals(getCurrentUserId(principal))) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
    
    // Validar unicidad
    if (empresaService.existsByNombreComercial(empresa.getNombreComercial())) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(Map.of("error", "Ya existe una empresa con ese nombre comercial"));
    }
    
    Empresa saved = empresaService.save(empresa);
    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
}
```

### 2. Proteger Actualización

```java
@PutMapping("/{id}")
@Secured({"EMP", "ADMIN", "COORD"})
@PreAuthorize("hasRole('ADMIN') or hasRole('COORD') or @empresaService.isOwner(#id, principal.id)")
public ResponseEntity<Empresa> updateEmpresa(@PathVariable Long id, @RequestBody Empresa empresa) {
    // ...
}
```

### 3. Agregar Paginación

```java
@GetMapping
@PermitAll
public ResponseEntity<Page<Empresa>> getAllEmpresas(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size) {
    return ResponseEntity.ok(empresaService.findAll(PageRequest.of(page, size)));
}
```

### 4. Agregar Endpoints de Verificación

```java
@GetMapping("/exists/nombre-comercial")
@PermitAll
public ResponseEntity<Boolean> existsByNombreComercial(@RequestParam String nombre) {
    return ResponseEntity.ok(empresaService.existsByNombreComercial(nombre));
}

@GetMapping("/exists/nombre-legal")
@PermitAll
public ResponseEntity<Boolean> existsByNombreLegal(@RequestParam String nombre) {
    return ResponseEntity.ok(empresaService.existsByNombreLegal(nombre));
}
```

### 5. Corregir Retorno de Búsquedas Únicas

```java
// Cambiar de List a Optional para campos únicos
@GetMapping("/nombre-comercial/{nombreComercial}")
@PermitAll
public ResponseEntity<Empresa> getEmpresaByNombreComercial(@PathVariable String nombreComercial) {
    return empresaService.findByNombreComercial(nombreComercial)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
}
```

### 6. Agregar Endpoint de Reactivación

```java
@PutMapping("/{id}/reactivar")
@Secured({"ADMIN", "COORD"})
public ResponseEntity<Empresa> reactivarEmpresa(@PathVariable Long id) {
    return ResponseEntity.ok(empresaService.reactivateById(id));
}
```

---

## Flujo de Registro de Empresa

```
┌─────────────────┐
│ Usuario se      │
│ registra como   │
│ representante   │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ Inicia sesión   │
│ (rol EMP)       │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ Registra        │
│ empresa         │
│ POST /empresas  │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ Crea solicitud  │
│ de proyecto     │
│ para la empresa │
└─────────────────┘
```

---

## Relación con Otras Entidades

```
Empresa
   │
   ├── Rubro (sector empresarial)
   │
   ├── Departamento (ubicación geográfica)
   │
   ├── Municipio (ubicación geográfica)
   │
   ├── Usuario userCreador (representante que registró)
   │
   ├──► Proyecto (proyectos de la empresa)
   │
   └──► SolicitudProyecto (solicitudes de la empresa)
```

---

## Campos de Empresa

| Campo | Tipo | Requerido | Descripción |
|-------|------|-----------|-------------|
| idEmpresa | Long | Auto | ID auto-generado |
| nombreComercial | String | Sí | Nombre comercial (único) |
| nombreLegal | String | Sí | Razón social (único) |
| direccion | String | Sí | Dirección física |
| telefono | String | No | Teléfono de empresa |
| correo | String | No | Correo de empresa |
| contactoNombre | String | No | Nombre del contacto |
| contactoTelefono | String | No | Teléfono del contacto |
| contactoCorreo | String | No | Correo del contacto |
| activo | Boolean | Sí | Estado (soft delete) |
| rubro | Rubro | Sí | Sector empresarial |
| departamento | Departamento | Sí | Ubicación |
| municipio | Municipio | Sí | Ubicación |
| userCreador | Usuario | Sí | Representante |

---

## Validaciones Requeridas

### En Creación

1. `nombreComercial` único
2. `nombreLegal` único
3. `userCreador` debe ser el usuario autenticado
4. `userCreador` debe tener rol EMP
5. `municipio` debe pertenecer al `departamento` seleccionado

### En Actualización

1. Usuario es propietario O tiene rol ADMIN/COORD
2. No cambiar `nombreComercial` a uno existente
3. No cambiar `nombreLegal` a uno existente
