# Entidad: SolicitudProyecto

## Descripción

Representa una solicitud de proyecto enviada por estudiantes o representantes de empresa. Las solicitudes pasan por un proceso de revisión antes de ser aprobadas y convertirse en proyectos.

---

## Campos

| Campo | Tipo | Nullable | Descripción |
|-------|------|----------|-------------|
| `idSolicitud` | Long | No | Identificador único (auto-generado) |
| `titulo` | String | No | Título propuesto para el proyecto |
| `descripcion` | String | No | Descripción detallada |
| `requisitos` | String | Sí | Requisitos sugeridos |
| `duracion` | Integer | No | Duración estimada en horas |
| `maxEstudiantes` | Integer | No | Estudiantes sugeridos |
| `empresa` | Empresa | Sí | Empresa que propone (si aplica) |
| `carrera` | Carrera | No | Carrera destino |
| `modalidad` | Modalidad | No | Modalidad sugerida |
| `estado` | Estado | No | Estado de la solicitud |
| `userCreador` | Usuario | No | Usuario que creó la solicitud |
| `adminRevisor` | Usuario | Sí | Admin asignado para revisar |
| `observaciones` | String | Sí | Observaciones del revisor |
| `fechaCreacion` | LocalDateTime | No | Fecha de creación (auto) |
| `fechaRevision` | LocalDateTime | Sí | Fecha de revisión |

---

## Relaciones

| Tipo | Entidad Relacionada | Descripción |
|------|---------------------|-------------|
| ManyToOne | `Empresa` | Empresa que propone |
| ManyToOne | `Carrera` | Carrera destino |
| ManyToOne | `Modalidad` | Modalidad sugerida |
| ManyToOne | `Estado` | Estado actual |
| ManyToOne | `Usuario` (userCreador) | Quien creó la solicitud |
| ManyToOne | `Usuario` (adminRevisor) | Quien revisa/revisó |

---

## Estados de la Solicitud

| Código | Nombre | Descripción |
|--------|--------|-------------|
| `PEND` | Pendiente | Recién creada, sin asignar |
| `REV` | En Revisión | Asignada a un revisor |
| `APR` | Aprobada | Solicitud aprobada |
| `RECH` | Rechazada | Solicitud rechazada |
| `CORREG` | En Corrección | Devuelta para correcciones |

---

## Flujo de Estados

```
PEND → REV → APR → (Se crea Proyecto)
         ↓
       RECH
         ↓
      CORREG → REV → ...
```

---

## Validaciones

| Campo | Validación | Mensaje |
|-------|------------|---------|
| titulo | @NotBlank | "El título es requerido" |
| titulo | @Size(max=200) | "Máximo 200 caracteres" |
| descripcion | @NotBlank | "La descripción es requerida" |
| duracion | @Min(1) | "Mínimo 1 hora" |
| maxEstudiantes | @Min(1) | "Mínimo 1 estudiante" |

---

## Mapeo JPA

```java
@Entity
@Table(name = "solicitud_proyecto")
public class SolicitudProyecto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSolicitud;
    
    @ManyToOne
    @JoinColumn(name = "id_empresa")
    private Empresa empresa;
    
    @ManyToOne
    @JoinColumn(name = "codigo_carrera")
    private Carrera carrera;
    
    @ManyToOne
    @JoinColumn(name = "codigo_modalidad")
    private Modalidad modalidad;
    
    @ManyToOne
    @JoinColumn(name = "codigo_estado")
    private Estado estado;
    
    @ManyToOne
    @JoinColumn(name = "id_user_creador")
    private Usuario userCreador;
    
    @ManyToOne
    @JoinColumn(name = "id_admin_revisor")
    private Usuario adminRevisor;
}
```

---

## Endpoints Relacionados

### Consultas
- `GET /api/solicitudes-proyectos` - Listar todas (paginado)
- `GET /api/solicitudes-proyectos/{id}` - Obtener por ID
- `GET /api/solicitudes-proyectos/estado-revision` - En estado revisión
- `GET /api/solicitudes-proyectos/user-creador/{id}` - Por usuario creador
- `GET /api/solicitudes-proyectos/admin-revisor/{id}` - Asignadas a revisor
- `GET /api/solicitudes-proyectos/empresa/{id}` - Por empresa
- `GET /api/solicitudes-proyectos/search-filters` - Búsqueda con filtros

### Mutaciones
- `POST /api/solicitudes-proyectos` - Crear solicitud
- `PUT /api/solicitudes-proyectos/admin/{id}` - Actualizar (admin)
- `PUT /api/solicitudes-proyectos/externo/{id}` - Actualizar (creador)
- `DELETE /api/solicitudes-proyectos/{id}` - Eliminar

---

## Queries Personalizadas

### Búsqueda con filtros

```java
@Query("SELECT s FROM SolicitudProyecto s WHERE " +
       "(:filter IS NULL OR LOWER(s.titulo) LIKE LOWER(CONCAT('%', :filter, '%')) " +
       "OR LOWER(s.carrera.nombre) LIKE LOWER(CONCAT('%', :filter, '%')) " +
       "OR LOWER(s.estado.nombre) LIKE LOWER(CONCAT('%', :filter, '%'))) " +
       "AND (:idDeptoCarrera IS NULL OR s.carrera.departamentoCarrera.idDepartamentoCarrera = :idDeptoCarrera)")
Page<SolicitudProyecto> searchByAnyField(String filter, Long idDeptoCarrera, Pageable pageable);
```

### Por usuario con filtros

```java
Page<SolicitudProyecto> searchByAnyFieldAndUser(String filter, Long idUserCreador, Long idDeptoCarrera, Pageable pageable);
```

---

## Reportes Disponibles

- **Por Estado**: `/api/solicitudes-proyectos/report-estado?codEstado=REV`
- **Por Carrera**: `/api/solicitudes-proyectos/report-carrera?codCarrera=IS`
- **Por Empresa**: `/api/solicitudes-proyectos/report-empresa?idEmpresa=1`

---

## Lógica de Negocio

### Crear Solicitud (save)
1. Se establecen fechaCreacion como null (se auto-genera)
2. Se limpia fechaRevision, observaciones, adminRevisor
3. Se asigna estado `PEND` automáticamente

### Actualizar por Admin (updateAdmin)
1. Permite cambiar: estado, adminRevisor, observaciones, fechaRevision
2. Al aprobar, debería crear proyecto automáticamente (no implementado)
3. Al rechazar, se guardan observaciones

### Actualizar por Externo (updateExterno)
1. Solo permite modificar datos del proyecto propuesto
2. No puede cambiar estado ni revisor

---

## Notas Importantes

1. **Estado inicial**: Siempre se crea con estado `PEND`, independiente de lo que envíe el cliente.

2. **userCreador**: Se guarda quién creó la solicitud para control y notificaciones.

3. **adminRevisor**: Se asigna cuando un admin toma la solicitud para revisión.

4. **Observaciones**: El revisor puede dejar comentarios, especialmente al rechazar.

5. **Email**: Al cambiar estado se debería notificar al creador (parcialmente implementado).

---

## Problemas Identificados

- **ERROR-B05**: `createSolicitud` usa `@PermitAll` (sin autenticación)
- **ERROR-B06**: `updateSolicitudAdmin` y `updateSolicitudExterno` usan `@PermitAll`
- **ERROR-B09**: Path `/admin/{id}` puede confundir el parámetro
- **FEAT-B04**: No se crea proyecto automáticamente al aprobar
- **FEAT-B07**: Falta notificación al cambiar estado
