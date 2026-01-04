# FEATURES_2.md — Correcciones y Mejoras Pendientes

**Fecha de creación:** 10 de diciembre de 2025  
**Branch:** `feature/fix-updates`  
**Estado:** Pendiente de implementación

---

## Índice

1. [Gestión de Solicitudes — Tabs por Rol](#1-gestión-de-solicitudes--tabs-por-rol)
2. [Gestión de Postulaciones — Ver Detalles Post-Aprobación](#2-gestión-de-postulaciones--ver-detalles-post-aprobación)
3. [Gestión de Proyectos — Recálculo de Cupos](#3-gestión-de-proyectos--recálculo-de-cupos)
4. [Gestión de Revisión — Asignación y Observaciones](#4-gestión-de-revisión--asignación-y-observaciones)
5. [Coordinador — Completar Flujo de Proyectos](#5-coordinador--completar-flujo-de-proyectos)
6. [Supervisor — Creación de Proyectos Propios](#6-supervisor--creación-de-proyectos-propios)
7. [Representante Empresa — Mi Empresa No Se Actualiza](#7-representante-empresa--mi-empresa-no-se-actualiza)
8. [Representante Empresa — Restricción de Empresa en Solicitud](#8-representante-empresa--restricción-de-empresa-en-solicitud)
9. [Representante Empresa — Acciones CRUD en Solicitudes](#9-representante-empresa--acciones-crud-en-solicitudes)
10. [Representante Empresa — Segregación de Dashboard y Sidebar](#10-representante-empresa--segregación-de-dashboard-y-sidebar)
11. [Modal "Detalle de Solicitud" — Botón Corregir y Reenviar](#11-modal-detalle-de-solicitud--botón-corregir-y-reenviar)
12. [Modal "Corregir Solicitud" — NPE en Backend y Z-Index](#12-modal-corregir-solicitud--npe-en-backend-y-z-index)
13. [Notificaciones — Badge en Header](#13-notificaciones--badge-en-header)
14. [Notificaciones — "Ver Todas" y Navegación](#14-notificaciones--ver-todas-y-navegación)

---

## 1. Gestión de Solicitudes — Tabs por Rol

### Descripción

Los tabs **Bandeja de entrada**, **Sin asignar** y **Solicitudes** deben mostrar datasets filtrados según el rol del usuario autenticado y, en el caso del **COORD**, filtrar por el departamento de su carrera.

### Estado

✅ **COMPLETADO** (03/01/2026)

**Cambios implementados:**

**Fase 1 - Bandeja de entrada COORD:**
- ✅ Backend: Queries con JOIN a Usuario y Carrera para filtrar por departamento
- ✅ Endpoints específicos: `/bandeja-entrada/coord/{idDeptoCarrera}` y count
- ✅ Frontend: Servicio y layout actualizados para COORD

**Fase 2 - Bandeja de entrada SUP:**
- ✅ Backend: Query existente `findBandejaEntrada(idAdmin)` ya funciona correctamente
- ✅ Filtra por `idAdminRevisor = idUsuario` y excluye estados APRO/RECH
- ✅ Frontend: `sup-solicitudes-page.component.ts` ya usa el endpoint correcto

**Fase 3 - Bandeja de entrada ADMIN:**
- ✅ Backend: Nuevo query `findBandejaEntradaAdmin()` sin filtro de usuario
- ✅ Endpoints específicos: `/bandeja-entrada/admin` y `/count/bandeja-entrada/admin`
- ✅ Retorna TODAS las solicitudes no cerradas (modo lectura)
- ✅ Frontend: `admin-solicitud-page-layout.component.ts` actualizado

**Archivos modificados (Fase 2 y 3):**
- Backend:
  - `SolicitudProyectoRepository.java`: Agregados `findBandejaEntradaAdmin()` y `countBandejaEntradaAdmin()`
  - `SolicitudProyectoService.java`: Interfaces para métodos ADMIN
  - `SolicitudProyectoServiceImpl.java`: Implementación de métodos ADMIN
  - `SolicitudProyectoController.java`: Endpoints `/bandeja-entrada/admin` y count
- Frontend:
  - `solicitud-proyecto.service.ts`: Métodos `getBandejaEntradaAdmin()` y count
  - `admin-solicitud-page-layout.component.ts`: Actualizado para usar endpoints ADMIN sin filtro de usuario

### Estado

#### Tab: **Bandeja de entrada**

| Rol | Comportamiento |
|-----|----------------|
| **COORD** | Ver solicitudes asignadas a él mismo o a cualquier **SUP** del mismo departamento de carrera. Estados: `REV` (en revisión). |
| **SUP** | Ver únicamente solicitudes asignadas a él mismo. Estado: `REV`. |
| **ADMIN** | Ver todas las solicitudes en estado `REV` (solo lectura). |

#### Tab: **Sin asignar**

| Rol | Comportamiento |
|-----|----------------|
| **COORD** | Ver solicitudes pendientes de asignar (`PEND`) del mismo departamento de carrera. |
| **SUP** | **Tab oculto** (no debe visualizarse). |
| **ADMIN** | Ver todas las solicitudes en estado `PEND` (solo lectura). |

#### Tab: **Solicitudes**

| Rol | Comportamiento |
|-----|----------------|
| **COORD** | Ver todas las solicitudes en cualquier estado, filtradas por el mismo departamento de carrera. |
| **SUP** | **Tab oculto** (no debe visualizarse). |
| **ADMIN** | Ver todas las solicitudes en cualquier estado (solo lectura). |

### Criterios de Aceptación

#### Tab: Bandeja de entrada

- [x] **Backend:** Endpoint para COORD con filtro por `idDeptoCarrera`
- [x] **Backend:** Endpoint para SUP con filtro por `idUsuario` (ya existía)
- [x] **Backend:** Endpoint para ADMIN sin filtro de usuario (todas las solicitudes no cerradas)
- [x] **Backend:** Validaciones de rol con `@Secured`
- [x] **Frontend:** COORD usa `getBandejaEntradaCoord()` filtrando por departamento
- [x] **Frontend:** SUP usa `getBandejaEntrada()` filtrando por su ID
- [x] **Frontend:** ADMIN usa `getBandejaEntradaAdmin()` sin filtros
- [x] **Frontend:** Badges actualizados con conteos correctos por rol

### Referencias

- **Endpoints:** Ver `/backend/ENDPOINTS.md` — sección `SolicitudProyectoController`.
- **Controlador:** `/backend/controladores/SolicitudProyectoController.md`.
- **Componentes:**
  - `rpups-ues-frontend/src/app/admin/pages/admin-solicitud-page-layout/admin-solicitud-page-layout.component.ts`
  - `rpups-ues-frontend/src/app/coordinador/pages/coord-solicitud-page-layout/coord-solicitud-page-layout.component.ts`
  - `rpups-ues-frontend/src/app/supervisor/pages/sup-solicitudes-page/sup-solicitudes-page.component.ts`
- **Servicio:** `rpups-ues-frontend/src/app/core/services/solicitud-proyecto.service.ts`

### Notas de Implementación

1. Agregar parámetro `idDepartamento` (opcional) en los métodos del repositorio y servicio backend.
2. Validar en el controlador que el usuario autenticado pertenece al departamento antes de retornar datos.
3. En el frontend, extraer `idDepartamento` del usuario logueado y pasarlo como query param.
4. Condicionar la visibilidad de tabs en `coord-solicitud-page-layout` y `sup-solicitudes-page` según rol.

---

## 2. Gestión de Postulaciones — Ver Detalles Post-Aprobación

### Descripción

Después de aprobar una postulación desde la vista de gestión de postulaciones, el botón **"Ver detalles"** no funciona correctamente o no refleja el cambio de estado.

### Reglas de Negocio

- Una vez aprobada, la postulación debe actualizar su estado en el servicio compartido y en la tabla.
- El botón "Ver detalles" debe abrir el modal de detalle con el estado actualizado (`APROBADO`).

### Criterios de Aceptación

- [ ] **Frontend:** Tras aprobar una postulación, actualizar el estado en `postulacion-shared.service.ts` usando el método correspondiente.
- [ ] **Frontend:** Asegurar que el componente tabla de postulaciones refresque o detecte el cambio vía `BehaviorSubject`.
- [ ] **Frontend:** El modal de detalle debe recibir el objeto actualizado con el nuevo estado.

### Referencias

- **Servicio:** `rpups-ues-frontend/src/app/core/services/postulacion-shared.service.ts`
- **Componente:** `rpups-ues-frontend/src/app/estudiantes/components/postulaciones/postulaciones.component.ts`
- **Modal:** Componente de detalle de postulación (ubicar en `estudiantes/components` o `shared`).

### Notas de Implementación

1. Verificar que el endpoint de aprobación retorna la postulación actualizada.
2. Usar el método `addPostulacion(postulacionActualizada)` para actualizar el estado compartido.
3. Validar que el modal de detalle escucha cambios del servicio compartido.

---

## 3. Gestión de Proyectos — Recálculo de Cupos

### Descripción

En la tabla de **Gestión de Proyectos**, la columna **Cupos** no se recalcula automáticamente cuando se realizan cambios (por ejemplo, al aprobar o rechazar postulaciones que afectan el número de cupos disponibles).

### Reglas de Negocio

- Cada proyecto tiene un `cupoMaximo` y un contador de postulaciones aprobadas.
- La columna **Cupos disponibles** debe reflejar `cupoMaximo - postulacionesAprobadas`.
- Al aprobar/rechazar una postulación, el contador debe actualizarse reactivamente.

### Criterios de Aceptación

- [ ] **Backend:** El DTO de proyecto debe incluir el campo `cuposDisponibles` calculado dinámicamente.
- [ ] **Backend:** Al aprobar/rechazar una postulación, el servicio de proyectos debe recalcular este valor.
- [ ] **Frontend:** La tabla de proyectos debe refrescar datos tras operaciones de postulaciones que afecten cupos.
- [ ] **Frontend:** Considerar uso de `proyecto-shared.service.ts` para emitir cambios y actualizar la tabla sin recargar toda la página.

### Referencias

- **Entidad:** `/backend/entidades/Proyecto.md`
- **Controlador:** `/backend/controladores/ProyectoController.md`
- **Servicio compartido:** `rpups-ues-frontend/src/app/core/services/proyecto-shared.service.ts`
- **Componente:** Tabla de gestión de proyectos (ubicar en `admin`, `coordinador` o `supervisor`).

### Notas de Implementación

1. Agregar lógica en `ProyectoService` para calcular `cuposDisponibles`.
2. Emitir evento desde `PostulacionService` al aprobar/rechazar que notifique a `ProyectoSharedService`.
3. Suscribirse en el componente de tabla para refrescar la fila correspondiente.

---

## 4. Gestión de Revisión — Asignación y Observaciones

### Descripción

En la **Gestión de Revisión** (asignación de solicitudes a revisores):

1. El **COORD** solo debe poder asignar solicitudes a usuarios con rol **COORD** o **SUP**.
2. El botón **Rechazar** no funciona.
3. Las observaciones no se persisten correctamente en la base de datos.
4. Cuando se vuelve a consultar una solicitud con observación, esta no se visualiza.
5. No debe permitirse agregar una nueva observación hasta que la primera haya sido resuelta.

### Reglas de Negocio

- **Asignación:** Solo a usuarios `COORD` o `SUP`.
- **Rechazo:** Al rechazar, la solicitud pasa a estado `RECHAZADO` y no puede volver a revisarse.
- **Observaciones:**
  - Se persisten en tabla `Observacion` (o campo `observaciones` en `SolicitudProyecto`).
  - Estado de la solicitud: `OBSERVACION`.
  - Bloquear creación de nueva observación mientras haya una activa sin resolver.
  - Al resolver (corregir y reenviar), cambiar estado a `PEND` o `REV`.

### Criterios de Aceptación

- [ ] **Backend:** Endpoint de asignación debe validar que el `idUsuarioAsignado` pertenece a un usuario con rol `COORD` o `SUP`.
- [ ] **Backend:** Endpoint de rechazo debe cambiar estado a `RECHAZADO` y retornar confirmación.
- [ ] **Backend:** Endpoint de observaciones:
  - Crear observación en tabla `Observacion`.
  - Vincular con `SolicitudProyecto`.
  - Cambiar estado de solicitud a `OBSERVACION`.
  - Validar que no exista observación activa sin resolver antes de crear nueva.
- [ ] **Backend:** Endpoint para obtener observaciones de una solicitud.
- [ ] **Frontend:** Modal de asignación debe filtrar lista de usuarios solo con roles `COORD` y `SUP`.
- [ ] **Frontend:** Botón "Rechazar" debe invocar el endpoint correcto y actualizar la tabla.
- [ ] **Frontend:** Al abrir detalle de solicitud en observación, mostrar observaciones pendientes.
- [ ] **Frontend:** Deshabilitar botón "Agregar observación" si hay una activa sin resolver.

### Referencias

- **Entidad:** `/backend/entidades/SolicitudProyecto.md`
- **Controlador:** `/backend/controladores/SolicitudProyectoController.md`
- **Endpoints:** Ver `/backend/ENDPOINTS.md` — sección `SolicitudProyectoController`.
- **Componente:** Modal de asignación/revisión (ubicar en `admin`, `coordinador` o `supervisor`).

### Notas de Implementación

1. Crear tabla `Observacion` si no existe (id, idSolicitudProyecto, texto, fechaCreacion, resuelta).
2. Agregar endpoint `POST /api/solicitudes/{id}/observaciones`.
3. Agregar endpoint `GET /api/solicitudes/{id}/observaciones`.
4. Validar rol en método de asignación con `@PreAuthorize`.
5. Implementar lógica de bloqueo en frontend para observaciones activas.

---

## 5. Coordinador — Completar Flujo de Proyectos

### Descripción

En el componente de proyectos para el usuario **COORD**, las opciones **"Agregar proyecto"** y **"Ver el detalle de un proyecto"** de la tabla no están completamente implementadas.

### Estado

✅ **COMPLETADO** (03/01/2026)

**Cambios implementados:**

**Fase 1 - Ver y Editar Proyectos:**
- Se integró el componente compartido `shared-proyecto-modal` en la tabla de proyectos del coordinador.
- El botón de acción ahora abre el modal de detalle con modo de edición.
- Se agregó funcionalidad de actualización y refresco automático de la lista tras editar.
- Se añadió `MessageService` para notificaciones de éxito/error.

**Fase 2 - Crear Proyectos:**
- Se completó la funcionalidad del botón "Nuevo Proyecto".
- Se integró `shared-proyecto-form` en el modal de creación (`coord-proyectos-modal`).
- Se implementó el método `onGuardarProyecto()` para crear proyectos vía API.
- Se agregó evento `proyectoCreated` para refrescar automáticamente la lista tras crear.
- Se configuró el modal con z-index adecuado y cierre automático tras éxito.
- Se añadieron notificaciones toast para éxito/error en la creación.

**Archivos modificados:**

*Ver y Editar:*
- `coord-proyectos-list.component.ts`: Agregado método `onUpdateProyecto()` y evento `onRefresh`.
- `coord-proyectos-list.component.html`: Integrado `<shared-proyecto-modal>` con modo edit.

*Crear:*
- `coord-proyectos-modal.component.ts`: Agregado `ProyectoService`, `MessageService`, método `onGuardarProyecto()` y evento `proyectoCreated`.
- `coord-proyectos-modal.component.html`: Integrado `<shared-proyecto-form>` con modo create.
- `coord-proyectos-page.component.ts`: Agregado método `onProyectoCreado()` para refrescar lista.
- `coord-proyectos-page.component.html`: Vinculado evento `(proyectoCreated)`.

### Reglas de Negocio

- El **COORD** puede crear proyectos para su departamento/carrera.
- El **COORD** puede ver y editar proyectos de su departamento.
- Debe validarse que el proyecto pertenece al departamento del coordinador antes de permitir edición.

### Criterios de Aceptación

- [x] **Backend:** Endpoint `POST /api/proyectos` debe aceptar `idDepartamento` o `idCarrera` y validar que el usuario autenticado es COORD de ese departamento.
- [x] **Backend:** Endpoint `GET /api/proyectos/{id}` debe validar que el proyecto pertenece al departamento del usuario autenticado.
- [x] **Frontend:** Implementar botón "Agregar proyecto" que abra modal de creación.
- [x] **Frontend:** Modal de creación debe enviar `idDepartamento` según el usuario logueado.
- [x] **Frontend:** Botón "Ver detalle" en tabla debe abrir modal con datos completos del proyecto.
- [x] **Frontend:** Validar permisos antes de mostrar opciones de edición.
- [x] **Frontend:** Integrar modal compartido `shared-proyecto-modal` con modo edit.
- [x] **Frontend:** Implementar actualización y refresco de lista tras edición.

### Referencias

- **Controlador:** `/backend/controladores/ProyectoController.md`
- **Componente:** 
  - `rpups-ues-frontend/src/app/coordinador/pages/coord-proyectos-page`
  - `rpups-ues-frontend/src/app/coordinador/components/coord-proyectos-list`
  - `rpups-ues-frontend/src/app/shared/components/proyecto-modal`
- **Endpoints:** Ver `/backend/ENDPOINTS.md` — sección `ProyectoController`.

### Notas de Implementación

1. ✅ Se reutilizó el componente compartido `shared-proyecto-modal` existente.
2. ✅ El modal muestra detalle completo del proyecto y permite edición.
3. ✅ La actualización se propaga automáticamente recargando la lista de proyectos.
4. ✅ Se añadieron notificaciones toast para feedback visual al usuario.
5. ⚠️ **Pendiente:** Validar en backend que el endpoint de creación filtra por `idDepartamento` del usuario.
6. ⚠️ **Pendiente:** Implementar lógica completa en el modal de creación `coord-proyectos-modal`.

---

## 6. Supervisor — Creación de Proyectos Propios

### Descripción

El usuario **SUP** debe poder crear proyectos, pero solo aquellos que él mismo ha creado (es decir, donde `idUsuarioCreador == idUsuario`).

### Reglas de Negocio

- El **SUP** puede crear proyectos.
- El **SUP** solo puede ver y editar proyectos donde él es el creador.
- La lista de proyectos del **SUP** debe filtrarse por `idUsuarioCreador`.

### Criterios de Aceptación

- [ ] **Backend:** Endpoint `GET /api/proyectos` debe aceptar parámetro `idUsuarioCreador` y filtrar.
- [ ] **Backend:** Endpoint `POST /api/proyectos` debe asignar automáticamente `idUsuarioCreador` al usuario autenticado.
- [ ] **Backend:** Endpoint `PUT /api/proyectos/{id}` debe validar que `idUsuarioCreador == idUsuarioAutenticado`.
- [ ] **Frontend:** Componente de proyectos para **SUP** debe llamar endpoint con filtro `idUsuarioCreador`.
- [ ] **Frontend:** Botón "Agregar proyecto" disponible para **SUP**.
- [ ] **Frontend:** Modal de creación debe enviar datos sin especificar `idUsuarioCreador` (backend lo asigna).

### Referencias

- **Controlador:** `/backend/controladores/ProyectoController.md`
- **Componente:** `rpups-ues-frontend/src/app/supervisor/pages/proyectos-sup` (o similar).
- **Endpoints:** Ver `/backend/ENDPOINTS.md` — sección `ProyectoController`.

### Notas de Implementación

1. Agregar método en `ProyectoRepository`: `findByIdUsuarioCreador(Long idUsuarioCreador)`.
2. Validar en controlador que el usuario autenticado es SUP antes de crear.
3. Asegurar que el filtro se aplica automáticamente en el frontend según rol.

---

## 7. Representante Empresa — Mi Empresa No Se Actualiza

### Descripción

Al crear una empresa con rol **EMP** (Representante de Empresa), la pestaña **"Mi empresa"** no se actualiza automáticamente para mostrar la empresa recién creada.

### Reglas de Negocio

- Un usuario **EMP** solo puede tener una empresa asociada.
- Tras crear la empresa, debe visualizarse inmediatamente en la pestaña "Mi empresa" sin necesidad de recargar la página.

### Criterios de Aceptación

- [ ] **Backend:** Endpoint `POST /api/empresas` debe retornar la empresa creada con su ID.
- [ ] **Backend:** Endpoint `GET /api/empresas/mi-empresa` debe retornar la empresa asociada al usuario autenticado.
- [ ] **Frontend:** Tras creación exitosa, actualizar el estado local o servicio compartido con la nueva empresa.
- [ ] **Frontend:** La pestaña "Mi empresa" debe suscribirse al servicio compartido para detectar cambios.
- [ ] **Frontend:** Redirigir o refrescar la vista de "Mi empresa" tras creación.

### Referencias

- **Controlador:** `/backend/controladores/EmpresaController.md`
- **Componente:** `rpups-ues-frontend/src/app/representante-empresa/pages/mi-empresa` (o similar).
- **Servicio:** `rpups-ues-frontend/src/app/core/services/empresa.service.ts`

### Notas de Implementación

1. Usar `BehaviorSubject` en `empresa.service.ts` para compartir estado de "Mi empresa".
2. Emitir la empresa creada tras éxito en el modal de creación.
3. Suscribirse en `mi-empresa.component.ts` para detectar el cambio y actualizar la vista.

---

## 8. Representante Empresa — Restricción de Empresa en Solicitud

### Descripción

Al crear una solicitud con el rol **EMP**, solo debe habilitarse la empresa creada por el mismo usuario. No debe permitirse seleccionar otras empresas.

### Reglas de Negocio

- Un **EMP** solo puede crear solicitudes para su propia empresa.
- El campo `idEmpresa` debe ser fijo y no editable en el formulario de solicitud.

### Criterios de Aceptación

- [ ] **Backend:** Validar en endpoint `POST /api/solicitudes-proyecto` que el `idEmpresa` pertenece al usuario autenticado (EMP).
- [ ] **Frontend:** En el formulario de solicitud, precargar y deshabilitar el campo de empresa con la empresa del usuario logueado.
- [ ] **Frontend:** Si el usuario no tiene empresa asociada, mostrar mensaje de error y deshabilitar creación de solicitud.

### Referencias

- **Controlador:** `/backend/controladores/SolicitudProyectoController.md`
- **Componente:** `rpups-ues-frontend/src/app/representante-empresa/pages/crear-solicitud` (o similar).
- **Servicio:** `rpups-ues-frontend/src/app/core/services/solicitud-proyecto.service.ts`

### Notas de Implementación

1. Obtener `idEmpresa` del usuario logueado desde `localStorage` o token JWT.
2. Validar en backend que `idEmpresa` en el request coincide con la empresa del token.
3. Deshabilitar el campo `empresa` en el formulario Angular (`formControl.disable()`).

---

## 9. Representante Empresa — Acciones CRUD en Solicitudes

### Descripción

Las solicitudes creadas con rol **EMP** deben incluir las opciones **Ver detalle**, **Editar** y **Eliminar**. Las opciones de edición y eliminación solo deben estar disponibles si la solicitud se encuentra en estado **PENDIENTE** o **OBSERVACION**.

### Reglas de Negocio

- **Ver detalle:** Siempre disponible.
- **Editar:** Solo en estados `PENDIENTE` o `OBSERVACION`.
- **Eliminar:** Solo en estados `PENDIENTE` o `OBSERVACION`.
- En cualquier otro estado (ej. `REV`, `APROBADO`, `RECHAZADO`), solo se permite ver detalle.

### Criterios de Aceptación

- [ ] **Backend:** Endpoints `PUT /api/solicitudes-proyecto/{id}` y `DELETE /api/solicitudes-proyecto/{id}` deben validar:
  - Usuario autenticado es el creador de la solicitud.
  - Estado de la solicitud es `PENDIENTE` o `OBSERVACION`.
- [ ] **Frontend:** En la tabla de solicitudes del **EMP**, agregar columna de acciones con botones:
  - **Ver detalle** (siempre visible).
  - **Editar** (visible solo si estado es `PENDIENTE` o `OBSERVACION`).
  - **Eliminar** (visible solo si estado es `PENDIENTE` o `OBSERVACION`).
- [ ] **Frontend:** Modal de edición debe permitir modificar campos y reenviar solicitud.

### Referencias

- **Controlador:** `/backend/controladores/SolicitudProyectoController.md`
- **Componente:** `rpups-ues-frontend/src/app/representante-empresa/pages/mis-solicitudes` (o similar).
- **Endpoints:** Ver `/backend/ENDPOINTS.md` — sección `SolicitudProyectoController`.

### Notas de Implementación

1. Agregar validaciones de estado en `SolicitudProyectoService.update()` y `delete()`.
2. En el frontend, usar `*ngIf` o `[hidden]` para condicionar visibilidad de botones según estado.
3. Mostrar mensaje de error si el usuario intenta editar/eliminar solicitud en estado no permitido.

---

## 10. Representante Empresa — Segregación de Dashboard y Sidebar

### Descripción

Cuando se accede con rol **EMP**, la ruta `dashboard/representante-empresa/empresa/mi-empresa` debe mostrar únicamente:

- El detalle de la empresa (si ya está creada).
- La opción para crear la empresa (si no existe).

Además, en el **dashboard** y el **sidebar**, deben aparecer por separado las opciones:

- **Proyectos** (vinculados a la empresa).
- **Solicitudes** (vinculadas a la empresa).

Ambas opciones deben estar claramente diferenciadas y navegar a rutas independientes.

### Reglas de Negocio

- La ruta "Mi empresa" es exclusiva para la gestión de la empresa (crear/ver detalle).
- Las rutas de "Proyectos" y "Solicitudes" son independientes y muestran datos filtrados por la empresa del usuario **EMP**.

### Criterios de Aceptación

- [ ] **Frontend:** Crear o ajustar rutas:
  - `dashboard/representante-empresa/mi-empresa` → Detalle/Creación de empresa.
  - `dashboard/representante-empresa/proyectos` → Proyectos de la empresa.
  - `dashboard/representante-empresa/solicitudes` → Solicitudes de la empresa.
- [ ] **Frontend:** Actualizar `representante-empresa-routing.module.ts` con las nuevas rutas.
- [ ] **Frontend:** Actualizar el sidebar para incluir enlaces a "Mi Empresa", "Proyectos" y "Solicitudes".
- [ ] **Frontend:** Cada componente debe filtrar datos por `idEmpresa` del usuario logueado.

### Referencias

- **Módulo:** `rpups-ues-frontend/src/app/representante-empresa/representante-empresa-routing.module.ts`
- **Componentes:**
  - `mi-empresa.component.ts`
  - `proyectos-empresa.component.ts` (crear si no existe)
  - `solicitudes-empresa.component.ts` (crear si no existe)
- **Sidebar:** `rpups-ues-frontend/src/app/layout/sidebar` (o componente equivalente)

### Notas de Implementación

1. Revisar estructura de rutas en el módulo `representante-empresa`.
2. Crear componentes faltantes para "Proyectos" y "Solicitudes" si no existen.
3. Actualizar el menú del sidebar condicionalmente según el rol `EMP`.
4. Asegurar que cada vista llama endpoints con filtro `idEmpresa`.

---

## 11. Modal "Detalle de Solicitud" — Botón Corregir y Reenviar

### Descripción

En el modal **"Detalle de Solicitud"**, cuando la solicitud se encuentra en estado **OBSERVACION**, el botón **"Corregir y Reenviar"** no funciona.

### Reglas de Negocio

- El botón debe abrir un modal de edición donde se puedan corregir los campos observados.
- Al reenviar, la solicitud debe cambiar de estado `OBSERVACION` a `PEND` o `REV` según la lógica de negocio.
- La observación debe marcarse como resuelta.

### Criterios de Aceptación

- [ ] **Backend:** Endpoint `PUT /api/solicitudes-proyecto/{id}/corregir` debe:
  - Validar que la solicitud está en estado `OBSERVACION`.
  - Actualizar campos corregidos.
  - Cambiar estado a `PEND`.
  - Marcar observación como resuelta.
- [ ] **Frontend:** Botón "Corregir y Reenviar" debe abrir modal de edición.
- [ ] **Frontend:** Modal debe precargar datos de la solicitud.
- [ ] **Frontend:** Tras reenviar, cerrar modal y refrescar tabla.

### Referencias

- **Controlador:** `/backend/controladores/SolicitudProyectoController.md`
- **Componente:** Modal de detalle de solicitud (ubicar en `shared`, `admin`, `coordinador` o `representante-empresa`).
- **Endpoints:** Ver `/backend/ENDPOINTS.md` — sección `SolicitudProyectoController`.

### Notas de Implementación

1. Verificar que el botón está vinculado a un método en el componente.
2. Crear modal de corrección si no existe, o reutilizar modal de edición con flag de "corrección".
3. Asegurar que el endpoint de backend maneja correctamente el cambio de estado y actualización de observaciones.

---

## 12. Modal "Corregir Solicitud" — NPE en Backend y Z-Index

### Descripción

En el modal **"Corregir Solicitud"** (cuando la solicitud está en observación):

1. Al dar clic en **"Reenviar Solicitud"**, el modal siempre se sobrepone al modal de confirmación (SweetAlert).
2. Al confirmar, el servicio backend retorna el error:

```
java.lang.NullPointerException: Cannot invoke "com.ues.edu.sv.rpups_ues.model.entity.Estado.getNombre()" because the return value of "com.ues.edu.sv.rpups_ues.model.entity.SolicitudProyecto.getEstado()" is null
    at com.ues.edu.sv.rpups_ues.controller.SolicitudProyectoController.updateSolicitudExterno(SolicitudProyectoController.java:324)
```

### Reglas de Negocio

- El modal de corrección debe cerrarse antes de mostrar la confirmación con SweetAlert.
- El backend debe asegurar que el campo `estado` de la solicitud no sea `null` al momento de actualizar.
- Si el estado no se pasa en el request, el backend debe asignarlo automáticamente (ej. `PEND`).

### Criterios de Aceptación

- [ ] **Backend:** Corregir NPE en `SolicitudProyectoController.updateSolicitudExterno()` línea 324:
  - Validar que `solicitudProyecto.getEstado()` no sea `null` antes de invocar `.getNombre()`.
  - Si `estado` es `null`, asignar un estado por defecto (ej. `PEND`).
- [ ] **Frontend:** Ajustar z-index del modal de corrección para que esté por debajo de SweetAlert.
- [ ] **Frontend:** Cerrar el modal antes de mostrar la confirmación con SweetAlert.
- [ ] **Frontend:** Reabrir el modal si el usuario cancela la confirmación o si ocurre un error.

### Referencias

- **Controlador:** `/backend/controladores/SolicitudProyectoController.md`
- **Código Backend:** `SolicitudProyectoController.java`, línea 324.
- **Componente:** Modal de corrección de solicitud (ubicar en `shared`, `admin`, `coordinador` o `representante-empresa`).
- **Dialog Config:** Archivo HTML del modal (ajustar `baseZIndex` en `<p-dialog>`).

### Notas de Implementación

1. **Backend:** Agregar validación en el método `updateSolicitudExterno()`:
   ```java
   if (solicitud.getEstado() == null) {
       Estado estadoPendiente = estadoRepository.findByNombre("PENDIENTE").orElseThrow();
       solicitud.setEstado(estadoPendiente);
   }
   ```
2. **Frontend:** Establecer `[baseZIndex]="1050"` en el modal de corrección (SweetAlert usa 1060 por defecto).
3. **Frontend:** Llamar `this.displayModal = false;` antes de mostrar SweetAlert.
4. **Frontend:** En el callback de cancelación, volver a abrir el modal con `this.displayModal = true;`.

---

## 13. Notificaciones — Badge en Header

### Descripción

El ícono de notificaciones en el **header** no indica visualmente que hay nuevas notificaciones. Solo al dar clic sobre el ícono es que se muestran las notificaciones.

### Reglas de Negocio

- El ícono de notificaciones debe mostrar un badge numérico con la cantidad de notificaciones no leídas.
- El badge debe actualizarse en tiempo real (o al menos al recargar la página o navegar).
- Al abrir el panel de notificaciones, las notificaciones deben marcarse como leídas y el badge debe actualizarse.

### Criterios de Aceptación

- [ ] **Backend:** Endpoint `GET /api/notificaciones/count/no-leidas` debe retornar el número de notificaciones no leídas del usuario autenticado.
- [ ] **Frontend:** Agregar badge en el ícono de notificaciones en el header.
- [ ] **Frontend:** Llamar endpoint al cargar la aplicación y tras operaciones que generen notificaciones.
- [ ] **Frontend:** Al abrir el panel, marcar notificaciones como leídas y actualizar el badge.

### Referencias

- **Controlador:** Crear o ubicar `NotificacionController.md` en `/backend/controladores`.
- **Componente:** Header o barra de navegación (ubicar en `rpups-ues-frontend/src/app/layout`).
- **Servicio:** Crear o ubicar `notificacion.service.ts` en `rpups-ues-frontend/src/app/core/services`.

### Notas de Implementación

1. Crear endpoint `GET /api/notificaciones/count/no-leidas`.
2. Agregar método en `NotificacionService` para obtener el conteo.
3. En el componente del header, suscribirse al servicio y mostrar badge si el conteo > 0.
4. Usar PrimeNG `p-badge` o similar para el badge visual.
5. Al abrir el panel, llamar endpoint `PUT /api/notificaciones/marcar-leidas` y refrescar el conteo.

---

## 14. Notificaciones — "Ver Todas" y Navegación

### Descripción

El componente o botón **"Ver todas las notificaciones"** no funciona. Tampoco funciona correctamente la navegación al seleccionar una notificación en específico.

### Reglas de Negocio

- El botón "Ver todas" debe redirigir a una página dedicada que muestre la lista completa de notificaciones del usuario.
- Al hacer clic en una notificación específica, debe redirigir al contexto relacionado (ej. detalle de solicitud, proyecto, postulación).
- La notificación debe marcarse como leída al hacer clic.

### Criterios de Aceptación

- [ ] **Backend:** Endpoint `GET /api/notificaciones` debe retornar todas las notificaciones del usuario autenticado, ordenadas por fecha (más recientes primero).
- [ ] **Backend:** Endpoint `PUT /api/notificaciones/{id}/marcar-leida` debe marcar una notificación como leída.
- [ ] **Frontend:** Crear componente `notificaciones-page.component.ts` en `rpups-ues-frontend/src/app/core/pages` (o módulo correspondiente).
- [ ] **Frontend:** Ruta `/dashboard/notificaciones` debe cargar el componente de lista de notificaciones.
- [ ] **Frontend:** Botón "Ver todas" en el panel de notificaciones debe navegar a `/dashboard/notificaciones`.
- [ ] **Frontend:** Al hacer clic en una notificación, marcarla como leída y redirigir según el `tipo` o `contexto` de la notificación.

### Referencias

- **Controlador:** Crear o ubicar `NotificacionController.md` en `/backend/controladores`.
- **Componente:** `rpups-ues-frontend/src/app/core/pages/notificaciones-page` (crear si no existe).
- **Servicio:** `rpups-ues-frontend/src/app/core/services/notificacion.service.ts`
- **Routing:** `rpups-ues-frontend/src/app/core/core-routing.module.ts` (o módulo correspondiente).

### Notas de Implementación

1. Crear endpoint `GET /api/notificaciones` con paginación opcional.
2. Crear método en `NotificacionService` para obtener lista completa.
3. Crear componente `notificaciones-page` con tabla de notificaciones usando PrimeNG.
4. Agregar ruta en el módulo de routing correspondiente.
5. Implementar lógica de navegación según el contexto de cada notificación (ej. si es de solicitud, redirigir a detalle de solicitud).
6. Llamar endpoint de marcar como leída al hacer clic en notificación.

---

## Resumen de Tareas Pendientes

| # | Tarea | Prioridad | Estado | Módulo/Área |
|---|-------|-----------|--------|-------------|
| 1 | Tabs de Solicitudes por Rol - Bandeja entrada | Alta | ✅ Completado | Backend + Frontend (Solicitudes) |
| 2 | Ver Detalles Post-Aprobación Postulaciones | Alta | ⏳ Pendiente | Frontend (Postulaciones) |
| 3 | Recálculo de Cupos en Proyectos | Media | ⏳ Pendiente | Backend + Frontend (Proyectos) |
| 4 | Gestión de Revisión (Asignación, Rechazo, Observaciones) | Alta | ⏳ Pendiente | Backend + Frontend (Revisión) |
| 5 | Completar Flujo de Proyectos COORD | Media | ✅ Completado | Backend + Frontend (Coordinador) |
| 6 | Creación de Proyectos Propios SUP | Media | ⏳ Pendiente | Backend + Frontend (Supervisor) |
| 7 | Actualizar "Mi Empresa" Post-Creación | Media | ⏳ Pendiente | Frontend (EMP) |
| 8 | Restricción de Empresa en Solicitud EMP | Alta | ⏳ Pendiente | Backend + Frontend (EMP) |
| 9 | Acciones CRUD en Solicitudes EMP | Alta | ⏳ Pendiente | Backend + Frontend (EMP) |
| 10 | Segregación Dashboard/Sidebar EMP | Media | ⏳ Pendiente | Frontend (EMP) |
| 11 | Botón "Corregir y Reenviar" en Detalle Solicitud | Alta | ⏳ Pendiente | Backend + Frontend (Solicitudes) |
| 12 | NPE Backend y Z-Index Modal Corrección | Alta | ⏳ Pendiente | Backend + Frontend (Solicitudes) |
| 13 | Badge Notificaciones en Header | Media | ⏳ Pendiente | Backend + Frontend (Notificaciones) |
| 14 | "Ver Todas" y Navegación Notificaciones | Media | ⏳ Pendiente | Backend + Frontend (Notificaciones) |

---

## Notas Finales

- Este documento detalla las correcciones y mejoras identificadas durante la fase de pruebas y validación del sistema.
- Se recomienda abordar las tareas de **Prioridad Alta** primero, ya que afectan funcionalidades críticas o bloquean flujos de usuario.
- Cada tarea debe validarse con pruebas funcionales y, de ser posible, con pruebas unitarias o de integración.
- Mantener actualizado este documento conforme se completen las tareas.

---

**Última actualización:** 03 de enero de 2026  
**Responsable:** Equipo de Desarrollo  
**Estado del documento:** En actualización continua  
**Completadas:** 1/14 tareas
