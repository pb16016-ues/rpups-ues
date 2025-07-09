-- Crear la base de datos (si no existe ya)
CREATE DATABASE IF NOT EXISTS `rpups_ues_fmocc`
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_general_ci;

-- Usar la base de datos recién creada
USE `rpups_ues_fmocc`;

-- Tabla de estados
CREATE TABLE estados (
    codigo_estado CHAR(5) PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

-- Tabla de departamentos
CREATE TABLE departamentos (
    codigo CHAR(2) PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

-- Tabla de municipios
CREATE TABLE municipios (
    codigo CHAR(4) PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    codigo_departamento CHAR(2) NOT NULL,
    FOREIGN KEY (codigo_departamento) REFERENCES departamentos(codigo)
);

-- Tabla de departamentos de carreras
CREATE TABLE departamentos_carreras (
    id_depto_carrera INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(250) NOT NULL UNIQUE
);

-- Tabla de carreras
CREATE TABLE carreras (
    codigo CHAR(10) PRIMARY KEY,
    nombre VARCHAR(250) NOT NULL UNIQUE,
    id_depto_carrera INT NOT NULL,
    FOREIGN KEY (id_depto_carrera) REFERENCES departamentos_carreras(id_depto_carrera)
);

-- Tabla de roles
CREATE TABLE roles (
    codigo CHAR(5) PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

-- Tabla de usuarios
CREATE TABLE usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombres VARCHAR(50) NOT NULL,
    apellidos VARCHAR(50) NOT NULL,
    carnet CHAR(8),
    correo_institucional VARCHAR(100) UNIQUE NOT NULL,
    correo_personal VARCHAR(100),
    telefono VARCHAR(12) NOT NULL,
    username VARCHAR(15) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    codigo_rol CHAR(5) NOT NULL,
    estado_activo BOOLEAN DEFAULT TRUE NOT NULL,
    id_depto_carrera INT NULL,
    FOREIGN KEY (id_depto_carrera) REFERENCES departamentos_carreras(id_depto_carrera),
    FOREIGN KEY (codigo_rol) REFERENCES roles(codigo),
    CONSTRAINT unique_user UNIQUE (username, correo_institucional)
);

-- Tabla de rubros
CREATE TABLE rubros (
    id_rubro INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE
);

-- Tabla de empresas
CREATE TABLE empresas (
    id_empresa INT AUTO_INCREMENT PRIMARY KEY,
    nombre_comercial VARCHAR(100) NOT NULL,
    nombre_legal VARCHAR(150) NOT NULL,
    contacto_nombre VARCHAR(150) NOT NULL,
    contacto_telefono VARCHAR(12) NOT NULL,
    contacto_email VARCHAR(100) NOT NULL,
    codigo_departamento CHAR(2) NOT NULL,
    codigo_municipio CHAR(4) NOT NULL,
    direccion_detallada TEXT,
    estado_activo BOOLEAN NOT NULL,
    id_rubro INT NOT NULL,
    id_user_creacion INT NOT NULL,
    FOREIGN KEY (codigo_departamento) REFERENCES departamentos(codigo),
    FOREIGN KEY (codigo_municipio) REFERENCES municipios(codigo),
    FOREIGN KEY (id_rubro) REFERENCES rubros(id_rubro),
    FOREIGN KEY (id_user_creacion) REFERENCES usuarios(id_usuario),
    CONSTRAINT unique_empresa UNIQUE (nombre_comercial, nombre_legal)
);

-- Tabla de modalidades
CREATE TABLE modalidades (
    codigo_modalidad CHAR(3) PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

-- Tabla de Solicitudes de proyectos
CREATE TABLE solicitudes_proyectos (
    id_solicitud INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(250) NOT NULL,
    descripcion TEXT NOT NULL,
    requisitos TEXT NOT NULL,
    fecha_inicio DATE,
    fecha_fin DATE,
    duracion INT NOT NULL,
    max_estudiantes INT NOT NULL,
    direccion_detallada TEXT,
    fecha_revision TIMESTAMP,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    observaciones TEXT,
    id_empresa INT NOT NULL,
    codigo_departamento CHAR(2) NOT NULL,
    codigo_municipio CHAR(4) NOT NULL,
    codigo_carrera CHAR(10) NOT NULL,
    codigo_modalidad CHAR(3) NOT NULL,
    id_admin_revision INT,
    id_user_creacion INT NOT NULL,
    codigo_estado CHAR(5) NOT NULL,
    FOREIGN KEY (id_empresa) REFERENCES empresas(id_empresa),
    FOREIGN KEY (codigo_departamento) REFERENCES departamentos(codigo),
    FOREIGN KEY (codigo_municipio) REFERENCES municipios(codigo),
    FOREIGN KEY (codigo_carrera) REFERENCES carreras(codigo),
    FOREIGN KEY (codigo_modalidad) REFERENCES modalidades(codigo_modalidad),
    FOREIGN KEY (id_admin_revision) REFERENCES usuarios(id_usuario),
    FOREIGN KEY (id_user_creacion) REFERENCES usuarios(id_usuario),
    FOREIGN KEY (codigo_estado) REFERENCES estados(codigo_estado),
    CONSTRAINT unique_solicitud UNIQUE (titulo, id_empresa)
);

-- Tabla de proyectos
CREATE TABLE proyectos (
    id_proyecto INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(250) NOT NULL,
    descripcion TEXT NOT NULL,
    requisitos TEXT NOT NULL,
    fecha_inicio DATE,
    fecha_fin DATE,
    duracion INT NOT NULL,
    max_estudiantes INT NOT NULL,
    direccion_detallada TEXT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_empresa INT NOT NULL,
    codigo_departamento CHAR(2) NOT NULL,
    codigo_municipio CHAR(4) NOT NULL,
    codigo_carrera CHAR(10) NOT NULL,
    codigo_modalidad CHAR(3) NOT NULL,
    id_admin_aprobacion INT NOT NULL,
    codigo_estado CHAR(5) NOT NULL,
    FOREIGN KEY (id_empresa) REFERENCES empresas(id_empresa),
    FOREIGN KEY (codigo_departamento) REFERENCES departamentos(codigo),
    FOREIGN KEY (codigo_municipio) REFERENCES municipios(codigo),
    FOREIGN KEY (codigo_carrera) REFERENCES carreras(codigo),
    FOREIGN KEY (codigo_modalidad) REFERENCES modalidades(codigo_modalidad),
    FOREIGN KEY (id_admin_aprobacion) REFERENCES usuarios(id_usuario),
    FOREIGN KEY (codigo_estado) REFERENCES estados(codigo_estado),
    CONSTRAINT unique_proyecto UNIQUE (titulo, id_empresa)
);

-- Tabla de postulaciones
CREATE TABLE postulaciones (
    id_postulacion INT AUTO_INCREMENT PRIMARY KEY,
    id_estudiante INT NOT NULL,
    id_proyecto INT NOT NULL,
    fecha_postulacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_estudiante) REFERENCES usuarios(id_usuario),
    FOREIGN KEY (id_proyecto) REFERENCES proyectos(id_proyecto),
    CONSTRAINT unique_postulacion UNIQUE (id_estudiante, id_proyecto)
);


-- Indexes para optimizar consultas
CREATE INDEX idx_codigo_estado ON estados(codigo_estado);

CREATE INDEX idx_codigo_departamento ON municipios(codigo_departamento);

CREATE INDEX idx_carnet ON usuarios(carnet);
CREATE INDEX idx_correo_institucional ON usuarios(correo_institucional);
CREATE INDEX idx_username ON usuarios(username);
CREATE INDEX idx_codigo_rol ON usuarios(codigo_rol);

CREATE INDEX idx_codigo_departamento ON empresas(codigo_departamento);
CREATE INDEX idx_codigo_municipio ON empresas(codigo_municipio);
CREATE INDEX idx_id_rubro ON empresas(id_rubro);

CREATE INDEX idx_id_empresa ON solicitudes_proyectos(id_empresa);
CREATE INDEX idx_codigo_departamento ON solicitudes_proyectos(codigo_departamento);
CREATE INDEX idx_codigo_municipio ON solicitudes_proyectos(codigo_municipio);
CREATE INDEX idx_codigo_carrera ON solicitudes_proyectos(codigo_carrera);
CREATE INDEX idx_codigo_modalidad ON solicitudes_proyectos(codigo_modalidad);
CREATE INDEX idx_id_admin_revision ON solicitudes_proyectos(id_admin_revision);
CREATE INDEX idx_codigo_estado ON solicitudes_proyectos(codigo_estado);
CREATE INDEX idx_fecha_creacion ON solicitudes_proyectos(fecha_creacion);

CREATE INDEX idx_id_empresa ON proyectos(id_empresa);
CREATE INDEX idx_codigo_departamento ON proyectos(codigo_departamento);
CREATE INDEX idx_codigo_municipio ON proyectos(codigo_municipio);
CREATE INDEX idx_codigo_carrera ON proyectos(codigo_carrera);
CREATE INDEX idx_codigo_modalidad ON proyectos(codigo_modalidad);
CREATE INDEX idx_id_admin_aprobacion ON proyectos(id_admin_aprobacion);
CREATE INDEX idx_codigo_estado ON proyectos(codigo_estado);
CREATE INDEX idx_fecha_creacion ON proyectos(fecha_creacion);

CREATE INDEX idx_id_estudiante ON postulaciones(id_estudiante);
CREATE INDEX idx_id_proyecto ON postulaciones(id_proyecto);
CREATE INDEX idx_fecha_postulacion ON postulaciones(fecha_postulacion);
CREATE INDEX idx_estudiante_proyecto ON postulaciones(id_estudiante, id_proyecto);



-- Insertar datos iniciales en la tabla de estados
INSERT INTO `estados` VALUES
('APRO', 'Aprobado'),
('CERR', 'Cerrado'),
('DIS', 'Disponible'),
('PEND', 'Pendiente'),
('CAN', 'Cancelado'),
('FIN', 'Finalizado'),
('RECH', 'Rechazado'),
('OBS', 'En Observación'),
('ELI', 'Eliminado'),
('REV', 'En Revisión');

-- Insertar datos iniciales en la tabla de modalidades
INSERT INTO `modalidades` VALUES
('HIB', 'Híbrido'),
('ONL', 'En Línea'),
('PRE', 'Presencial'),
('SEM', 'Semipresencial');

-- Insertar datos iniciales en la tabla de roles
INSERT INTO `roles` VALUES
('ADMIN', 'Administrador'),
('ESTUD', 'Estudiante'),
('EMP', 'Empresa'),
('COORD', 'Coordinador'),
('SUP', 'Supervisor');

-- Insertar datos iniciales en la tabla de departamentos de carreras
INSERT INTO departamentos_carreras (nombre) VALUES
('Departamento de Ingeniería y Arquitectura'),
('Departamento de Medicina'),
('Departamento de Ciencias y Humanidades'),
('Departamento de Ciencias Naturales y Matemática'),
('Departamento de Jurisprudencia y Ciencias Sociales'),
('Departamento de Economía');

-- Insertar datos iniciales en la tabla de usuarios - Administrador
-- Nota: La contraseña está encriptada usando bcrypt
INSERT INTO rpups_ues_fmocc.usuarios (nombres,apellidos,carnet,correo_institucional,correo_personal,telefono,username,password,codigo_rol,estado_activo,id_depto_carrera) VALUES
	 ('User','Administrador','','rpups.fmocc.ues@gmail.com','pb16016@ues.edu.sv','24840800','admuser','$2a$10$zSXZw5wtIb2iKJx//1vkBerAxfCGjODiYVK7HLPH5w9J.PJErvFmG','ADMIN', TRUE, null);


INSERT INTO carreras (codigo, nombre, id_depto_carrera) VALUES
-- Departamento de Ingeniería y Arquitectura (ID 1)
('ARQ', 'Arquitectura', 1),
('ING-CIVIL', 'Ingeniería Civil', 1),
('ING-ELEC', 'Ingeniería Eléctrica', 1),
('ING-INDU', 'Ingeniería Industrial', 1),
('ING-MEC', 'Ingeniería Mecánica', 1),
('ING-QUI', 'Ingeniería Química', 1),
('ING-SIST', 'Ingeniería en Sistemas Informáticos', 1),

-- Departamento de Medicina (ID 2)
('DOC-MED', 'Doctorado en Medicina', 2),

-- Departamento de Ciencias y Humanidades (ID 3)
('LIC-CEDUC', 'Licenciatura en Ciencias de la Educación', 3),
('LIC-CLLIT', 'Licenciatura en Ciencias del Lenguaje y Literatura', 3),
('LIC-ING', 'Licenciatura en Idioma Inglés', 3),
('PROF-EFD', 'Profesorado en Educación Física y Deportes', 3),
('PROF-EB', 'Profesorado en Educación Básica (1º y 2º ciclos)', 3),
('PROF-ING', 'Profesorado en Inglés (3º ciclo y bachillerato)', 3),
('PROF-LIT', 'Profesorado en Lenguaje y Literatura', 3),

-- Departamento de Ciencias Naturales y Matemática (ID 4)
('LIC-BIO', 'Licenciatura en Biología', 4),
('LIC-CQUI', 'Licenciatura en Ciencias Químicas', 4),
('LIC-EST', 'Licenciatura en Estadística', 4),
('LIC-GEOF', 'Licenciatura en Geofísica', 4),
('LIC-QFAR', 'Licenciatura en Química y Farmacia', 4),

-- Departamento de Jurisprudencia y Ciencias Sociales (ID 5)
('LIC-CJUR', 'Licenciatura en Ciencias Jurídicas', 5),
('LIC-SOC', 'Licenciatura en Sociología', 5),
('LIC-PSI', 'Licenciatura en Psicología', 5),

-- Departamento de Economía (ID 6)
('LIC-ADM', 'Licenciatura en Administración de empresas', 6),
('LIC-MERC', 'Licenciatura en Mercadeo Internacional', 6),
('LIC-CONT', 'Licenciatura en Contaduría Pública', 6);


-- Insertar datos iniciales en la tabla de rubros
INSERT INTO `rubros` (nombre) VALUES
('Aeronáutica y Espacial'),
('Agricultura'),
('Alimentos y Bebidas'),
('Artes y Cultura'),
('Automotriz'),
('Bienes Raíces'),
('Biotecnología'),
('Comercio'),
('Construcción'),
('Consultoría'),
('Deportes y Recreación'),
('Educación'),
('Electrónica'),
('Energía Renovable'),
('Gestión Ambiental'),
('Investigación y Desarrollo'),
('Manufactura'),
('Medios y Entretenimiento'),
('Minería'),
('Moda y Textiles'),
('Química'),
('Recursos Humanos'),
('Salud'),
('Seguridad y Vigilancia'),
('Seguros'),
('Servicios Financieros'),
('Tecnología de la Información'),
('Telecomunicaciones'),
('Transporte y Logística'),
('Turismo');

-- Insertar datos iniciales en la tabla de departamentos
INSERT INTO  `departamentos` VALUES
('01','Ahuachapán'),
('02','Cabañas'),
('03','Chalatenango'),
('04','Cuscatlán'),
('05','La Libertad'),
('06','La Paz'),
('07','La Unión'),
('08','Morazán'),
('09','San Miguel'),
('10','San Salvador'),
('11','San Vicente'),
('12','Santa Ana'),
('13','Sonsonate'),
('14','Usulután');

-- Insertar datos iniciales en la tabla de municipios

INSERT INTO `municipios` VALUES 
('0101','Ahuachapán','01'),
('0102','Apaneca','01'),
('0103','Atiquizaya','01'),
('0104','Concepción de Ataco','01'),
('0105','El Refugio','01'),
('0106','Guaymango','01'),
('0107','Jujutla','01'),
('0108','San Francisco Menéndez','01'),
('0109','San Lorenzo','01'),
('0110','San Pedro Puxtla','01'),
('0111','Tacuba','01'),
('0112','Turín','01'),
('0201','Sensuntepeque','02'),
('0202','Cinquera','02'),
('0203','Dolores','02'),
('0204','Guacotecti','02'),
('0205','Ilobasco','02'),
('0206','Jutiapa','02'),
('0207','San Isidro','02'),
('0208','Tejutepeque','02'),
('0209','Victoria','02'),
('0301','Chalatenango','03'),
('0302','Agua Caliente','03'),
('0303','Arcatao','03'),
('0304','Azacualpa','03'),
('0305','Cancasque','03'),
('0306','Citalá','03'),
('0307','Comalapa','03'),
('0308','Concepción Quezaltepeque','03'),
('0309','Dulce Nombre de María','03'),
('0310','El Carrizal','03'),
('0311','El Paraíso','03'),
('0312','La Laguna','03'),
('0313','La Palma','03'),
('0314','La Reina','03'),
('0315','Las Vueltas','03'),
('0316','Nombre de Jesús','03'),
('0317','Nueva Concepción','03'),
('0318','Nueva Trinidad','03'),
('0319','Ojos de Agua','03'),
('0320','Potonico','03'),
('0321','San Antonio de la Cruz','03'),
('0322','San Antonio los Ranchos','03'),
('0323','San Fernando','03'),
('0324','San Francisco Lempa','03'),
('0325','San Francisco Morazán','03'),
('0326','San Ignacio','03'),
('0327','San Isidro Labrador','03'),
('0328','San Luis del Carmen','03'),
('0329','San Miguel de Mercedes','03'),
('0330','San Rafael','03'),
('0331','Santa Rita','03'),
('0332','Tejutla','03'),
('0333','Las Flores','03'),
('0401','Cojutepeque','04'),
('0402','Candelaria','04'),
('0403','El Carmen','04'),
('0404','El Rosario','04'),
('0405','Monte San Juan','04'),
('0406','Oratorio de Concepción','04'),
('0407','San Bartolomé Perulapía','04'),
('0408','San Cristóbal','04'),
('0409','San José Guayabal','04'),
('0410','San Pedro Perulapán','04'),
('0411','San Rafael Cedros','04'),
('0412','San Ramón','04'),
('0413','Santa Cruz Analquito','04'),
('0414','Santa Cruz Michapa','04'),
('0415','Suchitoto','04'),
('0416','Tenancingo','04'),
('0501','Santa Tecla','05'),
('0502','Antiguo Cuscatlán','05'),
('0503','Colón','05'),
('0504','Huizúcar','05'),
('0505','Jayaque','05'),
('0506','Jicalapa','05'),
('0507','La Libertad','05'),
('0508','Nuevo Cuscatlán','05'),
('0509','San Juan Opico','05'),
('0510','Quezaltepeque','05'),
('0511','Sacacoyo','05'),
('0512','San José Villanueva','05'),
('0513','San Matías','05'),
('0514','San Pablo Tacachico','05'),
('0515','Talnique','05'),
('0516','Tamanique','05'),
('0517','Teotepeque','05'),
('0518','Tepecoyo','05'),
('0519','Zaragoza','05'),
('0520','Chiltuipán','05'),
('0521','Ciudad Arce','05'),
('0522','Comasagua','05'),
('0601','Zacatecoluca','06'),
('0602','San Juan Talpa','06'),
('0603','San Pedro Masahuat','06'),
('0604','San Juan Nonualco','06'),
('0605','Santiago Nonualco','06'),
('0606','Tapalhuaca','06'),
('0607','San Rafael Obrajuelo','06'),
('0608','San Emigdio','06'),
('0609','Santa María Ostuma','06'),
('0610','San Miguel Tepezontes','06'),
('0611','Mercedes La Ceiba','06'),
('0612','Olocuilta','06'),
('0613','Cuyultitán','06'),
('0614','San Pedro Nonualco','06'),
('0615','San Francisco Chinameca','06'),
('0616','San Antonio Masahuat','06'),
('0617','San Luis La Herradura','06'),
('0618','Paraiso de Osorio','06'),
('0619','San Luis Talpa','06'),
('0620','El Rosario','06'),
('0621','Jerusalén','06'),
('0622','San Juan Tepezontes','06'),
('0701','La Unión','07'),
('0702','Anamorós','07'),
('0703','Bolívar','07'),
('0704','Concepción de Oriente','07'),
('0705','Conchagua','07'),
('0706','El Carmen','07'),
('0707','El Sauce','07'),
('0708','Intipucá','07'),
('0709','Lislique','07'),
('0710','Meanguera del Golfo','07'),
('0711','Nueva Esparta','07'),
('0712','Pasaquina','07'),
('0713','Polorós','07'),
('0714','San Alejo','07'),
('0715','San José','07'),
('0716','Santa Rosa de Lima','07'),
('0717','Yayantique','07'),
('0718','Yucuaiquín','07'),
('0801','San Francisco Gotera','08'),
('0802','Arambala','08'),
('0803','Cacaopera','08'),
('0804','Chilanga','08'),
('0805','Corinto','08'),
('0806','Delicias de Concepción','08'),
('0807','El Divisadero','08'),
('0808','El Rosario','08'),
('0809','Gualococti','08'),
('0810','Guatajiagua','08'),
('0811','Joateca','08'),
('0812','Jocoaitique','08'),
('0813','Meanguera','08'),
('0814','Osicala','08'),
('0815','Perquín','08'),
('0816','San Carlos','08'),
('0817','San Fernando','08'),
('0818','San Isidro','08'),
('0819','San Simón','08'),
('0820','Sensembra','08'),
('0821','Sociedad','08'),
('0822','Torola','08'),
('0823','Yamabal','08'),
('0824','Jocoro','08'),
('0825','Lolotiquillo','08'),
('0826','Yoloaiquín','08'),
('0901','San Miguel','09'),
('0902','Carolina','09'),
('0903','Chapeltique','09'),
('0904','Chinameca','09'),
('0905','Chirilagua','09'),
('0906','Ciudad Barrios','09'),
('0907','Comacarán','09'),
('0908','El Tránsito','09'),
('0909','Lolotique','09'),
('0910','Moncagua','09'),
('0911','Nueva Guadalupe','09'),
('0912','Nuevo Edén de San Juan','09'),
('0913','Quelepa','09'),
('0914','San Antonio del Mosco','09'),
('0915','San Gerardo','09'),
('0916','San Jorge','09'),
('0917','San Luis de la Reina','09'),
('0918','San Rafael Oriente','09'),
('0919','Sesori','09'),
('0920','Uluazapa','09'),
('1001','San Salvador','10'),
('1002','Aguilares','10'),
('1003','Apopa','10'),
('1004','Ayutuxtepeque','10'),
('1005','Cuscatancingo','10'),
('1006','Ciudad Delgado','10'),
('1007','El Paisnal','10'),
('1008','Guazapa','10'),
('1009','Ilopango','10'),
('1010','Mejicanos','10'),
('1011','Nejapa','10'),
('1012','Panchimalco','10'),
('1013','Rosario de Mora','10'),
('1014','San Marcos','10'),
('1015','San Martín','10'),
('1016','Santiago Texacuangos','10'),
('1017','Santo Tomás','10'),
('1018','Soyapango','10'),
('1019','Tonacatepeque','10'),
('1101','San Vicente','11'),
('1102','Apastepeque','11'),
('1103','Guadalupe','11'),
('1104','San Cayetano Istepeque','11'),
('1105','San Esteban Catarina','11'),
('1106','San Ildefonso','11'),
('1107','San Lorenzo','11'),
('1108','San Sebastián','11'),
('1109','Santa Clara','11'),
('1110','Santo Domingo','11'),
('1111','Tecoluca','11'),
('1112','Tepetitán','11'),
('1113','Verapaz','11'),
('1201','Santa Ana','12'),
('1202','Candelaria de la Frontera','12'),
('1203','Chalchuapa','12'),
('1204','Coatepeque','12'),
('1205','El Congo','12'),
('1206','El Porvenir','12'),
('1207','Masahuat','12'),
('1208','Metapán','12'),
('1209','San Antonio Pajonal','12'),
('1210','San Sebastián Salitrillo','12'),
('1211','Santa Rosa Guachipilín','12'),
('1212','Santiago de la Frontera','12'),
('1213','Texistepeque','12'),
('1301','Sonsonate','13'),
('1302','Acajutla','13'),
('1303','Armenia','13'),
('1304','Caluco','13'),
('1305','Cuisnahuat','13'),
('1306','Izalco','13'),
('1307','Juayúa','13'),
('1308','Nahuizalco','13'),
('1309','Nahulingo','13'),
('1310','Salcoatitán','13'),
('1311','San Antonio del Monte','13'),
('1312','San Julián','13'),
('1313','Santa Catarina Masahuat','13'),
('1314','Santa Isabel Ishuatán','13'),
('1315','Santo Domingo de Guzmán','13'),
('1316','Sonzacate','13'),
('1401','Usulután','14'),
('1402','Alegría','14'),
('1403','Berlín','14'),
('1404','California','14'),
('1405','Concepción Batres','14'),
('1406','El Triunfo','14'),
('1407','Ereguayquín','14'),
('1408','Estanzuelas','14'),
('1409','Jiquilisco','14'),
('1410','Jucuarán','14'),
('1411','Ozatlán','14'),
('1412','Puerto El Triunfo','14'),
('1413','San Agustín','14'),
('1414','San Buenaventura','14'),
('1415','San Dionisio','14'),
('1416','San Francisco Javier','14'),
('1417','Santa Elena','14'),
('1418','Santa María','14'),
('1419','Santiago de María','14'),
('1420','Tecapán','14'),
('1421','Jucuapa','14'),
('1422','Mercedes Umaña','14'),
('1423','Nueva Granada','14');


INSERT INTO empresas (nombre_comercial,nombre_legal,contacto_nombre,contacto_telefono,contacto_email,codigo_departamento,codigo_municipio,direccion_detallada,estado_activo,id_rubro,id_user_creacion) VALUES (
    'Universidad de El Salvador Facultad Multidisciplinaria de Occidente',
    'Universidad de El Salvador',
    'User Administrador',
    '24840800',
    'rpups.fmocc.ues@gmail.com',
    '12',
    '1201',
    'Final Ave. Fray Felipe de Jesus Moraga Sur, Santa Ana, El Salvador',
    TRUE,
    12,
    1
);
