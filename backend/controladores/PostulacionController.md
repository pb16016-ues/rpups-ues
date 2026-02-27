# PostulacionController

## Información General

| Propiedad | Valor |
|-----------|-------|
| **Ruta base** | `/api/postulaciones` |
| **Archivo** | `controller/PostulacionController.java` |
| **Dependencias** | `PostulacionService` |

---

## Endpoints

| Método | Path | Seguridad | Descripción |
|--------|------|-----------|-------------|
| GET | `/` | PermitAll | Listar todas las postulaciones |
| GET | `/{id}` | PermitAll | Obtener por ID |
| GET | `/proyecto/{id}` | PermitAll | Por proyecto |
| GET | `/estudiante/{idEstudiante}` | PermitAll | Por estudiante |
| GET | `/estudiante/{idEst}/proyecto/{idProy}` | PermitAll | Por estudiante y proyecto |
| POST | `/` | PermitAll | Crear postulación |
| DELETE | `/{id}` | ADMIN, COORD, SUP, EST | Eliminar postulación |

---

## Detalle de Endpoints

### GET / - Listar Todas

**Retorna**: `List<Postulacion>` (sin paginación)

**Problema**: Lista completa sin paginación puede ser muy grande.

---

### GET /proyecto/{id} - Postulaciones de un Proyecto

**Propósito**: Ver todos los estudiantes postulados a un proyecto.

**Uso**: Panel de coordinador/supervisor para gestionar postulaciones.

---

### GET /estudiante/{idEstudiante} - Postulaciones de un Estudiante

**Propósito**: Estudiante ve sus propias postulaciones.

**Uso**: Panel de estudiante → "Mis Postulaciones".

---

### GET /estudiante/{idEstudiante}/proyecto/{idProyecto}

**Propósito**: Verificar si un estudiante ya está postulado a un proyecto.

**Uso en frontend**: Antes de mostrar botón "Postularse", verificar si ya existe postulación.

**Retorna**: 
- `200 OK` + Postulacion si existe
- `404 NOT FOUND` si no existe

---

### POST / - Crear Postulación

**Seguridad**: PermitAll ⚠️

**Validación en servicio**: Lanza `IllegalArgumentException` si:
- El estudiante ya está postulado al proyecto
- El proyecto no tiene vacantes disponibles

**Request Body**:
```json
{
  "estudiante": { "idUsuario": 10 },
  "proyecto": { "idProyecto": 5 },
  "motivacion": "Me interesa el proyecto porque...",
  "estado": { "codigoEstado": "PEND" }
}
```

**Respuesta error** (409 CONFLICT):
```json
null
```

**Problema**: No retorna mensaje de error descriptivo, solo `null`.

---

### DELETE /{id} - Eliminar Postulación

**Seguridad**: ADMIN, COORD, SUP, EST

**Nota**: Estudiante puede eliminar sus propias postulaciones (retirar postulación).

**Problema**: No valida que el estudiante solo pueda eliminar SUS postulaciones.

---

## Problemas Identificados

### Seguridad

| ID | Endpoint | Problema | Impacto |
|----|----------|----------|---------|
| PO-01 | `GET /` | @PermitAll + sin paginación | Exposición masiva de datos |
| PO-02 | `POST /` | @PermitAll | Cualquiera puede crear postulaciones |
| PO-03 | `DELETE /{id}` | No valida propiedad | Estudiante puede eliminar postulaciones de otros |

### Lógica

| ID | Problema | Ubicación |
|----|----------|-----------|
| PO-04 | Sin paginación en listados | Todos los GET que retornan List |
| PO-05 | Error 409 retorna null sin mensaje | `POST /` |
| PO-06 | No hay endpoint para actualizar estado | Falta `PUT /{id}` |

### Funcionalidad Faltante

| ID | Funcionalidad | Descripción |
|----|---------------|-------------|
| PO-07 | Aprobar postulación | No hay endpoint para aprobar/rechazar |
| PO-08 | Filtrar por estado | No hay `/estado/{codigoEstado}` |
| PO-09 | Paginación | Todos los endpoints deberían paginar |

