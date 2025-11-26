# Backend - Features Pendientes

Este documento describe las funcionalidades que faltan por implementar o que están incompletas en el backend.

---

## 🔴 Funcionalidades Críticas (Core del Negocio)

### FEAT-B01: Módulo de Seguimiento de Horas

**Estado**: No implementado

**Descripción**: El sistema no tiene forma de registrar las horas que los estudiantes trabajan en los proyectos.

**Requerimientos**:
- Entidad `RegistroHoras` con campos: idRegistro, estudiante, proyecto, fecha, horasTrabajadas, descripcionActividad, estado
- Endpoints CRUD para registros de horas
- Endpoint para que admin apruebe/rechace registros
- Cálculo de horas totales por estudiante/proyecto

**Impacto**: Sin esta funcionalidad, el sistema no puede cumplir su propósito principal de controlar horas sociales.

---

### FEAT-B02: Asignación Formal de Estudiantes a Proyectos

**Estado**: Parcialmente implementado

**Descripción**: Existe el módulo de postulaciones pero no hay flujo para asignar formalmente a un estudiante.

**Estado actual**: 
- Estudiante se postula ✅
- Admin acepta/rechaza postulación ❌
- Estudiante queda asignado formalmente ❌

**Requerimientos**:
- Campo `estadoPostulacion` en entidad Postulacion (PENDIENTE, ACEPTADA, RECHAZADA)
- Endpoint para cambiar estado de postulación
- Notificación al estudiante cuando es aceptado/rechazado
- Validación de cupo máximo al aceptar

---

### FEAT-B03: Validación de Cupos en Postulaciones

**Estado**: No implementado

**Descripción**: El campo `maxEstudiantes` existe en Proyecto pero no se valida al crear postulaciones.

**Requerimientos**:
- Contar postulaciones aceptadas antes de permitir nueva aceptación
- Retornar error si se excede el límite
- Mostrar cupos disponibles en el endpoint de proyectos

---

### FEAT-B04: Flujo Automático Solicitud → Proyecto

**Estado**: No implementado

**Descripción**: Cuando una solicitud es aprobada, el proyecto debe crearse automáticamente.

**Estado actual**: El admin debe crear el proyecto manualmente después de aprobar.

**Requerimientos**:
- Al cambiar estado de solicitud a "APROBADO", crear Proyecto automáticamente
- Copiar datos de la solicitud al nuevo proyecto
- Mantener referencia solicitud → proyecto

---

### FEAT-B05: Gestión de Convenios con Empresas

**Estado**: No implementado

**Descripción**: No hay registro de convenios formales entre la universidad y las empresas.

**Requerimientos**:
- Entidad `Convenio` con: empresa, fechaInicio, fechaFin, estado, documentoURL
- Validar que empresa tenga convenio vigente antes de aprobar proyectos
- Alertas de convenios próximos a vencer

---

## 🟠 Funcionalidades Importantes

### FEAT-B06: Certificados de Finalización

**Estado**: No implementado

**Descripción**: El sistema no genera certificados cuando un estudiante completa sus horas.

**Requerimientos**:
- Template de certificado en PDF
- Endpoint para generar certificado por estudiante
- Validar que el estudiante haya completado las horas requeridas
- Firma digital o código de verificación

---

### FEAT-B07: Sistema de Notificaciones

**Estado**: Parcialmente implementado (solo email)

**Descripción**: Solo existe envío de email para recuperación de contraseña.

**Requerimientos**:
- Notificación al crear solicitud (al admin)
- Notificación al aprobar/rechazar solicitud (al creador)
- Notificación al aceptar postulación (al estudiante)
- Notificación de recordatorio de horas pendientes

---

### FEAT-B08: Auditoría de Cambios

**Estado**: No implementado

**Descripción**: No hay registro de quién modificó qué y cuándo.

**Requerimientos**:
- Tabla de auditoría con: entidad, idEntidad, acción, usuarioId, fecha, datosAnteriores, datosNuevos
- Interceptor JPA para capturar cambios automáticamente
- Endpoint para consultar historial de cambios

---

### FEAT-B09: Supervisores por Proyecto

**Estado**: No implementado

**Descripción**: No hay forma de asignar supervisores específicos a proyectos.

**Estado actual**: El revisor de solicitud queda como adminRevisor, pero no hay supervisor de ejecución.

**Requerimientos**:
- Relación ManyToMany entre Proyecto y Usuario (supervisores)
- Endpoint para asignar/desasignar supervisores
- Los supervisores solo ven proyectos asignados

---

### FEAT-B10: Reportes Adicionales

**Estado**: Parcialmente implementado

**Descripción**: Existen reportes por estado, carrera y empresa. Faltan otros.

**Reportes faltantes**:
- Reporte de horas por estudiante
- Reporte de estudiantes por proyecto
- Reporte de proyectos finalizados en periodo
- Reporte consolidado anual
- Exportación a Excel (actualmente solo PDF)

---

## 🟡 Mejoras Sugeridas

### FEAT-B11: Documentación de API (OpenAPI/Swagger)

**Estado**: No implementado

**Descripción**: No hay documentación automática del API.

**Beneficios**:
- Documentación interactiva para frontend developers
- Testing de endpoints desde navegador
- Generación automática de clientes

**Implementación**: Agregar dependencia `springdoc-openapi-starter-webmvc-ui`

---

