# SolicitudProyectoController

## Información General

| Propiedad | Valor |
|-----------|-------|
| **Ruta base** | `/api/solicitudes-proyectos` |
| **Archivo** | `controller/SolicitudProyectoController.java` |
| **Dependencias** | `SolicitudProyectoService`, `EmailService`, `EstadoService`, `CarreraService`, `DepartamentoCarreraService`, `EmpresaService` |

---

## Endpoints

### Consultas

| Método | Path | Seguridad | Descripción |
|--------|------|-----------|-------------|
| GET | `/` | PermitAll | Listar todas (paginado) |
| GET | `/estado-revision` | ADMIN, COORD, SUP | Solicitudes en revisión (paginado) |
| GET | `/{id}` | PermitAll | Obtener por ID |
| GET | `/titulo/{titulo}` | PermitAll | Buscar por título |
| GET | `/estado/{codigoEstado}` | PermitAll | Filtrar por estado |
| GET | `/empresa/{idEmpresa}` | PermitAll | Por empresa (paginado) |
| GET | `/carrera/{codigoCarrera}` | PermitAll | Por carrera |
| GET | `/modalidad/{codigoModalidad}` | PermitAll | Por modalidad |
| GET | `/admin-revisor/{idUsuario}` | PermitAll | Por admin revisor |
| GET | `/user-creador/{idUsuario}` | PermitAll | Por usuario creador (paginado) |

### Consultas Combinadas

| Método | Path | Seguridad | Descripción |
|--------|------|-----------|-------------|
| GET | `/empresa/{id}/estado/{estado}` | PermitAll | Por empresa y estado |
| GET | `/carrera/{carrera}/estado/{estado}` | PermitAll | Por carrera y estado |
| GET | `/modalidad/{modalidad}/estado/{estado}` | PermitAll | Por modalidad y estado |

### Búsquedas con Filtros

| Método | Path | Seguridad | Descripción |
|--------|------|-----------|-------------|
| GET | `/search-filters` | ADMIN, COORD, SUP | Búsqueda administrativa |
| GET | `/user-search-filters` | PermitAll | Búsqueda por usuario |

### CRUD

| Método | Path | Seguridad | Descripción |
|--------|------|-----------|-------------|
| POST | `/` | PermitAll ⚠️ | Crear solicitud |
| PUT | `/admin/{id}` | PermitAll ⚠️ | Actualizar (admin) |
| PUT | `/externo/{id}` | PermitAll ⚠️ | Actualizar (empresa) |
| DELETE | `/{id}` | ADMIN, COORD, SUP | Eliminar |

### Reportes PDF

| Método | Path | Seguridad | Descripción |
|--------|------|-----------|-------------|
| GET | `/report-estado` | ADMIN, COORD, SUP | Reporte por estado |
| GET | `/report-carrera` | ADMIN, COORD, SUP | Reporte por carrera |
| GET | `/report-empresa` | ADMIN, COORD, SUP | Reporte por empresa |
| GET | `/report-depto-carrera` | ADMIN, COORD, SUP | Reporte por depto académico |

---

## Detalle de Endpoints Principales

### POST / - Crear Solicitud

**Seguridad**: PermitAll ⚠️ (PROBLEMA)

**Flujo esperado**:
1. Representante de empresa inicia sesión
2. Crea solicitud de proyecto
3. Solicitud queda en estado "Pendiente" (PEND)
4. Admin/Coord revisa

**Request Body**:
```json
{
  "titulo": "Desarrollo de App Móvil",
  "descripcion": "Aplicación para gestión de inventario...",
  "objetivos": "Desarrollar una aplicación...",
  "requisitos": "Conocimientos en Flutter, Firebase",
  "vacantes": 3,
  "horasTotales": 500,
  "fechaInicio": "2024-02-01",
  "fechaFin": "2024-07-01",
  "ubicacion": "San Salvador",
  "empresa": { "idEmpresa": 1 },
  "carrera": { "codigo": "LIS" },
  "modalidad": { "codigoModalidad": "HIB" },
  "estado": { "codigoEstado": "PEND" },
  "userCreador": { "idUsuario": 5 }
}
```

---

### PUT /admin/{id} - Actualización Administrativa

**Propósito**: Admin revisa solicitud y cambia estado (Aprueba, Rechaza, En Observación).

**Flujo**:
1. Busca solicitud existente
2. Actualiza campos con `updateAdmin()`
3. Si estado es Aprobado, Rechazado o En Observación:
   - Envía email al creador (correo institucional)
   - Envía email al creador (correo personal, si existe y es diferente)
   - Envía email a la empresa (si correo diferente al creador)

**Notificaciones por correo**:
```
Destinatarios:
- userCreador.correoInstitucional (siempre)
- userCreador.correoPersonal (si existe y diferente)
- empresa.contactoEmail (si diferente a los anteriores)
```

**Request Body**: `SolicitudProyecto` completo con nuevos valores.

---

### PUT /externo/{id} - Actualización Externa (Empresa)

**Propósito**: Representante de empresa modifica o reenvía solicitud.

**Flujo**:
1. Busca solicitud existente
2. Actualiza campos con `updateExterno()`
3. Si estado es "En Revisión" o "Pendiente":
   - Notifica al admin revisor asignado

