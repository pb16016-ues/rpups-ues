# Backend - Arquitectura General

## Tecnologías Utilizadas

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| Java | 17 | Lenguaje principal |
| Spring Boot | 3.4.5 | Framework backend |
| Spring Security | 6.x | Autenticación y autorización |
| Spring Data JPA | 3.x | Persistencia de datos |
| MySQL | 8.x | Base de datos |
| JWT (jjwt) | 0.12.6 | Tokens de autenticación |
| OpenHTMLToPDF | 1.1.22 | Generación de reportes PDF |
| Thymeleaf | 3.x | Templates para reportes |
| Maven | 3.x | Gestión de dependencias |

---

## Estructura del Proyecto

```
rpups-ues/
├── src/
│   ├── main/
│   │   ├── java/com/ues/edu/sv/rpups_ues/
│   │   │   ├── RpupsUesApplication.java    # Clase principal
│   │   │   ├── auth/                        # Configuración de seguridad
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   ├── JwtAuthenticationFilter.java
│   │   │   │   ├── JwtAuthorizationFilter.java
│   │   │   │   └── JwtService.java
│   │   │   ├── controller/                  # Controladores REST
│   │   │   │   ├── UsuarioController.java
│   │   │   │   ├── ProyectoController.java
│   │   │   │   ├── SolicitudProyectoController.java
│   │   │   │   ├── PostulacionController.java
│   │   │   │   ├── EmpresaController.java
│   │   │   │   └── ...catálogos
│   │   │   ├── model/
│   │   │   │   ├── entity/                  # Entidades JPA
│   │   │   │   └── repository/              # Repositorios
│   │   │   ├── service/                     # Interfaces de servicios
│   │   │   │   └── impl/                    # Implementaciones
│   │   │   ├── exceptions/                  # Manejo de excepciones
│   │   │   └── utils/                       # Utilidades
│   │   └── resources/
│   │       ├── application.properties       # Configuración
│   │       ├── static/                      # Recursos estáticos
│   │       │   └── css/reportes.css
│   │       └── templates/                   # Templates Thymeleaf
│   │           ├── proyectos/
│   │           └── solicitudes-proyectos/
│   └── test/                                # Tests
├── mysql/
│   ├── docker-compose.yaml                  # Docker para MySQL
│   └── init.sql                             # Script inicial BD
└── pom.xml                                  # Dependencias Maven
```

---

## Configuración Principal

### application.properties

```properties
# Servidor
server.port=8080

# Base de datos
spring.datasource.url=jdbc:mysql://localhost:3306/rpups_ues
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update

# Email (para recuperación de contraseña)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=correo@gmail.com
spring.mail.password=app-password
```

---

## Capas de la Aplicación

### 1. Controller (Capa de Presentación)
- Recibe peticiones HTTP
- Valida datos de entrada con `@Valid`
- Delega lógica al servicio
- Retorna ResponseEntity con DTOs

### 2. Service (Capa de Negocio)
- Contiene la lógica de negocio
- Maneja transacciones con `@Transactional`
- Orquesta operaciones entre repositorios

### 3. Repository (Capa de Datos)
- Extiende `JpaRepository`
- Define queries personalizadas con `@Query`
- Maneja la persistencia

### 4. Entity (Modelo de Dominio)
- Clases anotadas con `@Entity`
- Mapeo a tablas de BD
- Validaciones con Bean Validation

---

## Patrones Utilizados

| Patrón | Uso |
|--------|-----|
| Repository | Acceso a datos |
| Service Layer | Lógica de negocio |
| DTO | Transferencia de datos (parcial) |
| Filter Chain | Autenticación JWT |
| Template Method | Generación de reportes |

---

## Dependencias Principales (pom.xml)

```xml
<dependencies>
    <!-- Spring Boot Starters -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-mail</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-thymeleaf</artifactId>
    </dependency>
    
    <!-- JWT -->
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
        <version>0.12.6</version>
    </dependency>
    
    <!-- MySQL -->
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
        <scope>runtime</scope>
    </dependency>
    
    <!-- PDF Generation -->
    <dependency>
        <groupId>com.openhtmltopdf</groupId>
        <artifactId>openhtmltopdf-pdfbox</artifactId>
        <version>1.1.22</version>
    </dependency>
</dependencies>
```

---

## Ejecución del Proyecto

### Requisitos Previos
1. Java 17 instalado
2. MySQL 8 corriendo en puerto 3306
3. Base de datos `rpups_ues` creada

### Comandos

```bash
# Con Maven Wrapper
./mvnw spring-boot:run

# O con Maven instalado
mvn spring-boot:run

# Compilar JAR
./mvnw clean package

# Ejecutar JAR
java -jar target/rpups-ues-0.0.1-SNAPSHOT.jar
```

### Docker (MySQL)

```bash
cd mysql/
docker-compose up -d
```

---

## Documentación Relacionada

- [Seguridad y JWT](./SEGURIDAD.md)
- [Catálogo de Endpoints](./ENDPOINTS.md)
- [Errores Identificados](./ERRORES.md)
- [Features Pendientes](./FEATURES.md)
