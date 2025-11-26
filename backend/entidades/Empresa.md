# Entidad: Empresa

## Descripción

Representa una empresa o institución que puede ofrecer proyectos de horas sociales. Las empresas son registradas por representantes que luego pueden crear solicitudes de proyectos.

---

## Campos

| Campo | Tipo | Nullable | Descripción |
|-------|------|----------|-------------|
| `idEmpresa` | Long | No | Identificador único (auto-generado) |
| `nombreComercial` | String | No | Nombre comercial (único) |
| `nombreLegal` | String | No | Razón social (único) |
| `direccion` | String | No | Dirección física |
| `telefono` | String | Sí | Teléfono de la empresa |
| `correo` | String | Sí | Correo de la empresa |
| `contactoNombre` | String | Sí | Nombre del contacto |
| `contactoTelefono` | String | Sí | Teléfono del contacto |
| `contactoCorreo` | String | Sí | Correo del contacto |
| `activo` | Boolean | No | Estado de la empresa (soft delete) |
| `rubro` | Rubro | No | Rubro/sector de la empresa |
| `departamento` | Departamento | No | Departamento geográfico |
| `municipio` | Municipio | No | Municipio |
| `userCreador` | Usuario | No | Representante que registró |

---

## Relaciones

| Tipo | Entidad Relacionada | Descripción |
|------|---------------------|-------------|
| ManyToOne | `Rubro` | Sector empresarial |
| ManyToOne | `Departamento` | Ubicación - departamento |
| ManyToOne | `Municipio` | Ubicación - municipio |
| ManyToOne | `Usuario` | Representante que creó |
| OneToMany | `Proyecto` | Proyectos de la empresa |
| OneToMany | `SolicitudProyecto` | Solicitudes de la empresa |

---

## Validaciones

| Campo | Validación | Mensaje |
|-------|------------|---------|
| nombreComercial | @NotBlank | "Nombre comercial requerido" |
| nombreComercial | @Size(max=150) | "Máximo 150 caracteres" |
| nombreLegal | @NotBlank | "Nombre legal requerido" |
| nombreLegal | @Size(max=200) | "Máximo 200 caracteres" |
| direccion | @NotBlank | "Dirección requerida" |
| telefono | @Size(max=15) | "Máximo 15 caracteres" |
| correo | @Email | "Correo válido" |

---

## Mapeo JPA

```java
@Entity
@Table(name = "empresa")
public class Empresa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEmpresa;
    
    @Column(unique = true)
    private String nombreComercial;
    
    @Column(unique = true)
    private String nombreLegal;
    
    private String direccion;
    private String telefono;
    private String correo;
    
    private String contactoNombre;
    private String contactoTelefono;
    private String contactoCorreo;
    
    private Boolean activo = true;
    
    @ManyToOne
    @JoinColumn(name = "id_rubro")
    private Rubro rubro;
    
    @ManyToOne
    @JoinColumn(name = "id_departamento")
    private Departamento departamento;
    
    @ManyToOne
    @JoinColumn(name = "id_municipio")
    private Municipio municipio;
    
    @ManyToOne
    @JoinColumn(name = "id_user_creador")
    private Usuario userCreador;
}
```

---

## Endpoints Relacionados

- `GET /api/empresas` - Listar todas
- `GET /api/empresas/{id}` - Obtener por ID
- `GET /api/empresas/rubro/{id}` - Por rubro
- `GET /api/empresas/user-creador/{id}` - Por usuario creador
- `GET /api/empresas/exists/nombreComercial?nombre=X` - Verificar nombre comercial
- `GET /api/empresas/exists/nombreLegal?nombre=X` - Verificar nombre legal
- `POST /api/empresas` - Crear empresa
- `PUT /api/empresas/{id}` - Actualizar empresa
- `DELETE /api/empresas/{id}` - Desactivar empresa

---

## Lógica de Negocio

### Crear Empresa

1. Verificar que nombreComercial no exista
2. Verificar que nombreLegal no exista
3. Establecer `activo = true`
4. Registrar `userCreador`
5. Guardar empresa

### Desactivar Empresa (soft delete)

1. Buscar empresa por ID
2. Establecer `activo = false`
3. Los proyectos y solicitudes de la empresa permanecen

---

## Casos de Uso

### 1. Registrar empresa

```json
POST /api/empresas
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
  "departamento": { "idDepartamento": 2 },
  "municipio": { "idMunicipio": 25 },
  "userCreador": { "idUsuario": 5 }
}
```

### 2. Verificar si nombre existe

```
GET /api/empresas/exists/nombreComercial?nombre=Tech%20Solutions
```

Retorna: `true` o `false`

### 3. Obtener empresas del representante

```
GET /api/empresas/user-creador/5
```

---

## Notas Importantes

1. **Nombres únicos**: Tanto `nombreComercial` como `nombreLegal` deben ser únicos en el sistema.

2. **Soft Delete**: Se usa el campo `activo` para "eliminar" empresas sin borrar datos históricos.

3. **userCreador**: El representante que registró la empresa. Una empresa puede tener un solo creador.

4. **Datos de contacto**: Son opcionales pero recomendados para comunicación.

5. **Ubicación**: Se requiere departamento y municipio para reportes geográficos.

---

## Problemas Identificados

- **ERROR-B07**: Los endpoints `POST` y `PUT` usan `@PermitAll`, cualquiera puede crear/modificar empresas
- **ERROR-B13**: Inconsistencia en validación @Size de teléfono
- **Falta validación**: No se verifica que el municipio pertenezca al departamento seleccionado
- **Falta paginación**: El listado de empresas no está paginado

---

## Funcionalidades Faltantes

### Gestión de Convenios

La empresa debería tener un convenio vigente con la universidad para poder ofrecer proyectos.

**Propuesta**:
```java
@OneToMany(mappedBy = "empresa")
private List<Convenio> convenios;

// Validar antes de aprobar solicitud
boolean tieneConvenioVigente = empresa.getConvenios().stream()
    .anyMatch(c -> c.getFechaFin().isAfter(LocalDate.now()));
```

### Múltiples Representantes

Actualmente solo hay un `userCreador`, pero una empresa podría tener varios representantes.

**Propuesta**:
```java
@ManyToMany
@JoinTable(name = "empresa_representante")
private Set<Usuario> representantes;
```

### Documentos de Empresa

Adjuntar documentos como NIT, escritura, convenio firmado.

---

## Relación con Otras Entidades

```
Empresa
   │
   ├── Rubro (sector empresarial)
   │
   ├── Departamento/Municipio (ubicación)
   │
   ├── Usuario userCreador (representante)
   │
   ├──> Proyecto (proyectos ofrecidos)
   │
   └──> SolicitudProyecto (solicitudes creadas)
```
