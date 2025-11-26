# Entidad: Proyecto

## Descripción

Representa un proyecto de horas sociales disponible para que los estudiantes se postulen. Los proyectos son creados por administradores, típicamente a partir de solicitudes aprobadas.

---

## Campos

| Campo | Tipo | Nullable | Descripción |
|-------|------|----------|-------------|
| `idProyecto` | Long | No | Identificador único (auto-generado) |
| `titulo` | String | No | Título del proyecto (único) |
| `descripcion` | String | No | Descripción detallada |
| `requisitos` | String | Sí | Requisitos para participar |
| `fechaInicio` | LocalDate | No | Fecha de inicio del proyecto |
| `fechaFin` | LocalDate | No | Fecha de finalización |
| `duracion` | Integer | No | Duración en horas |
| `maxEstudiantes` | Integer | No | Número máximo de estudiantes |
| `empresa` | Empresa | No | Empresa/institución que ofrece el proyecto |
| `carrera` | Carrera | No | Carrera a la que aplica |
| `modalidad` | Modalidad | No | Modalidad (presencial, virtual, híbrido) |
| `estado` | Estado | No | Estado actual del proyecto |
| `adminAprobador` | Usuario | Sí | Usuario que aprobó/creó el proyecto |
| `fechaCreacion` | LocalDateTime | No | Fecha de creación (auto) |
| `fechaModificacion` | LocalDateTime | Sí | Última modificación (auto) |

---

## Relaciones

| Tipo | Entidad Relacionada | Descripción |
|------|---------------------|-------------|
| ManyToOne | `Empresa` | Empresa que ofrece el proyecto |
| ManyToOne | `Carrera` | Carrera a la que está dirigido |
| ManyToOne | `Modalidad` | Modalidad de ejecución |
| ManyToOne | `Estado` | Estado actual |
| ManyToOne | `Usuario` | Admin que aprobó |
| OneToMany | `Postulacion` | Postulaciones recibidas |

---

## Estados del Proyecto

| Código | Nombre | Descripción |
|--------|--------|-------------|
| `DISP` | Disponible | Abierto para postulaciones |
| `ENEJ` | En Ejecución | Proyecto en desarrollo |
| `FIN` | Finalizado | Proyecto completado |
| `CANC` | Cancelado | Proyecto cancelado |

---

## Validaciones

| Campo | Validación | Mensaje |
|-------|------------|---------|
| titulo | @NotBlank | "El título es requerido" |
| titulo | @Size(max=200) | "Máximo 200 caracteres" |
| descripcion | @NotBlank | "La descripción es requerida" |
| fechaInicio | @NotNull, @FutureOrPresent | "Fecha válida requerida" |
| fechaFin | @NotNull, @FutureOrPresent | "Fecha válida requerida" |
| duracion | @Min(1) | "Mínimo 1 hora" |
| maxEstudiantes | @Min(1) | "Mínimo 1 estudiante" |

---

## Mapeo JPA

```java
@Entity
@Table(name = "proyecto")
public class Proyecto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProyecto;
    
    @Column(unique = true)
    private String titulo;
    
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
    @JoinColumn(name = "id_admin_aprobador")
    private Usuario adminAprobador;
}
```

---

## Endpoints Relacionados

- `GET /api/proyectos` - Listar todos (paginado)
- `GET /api/proyectos/public` - Listar públicos
- `GET /api/proyectos/{id}` - Obtener por ID
- `GET /api/proyectos/search-filters` - Búsqueda con filtros (admin)
- `GET /api/proyectos/search-filters-disponibles` - Proyectos disponibles
- `GET /api/proyectos/empresa/{id}` - Por empresa
- `POST /api/proyectos` - Crear proyecto
- `PUT /api/proyectos/{id}` - Actualizar
- `DELETE /api/proyectos/{id}` - Eliminar

---

## Queries Personalizadas

### Búsqueda con filtros

```java
@Query("SELECT p FROM Proyecto p WHERE " +
       "(:filter IS NULL OR LOWER(p.titulo) LIKE LOWER(CONCAT('%', :filter, '%')) " +
       "OR LOWER(p.carrera.nombre) LIKE LOWER(CONCAT('%', :filter, '%'))) " +
       "AND (:idDeptoCarrera IS NULL OR p.carrera.departamentoCarrera.idDepartamentoCarrera = :idDeptoCarrera)")
Page<Proyecto> searchByAnyField(String filter, Long idDeptoCarrera, Pageable pageable);
```

### Proyectos disponibles

```java
@Query("... AND p.estado.codigoEstado = 'DISP'")
Page<Proyecto> searchByAnyFieldWithEstadoDisponible(...);
```

---

## Reportes Disponibles

- **Por Estado**: `/api/proyectos/report-estado?codEstado=DISP`
- **Por Carrera**: `/api/proyectos/report-carrera?codCarrera=IS`
- **Por Empresa**: `/api/proyectos/report-empresa?idEmpresa=1`

Los reportes se generan en PDF usando Thymeleaf + OpenHTMLToPDF.

---

## Notas Importantes

1. **Título único**: No pueden existir dos proyectos con el mismo título.

2. **Fechas**: La validación `@FutureOrPresent` puede causar problemas al registrar proyectos históricos.

3. **maxEstudiantes**: Este campo debería validarse al aceptar postulaciones (actualmente no se valida).

4. **adminAprobador**: Se registra quién creó/aprobó el proyecto para auditoría.

---

## Problemas Identificados

- **ERROR-B11**: Validación `@FutureOrPresent` impide fechas pasadas
- **ERROR-B12**: Inconsistencia en mensaje de @Size
- **FEAT-B03**: No se valida maxEstudiantes al crear postulaciones
