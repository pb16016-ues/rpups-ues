# Entidades de Catálogo / Lookup

Este documento describe todas las entidades de catálogo (lookup) del sistema RPUPS-UES. Estas entidades son tablas auxiliares que contienen datos de referencia utilizados por otras entidades principales del sistema.

---

## Índice

1. [Carrera](#1-carrera)
2. [Modalidad](#2-modalidad)
3. [Estado](#3-estado)
4. [Departamento](#4-departamento)
5. [DepartamentoCarrera](#5-departamentocarrera)
6. [Rubro](#6-rubro)
7. [Municipio](#7-municipio)
8. [Rol](#8-rol)

---

## 1. Carrera

### Descripción
Representa las carreras universitarias disponibles en el sistema. Cada carrera pertenece a un departamento académico.

### Ubicación del archivo
- **Entidad**: `rpups-ues/src/main/java/com/ues/edu/sv/rpups_ues/model/entity/Carrera.java`
- **Repositorio**: `rpups-ues/src/main/java/com/ues/edu/sv/rpups_ues/model/repository/CarreraRepository.java`
- **Controlador**: `rpups-ues/src/main/java/com/ues/edu/sv/rpups_ues/controller/CarreraController.java`

### Tabla en BD
`carreras`

### Campos

| Campo | Tipo Java | Columna BD | Restricciones | Descripción |
|-------|-----------|------------|---------------|-------------|
| `codigo` | `String` | `codigo` | PK, max 7 chars, NOT NULL, UNIQUE | Código único de la carrera |
| `nombre` | `String` | `nombre` | max 250 chars, NOT NULL | Nombre completo de la carrera |
| `idDepartamentoCarrera` | `Long` | `id_depto_carrera` | NOT NULL | FK al departamento de carrera |

### Anotaciones JPA
```java
@Entity
@Table(name = "carreras")
@Id @Column(name = "codigo", length = 7, nullable = false, unique = true)
@Size(max = 7) @NotBlank
@ManyToOne(optional = false)
@JoinColumn(name = "id_depto_carrera", referencedColumnName = "id_depto_carrera", insertable = false, updatable = false)
```

### Relaciones
| Tipo | Entidad relacionada | Descripción |
|------|---------------------|-------------|
| `@ManyToOne` | `DepartamentoCarrera` | Cada carrera pertenece a un departamento de carrera |

### Métodos del Repositorio
```java
extends JpaRepository<Carrera, String>
Optional<Carrera> findByNombre(String nombre)
boolean existsByNombre(String nombre)
List<Carrera> findByIdDepartamentoCarrera(Long idDepartamentoCarrera)
```

### Endpoints del Controlador

| Método | Endpoint | Seguridad | Descripción |
|--------|----------|-----------|-------------|
| GET | `/api/carreras` | PermitAll | Obtener todas las carreras |
| GET | `/api/carreras/{codigo}` | PermitAll | Obtener carrera por código |
| GET | `/api/carreras/nombre/{nombre}` | PermitAll | Obtener carrera por nombre |
| GET | `/api/carreras/exists/{nombre}` | PermitAll | Verificar si existe por nombre |
| GET | `/api/carreras/by-depto/{idDeptoCarrera}` | PermitAll | Obtener carreras por departamento |
| POST | `/api/carreras` | ADMIN, COORD, SUP | Crear nueva carrera |
| PUT | `/api/carreras/{codigo}` | ADMIN, COORD, SUP | Actualizar carrera |
| DELETE | `/api/carreras/{codigo}` | ADMIN, COORD, SUP | Eliminar carrera |

---

## 2. Modalidad

### Descripción
Define las modalidades de servicio social disponibles (ej: presencial, virtual, híbrido).

### Ubicación del archivo
- **Entidad**: `rpups-ues/src/main/java/com/ues/edu/sv/rpups_ues/model/entity/Modalidad.java`
- **Repositorio**: `rpups-ues/src/main/java/com/ues/edu/sv/rpups_ues/model/repository/ModalidadRepository.java`
- **Controlador**: `rpups-ues/src/main/java/com/ues/edu/sv/rpups_ues/controller/ModalidadController.java`

### Tabla en BD
`modalidades`

### Campos

| Campo | Tipo Java | Columna BD | Restricciones | Descripción |
|-------|-----------|------------|---------------|-------------|
| `codigoModalidad` | `String` | `codigo_modalidad` | PK, max 3 chars, NOT NULL, UNIQUE | Código único de la modalidad |
| `nombre` | `String` | `nombre` | max 50 chars, NOT NULL, UNIQUE | Nombre de la modalidad |

### Anotaciones JPA
```java
@Entity
@Table(name = "modalidades")
@Id @Column(name = "codigo_modalidad", length = 3, nullable = false, unique = true)
@Column(name = "nombre", length = 50, nullable = false, unique = true)
```

### Relaciones
No tiene relaciones definidas en la entidad.

### Métodos del Repositorio
```java
extends JpaRepository<Modalidad, String>
Optional<Modalidad> findByNombre(String nombre)
boolean existsByNombre(String nombre)
```

### Endpoints del Controlador

| Método | Endpoint | Seguridad | Descripción |
|--------|----------|-----------|-------------|
| GET | `/api/modalidades` | PermitAll | Obtener todas las modalidades |
| GET | `/api/modalidades/{codigoModalidad}` | PermitAll | Obtener modalidad por código |
| GET | `/api/modalidades/nombre/{nombre}` | PermitAll | Obtener modalidad por nombre |
| GET | `/api/modalidades/exists/{nombre}` | PermitAll | Verificar si existe por nombre |
| POST | `/api/modalidades` | ADMIN, COORD, SUP | Crear nueva modalidad |
| PUT | `/api/modalidades/{codigoModalidad}` | ADMIN, COORD, SUP | Actualizar modalidad |
| DELETE | `/api/modalidades/{codigoModalidad}` | ADMIN, COORD, SUP | Eliminar modalidad |

---

## 3. Estado

### Descripción
Define los estados posibles para los diferentes procesos del sistema (proyectos, solicitudes, postulaciones, etc.).

### Ubicación del archivo
- **Entidad**: `rpups-ues/src/main/java/com/ues/edu/sv/rpups_ues/model/entity/Estado.java`
- **Repositorio**: `rpups-ues/src/main/java/com/ues/edu/sv/rpups_ues/model/repository/EstadoRepository.java`
- **Controlador**: `rpups-ues/src/main/java/com/ues/edu/sv/rpups_ues/controller/EstadoController.java`

### Tabla en BD
`estados`

### Campos

| Campo | Tipo Java | Columna BD | Restricciones | Descripción |
|-------|-----------|------------|---------------|-------------|
| `codigoEstado` | `String` | `codigo_estado` | PK, max 5 chars, NOT NULL, UNIQUE | Código único del estado |
| `nombre` | `String` | `nombre` | max 50 chars, NOT NULL, UNIQUE | Nombre descriptivo del estado |

### Anotaciones JPA
```java
@Entity
@Table(name = "estados")
@Id @Column(name = "codigo_estado", length = 5, nullable = false, unique = true)
@Column(name = "nombre", length = 50, nullable = false, unique = true)
```

### Relaciones
No tiene relaciones definidas en la entidad.

### Métodos del Repositorio
```java
extends JpaRepository<Estado, String>
boolean existsByNombre(String nombre)
Estado findByNombre(String nombre)
```

### Endpoints del Controlador

| Método | Endpoint | Seguridad | Descripción |
|--------|----------|-----------|-------------|
| GET | `/api/estados` | ADMIN, COORD, SUP | Obtener todos los estados |
| GET | `/api/estados/{codigoEstado}` | ADMIN, COORD, SUP | Obtener estado por código |
| GET | `/api/estados/nombre/{nombre}` | ADMIN, COORD, SUP | Obtener estado por nombre |
| GET | `/api/estados/exists/{nombre}` | ADMIN, COORD, SUP | Verificar si existe por nombre |
| POST | `/api/estados` | ADMIN, COORD, SUP | Crear nuevo estado |
| PUT | `/api/estados/{codigoEstado}` | ADMIN, COORD, SUP | Actualizar estado |
| DELETE | `/api/estados/{codigoEstado}` | ADMIN | Eliminar estado |

---

## 4. Departamento

### Descripción
Representa los departamentos geográficos de El Salvador. Se utiliza para ubicación de empresas y proyectos.

### Ubicación del archivo
- **Entidad**: `rpups-ues/src/main/java/com/ues/edu/sv/rpups_ues/model/entity/Departamento.java`
- **Repositorio**: `rpups-ues/src/main/java/com/ues/edu/sv/rpups_ues/model/repository/DepartamentoRepository.java`
- **Controlador**: `rpups-ues/src/main/java/com/ues/edu/sv/rpups_ues/controller/DepartamentoController.java`

### Tabla en BD
`departamentos`

### Campos

| Campo | Tipo Java | Columna BD | Restricciones | Descripción |
|-------|-----------|------------|---------------|-------------|
| `codigo` | `String` | `codigo` | PK, max 2 chars, NOT NULL, UNIQUE | Código único del departamento |
| `nombre` | `String` | `nombre` | max 50 chars, NOT NULL, UNIQUE | Nombre del departamento |

### Anotaciones JPA
```java
@Entity
@Table(name = "departamentos")
@Id @Column(name = "codigo", length = 2, nullable = false, unique = true)
@Column(name = "nombre", length = 50, nullable = false, unique = true)
```

### Relaciones
No tiene relaciones definidas en la entidad (es referenciado por `Municipio`).

### Métodos del Repositorio
```java
extends JpaRepository<Departamento, String>
Departamento findByNombre(String nombre)
boolean existsByNombre(String nombre)
```

### Endpoints del Controlador

| Método | Endpoint | Seguridad | Descripción |
|--------|----------|-----------|-------------|
| GET | `/api/departamentos` | PermitAll | Obtener todos los departamentos |
| GET | `/api/departamentos/{codigo}` | PermitAll | Obtener departamento por código |
| GET | `/api/departamentos/nombre/{nombre}` | PermitAll | Obtener departamento por nombre |
| GET | `/api/departamentos/exists/{nombre}` | PermitAll | Verificar si existe por nombre |
| POST | `/api/departamentos` | ADMIN, COORD, SUP | Crear nuevo departamento |
| PUT | `/api/departamentos/{codigo}` | ADMIN, COORD, SUP | Actualizar departamento |
| DELETE | `/api/departamentos/{codigo}` | ADMIN, COORD, SUP | Eliminar departamento |

---

## 5. DepartamentoCarrera

### Descripción
Representa los departamentos académicos de la universidad (ej: Ingeniería, Ciencias Económicas). Agrupa las carreras por área de estudio.

### Ubicación del archivo
- **Entidad**: `rpups-ues/src/main/java/com/ues/edu/sv/rpups_ues/model/entity/DepartamentoCarrera.java`
- **Repositorio**: `rpups-ues/src/main/java/com/ues/edu/sv/rpups_ues/model/repository/DepartamentoCarreraRepository.java`
- **Controlador**: `rpups-ues/src/main/java/com/ues/edu/sv/rpups_ues/controller/DepartamentoCarreraController.java`

### Tabla en BD
`departamentos_carreras`

### Campos

| Campo | Tipo Java | Columna BD | Restricciones | Descripción |
|-------|-----------|------------|---------------|-------------|
| `idDepartamentoCarrera` | `Long` | `id_depto_carrera` | PK, AUTO_INCREMENT | ID único del departamento de carrera |
| `nombre` | `String` | `nombre` | max 250 chars, NOT NULL, UNIQUE | Nombre del departamento académico |

### Anotaciones JPA
```java
@Entity
@Table(name = "departamentos_carreras")
@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "id_depto_carrera", nullable = true)
@Column(name = "nombre", length = 250, nullable = false, unique = true)
```

### Relaciones
Es referenciado por la entidad `Carrera` (@ManyToOne).

### Métodos del Repositorio
```java
extends JpaRepository<DepartamentoCarrera, Long>
Optional<DepartamentoCarrera> findByNombre(String nombre)
boolean existsByNombre(String nombre)
```

### Endpoints del Controlador

| Método | Endpoint | Seguridad | Descripción |
|--------|----------|-----------|-------------|
| GET | `/api/deptos-carreras/deptos` | PermitAll | Obtener todos los departamentos de carrera |
| GET | `/api/deptos-carreras/{idDepartamentoCarrera}` | PermitAll | Obtener por ID |
| GET | `/api/deptos-carreras/nombre/{nombre}` | PermitAll | Obtener por nombre |
| GET | `/api/deptos-carreras/exists/{nombre}` | PermitAll | Verificar si existe por nombre |
| POST | `/api/deptos-carreras` | ADMIN, COORD, SUP | Crear nuevo departamento de carrera |
| PUT | `/api/deptos-carreras/{idDepartamentoCarrera}` | ADMIN, COORD, SUP | Actualizar departamento de carrera |
| DELETE | `/api/deptos-carreras/{idDepartamentoCarrera}` | ADMIN, COORD, SUP | Eliminar departamento de carrera |

---

## 6. Rubro

### Descripción
Define los rubros o sectores económicos de las empresas (ej: Tecnología, Salud, Educación).

### Ubicación del archivo
- **Entidad**: `rpups-ues/src/main/java/com/ues/edu/sv/rpups_ues/model/entity/Rubro.java`
- **Repositorio**: `rpups-ues/src/main/java/com/ues/edu/sv/rpups_ues/model/repository/RubroRepository.java`
- **Controlador**: `rpups-ues/src/main/java/com/ues/edu/sv/rpups_ues/controller/RubroController.java`

### Tabla en BD
`rubros`

### Campos

| Campo | Tipo Java | Columna BD | Restricciones | Descripción |
|-------|-----------|------------|---------------|-------------|
| `idRubro` | `Long` | `id_rubro` | PK, AUTO_INCREMENT, NOT NULL | ID único del rubro |
| `nombre` | `String` | `nombre` | max 100 chars, NOT NULL, UNIQUE | Nombre del rubro |

### Anotaciones JPA
```java
@Entity
@Table(name = "rubros")
@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "id_rubro", nullable = false)
@NotBlank @Size(min = 1, max = 100)
@Column(nullable = false, unique = true, length = 100)
```

### Relaciones
No tiene relaciones definidas en la entidad (referenciado por `Empresa`).

### Métodos del Repositorio
```java
extends JpaRepository<Rubro, Long>
Optional<Rubro> findByNombre(String nombre)
boolean existsByNombre(String nombre)
```

### Endpoints del Controlador

| Método | Endpoint | Seguridad | Descripción |
|--------|----------|-----------|-------------|
| GET | `/api/rubros` | PermitAll | Obtener todos los rubros |
| GET | `/api/rubros/{id}` | PermitAll | Obtener rubro por ID |
| GET | `/api/rubros/nombre/{nombre}` | PermitAll | Obtener rubro por nombre |
| GET | `/api/rubros/exists/{nombre}` | PermitAll | Verificar si existe por nombre |
| POST | `/api/rubros` | PermitAll | Crear nuevo rubro |
| PUT | `/api/rubros/{id}` | ADMIN, COORD, SUP | Actualizar rubro |
| DELETE | `/api/rubros/{id}` | ADMIN, COORD, SUP | Eliminar rubro |

---

## 7. Municipio

### Descripción
Representa los municipios de El Salvador. Cada municipio pertenece a un departamento geográfico.

### Ubicación del archivo
- **Entidad**: `rpups-ues/src/main/java/com/ues/edu/sv/rpups_ues/model/entity/Municipio.java`
- **Repositorio**: `rpups-ues/src/main/java/com/ues/edu/sv/rpups_ues/model/repository/MunicipioRepository.java`
- **Controlador**: `rpups-ues/src/main/java/com/ues/edu/sv/rpups_ues/controller/MunicipioController.java`

### Tabla en BD
`municipios`

### Campos

| Campo | Tipo Java | Columna BD | Restricciones | Descripción |
|-------|-----------|------------|---------------|-------------|
| `codigo` | `String` | `codigo` | PK, max 4 chars, NOT NULL, UNIQUE | Código único del municipio |
| `nombre` | `String` | `nombre` | max 50 chars, NOT NULL | Nombre del municipio |
| `codigoDepartamento` | `String` | `codigo_departamento` | max 2 chars, NOT NULL | FK al departamento |

### Anotaciones JPA
```java
@Entity
@Table(name = "municipios")
@Id @Column(name = "codigo", length = 4, nullable = false, unique = true)
@Column(name = "nombre", length = 50, nullable = false)
@Column(name = "codigo_departamento", length = 2, nullable = false)
@ManyToOne
@JoinColumn(name = "codigo_departamento", referencedColumnName = "codigo", insertable = false, updatable = false)
@JsonIgnore
```

### Relaciones
| Tipo | Entidad relacionada | Descripción |
|------|---------------------|-------------|
| `@ManyToOne` | `Departamento` | Cada municipio pertenece a un departamento (marcado con `@JsonIgnore`) |

### Métodos del Repositorio
```java
extends JpaRepository<Municipio, String>
List<Municipio> findByCodigoDepartamento(String codigoDepartamento)
boolean existsByNombre(String nombre)
```

### Endpoints del Controlador

| Método | Endpoint | Seguridad | Descripción |
|--------|----------|-----------|-------------|
| GET | `/api/municipios` | PermitAll | Obtener todos los municipios |
| GET | `/api/municipios/{codigo}` | PermitAll | Obtener municipio por código |
| GET | `/api/municipios/departamento/{codigoDepartamento}` | PermitAll | Obtener municipios por departamento |
| GET | `/api/municipios/nombre/{nombre}` | PermitAll | Obtener municipio por nombre |
| GET | `/api/municipios/exists/{nombre}` | PermitAll | Verificar si existe por nombre |
| POST | `/api/municipios` | ADMIN, COORD, SUP | Crear nuevo municipio |
| PUT | `/api/municipios/{codigo}` | ADMIN, COORD, SUP | Actualizar municipio |
| DELETE | `/api/municipios/{codigo}` | ADMIN, COORD, SUP | Eliminar municipio |

---

## 8. Rol

### Descripción
Define los roles de usuario en el sistema (ej: ADMIN, COORD, EST, REP).

### Ubicación del archivo
- **Entidad**: `rpups-ues/src/main/java/com/ues/edu/sv/rpups_ues/model/entity/Rol.java`
- **Repositorio**: `rpups-ues/src/main/java/com/ues/edu/sv/rpups_ues/model/repository/RolRepository.java`
- **Controlador**: `rpups-ues/src/main/java/com/ues/edu/sv/rpups_ues/controller/RolController.java`

### Tabla en BD
`roles`

### Campos

| Campo | Tipo Java | Columna BD | Restricciones | Descripción |
|-------|-----------|------------|---------------|-------------|
| `codigo` | `String` | `codigo` | PK, max 5 chars, NOT NULL, UNIQUE | Código único del rol |
| `nombre` | `String` | `nombre` | max 50 chars, NOT NULL, UNIQUE | Nombre descriptivo del rol |

### Anotaciones JPA
```java
@Entity
@Table(name = "roles")
@Id @Column(name = "codigo", length = 5, nullable = false, unique = true)
@Column(name = "nombre", length = 50, nullable = false, unique = true)
```

### Relaciones
No tiene relaciones definidas en la entidad (referenciado por `Usuario`).

### Métodos del Repositorio
```java
extends JpaRepository<Rol, String>
Rol findByNombre(String nombre)
boolean existsByNombre(String nombre)
```

### Endpoints del Controlador

| Método | Endpoint | Seguridad | Descripción |
|--------|----------|-----------|-------------|
| GET | `/api/roles` | ADMIN, COORD | Obtener todos los roles |
| GET | `/api/roles/{codigo}` | ADMIN, COORD | Obtener rol por código |
| GET | `/api/roles/nombre/{nombre}` | ADMIN, COORD | Obtener rol por nombre |
| GET | `/api/roles/exists/{nombre}` | ADMIN, COORD | Verificar si existe por nombre |
| POST | `/api/roles` | ADMIN | Crear nuevo rol |
| PUT | `/api/roles/{codigo}` | ADMIN | Actualizar rol |
| DELETE | `/api/roles/{codigo}` | ADMIN | Eliminar rol |

---

## Resumen de Entidades

| Entidad | Tabla BD | Tipo PK | Relaciones | Acceso Público |
|---------|----------|---------|------------|----------------|
| Carrera | `carreras` | String (codigo) | → DepartamentoCarrera | ✅ |
| Modalidad | `modalidades` | String (codigo_modalidad) | Ninguna | ✅ |
| Estado | `estados` | String (codigo_estado) | Ninguna | ❌ |
| Departamento | `departamentos` | String (codigo) | ← Municipio | ✅ |
| DepartamentoCarrera | `departamentos_carreras` | Long (AUTO) | ← Carrera | ✅ |
| Rubro | `rubros` | Long (AUTO) | Ninguna | ✅ |
| Municipio | `municipios` | String (codigo) | → Departamento | ✅ |
| Rol | `roles` | String (codigo) | Ninguna | ❌ |

---

## Diagrama de Relaciones

```
┌─────────────────────────┐
│   DepartamentoCarrera   │
│   (departamentos_       │
│    carreras)            │
└───────────┬─────────────┘
            │ 1
            │
            │ *
┌───────────▼─────────────┐
│        Carrera          │
│       (carreras)        │
└─────────────────────────┘

┌─────────────────────────┐
│      Departamento       │
│    (departamentos)      │
└───────────┬─────────────┘
            │ 1
            │
            │ *
┌───────────▼─────────────┐
│       Municipio         │
│      (municipios)       │
└─────────────────────────┘
```

---

## Notas Técnicas

### Lombok
Todas las entidades utilizan Lombok para generar automáticamente:
- `@Getter` / `@Setter`: Getters y setters
- `@NoArgsConstructor`: Constructor sin argumentos
- `@AllArgsConstructor`: Constructor con todos los argumentos
- `@ToString`: Método toString()

### Serialización
Todas las entidades implementan `Serializable`.

### Validación
- Las entidades usan anotaciones de Jakarta Validation (`@NotBlank`, `@Size`, etc.)
- Los controladores retornan `409 CONFLICT` cuando se intenta crear un registro con nombre duplicado

### Seguridad
- Endpoints públicos usan `@PermitAll`
- Endpoints protegidos usan `@Secured` con roles: `ADMIN`, `COORD`, `SUP`, `EST`, `REP`
