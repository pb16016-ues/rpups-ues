# Entidad: Postulacion

## Descripción

Representa la postulación de un estudiante a un proyecto de horas sociales. Registra el interés del estudiante en participar en un proyecto específico.

---

## Campos

| Campo | Tipo | Nullable | Descripción |
|-------|------|----------|-------------|
| `idPostulacion` | Long | No | Identificador único (auto-generado) |
| `estudiante` | Usuario | No | Estudiante que se postula |
| `proyecto` | Proyecto | No | Proyecto al que se postula |
| `fechaPostulacion` | LocalDateTime | No | Fecha de la postulación (auto) |

---

## Relaciones

| Tipo | Entidad Relacionada | Descripción |
|------|---------------------|-------------|
| ManyToOne | `Usuario` | Estudiante que se postula |
| ManyToOne | `Proyecto` | Proyecto destino |

---

## Mapeo JPA

```java
@Entity
@Table(name = "postulacion")
public class Postulacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPostulacion;
    
    @ManyToOne
    @JoinColumn(name = "id_estudiante")
    private Usuario estudiante;
    
    @ManyToOne
    @JoinColumn(name = "id_proyecto")
    private Proyecto proyecto;
    
    private LocalDateTime fechaPostulacion;
}
```

---

## Endpoints Relacionados

- `GET /api/postulaciones` - Listar todas
- `GET /api/postulaciones/{id}` - Obtener por ID
- `GET /api/postulaciones/proyecto/{id}` - Por proyecto
- `GET /api/postulaciones/estudiante/{id}` - Por estudiante
- `GET /api/postulaciones/estudiante/{id}/proyecto/{id}` - Verificar postulación específica
- `POST /api/postulaciones` - Crear postulación
- `DELETE /api/postulaciones/{id}` - Eliminar postulación

---

## Lógica de Negocio

### Crear Postulación (save)

1. Verificar que no exista postulación previa del mismo estudiante al mismo proyecto
2. Si existe, lanzar `IllegalArgumentException`
3. Establecer `fechaPostulacion` automáticamente
4. Guardar postulación

```java
public Postulacion save(Postulacion postulacion) {
    Optional<Postulacion> existente = postulacionRepository
        .findByEstudianteAndProyecto(
            postulacion.getEstudiante().getIdUsuario(),
            postulacion.getProyecto().getIdProyecto()
        );
    
    if (existente.isPresent()) {
        throw new IllegalArgumentException("Ya existe una postulación");
    }
    
    postulacion.setFechaPostulacion(LocalDateTime.now());
    return postulacionRepository.save(postulacion);
}
```

---

## Queries del Repositorio

```java
public interface PostulacionRepository extends JpaRepository<Postulacion, Long> {
    
    List<Postulacion> findByProyecto(Long idProyecto);
    
    List<Postulacion> findByEstudiante(Long idEstudiante);
    
    Optional<Postulacion> findByEstudianteAndProyecto(Long idEstudiante, Long idProyecto);
}
```

---

## Casos de Uso

### 1. Estudiante se postula a proyecto

```json
POST /api/postulaciones
{
  "estudiante": { "idUsuario": 10 },
  "proyecto": { "idProyecto": 5 }
}
```

**Respuesta exitosa**: 201 Created con la postulación creada

**Respuesta error (duplicado)**: 409 Conflict

### 2. Verificar si ya está postulado

```
GET /api/postulaciones/estudiante/10/proyecto/5
```

**Si existe**: 200 OK con la postulación

**Si no existe**: 404 Not Found

### 3. Ver postulantes de un proyecto

```
GET /api/postulaciones/proyecto/5
```

Retorna lista de todas las postulaciones al proyecto.

---

## Notas Importantes

1. **Unicidad**: Un estudiante solo puede postularse una vez a cada proyecto. Esto se valida en el servicio.

2. **Fecha automática**: La fecha de postulación se establece automáticamente al crear.

3. **Sin paginación**: Los endpoints retornan listas completas, podría ser problema con muchas postulaciones.

4. **Eliminación**: Tanto el estudiante como los admins pueden eliminar postulaciones.

---

## Funcionalidades Faltantes

### Estado de Postulación

**Estado actual**: La postulación solo existe o no existe, no hay estados intermedios.

**Lo que debería tener**:
- `PENDIENTE` - Recién creada
- `ACEPTADA` - Admin aprobó al estudiante
- `RECHAZADA` - Admin rechazó al estudiante
- `CANCELADA` - Estudiante canceló su postulación

### Validación de Cupos

**Estado actual**: No se valida si el proyecto tiene cupo disponible.

**Lo que debería hacer**:
```java
// Al aceptar postulación
int aceptadas = postulacionRepository.countByProyectoAndEstado(idProyecto, "ACEPTADA");
if (aceptadas >= proyecto.getMaxEstudiantes()) {
    throw new CupoLlenoException("El proyecto no tiene cupos disponibles");
}
```

### Notificaciones

**Faltante**: Notificar al estudiante cuando su postulación es aceptada/rechazada.

---

## Problemas Identificados

- **ERROR-B08**: `createPostulacion` usa `@PermitAll` (cualquiera puede crear postulaciones)
- **FEAT-B02**: Falta flujo de aceptar/rechazar postulaciones
- **FEAT-B03**: No se valida `maxEstudiantes` del proyecto
- **Falta paginación**: Podría ser problema de rendimiento
- **Falta campo estado**: No se puede gestionar el flujo de asignación

---

## Propuesta de Mejora

### Nueva estructura de Postulacion

```java
@Entity
public class Postulacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPostulacion;
    
    @ManyToOne
    private Usuario estudiante;
    
    @ManyToOne
    private Proyecto proyecto;
    
    private LocalDateTime fechaPostulacion;
    
    // NUEVOS CAMPOS
    @Enumerated(EnumType.STRING)
    private EstadoPostulacion estado; // PENDIENTE, ACEPTADA, RECHAZADA, CANCELADA
    
    private LocalDateTime fechaRespuesta;
    
    @ManyToOne
    private Usuario adminRespondio;
    
    private String observaciones;
}
```

### Nuevos endpoints sugeridos

- `PUT /api/postulaciones/{id}/aceptar` - Admin acepta postulación
- `PUT /api/postulaciones/{id}/rechazar` - Admin rechaza postulación
- `GET /api/postulaciones/proyecto/{id}/aceptadas` - Postulaciones aceptadas de un proyecto
