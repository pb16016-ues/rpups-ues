# Backend - Errores e Inconsistencias

Este documento detalla los errores e inconsistencias identificados en el código del backend, organizados por severidad.

---

## 🔴 Errores Críticos (Seguridad)

### ERROR-B01: SECRET_KEY Hardcodeada y Débil

**Archivo**: `src/main/java/com/ues/edu/sv/rpups_ues/auth/JwtService.java`  
**Línea**: 22

**Descripción**: La clave secreta para firmar tokens JWT está hardcodeada en el código fuente y utiliza una cadena predecible.

**Código actual**:
```java
private static final String SECRET_KEY = "UniversidadDeElSalvadorFacultadMultidisciplinariaDeOccidenteHorasSociales";
```

**Impacto**: Un atacante podría generar tokens válidos si conoce esta clave, comprometiendo toda la autenticación del sistema.

**Solución recomendada**: Mover a variable de entorno y usar clave generada aleatoriamente de al menos 256 bits.

---

### ERROR-B02: CORS Sin Restricciones

**Archivo**: `src/main/java/com/ues/edu/sv/rpups_ues/auth/SecurityConfig.java`  
**Líneas**: 45-50

**Descripción**: La configuración CORS permite cualquier origen, método y header.

**Código actual**:
```java
configuration.addAllowedOrigin("*");
configuration.addAllowedMethod("*");
configuration.addAllowedHeader("*");
```

**Impacto**: Cualquier sitio web malicioso puede hacer peticiones al API en nombre de un usuario autenticado (CSRF).

**Solución recomendada**: Especificar orígenes permitidos explícitamente.

---

### ERROR-B03: Credenciales de Email Expuestas

**Archivo**: `src/main/resources/application.properties`  
**Línea**: 25

**Descripción**: El password de la cuenta de Gmail para envío de correos está en texto plano.

**Impacto**: Si el repositorio es público o accedido por terceros, las credenciales quedan expuestas.

**Solución recomendada**: Usar variables de entorno `${MAIL_PASSWORD}`.

---

### ERROR-B04: Credenciales de Base de Datos Expuestas

**Archivo**: `src/main/resources/application.properties`  
**Líneas**: 13-14

**Descripción**: Usuario y contraseña de MySQL en texto plano.

**Impacto**: Acceso no autorizado a la base de datos si el código se filtra.

**Solución recomendada**: Usar variables de entorno `${DB_USERNAME}` y `${DB_PASSWORD}`.

---

### ERROR-B05: Endpoint createSolicitud Sin Protección

**Archivo**: `src/main/java/com/ues/edu/sv/rpups_ues/controller/SolicitudProyectoController.java`  
**Línea**: 48

**Descripción**: El endpoint para crear solicitudes usa `@PermitAll`, permitiendo que usuarios no autenticados creen solicitudes.

**Impacto**: Cualquier persona puede crear solicitudes de proyecto sin autenticarse, posible spam o abuso.

**Solución recomendada**: Cambiar a `@Secured({"ESTUD", "EMP"})` o verificar autenticación.

---

### ERROR-B06: Endpoints PUT de Solicitud Sin Protección

**Archivo**: `src/main/java/com/ues/edu/sv/rpups_ues/controller/SolicitudProyectoController.java`  
**Líneas**: 152, 188

**Descripción**: Los endpoints `updateSolicitudAdmin` y `updateSolicitudExterno` usan `@PermitAll`.

**Impacto**: Cualquier usuario puede modificar cualquier solicitud sin verificar permisos.

**Solución recomendada**: 
- `updateSolicitudAdmin`: `@Secured({"ADMIN", "COORD", "SUP"})`
- `updateSolicitudExterno`: Verificar que el usuario sea el creador

---

### ERROR-B07: CRUD de Empresas Sin Protección

**Archivo**: `src/main/java/com/ues/edu/sv/rpups_ues/controller/EmpresaController.java`  
**Líneas**: 55, 62

**Descripción**: Los endpoints `createEmpresa` y `updateEmpresa` usan `@PermitAll`.

**Impacto**: Cualquier persona puede crear o modificar datos de empresas.

**Solución recomendada**: Restringir a usuarios autenticados y verificar propiedad.

---

### ERROR-B08: Crear Postulación Sin Autenticación

**Archivo**: `src/main/java/com/ues/edu/sv/rpups_ues/controller/PostulacionController.java`  
**Línea**: 48

**Descripción**: El endpoint `createPostulacion` usa `@PermitAll`.

**Impacto**: Se pueden crear postulaciones falsas sin verificar que el estudiante existe o está autenticado.

**Solución recomendada**: `@Secured({"ESTUD"})` y verificar que el usuario autenticado es el estudiante de la postulación.

---

## 🟠 Errores de Lógica

### ERROR-B09: Path Variable Incorrecto en updateSolicitudAdmin

**Archivo**: `src/main/java/com/ues/edu/sv/rpups_ues/controller/SolicitudProyectoController.java`  
**Línea**: 152

**Descripción**: El endpoint es `/admin/{id}` pero el nombre del parámetro puede confundirse con `idAdmin`.

**Código actual**:
```java
@PutMapping("/admin/{id}")
public ResponseEntity<?> updateSolicitudAdmin(@PathVariable Long id, ...)
```

**Impacto**: Confusión en el código, el `id` se refiere a la solicitud, no al admin.

**Solución recomendada**: Renombrar a `/solicitud/{idSolicitud}` o documentar claramente.

---

### ERROR-B10: Validación de Carnet Comentada