---

## Mejoras Sugeridas

### 1. Agregar Paginación

```java
@GetMapping
@Secured({"ADMIN", "COORD", "SUP"})
public ResponseEntity<Page<Postulacion>> getAllPostulaciones(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size) {
    return ResponseEntity.ok(postulacionService.findAll(PageRequest.of(page, size)));
}
```

### 2. Proteger Creación

```java
@PostMapping
@Secured({"ESTUD"})
@PreAuthorize("#postulacion.estudiante.idUsuario == principal.id")
public ResponseEntity<?> createPostulacion(@RequestBody Postulacion postulacion) {
    try {
        Postulacion saved = postulacionService.save(postulacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    } catch (IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(Map.of("error", e.getMessage()));
    }
}
```

### 3. Agregar Endpoint de Actualización

```java
@PutMapping("/{id}")
@Secured({"ADMIN", "COORD", "SUP"})
public ResponseEntity<Postulacion> updatePostulacion(
    @PathVariable Long id, 
    @RequestBody Postulacion postulacion) {
    // Cambiar estado: PEND → APRB o RCHZ
}
```

### 4. Validar Propiedad en DELETE

```java
@DeleteMapping("/{id}")
@PreAuthorize("hasAnyRole('ADMIN','COORD','SUP') or @postulacionService.isOwner(#id, principal.id)")
public ResponseEntity<Void> deletePostulacion(@PathVariable Long id) {
    // ...
}
```

### 5. Mejorar Respuesta de Error

```java
} catch (IllegalArgumentException e) {
    Map<String, String> error = new HashMap<>();
    error.put("mensaje", e.getMessage());
    error.put("codigo", "POSTULACION_DUPLICADA");
    return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
}
```

---

## Flujo de Postulación

```
┌─────────────┐     Postularse    ┌─────────────┐
│  Estudiante │ ────────────────► │ Postulación │
└─────────────┘                   │   (PEND)    │
                                  └──────┬──────┘
                                         │
                          Coord/Sup revisa
                                         │
                    ┌────────────────────┼────────────────────┐
                    │                    │                    │
                    ▼                    ▼                    ▼
             ┌───────────┐        ┌───────────┐        ┌───────────┐
             │   APRB    │        │   RCHZ    │        │   RETIR   │
             │ Aprobada  │        │ Rechazada │        │ Cancelada  │
             └─────┬─────┘        └───────────┘        └───────────┘
                   │
                   │ Asignar a proyecto
                   ▼
             ┌───────────┐
             │ Estudiante│
             │ asignado  │
             └───────────┘
```

---

## Estados de Postulación

| Código | Nombre | Descripción | Acción siguiente |
|--------|--------|-------------|------------------|
| PEND | Pendiente | Esperando revisión | Aprobar/Rechazar |
| APRB | Aprobada | Estudiante aceptado | Asignar al proyecto |
| RCHZ | Rechazada | Estudiante no aceptado | Fin |
| RETIR | Cancelada | Estudiante se retiró | Fin |

**Nota**: Estos estados deberían existir en la tabla `estados` pero actualmente no hay endpoint para gestionarlos en postulaciones.

---

## Relación con Otras Entidades

```
Postulacion
     │
     ├── Usuario estudiante (quién se postula)
     │
     ├── Proyecto (a cuál proyecto)
     │
     └── Estado (PEND, APRB, RCHZ, RETIR)
```

---

## Campos de Postulacion

| Campo | Tipo | Descripción |
|-------|------|-------------|
| idPostulacion | Long | ID auto-generado |
| estudiante | Usuario | Estudiante que se postula |
| proyecto | Proyecto | Proyecto objetivo |
| fechaPostulacion | LocalDateTime | Cuándo se postuló |
| motivacion | String | Carta de motivación |
| estado | Estado | Estado actual |
| observaciones | String | Notas del revisor |
