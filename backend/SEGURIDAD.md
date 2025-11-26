# Backend - Seguridad y Autenticación

## Configuración de Seguridad

El sistema utiliza Spring Security con autenticación basada en JWT (JSON Web Tokens).

---

## Arquitectura de Seguridad

### Componentes Principales

| Archivo | Propósito |
|---------|-----------|
| `SecurityConfig.java` | Configuración principal de Spring Security |
| `JwtAuthenticationFilter.java` | Filtro para login y generación de token |
| `JwtAuthorizationFilter.java` | Filtro para validar token en cada request |
| `JwtService.java` | Servicio para crear, parsear y validar tokens |

---

## Flujo de Autenticación

### 1. Login (POST /api/v1/auth/login)

1. Usuario envía credenciales (username, password)
2. `JwtAuthenticationFilter` intercepta la petición
3. `AuthenticationManager` valida credenciales contra BD
4. Si es válido, `JwtService` genera token JWT
5. Token se retorna en header `Authorization: Bearer <token>`

### 2. Peticiones Autenticadas

1. Cliente envía token en header `Authorization: Bearer <token>`
2. `JwtAuthorizationFilter` intercepta la petición
3. `JwtService.parseToken()` valida y extrae claims
4. Se establece el `SecurityContext` con el usuario autenticado
5. La petición continúa al controlador

---

## Configuración JWT

### JwtService.java

```java
// Configuración actual (INSEGURA - ver errores)
private static final String SECRET_KEY = "UniversidadDeElSalvadorFacultadMultidisciplinariaDeOccidenteHorasSociales";
private static final long EXPIRATION_TIME = 3_600_000; // 1 hora
```

### Estructura del Token

```json
{
  "sub": "username",
  "authorities": ["ADMIN", "COORD"],
  "iat": 1700000000,
  "exp": 1700003600
}
```

---

## Roles y Permisos

### Roles Definidos

| Código | Nombre Completo | Nivel de Acceso |
|--------|-----------------|-----------------|
| `ADMIN` | Administrador | Acceso total |
| `COORD` | Coordinador | Gestión de usuarios admin, solicitudes, proyectos |
| `SUP` | Supervisor | Bandeja de solicitudes asignadas |
| `ESTUD` | Estudiante | Ver proyectos, postularse |
| `EMP` | Empresa | Crear solicitudes, ver proyectos propios |

### Jerarquía de Permisos

```
ADMIN
  └── COORD
        └── SUP
              └── ESTUD / EMP
```

---

## Endpoints Públicos vs Protegidos

### Endpoints Públicos (Sin autenticación)

```java
// SecurityConfig.java
.requestMatchers("/api/v1/auth/**").permitAll()
.requestMatchers("/api/usuarios/register").permitAll()
.requestMatchers("/api/usuarios/representante/**").permitAll()
.requestMatchers("/api/proyectos/public/**").permitAll()
```

### Endpoints Protegidos por Rol

```java
// Ejemplo en controladores
@Secured({"ADMIN", "COORD", "SUP"})
@GetMapping("/search-filters")
public ResponseEntity<Page<Proyecto>> getProyectosByFiltros(...) { }

@Secured({"ADMIN", "COORD"})
@DeleteMapping("/{id}")
public ResponseEntity<Void> deleteUsuario(...) { }
```

---

## Configuración CORS

### SecurityConfig.java (Actual)

```java
@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.addAllowedOrigin("*");      // PROBLEMA: Permite todo
    configuration.addAllowedMethod("*");
    configuration.addAllowedHeader("*");
    // ...
}
```

---

## Problemas de Seguridad Identificados

### 🔴 Crítico 1: SECRET_KEY Hardcodeada

**Ubicación**: `JwtService.java:22`

**Problema**: La clave secreta está en el código fuente, es predecible y débil.

**Solución recomendada**:
- Mover a variable de entorno
- Usar clave de al menos 256 bits generada aleatoriamente
- Ejemplo: `JWT_SECRET=clave-generada-con-openssl-rand-base64-32`

### 🔴 Crítico 2: CORS Sin Restricciones

**Ubicación**: `SecurityConfig.java:45-50`

**Problema**: `addAllowedOrigin("*")` permite peticiones desde cualquier dominio.

**Solución recomendada**:
- Especificar dominios permitidos explícitamente
- En desarrollo: `http://localhost:4200`
- En producción: `https://tu-dominio.com`

### 🔴 Crítico 3: Credenciales en application.properties

**Ubicación**: `application.properties:13-14, 25`

**Problema**: Password de BD y email en texto plano en el repositorio.

**Solución recomendada**:
- Usar variables de entorno
- `spring.datasource.password=${DB_PASSWORD}`
- `spring.mail.password=${MAIL_PASSWORD}`

### 🟠 Alto 4: Endpoints Sin Protección Adecuada

**Ubicaciones**:
- `SolicitudProyectoController.java:48` - createSolicitud usa `@PermitAll`
- `SolicitudProyectoController.java:152, 188` - updateSolicitud usa `@PermitAll`
- `EmpresaController.java:55, 62` - create/update usa `@PermitAll`
- `PostulacionController.java:48` - createPostulacion usa `@PermitAll`

**Problema**: Cualquier usuario no autenticado puede crear/modificar datos.

**Solución recomendada**:
- Cambiar `@PermitAll` por `@Secured` con roles apropiados
- O usar autenticación y verificar que el usuario tenga permiso sobre el recurso

### 🟡 Medio 5: Token con Expiración Corta

**Ubicación**: `JwtService.java:23`

**Problema**: Token expira en 1 hora, lo que puede ser molesto para usuarios.

**Solución recomendada**:
- Aumentar a 8-24 horas para access token
- Implementar refresh token con expiración de 7 días

---

## Recomendaciones de Mejora

### 1. Implementar Refresh Token

```java
// Endpoints sugeridos
POST /api/v1/auth/refresh  // Obtener nuevo access token
POST /api/v1/auth/logout   // Invalidar tokens
```

### 2. Validación de Propiedad de Recursos

```java
// Verificar que el usuario puede modificar el recurso
if (!solicitud.getUserCreador().equals(currentUser) && !isAdmin(currentUser)) {
    throw new AccessDeniedException("No tiene permiso");
}
```

### 3. Configuración por Ambiente

```properties
# application-dev.properties
spring.datasource.password=dev-password

# application-prod.properties
spring.datasource.password=${DB_PASSWORD}
```

### 4. Rate Limiting

Agregar límite de intentos de login para prevenir fuerza bruta.

---

## Documentación Relacionada

- [Arquitectura Backend](./README.md)
- [Errores Identificados](./ERRORES.md)
- [UsuarioController](./controladores/UsuarioController.md)
