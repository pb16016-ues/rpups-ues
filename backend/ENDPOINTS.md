# Backend - Catálogo de Endpoints

## Base URL

```
http://localhost:8080/api
```

---

## Autenticación

| Método | Endpoint | Acceso | Descripción |
|--------|----------|--------|-------------|
| POST | `/v1/auth/login` | Público | Iniciar sesión |
| GET | `/v1/auth/token/refresh` | Autenticado | Refrescar token |
| GET | `/v1/auth/reset-password` | Público | Restablecer contraseña |

### Login - Request

```json
POST /api/v1/auth/login
Content-Type: application/json

{
  "username": "usuario",
  "password": "contraseña"
}
```

### Login - Response

```
Header: Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

---

## Usuarios

| Método | Endpoint | Acceso | Descripción |
|--------|----------|--------|-------------|
| GET | `/usuarios` | ADMIN, COORD, SUP | Listar usuarios paginados |
| GET | `/usuarios/{id}` | PermitAll | Obtener usuario por ID |
| GET | `/usuarios/username/{username}` | PermitAll | Obtener por username |
| GET | `/usuarios/correo/{correo}` | PermitAll | Obtener por correo |
| GET | `/usuarios/exists/username/{username}` | PermitAll | Verificar si existe username |
| GET | `/usuarios/exists/correo/{correo}` | PermitAll | Verificar si existe correo |
| GET | `/usuarios/rol/{codigoRol}` | ADMIN, COORD, SUP | Listar por rol |
| POST | `/usuarios/register` | Público | Registrar estudiante |
| POST | `/usuarios/administrativo` | COORD | Crear usuario administrativo |
| POST | `/usuarios/representante/register` | Público | Registrar representante |
| PUT | `/usuarios/{id}/change-password` | Autenticado | Cambiar contraseña |
| PUT | `/usuarios/{id}` | PermitAll | Editar usuario |
| DELETE | `/usuarios/{id}` | ADMIN, COORD | Desactivar usuario |

### Registrar Estudiante - Request

```json
POST /api/usuarios/register
{
  "nombres": "Juan Carlos",
  "apellidos": "Pérez López",
  "carnet": "PL20001",
  "correoInstitucional": "pl20001@ues.edu.sv",
  "username": "jperez",
  "password": "contraseña123",
  "departamentoCarrera": { "idDepartamentoCarrera": 1 }
}
```

---

## Proyectos

| Método | Endpoint | Acceso | Descripción |
|--------|----------|--------|-------------|
| GET | `/proyectos` | PermitAll | Listar todos (paginado) |
| GET | `/proyectos/public` | Público | Listar públicos (paginado) |
| GET | `/proyectos/{id}` | PermitAll | Obtener por ID |
| GET | `/proyectos/titulo/{titulo}` | PermitAll | Buscar por título |
| GET | `/proyectos/estado/{codigoEstado}` | PermitAll | Filtrar por estado |
| GET | `/proyectos/empresa/{idEmpresa}` | PermitAll | Filtrar por empresa (paginado) |
| GET | `/proyectos/carrera/{codigoCarrera}` | PermitAll | Filtrar por carrera |
| GET | `/proyectos/modalidad/{codigoModalidad}` | PermitAll | Filtrar por modalidad |
| GET | `/proyectos/admin-aprobador/{idUsuario}` | PermitAll | Por admin aprobador |
| GET | `/proyectos/search-filters` | ADMIN, COORD, SUP | Búsqueda con filtros |
| GET | `/proyectos/search-filters-disponibles` | PermitAll | Proyectos disponibles |
| GET | `/proyectos/exists/titulo` | PermitAll | Verificar si existe título |
| POST | `/proyectos` | ADMIN, COORD, SUP | Crear proyecto |
| PUT | `/proyectos/{id}` | ADMIN, COORD, SUP | Actualizar proyecto |
| DELETE | `/proyectos/{id}` | ADMIN, COORD, SUP | Eliminar proyecto |
| GET | `/proyectos/report-estado` | ADMIN, COORD, SUP | Reporte PDF por estado |
| GET | `/proyectos/report-carrera` | ADMIN, COORD, SUP | Reporte PDF por carrera |
| GET | `/proyectos/report-empresa` | ADMIN, COORD, SUP | Reporte PDF por empresa |

### Búsqueda con Filtros - Query Params

```
GET /api/proyectos/search-filters?filter=texto&idDepto=1&page=0&size=10
```

| Parámetro | Tipo | Descripción |
|-----------|------|-------------|
| filter | String | Texto a buscar en título, carrera, modalidad, estado |
| idDepto | Long | ID del departamento de carrera |
| page | int | Número de página (desde 0) |
| size | int | Tamaño de página |

---

## Solicitudes de Proyecto

| Método | Endpoint | Acceso | Descripción |
|--------|----------|--------|-------------|
| GET | `/solicitudes-proyectos` | PermitAll | Listar todas (paginado) |
| GET | `/solicitudes-proyectos/estado-revision` | ADMIN, COORD, SUP | En estado revisión |
| GET | `/solicitudes-proyectos/{id}` | PermitAll | Obtener por ID |
| GET | `/solicitudes-proyectos/titulo/{titulo}` | PermitAll | Buscar por título |
| GET | `/solicitudes-proyectos/estado/{codigoEstado}` | PermitAll | Filtrar por estado |
| GET | `/solicitudes-proyectos/empresa/{idEmpresa}` | PermitAll | Por empresa (paginado) |
| GET | `/solicitudes-proyectos/carrera/{codigoCarrera}` | PermitAll | Por carrera |
| GET | `/solicitudes-proyectos/modalidad/{codigoModalidad}` | PermitAll | Por modalidad |
| GET | `/solicitudes-proyectos/admin-revisor/{idUsuario}` | PermitAll | Asignadas a revisor |
| GET | `/solicitudes-proyectos/user-creador/{idUsuario}` | PermitAll | Por usuario creador (paginado) |
| GET | `/solicitudes-proyectos/search-filters` | ADMIN, COORD, SUP | Búsqueda con filtros |
| GET | `/solicitudes-proyectos/user-search-filters` | PermitAll | Búsqueda por usuario |
| POST | `/solicitudes-proyectos` | PermitAll | Crear solicitud |
| PUT | `/solicitudes-proyectos/admin/{id}` | PermitAll | Actualizar (admin) |
| PUT | `/solicitudes-proyectos/externo/{id}` | PermitAll | Actualizar (externo) |
| DELETE | `/solicitudes-proyectos/{id}` | ADMIN, COORD, SUP | Eliminar solicitud |
| GET | `/solicitudes-proyectos/report-estado` | ADMIN, COORD, SUP | Reporte PDF por estado |
| GET | `/solicitudes-proyectos/report-carrera` | ADMIN, COORD, SUP | Reporte PDF por carrera |
| GET | `/solicitudes-proyectos/report-empresa` | ADMIN, COORD, SUP | Reporte PDF por empresa |

### Crear Solicitud - Request

```json
POST /api/solicitudes-proyectos
{
  "titulo": "Desarrollo de Sistema Web",
  "descripcion": "Sistema para gestión de inventario",
  "requisitos": "Conocimientos en Angular y Spring Boot",
  "duracion": 300,
  "maxEstudiantes": 3,
  "empresa": { "idEmpresa": 1 },
  "carrera": { "codigo": "IS" },
  "modalidad": { "codigoModalidad": "PRES" },
  "userCreador": { "idUsuario": 5 }
}
```

---

## Postulaciones

| Método | Endpoint | Acceso | Descripción |
|--------|----------|--------|-------------|
| GET | `/postulaciones` | PermitAll | Listar todas |
| GET | `/postulaciones/{id}` | PermitAll | Obtener por ID |
| GET | `/postulaciones/proyecto/{id}` | PermitAll | Por proyecto |
| GET | `/postulaciones/estudiante/{idEstudiante}` | PermitAll | Por estudiante |
| GET | `/postulaciones/estudiante/{id}/proyecto/{id}` | PermitAll | Verificar postulación |
| POST | `/postulaciones` | PermitAll | Crear postulación |
| DELETE | `/postulaciones/{id}` | ADMIN, COORD, SUP, EST | Eliminar postulación |

### Crear Postulación - Request

```json
POST /api/postulaciones
{
  "estudiante": { "idUsuario": 10 },
  "proyecto": { "idProyecto": 5 }
}
```

---

## Empresas

| Método | Endpoint | Acceso | Descripción |
|--------|----------|--------|-------------|
| GET | `/empresas` | PermitAll | Listar todas |
| GET | `/empresas/{id}` | PermitAll | Obtener por ID |
| GET | `/empresas/rubro/{idRubro}` | PermitAll | Por rubro |
| GET | `/empresas/user-creador/{idUsuario}` | PermitAll | Por usuario creador |
| GET | `/empresas/exists/nombreComercial` | PermitAll | Verificar nombre comercial |
| GET | `/empresas/exists/nombreLegal` | PermitAll | Verificar nombre legal |
| POST | `/empresas` | PermitAll | Crear empresa |
| PUT | `/empresas/{id}` | PermitAll | Actualizar empresa |
| DELETE | `/empresas/{id}` | ADMIN, COORD, SUP | Desactivar empresa |

### Crear Empresa - Request

```json
POST /api/empresas
{
  "nombreComercial": "Tech Solutions",
  "nombreLegal": "Tech Solutions S.A. de C.V.",
  "direccion": "Santa Ana, El Salvador",
  "telefono": "2440-0000",
  "correo": "contacto@techsolutions.com",
  "contactoNombre": "María García",
  "contactoTelefono": "7890-1234",
  "contactoCorreo": "maria@techsolutions.com",
  "rubro": { "idRubro": 1 },
  "departamento": { "idDepartamento": 1 },
  "municipio": { "idMunicipio": 1 },
  "userCreador": { "idUsuario": 5 }
}
```

---

## Catálogos

### Departamentos de Carrera

| Método | Endpoint | Acceso | Descripción |
|--------|----------|--------|-------------|
| GET | `/departamentos-carrera` | PermitAll | Listar todos |
| GET | `/departamentos-carrera/{id}` | PermitAll | Obtener por ID |

### Carreras

| Método | Endpoint | Acceso | Descripción |
|--------|----------|--------|-------------|
| GET | `/carreras` | PermitAll | Listar todas |
| GET | `/carreras/{codigo}` | PermitAll | Obtener por código |
| GET | `/carreras/departamento/{idDepto}` | PermitAll | Por departamento |

### Estados

| Método | Endpoint | Acceso | Descripción |
|--------|----------|--------|-------------|
| GET | `/estados` | PermitAll | Listar todos |
| GET | `/estados/{codigo}` | PermitAll | Obtener por código |

### Modalidades

| Método | Endpoint | Acceso | Descripción |
|--------|----------|--------|-------------|
| GET | `/modalidades` | PermitAll | Listar todas |
| GET | `/modalidades/{codigo}` | PermitAll | Obtener por código |

### Rubros

| Método | Endpoint | Acceso | Descripción |
|--------|----------|--------|-------------|
| GET | `/rubros` | PermitAll | Listar todos |
| GET | `/rubros/{id}` | PermitAll | Obtener por ID |

### Departamentos (Geográficos)

| Método | Endpoint | Acceso | Descripción |
|--------|----------|--------|-------------|
| GET | `/departamentos` | PermitAll | Listar todos |
| GET | `/departamentos/{id}` | PermitAll | Obtener por ID |

### Municipios

| Método | Endpoint | Acceso | Descripción |
|--------|----------|--------|-------------|
| GET | `/municipios` | PermitAll | Listar todos |
| GET | `/municipios/{id}` | PermitAll | Obtener por ID |
| GET | `/municipios/departamento/{idDepto}` | PermitAll | Por departamento |

---

## Códigos de Respuesta

| Código | Significado |
|--------|-------------|
| 200 | OK - Operación exitosa |
| 201 | Created - Recurso creado |
| 204 | No Content - Eliminación exitosa |
| 400 | Bad Request - Error de validación |
| 401 | Unauthorized - No autenticado |
| 403 | Forbidden - Sin permisos |
| 404 | Not Found - Recurso no encontrado |
| 409 | Conflict - Recurso duplicado |
| 500 | Internal Server Error |

---

## Paginación

Los endpoints paginados retornan un objeto `Page`:

```json
{
  "content": [...],
  "totalElements": 100,
  "totalPages": 10,
  "size": 10,
  "number": 0,
  "first": true,
  "last": false
}
```