**Datos enviados en notificación**:
```
titulo | nombreCreador | correoCreador | observaciones
```

---

### GET /estado-revision - Solicitudes para Revisar

**Propósito**: Lista solicitudes pendientes de revisión para el panel administrativo.

**Filtro**: Solo solicitudes con estado de revisión (PEND, REV).

---

### GET /user-creador/{idUsuario} - Solicitudes del Usuario

**Propósito**: Representante de empresa ve sus propias solicitudes.

**Paginación**: Sí

**Uso en frontend**: Panel de empresa → Lista de solicitudes.

---

### GET /user-search-filters - Búsqueda por Usuario

**Propósito**: Filtrar solicitudes de un usuario específico.

**Parámetros**:
- `idUser` (Long): ID del usuario creador
- `idDepto` (Long): Departamento académico
- `filter` (String): Término de búsqueda
- `page`, `size`: Paginación

---

## Problemas Identificados

### Seguridad Crítica ⚠️

| ID | Endpoint | Problema | Impacto |
|----|----------|----------|---------|
| S-01 | `POST /` | @PermitAll | Cualquiera puede crear solicitudes |
| S-02 | `PUT /admin/{id}` | @PermitAll | Cualquiera puede aprobar/rechazar |
| S-03 | `PUT /externo/{id}` | @PermitAll | Cualquiera puede modificar solicitudes |

### Lógica

| ID | Problema | Ubicación |
|----|----------|-----------|
| S-04 | Validación redundante de `solicitudProyectoBD == null` | `PUT /admin/{id}` líneas 175-180 |
| S-05 | No valida que usuario externo solo modifique sus solicitudes | `PUT /externo/{id}` |
| S-06 | Acceso a `empresa.getContactoEmail()` puede ser null | `PUT /admin/{id}` línea 197 |

### Mejoras Sugeridas

1. **Proteger creación de solicitudes**:
   ```java
   @PostMapping
   @Secured({"EMP"})  // Solo representantes de empresa
   public ResponseEntity<SolicitudProyecto> createSolicitud(...)
   ```

2. **Proteger actualización administrativa**:
   ```java
   @PutMapping("/admin/{id}")
   @Secured({"ADMIN", "COORD", "SUP"})
   ```

3. **Validar propiedad en actualización externa**:
   ```java
   @PutMapping("/externo/{id}")
   @Secured({"EMP"})
   @PreAuthorize("@securityService.isSolicitudOwner(#id, principal)")
   ```

4. **Validar null en contactoEmail**:
   ```java
   if (updatedSolicitud.getEmpresa().getContactoEmail() != null 
       && !updatedSolicitud.getEmpresa().getContactoEmail().equals(...)) {
       emailService.sendNotification(...);
   }
   ```

5. **Simplificar validación redundante**:
   ```java
   // Cambiar de:
   if (!solicitudProyectoService.findById(id).isPresent()) { ... }
   Optional<SolicitudProyecto> opt = solicitudProyectoService.findById(id);
   
   // A:
   SolicitudProyecto solicitud = solicitudProyectoService.findById(id)
       .orElseThrow(() -> new EntityNotFoundException("Solicitud no encontrada"));
   ```

---

## Flujo de Estados

```
┌──────────┐     Crear      ┌──────────┐
│  (none)  │ ────────────►  │   PEND   │
└──────────┘                └────┬─────┘
                                 │
                    Admin asigna revisor
                                 │
                                 ▼
                            ┌──────────┐
                            │   REV    │ En Revisión
                            └────┬─────┘
                                 │
              ┌──────────────────┼──────────────────┐
              │                  │                  │
              ▼                  ▼                  ▼
        ┌──────────┐      ┌──────────┐      ┌──────────┐
        │   APRB   │      │   OBSV   │      │   RCHZ   │
        │ Aprobado │      │En Observ │      │Rechazado │
        └────┬─────┘      └────┬─────┘      └──────────┘
             │                 │
             │    Empresa corrige
             │                 │
             │                 ▼
             │           ┌──────────┐
             │           │   PEND   │ (vuelve a revisión)
             │           └──────────┘
             │
             ▼
    ┌─────────────────┐
    │ Proyecto creado │
    │ (automático)    │
    └─────────────────┘
```

---

## Notificaciones por Email

### Al aprobar/rechazar (Admin)

**Plantilla**: Notificación de estado de solicitud

**Contenido**:
- Estado nuevo de la solicitud
- Observaciones del revisor

**Destinatarios**:
1. Correo institucional del creador
2. Correo personal del creador (opcional)
3. Correo de contacto de la empresa

### Al enviar a revisión (Empresa)

**Plantilla**: Notificación de solicitud pendiente

**Contenido**:
- Título de la solicitud
- Nombre del solicitante
- Correo de contacto
- Observaciones

**Destinatario**: Admin revisor asignado

---

## Relación con Otras Entidades

```
SolicitudProyecto
       │
       ├── Empresa (quién solicita)
       │
       ├── Usuario userCreador (representante)
       │
       ├── Usuario adminRevisor (quién revisa)
       │
       ├── Carrera (carrera objetivo)
       │
       ├── Modalidad (presencial/virtual/híbrido)
       │
       ├── Estado (PEND, REV, APRB, RCHZ, OBSV)
       │
       └──► Proyecto (se crea al aprobar)
```
