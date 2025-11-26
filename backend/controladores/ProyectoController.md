# ProyectoController

## Información General

| Propiedad | Valor |
|-----------|-------|
| **Ruta base** | `/api/proyectos` |
| **Archivo** | `controller/ProyectoController.java` |
| **Dependencias** | `ProyectoService`, `EstadoService`, `CarreraService`, `DepartamentoCarreraService`, `EmpresaService` |

---

## Endpoints

### Consultas Públicas

| Método | Path | Seguridad | Descripción |
|--------|------|-----------|-------------|
| GET | `/` | PermitAll | Listar proyectos (paginado) |
| GET | `/public` | PermitAll | Listar proyectos públicos (paginado) |
| GET | `/{id}` | PermitAll | Obtener por ID |
| GET | `/titulo/{titulo}` | PermitAll | Buscar por título |
| GET | `/estado/{codigoEstado}` | PermitAll | Filtrar por estado |
| GET | `/empresa/{idEmpresa}` | PermitAll | Por empresa (paginado) |
| GET | `/carrera/{codigoCarrera}` | PermitAll | Por carrera |
| GET | `/modalidad/{codigoModalidad}` | PermitAll | Por modalidad |
| GET | `/admin-aprobador/{idUsuario}` | PermitAll | Por admin que aprobó |

### Consultas Combinadas

| Método | Path | Seguridad | Descripción |
|--------|------|-----------|-------------|
| GET | `/empresa/{id}/estado/{estado}` | PermitAll | Por empresa y estado |
| GET | `/carrera/{carrera}/estado/{estado}` | PermitAll | Por carrera y estado |
| GET | `/modalidad/{modalidad}/estado/{estado}` | PermitAll | Por modalidad y estado |

### Búsquedas con Filtros

| Método | Path | Seguridad | Descripción |
|--------|------|-----------|-------------|
| GET | `/search-filters` | ADMIN, COORD, SUP | Búsqueda avanzada |
| GET | `/disp-search-filters` | PermitAll | Búsqueda solo estado disponible |
| GET | `/exists/titulo` | PermitAll | Verificar si título existe |

### CRUD Administrativo

| Método | Path | Seguridad | Descripción |
|--------|------|-----------|-------------|
| POST | `/` | ADMIN, COORD, SUP | Crear proyecto |
| PUT | `/{id}` | ADMIN, COORD, SUP | Actualizar proyecto |
| DELETE | `/{id}` | ADMIN, COORD, SUP | Eliminar proyecto |

### Generación de Reportes PDF

| Método | Path | Seguridad | Descripción |
|--------|------|-----------|-------------|
| GET | `/report-estado` | ADMIN, COORD, SUP | Reporte por estado |
| GET | `/report-carrera` | ADMIN, COORD, SUP | Reporte por carrera |
| GET | `/report-empresa` | ADMIN, COORD, SUP | Reporte por empresa |
| GET | `/report-depto-carrera` | ADMIN, COORD, SUP | Reporte por depto académico |

---

## Detalle de Endpoints Principales

### GET / - Listar Proyectos

**Parámetros**:
- `page` (int, default: 0): Número de página
- `size` (int, default: 10): Tamaño de página

**Respuesta**: `Page<Proyecto>` con proyectos en cualquier estado.

---

### GET /disp-search-filters - Búsqueda Proyectos Disponibles

**Propósito**: Endpoint para estudiantes que buscan proyectos donde postularse.

**Parámetros**:
- `filter` (String): Término de búsqueda
- `idDepto` (Long): Departamento académico
- `page`, `size`: Paginación

**Filtro automático**: Solo retorna proyectos con estado "Disponible" (DISP).

**Validación**: Caracteres especiales solos → página vacía.

---

### GET /search-filters - Búsqueda Administrativa

**Diferencia con `/disp-search-filters`**: No filtra por estado, muestra todos los proyectos.

**Uso**: Para administrativos que necesitan ver proyectos en cualquier estado.

---

### POST / - Crear Proyecto

**Seguridad**: ADMIN, COORD, SUP

**Nota importante**: Los proyectos generalmente se crean automáticamente cuando una solicitud es aprobada. Este endpoint es para creación manual.

**Request Body**:
```json
{
  "titulo": "Sistema de Gestión Escolar",
  "descripcion": "Desarrollo de sistema web...",
  "objetivos": "Objetivo general...",
  "requisitos": "Conocimientos en Java, SQL...",
  "vacantes": 5,
  "horasTotales": 500,
  "fechaInicio": "2024-01-15",
  "fechaFin": "2024-06-15",
  "ubicacion": "Santa Ana, El Salvador",
  "empresa": { "idEmpresa": 1 },
  "carrera": { "codigo": "LIS" },
  "modalidad": { "codigoModalidad": "PRE" },
  "estado": { "codigoEstado": "DISP" },
  "adminAprobador": { "idUsuario": 2 }
}
```