### FEAT-B12: Carga de Documentos/Archivos

**Estado**: No implementado

**Descripción**: No se pueden adjuntar archivos a solicitudes o proyectos.

**Casos de uso**:
- Carta de convenio de empresa
- Documentos del estudiante (DUI, carnet)
- Reportes de avance
- Evidencias de trabajo

**Requerimientos**:
- Servicio de almacenamiento (local o S3)
- Entidad `Documento` con metadata
- Endpoints para upload/download
- Validación de tipos de archivo permitidos

---

### FEAT-B13: Rate Limiting

**Estado**: No implementado

**Descripción**: No hay protección contra abuso del API.

**Requerimientos**:
- Límite de peticiones por IP/usuario
- Límite específico en endpoint de login (prevenir fuerza bruta)
- Headers de rate limit en respuestas

**Implementación sugerida**: Bucket4j

---

### FEAT-B14: Health Checks

**Estado**: No implementado

**Descripción**: No hay endpoints para verificar estado del sistema.

**Requerimientos**:
- Endpoint `/actuator/health` con estado de BD, email, etc.
- Métricas de uso (`/actuator/metrics`)

**Implementación**: Agregar `spring-boot-starter-actuator`

---

### FEAT-B15: Configuración Multi-Ambiente

**Estado**: No implementado

**Descripción**: Solo existe `application.properties` para desarrollo.

**Requerimientos**:
- `application-dev.properties` - Desarrollo local
- `application-staging.properties` - Ambiente de pruebas
- `application-prod.properties` - Producción
- Perfiles de Maven para cada ambiente

---

### FEAT-B16: Tests Automatizados

**Estado**: Mínimamente implementado

**Descripción**: Solo existe un test básico que verifica que el contexto carga.

**Requerimientos mínimos**:
- Tests unitarios para servicios
- Tests de integración para controladores
- Tests de repositorios
- Cobertura mínima del 60%

**Frameworks**: JUnit 5, Mockito, TestContainers (para BD)

---

## Flujos de Negocio Incompletos

### Flujo: Registro de Usuario

| Paso | Estado | Descripción |
|------|--------|-------------|
| 1. Llenar formulario | ✅ | Funciona |
| 2. Validar datos | ⚠️ | Validación de carnet comentada |
| 3. Verificar email único | ✅ | Funciona |
| 4. Crear usuario | ✅ | Funciona |
| 5. Enviar email de confirmación | ❌ | No implementado |
| 6. Activar cuenta | ❌ | No implementado |

### Flujo: Solicitud de Proyecto

| Paso | Estado | Descripción |
|------|--------|-------------|
| 1. Crear solicitud | ✅ | Funciona |
| 2. Admin recibe notificación | ❌ | No implementado |
| 3. Admin asigna revisor | ✅ | Funciona |
| 4. Revisor revisa | ✅ | Funciona |
| 5. Aprobar/Rechazar | ✅ | Funciona |
| 6. Notificar creador | ❌ | No implementado |
| 7. Crear proyecto automático | ❌ | No implementado |

### Flujo: Postulación a Proyecto

| Paso | Estado | Descripción |
|------|--------|-------------|
| 1. Estudiante se postula | ✅ | Funciona |
| 2. Validar cupo disponible | ❌ | No implementado |
| 3. Admin revisa postulantes | ⚠️ | Solo puede ver, no gestionar |
| 4. Aceptar/Rechazar | ❌ | No implementado |
| 5. Notificar estudiante | ❌ | No implementado |
| 6. Asignar formalmente | ❌ | No implementado |

### Flujo: Ejecución de Proyecto

| Paso | Estado | Descripción |
|------|--------|-------------|
| 1. Iniciar proyecto | ✅ | Cambiar estado a "En Ejecución" |
| 2. Registrar horas | ❌ | No implementado |
| 3. Supervisor valida | ❌ | No implementado |
| 4. Completar horas | ❌ | No implementado |
| 5. Finalizar proyecto | ⚠️ | Estado existe, sin validación |
| 6. Generar certificado | ❌ | No implementado |

---

## Resumen de Features

| ID | Prioridad | Descripción | Esfuerzo |
|----|-----------|-------------|----------|
| B01 | 🔴 Crítico | Seguimiento de horas | 2 semanas |
| B02 | 🔴 Crítico | Asignación de estudiantes | 1 semana |
| B03 | 🔴 Crítico | Validación de cupos | 2 días |
| B04 | 🔴 Crítico | Flujo solicitud→proyecto | 3 días |
| B05 | 🟠 Alto | Gestión de convenios | 1 semana |
| B06 | 🟠 Alto | Certificados | 1 semana |
| B07 | 🟠 Alto | Notificaciones | 1 semana |
| B08 | 🟠 Alto | Auditoría | 3 días |
| B09 | 🟠 Alto | Supervisores por proyecto | 2 días |
| B10 | 🟡 Medio | Reportes adicionales | 1 semana |
| B11 | 🟡 Medio | OpenAPI/Swagger | 1 día |
| B12 | 🟡 Medio | Carga de documentos | 1 semana |
| B13 | 🟢 Bajo | Rate limiting | 1 día |
| B14 | 🟢 Bajo | Health checks | 1 día |
| B15 | 🟢 Bajo | Multi-ambiente | 2 días |
| B16 | 🟢 Bajo | Tests automatizados | 2 semanas |