**Archivo**: `src/main/java/com/ues/edu/sv/rpups_ues/model/entity/Usuario.java`  
**Líneas**: 30-31

**Descripción**: La validación `@Pattern` para el carnet está comentada.

**Impacto**: Se pueden registrar carnets con formato inválido.

**Solución recomendada**: Descomentar la validación o eliminar el comentario si no aplica.

---

### ERROR-B11: Validación de Fechas Incorrecta en Proyecto

**Archivo**: `src/main/java/com/ues/edu/sv/rpups_ues/model/entity/Proyecto.java`  
**Líneas**: 47-50

**Descripción**: Las anotaciones `@FutureOrPresent` en `fechaInicio` y `fechaFin` impiden crear proyectos con fechas pasadas.

**Impacto**: No se pueden registrar proyectos históricos o migrar datos existentes.

**Solución recomendada**: Remover la validación o aplicarla solo en creación, no en actualización.

---

### ERROR-B12: Inconsistencia en @Size de descripción

**Archivo**: `src/main/java/com/ues/edu/sv/rpups_ues/model/entity/Proyecto.java`  
**Línea**: 26

**Descripción**: El mensaje dice "máximo 150 caracteres" pero el `max` puede ser diferente.

**Impacto**: Mensaje de error confuso para el usuario.

**Solución recomendada**: Verificar y alinear el mensaje con el valor real de `max`.

---

### ERROR-B13: Inconsistencia en @Size de teléfono

**Archivo**: `src/main/java/com/ues/edu/sv/rpups_ues/model/entity/Empresa.java`  
**Líneas**: 47-48

**Descripción**: El mensaje dice "máximo 15 caracteres" pero la columna de BD puede tener diferente tamaño.

**Impacto**: Posible truncamiento de datos o errores de validación inesperados.

**Solución recomendada**: Alinear validación con definición de columna.

---

### ERROR-B14: Token JWT con Expiración Muy Corta

**Archivo**: `src/main/java/com/ues/edu/sv/rpups_ues/auth/JwtService.java`  
**Línea**: 23

**Descripción**: `EXPIRATION_TIME = 3_600_000` (1 hora) es muy corto para uso normal.

**Impacto**: Los usuarios deben volver a iniciar sesión frecuentemente, mala experiencia de usuario.

**Solución recomendada**: Aumentar a 8-24 horas o implementar refresh token.

---

### ERROR-B15: Métodos Sin Paginación

**Archivo**: `src/main/java/com/ues/edu/sv/rpups_ues/service/ProyectoService.java`

**Descripción**: Varios métodos como `findByCarrera`, `findByModalidad` retornan `List` sin paginación.

**Impacto**: Problemas de rendimiento si hay muchos registros.

**Solución recomendada**: Agregar versiones paginadas de estos métodos.

---

### ERROR-B16: Status Code Incorrecto en GlobalExceptionHandler

**Archivo**: `src/main/java/com/ues/edu/sv/rpups_ues/exceptions/GlobalExceptionHandler.java`  
**Líneas**: 24-26

**Descripción**: Retorna `HttpStatus.BAD_REQUEST` pero el `ErrorResponse` interno tiene status 500.

**Impacto**: Inconsistencia entre el código HTTP y el cuerpo de la respuesta.

**Solución recomendada**: Alinear ambos valores.

---

## 🟡 Advertencias (Código)

### WARN-B01: Código Duplicado en Reportes

**Archivos**: 
- `ProyectoServiceImpl.java`
- `SolicitudProyectoServiceImpl.java`

**Descripción**: La lógica de generación de PDF es casi idéntica en ambos servicios.

**Solución recomendada**: Extraer a un servicio común `ReporteService`.

---

### WARN-B02: System.out.println en lugar de Logger

**Archivos**: Varios controladores y servicios

**Descripción**: Se usa `System.out.println` para logging en lugar de un framework apropiado.

**Solución recomendada**: Usar SLF4J con Logback.

---

### WARN-B03: Falta de Soft Delete Consistente

**Archivos**: Varios repositorios y servicios

**Descripción**: Algunas entidades usan soft delete (campo `activo`) y otras usan delete real.

**Solución recomendada**: Implementar soft delete consistente con `@SQLDelete` y `@Where`.

---

## Resumen de Errores

| ID | Severidad | Archivo | Descripción |
|----|-----------|---------|-------------|
| B01 | 🔴 Crítico | JwtService.java | SECRET_KEY hardcodeada |
| B02 | 🔴 Crítico | SecurityConfig.java | CORS sin restricciones |
| B03 | 🔴 Crítico | application.properties | Credenciales email |
| B04 | 🔴 Crítico | application.properties | Credenciales BD |
| B05 | 🔴 Crítico | SolicitudProyectoController | createSolicitud sin auth |
| B06 | 🔴 Crítico | SolicitudProyectoController | PUT sin protección |
| B07 | 🔴 Crítico | EmpresaController | CRUD sin protección |
| B08 | 🔴 Crítico | PostulacionController | POST sin auth |
| B09 | 🟠 Alto | SolicitudProyectoController | Path variable confuso |
| B10 | 🟠 Alto | Usuario.java | Validación comentada |
| B11 | 🟠 Alto | Proyecto.java | Validación fechas |
| B12 | 🟡 Medio | Proyecto.java | Mensaje @Size |
| B13 | 🟡 Medio | Empresa.java | Mensaje @Size |
| B14 | 🟡 Medio | JwtService.java | Token expiración corta |
| B15 | 🟡 Medio | ProyectoService.java | Falta paginación |
| B16 | 🟡 Medio | GlobalExceptionHandler | Status inconsistente |