---

### PUT /{id} - Actualizar Proyecto

**Casos de uso**:
1. Modificar datos del proyecto
2. Cambiar estado (DISP → LLENO, COMP, etc.)
3. Actualizar vacantes disponibles

**Validación**: Verifica que el proyecto existe antes de actualizar.

---

### GET /report-estado - Reporte PDF por Estado

**Parámetros**:
- `codEstado` (String, requerido): Código del estado

**Flujo**:
1. Valida que `codEstado` no esté vacío
2. Busca el estado en BD para obtener nombre
3. Llama a `proyectoService.generarReportePorEstado()`
4. Retorna PDF como attachment

**Headers de respuesta**:
```
Content-Type: application/pdf
Content-Disposition: attachment; filename="Reporte de proyectos por estado {nombre}.pdf"
```

---

### GET /report-depto-carrera - Reporte por Departamento y Carrera

**Parámetros**:
- `idDeptoCarrera` (Long, requerido): ID del departamento académico
- `codCarrera` (String, opcional): Código de carrera específica

**Comportamiento**:
- Si solo se envía `idDeptoCarrera`: Reporte de todas las carreras del departamento
- Si se envía `codCarrera`: Reporte filtrado por carrera específica

---

## Problemas Identificados

### Seguridad

| ID | Endpoint | Problema |
|----|----------|----------|
| P-01 | `GET /public` | Duplica funcionalidad de `GET /` |

### Lógica

| ID | Problema | Ubicación |
|----|----------|-----------|
| P-02 | Algunos endpoints retornan `List<>` sin paginación | `/titulo/{titulo}`, `/estado/{estado}` |
| P-03 | No hay validación de vacantes vs postulaciones | `PUT /{id}` |
| P-04 | Nombres de archivo de reportes no sanitizados completamente | Reportes PDF |

### Mejoras Sugeridas

1. **Eliminar endpoint duplicado**: `GET /public` es idéntico a `GET /`

2. **Agregar paginación a endpoints que retornan listas**:
   ```java
   @GetMapping("/estado/{codigoEstado}")
   public ResponseEntity<Page<Proyecto>> getProyectosByEstado(
       @PathVariable String codigoEstado,
       @RequestParam(defaultValue = "0") int page,
       @RequestParam(defaultValue = "10") int size)
   ```

3. **Validar integridad de vacantes**:
   ```java
   // Antes de actualizar
   int postulacionesAprobadas = postulacionService.countAprobadas(id);
   if (proyecto.getVacantes() < postulacionesAprobadas) {
       throw new BusinessException("No se pueden reducir vacantes por debajo de postulaciones aprobadas");
   }
   ```

4. **Centralizar generación de nombres de archivo**:
   ```java
   private String sanitizeFileName(String name) {
       return name.replaceAll("[^a-zA-Z0-9\\-_ áéíóúÁÉÍÓÚñÑ]", "_");
   }
   ```

---

## Relación con SolicitudProyecto

| Acción | SolicitudProyecto | Proyecto |
|--------|-------------------|----------|
| Empresa crea solicitud | Se crea con estado PEND | No existe |
| Admin aprueba | Estado → APRB | Se crea automáticamente con estado DISP |
| Admin rechaza | Estado → RCHZ | No se crea |
| Proyecto se llena | - | Estado → LLENO |
| Proyecto finaliza | - | Estado → COMP |

---

## Estados de Proyecto

| Código | Nombre | Descripción |
|--------|--------|-------------|
| DISP | Disponible | Acepta postulaciones |
| LLENO | Lleno | Vacantes agotadas |
| EJEC | En Ejecución | En curso |
| COMP | Completado | Finalizado |
| CANC | Cancelado | Cancelado |

---

## Servicio de Reportes

El controlador delega la generación de PDFs al servicio `ProyectoService` que usa:

1. **Thymeleaf**: Plantilla HTML para el reporte
2. **OpenHTMLToPDF**: Convierte HTML a PDF

**Ubicación de plantillas**: `src/main/resources/templates/proyectos/`

**Archivos**:
- `reporte-proyectos-estado.html`
- `reporte-proyectos-carrera.html`
- `reporte-proyectos-empresa.html`
- `reporte-proyectos-depto-carrera.html`
