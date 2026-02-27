-- Crear la base de datos (si no existe ya)
CREATE DATABASE IF NOT EXISTS `rpups_ues_fmocc`
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_general_ci;

-- Usar la base de datos recién creada
USE `rpups_ues_fmocc`;
-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host:     Database: rpups_ues_fmocc
-- ------------------------------------------------------
-- Server version	8.0.42

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `carreras`
--

DROP TABLE IF EXISTS `carreras`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `carreras` (
  `codigo` char(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nombre` varchar(250) COLLATE utf8mb4_unicode_ci NOT NULL,
  `id_depto_carrera` int NOT NULL,
  PRIMARY KEY (`codigo`),
  UNIQUE KEY `nombre` (`nombre`),
  KEY `id_depto_carrera` (`id_depto_carrera`),
  CONSTRAINT `carreras_ibfk_1` FOREIGN KEY (`id_depto_carrera`) REFERENCES `departamentos_carreras` (`id_depto_carrera`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carreras`
--

LOCK TABLES `carreras` WRITE;
/*!40000 ALTER TABLE `carreras` DISABLE KEYS */;
INSERT INTO `carreras` VALUES ('ARQ','Arquitectura',1),('DOC-MED','Doctorado en Medicina',2),('ING-CIVIL','Ingeniería Civil',1),('ING-ELEC','Ingeniería Eléctrica',1),('ING-INDU','Ingeniería Industrial',1),('ING-MEC','Ingeniería Mecánica',1),('ING-QUI','Ingeniería Química',1),('ING-SIST','Ingeniería en Sistemas Informáticos',1),('LIC-ADM','Licenciatura en Administración de empresas',6),('LIC-BIO','Licenciatura en Biología',4),('LIC-CEDUC','Licenciatura en Ciencias de la Educación',3),('LIC-CJUR','Licenciatura en Ciencias Jurídicas',5),('LIC-CLLIT','Licenciatura en Ciencias del Lenguaje y Literatura',3),('LIC-CONT','Licenciatura en Contaduría Pública',6),('LIC-CQUI','Licenciatura en Ciencias Químicas',4),('LIC-EST','Licenciatura en Estadística',4),('LIC-GEOF','Licenciatura en Geofísica',4),('LIC-ING','Licenciatura en Idioma Inglés',3),('LIC-MERC','Licenciatura en Mercadeo Internacional',6),('LIC-PSI','Licenciatura en Psicología',5),('LIC-QFAR','Licenciatura en Química y Farmacia',4),('LIC-SOC','Licenciatura en Sociología',5),('PROF-EB','Profesorado en Educación Básica (1º y 2º ciclos)',3),('PROF-EFD','Profesorado en Educación Física y Deportes',3),('PROF-ING','Profesorado en Inglés (3º ciclo y bachillerato)',3),('PROF-LIT','Profesorado en Lenguaje y Literatura',3);
/*!40000 ALTER TABLE `carreras` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `departamentos`
--

DROP TABLE IF EXISTS `departamentos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `departamentos` (
  `codigo` char(2) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nombre` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`codigo`),
  UNIQUE KEY `nombre` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `departamentos`
--

LOCK TABLES `departamentos` WRITE;
/*!40000 ALTER TABLE `departamentos` DISABLE KEYS */;
INSERT INTO `departamentos` VALUES ('01','Ahuachapán'),('02','Cabañas'),('03','Chalatenango'),('04','Cuscatlán'),('05','La Libertad'),('06','La Paz'),('07','La Unión'),('08','Morazán'),('09','San Miguel'),('10','San Salvador'),('11','San Vicente'),('12','Santa Ana'),('13','Sonsonate'),('14','Usulután');
/*!40000 ALTER TABLE `departamentos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `departamentos_carreras`
--

DROP TABLE IF EXISTS `departamentos_carreras`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `departamentos_carreras` (
  `id_depto_carrera` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(250) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id_depto_carrera`),
  UNIQUE KEY `nombre` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `departamentos_carreras`
--

LOCK TABLES `departamentos_carreras` WRITE;
/*!40000 ALTER TABLE `departamentos_carreras` DISABLE KEYS */;
INSERT INTO `departamentos_carreras` VALUES (4,'Departamento de Ciencias Naturales y Matemática'),(3,'Departamento de Ciencias y Humanidades'),(6,'Departamento de Economía'),(1,'Departamento de Ingeniería y Arquitectura'),(5,'Departamento de Jurisprudencia y Ciencias Sociales'),(2,'Departamento de Medicina');
/*!40000 ALTER TABLE `departamentos_carreras` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `empresas`
--

DROP TABLE IF EXISTS `empresas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `empresas` (
  `id_empresa` int NOT NULL AUTO_INCREMENT,
  `nombre_comercial` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nombre_legal` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `contacto_nombre` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `contacto_telefono` varchar(12) COLLATE utf8mb4_unicode_ci NOT NULL,
  `contacto_email` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `codigo_departamento` char(2) COLLATE utf8mb4_unicode_ci NOT NULL,
  `codigo_municipio` char(4) COLLATE utf8mb4_unicode_ci NOT NULL,
  `direccion_detallada` text COLLATE utf8mb4_unicode_ci,
  `estado_activo` tinyint(1) NOT NULL,
  `id_rubro` int NOT NULL,
  `id_user_creacion` int NOT NULL,
  PRIMARY KEY (`id_empresa`),
  UNIQUE KEY `unique_empresa` (`nombre_comercial`,`nombre_legal`),
  KEY `id_user_creacion` (`id_user_creacion`),
  KEY `idx_empresa_departamento` (`codigo_departamento`),
  KEY `idx_empresa_municipio` (`codigo_municipio`),
  KEY `idx_empresa_rubro` (`id_rubro`),
  CONSTRAINT `empresas_ibfk_1` FOREIGN KEY (`codigo_departamento`) REFERENCES `departamentos` (`codigo`),
  CONSTRAINT `empresas_ibfk_2` FOREIGN KEY (`codigo_municipio`) REFERENCES `municipios` (`codigo`),
  CONSTRAINT `empresas_ibfk_3` FOREIGN KEY (`id_rubro`) REFERENCES `rubros` (`id_rubro`),
  CONSTRAINT `empresas_ibfk_4` FOREIGN KEY (`id_user_creacion`) REFERENCES `usuarios` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `empresas`
--

LOCK TABLES `empresas` WRITE;
/*!40000 ALTER TABLE `empresas` DISABLE KEYS */;
INSERT INTO `empresas` VALUES (1,'Universidad de El Salvador Facultad Multidisciplinaria de Occidente','Universidad de El Salvador','User Administrador','24840800','rpups.fmocc.ues@gmail.com','12','1201','Final Ave. Fray Felipe de Jesus Moraga Sur, Santa Ana, El Salvador',1,12,1),(2,'test','test','test','7636-3622','test@test.com','06','0603','idhfs isuhfsiud isdhfsi isdhfisfs',1,27,2),(3,'test eemp','test emp','ale','7363-6226','ale@ale.com','01','0103','jahkfdhas ashdgfjakhsd fas',1,4,5);
/*!40000 ALTER TABLE `empresas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `estados`
--

DROP TABLE IF EXISTS `estados`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `estados` (
  `codigo_estado` char(5) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nombre` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`codigo_estado`),
  UNIQUE KEY `nombre` (`nombre`),
  KEY `idx_estado_codigo` (`codigo_estado`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `estados`
--

LOCK TABLES `estados` WRITE;
/*!40000 ALTER TABLE `estados` DISABLE KEYS */;
INSERT INTO `estados` VALUES ('APRO','Aprobado'),('CAN','Cancelado'),('CERR','Cerrado'),('DIS','Disponible'),('ELI','Eliminado'),('OBS','En Observación'),('REV','En Revisión'),('FIN','Finalizado'),('PEND','Pendiente'),('RECH','Rechazado');
/*!40000 ALTER TABLE `estados` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `modalidades`
--

DROP TABLE IF EXISTS `modalidades`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `modalidades` (
  `codigo_modalidad` char(3) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nombre` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`codigo_modalidad`),
  UNIQUE KEY `nombre` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `modalidades`
--

LOCK TABLES `modalidades` WRITE;
/*!40000 ALTER TABLE `modalidades` DISABLE KEYS */;
INSERT INTO `modalidades` VALUES ('ONL','En Línea'),('HIB','Híbrido'),('PRE','Presencial'),('SEM','Semipresencial');
/*!40000 ALTER TABLE `modalidades` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `municipios`
--

DROP TABLE IF EXISTS `municipios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `municipios` (
  `codigo` char(4) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nombre` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `codigo_departamento` char(2) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`codigo`),
  KEY `idx_municipio_departamento` (`codigo_departamento`),
  CONSTRAINT `municipios_ibfk_1` FOREIGN KEY (`codigo_departamento`) REFERENCES `departamentos` (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `municipios`
--

LOCK TABLES `municipios` WRITE;
/*!40000 ALTER TABLE `municipios` DISABLE KEYS */;
INSERT INTO `municipios` VALUES ('0101','Ahuachapán','01'),('0102','Apaneca','01'),('0103','Atiquizaya','01'),('0104','Concepción de Ataco','01'),('0105','El Refugio','01'),('0106','Guaymango','01'),('0107','Jujutla','01'),('0108','San Francisco Menéndez','01'),('0109','San Lorenzo','01'),('0110','San Pedro Puxtla','01'),('0111','Tacuba','01'),('0112','Turín','01'),('0201','Sensuntepeque','02'),('0202','Cinquera','02'),('0203','Dolores','02'),('0204','Guacotecti','02'),('0205','Ilobasco','02'),('0206','Jutiapa','02'),('0207','San Isidro','02'),('0208','Tejutepeque','02'),('0209','Victoria','02'),('0301','Chalatenango','03'),('0302','Agua Caliente','03'),('0303','Arcatao','03'),('0304','Azacualpa','03'),('0305','Cancasque','03'),('0306','Citalá','03'),('0307','Comalapa','03'),('0308','Concepción Quezaltepeque','03'),('0309','Dulce Nombre de María','03'),('0310','El Carrizal','03'),('0311','El Paraíso','03'),('0312','La Laguna','03'),('0313','La Palma','03'),('0314','La Reina','03'),('0315','Las Vueltas','03'),('0316','Nombre de Jesús','03'),('0317','Nueva Concepción','03'),('0318','Nueva Trinidad','03'),('0319','Ojos de Agua','03'),('0320','Potonico','03'),('0321','San Antonio de la Cruz','03'),('0322','San Antonio los Ranchos','03'),('0323','San Fernando','03'),('0324','San Francisco Lempa','03'),('0325','San Francisco Morazán','03'),('0326','San Ignacio','03'),('0327','San Isidro Labrador','03'),('0328','San Luis del Carmen','03'),('0329','San Miguel de Mercedes','03'),('0330','San Rafael','03'),('0331','Santa Rita','03'),('0332','Tejutla','03'),('0333','Las Flores','03'),('0401','Cojutepeque','04'),('0402','Candelaria','04'),('0403','El Carmen','04'),('0404','El Rosario','04'),('0405','Monte San Juan','04'),('0406','Oratorio de Concepción','04'),('0407','San Bartolomé Perulapía','04'),('0408','San Cristóbal','04'),('0409','San José Guayabal','04'),('0410','San Pedro Perulapán','04'),('0411','San Rafael Cedros','04'),('0412','San Ramón','04'),('0413','Santa Cruz Analquito','04'),('0414','Santa Cruz Michapa','04'),('0415','Suchitoto','04'),('0416','Tenancingo','04'),('0501','Santa Tecla','05'),('0502','Antiguo Cuscatlán','05'),('0503','Colón','05'),('0504','Huizúcar','05'),('0505','Jayaque','05'),('0506','Jicalapa','05'),('0507','La Libertad','05'),('0508','Nuevo Cuscatlán','05'),('0509','San Juan Opico','05'),('0510','Quezaltepeque','05'),('0511','Sacacoyo','05'),('0512','San José Villanueva','05'),('0513','San Matías','05'),('0514','San Pablo Tacachico','05'),('0515','Talnique','05'),('0516','Tamanique','05'),('0517','Teotepeque','05'),('0518','Tepecoyo','05'),('0519','Zaragoza','05'),('0520','Chiltuipán','05'),('0521','Ciudad Arce','05'),('0522','Comasagua','05'),('0601','Zacatecoluca','06'),('0602','San Juan Talpa','06'),('0603','San Pedro Masahuat','06'),('0604','San Juan Nonualco','06'),('0605','Santiago Nonualco','06'),('0606','Tapalhuaca','06'),('0607','San Rafael Obrajuelo','06'),('0608','San Emigdio','06'),('0609','Santa María Ostuma','06'),('0610','San Miguel Tepezontes','06'),('0611','Mercedes La Ceiba','06'),('0612','Olocuilta','06'),('0613','Cuyultitán','06'),('0614','San Pedro Nonualco','06'),('0615','San Francisco Chinameca','06'),('0616','San Antonio Masahuat','06'),('0617','San Luis La Herradura','06'),('0618','Paraiso de Osorio','06'),('0619','San Luis Talpa','06'),('0620','El Rosario','06'),('0621','Jerusalén','06'),('0622','San Juan Tepezontes','06'),('0701','La Unión','07'),('0702','Anamorós','07'),('0703','Bolívar','07'),('0704','Concepción de Oriente','07'),('0705','Conchagua','07'),('0706','El Carmen','07'),('0707','El Sauce','07'),('0708','Intipucá','07'),('0709','Lislique','07'),('0710','Meanguera del Golfo','07'),('0711','Nueva Esparta','07'),('0712','Pasaquina','07'),('0713','Polorós','07'),('0714','San Alejo','07'),('0715','San José','07'),('0716','Santa Rosa de Lima','07'),('0717','Yayantique','07'),('0718','Yucuaiquín','07'),('0801','San Francisco Gotera','08'),('0802','Arambala','08'),('0803','Cacaopera','08'),('0804','Chilanga','08'),('0805','Corinto','08'),('0806','Delicias de Concepción','08'),('0807','El Divisadero','08'),('0808','El Rosario','08'),('0809','Gualococti','08'),('0810','Guatajiagua','08'),('0811','Joateca','08'),('0812','Jocoaitique','08'),('0813','Meanguera','08'),('0814','Osicala','08'),('0815','Perquín','08'),('0816','San Carlos','08'),('0817','San Fernando','08'),('0818','San Isidro','08'),('0819','San Simón','08'),('0820','Sensembra','08'),('0821','Sociedad','08'),('0822','Torola','08'),('0823','Yamabal','08'),('0824','Jocoro','08'),('0825','Lolotiquillo','08'),('0826','Yoloaiquín','08'),('0901','San Miguel','09'),('0902','Carolina','09'),('0903','Chapeltique','09'),('0904','Chinameca','09'),('0905','Chirilagua','09'),('0906','Ciudad Barrios','09'),('0907','Comacarán','09'),('0908','El Tránsito','09'),('0909','Lolotique','09'),('0910','Moncagua','09'),('0911','Nueva Guadalupe','09'),('0912','Nuevo Edén de San Juan','09'),('0913','Quelepa','09'),('0914','San Antonio del Mosco','09'),('0915','San Gerardo','09'),('0916','San Jorge','09'),('0917','San Luis de la Reina','09'),('0918','San Rafael Oriente','09'),('0919','Sesori','09'),('0920','Uluazapa','09'),('1001','San Salvador','10'),('1002','Aguilares','10'),('1003','Apopa','10'),('1004','Ayutuxtepeque','10'),('1005','Cuscatancingo','10'),('1006','Ciudad Delgado','10'),('1007','El Paisnal','10'),('1008','Guazapa','10'),('1009','Ilopango','10'),('1010','Mejicanos','10'),('1011','Nejapa','10'),('1012','Panchimalco','10'),('1013','Rosario de Mora','10'),('1014','San Marcos','10'),('1015','San Martín','10'),('1016','Santiago Texacuangos','10'),('1017','Santo Tomás','10'),('1018','Soyapango','10'),('1019','Tonacatepeque','10'),('1101','San Vicente','11'),('1102','Apastepeque','11'),('1103','Guadalupe','11'),('1104','San Cayetano Istepeque','11'),('1105','San Esteban Catarina','11'),('1106','San Ildefonso','11'),('1107','San Lorenzo','11'),('1108','San Sebastián','11'),('1109','Santa Clara','11'),('1110','Santo Domingo','11'),('1111','Tecoluca','11'),('1112','Tepetitán','11'),('1113','Verapaz','11'),('1201','Santa Ana','12'),('1202','Candelaria de la Frontera','12'),('1203','Chalchuapa','12'),('1204','Coatepeque','12'),('1205','El Congo','12'),('1206','El Porvenir','12'),('1207','Masahuat','12'),('1208','Metapán','12'),('1209','San Antonio Pajonal','12'),('1210','San Sebastián Salitrillo','12'),('1211','Santa Rosa Guachipilín','12'),('1212','Santiago de la Frontera','12'),('1213','Texistepeque','12'),('1301','Sonsonate','13'),('1302','Acajutla','13'),('1303','Armenia','13'),('1304','Caluco','13'),('1305','Cuisnahuat','13'),('1306','Izalco','13'),('1307','Juayúa','13'),('1308','Nahuizalco','13'),('1309','Nahulingo','13'),('1310','Salcoatitán','13'),('1311','San Antonio del Monte','13'),('1312','San Julián','13'),('1313','Santa Catarina Masahuat','13'),('1314','Santa Isabel Ishuatán','13'),('1315','Santo Domingo de Guzmán','13'),('1316','Sonzacate','13'),('1401','Usulután','14'),('1402','Alegría','14'),('1403','Berlín','14'),('1404','California','14'),('1405','Concepción Batres','14'),('1406','El Triunfo','14'),('1407','Ereguayquín','14'),('1408','Estanzuelas','14'),('1409','Jiquilisco','14'),('1410','Jucuarán','14'),('1411','Ozatlán','14'),('1412','Puerto El Triunfo','14'),('1413','San Agustín','14'),('1414','San Buenaventura','14'),('1415','San Dionisio','14'),('1416','San Francisco Javier','14'),('1417','Santa Elena','14'),('1418','Santa María','14'),('1419','Santiago de María','14'),('1420','Tecapán','14'),('1421','Jucuapa','14'),('1422','Mercedes Umaña','14'),('1423','Nueva Granada','14');
/*!40000 ALTER TABLE `municipios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notificaciones`
--

DROP TABLE IF EXISTS `notificaciones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notificaciones` (
  `id_notificacion` int NOT NULL AUTO_INCREMENT,
  `id_usuario` int NOT NULL,
  `titulo` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `mensaje` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `tipo` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'INFO',
  `leida` tinyint(1) DEFAULT '0',
  `fecha_creacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_lectura` timestamp NULL DEFAULT NULL,
  `enlace` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `id_referencia` int DEFAULT NULL,
  `tipo_referencia` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id_notificacion`),
  KEY `idx_notificacion_usuario` (`id_usuario`),
  KEY `idx_notificacion_leida` (`id_usuario`,`leida`),
  KEY `idx_notificacion_fecha` (`fecha_creacion`),
  CONSTRAINT `notificaciones_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notificaciones`
--
-- Table structure for table `postulaciones`
--

DROP TABLE IF EXISTS `postulaciones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `postulaciones` (
  `id_postulacion` int NOT NULL AUTO_INCREMENT,
  `id_estudiante` int NOT NULL,
  `id_proyecto` int NOT NULL,
  `fecha_postulacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `codigo_estado` char(5) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PEND',
  `fecha_cambio_estado` timestamp NULL DEFAULT NULL,
  `id_admin_cambio_estado` int DEFAULT NULL,
  `observaciones` text COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`id_postulacion`),
  UNIQUE KEY `unique_postulacion` (`id_estudiante`,`id_proyecto`),
  KEY `id_admin_cambio_estado` (`id_admin_cambio_estado`),
  KEY `idx_postulacion_estudiante` (`id_estudiante`),
  KEY `idx_postulacion_proyecto` (`id_proyecto`),
  KEY `idx_postulacion_fecha` (`fecha_postulacion`),
  KEY `idx_postulacion_estudiante_proyecto` (`id_estudiante`,`id_proyecto`),
  KEY `idx_postulacion_estado` (`codigo_estado`),
  KEY `idx_postulacion_proyecto_estado` (`id_proyecto`,`codigo_estado`),
  CONSTRAINT `postulaciones_ibfk_1` FOREIGN KEY (`id_estudiante`) REFERENCES `usuarios` (`id_usuario`),
  CONSTRAINT `postulaciones_ibfk_2` FOREIGN KEY (`id_proyecto`) REFERENCES `proyectos` (`id_proyecto`),
  CONSTRAINT `postulaciones_ibfk_3` FOREIGN KEY (`codigo_estado`) REFERENCES `estados` (`codigo_estado`),
  CONSTRAINT `postulaciones_ibfk_4` FOREIGN KEY (`id_admin_cambio_estado`) REFERENCES `usuarios` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `postulaciones`
--
--
-- Table structure for table `proyectos`
--

DROP TABLE IF EXISTS `proyectos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `proyectos` (
  `id_proyecto` int NOT NULL AUTO_INCREMENT,
  `titulo` varchar(250) COLLATE utf8mb4_unicode_ci NOT NULL,
  `descripcion` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `requisitos` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `fecha_inicio` date DEFAULT NULL,
  `fecha_fin` date DEFAULT NULL,
  `duracion` int NOT NULL,
  `max_estudiantes` int NOT NULL,
  `direccion_detallada` text COLLATE utf8mb4_unicode_ci,
  `fecha_creacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `id_empresa` int NOT NULL,
  `codigo_departamento` char(2) COLLATE utf8mb4_unicode_ci NOT NULL,
  `codigo_municipio` char(4) COLLATE utf8mb4_unicode_ci NOT NULL,
  `codigo_carrera` char(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `codigo_modalidad` char(3) COLLATE utf8mb4_unicode_ci NOT NULL,
  `id_admin_aprobacion` int NOT NULL,
  `codigo_estado` char(5) COLLATE utf8mb4_unicode_ci NOT NULL,
  `id_solicitud_origen` int DEFAULT NULL,
  PRIMARY KEY (`id_proyecto`),
  UNIQUE KEY `unique_proyecto` (`titulo`,`id_empresa`),
  KEY `id_solicitud_origen` (`id_solicitud_origen`),
  KEY `idx_proyecto_empresa` (`id_empresa`),
  KEY `idx_proyecto_departamento` (`codigo_departamento`),
  KEY `idx_proyecto_municipio` (`codigo_municipio`),
  KEY `idx_proyecto_carrera` (`codigo_carrera`),
  KEY `idx_proyecto_modalidad` (`codigo_modalidad`),
  KEY `idx_proyecto_admin` (`id_admin_aprobacion`),
  KEY `idx_proyecto_estado` (`codigo_estado`),
  KEY `idx_proyecto_fecha` (`fecha_creacion`),
  CONSTRAINT `proyectos_ibfk_1` FOREIGN KEY (`id_empresa`) REFERENCES `empresas` (`id_empresa`),
  CONSTRAINT `proyectos_ibfk_2` FOREIGN KEY (`codigo_departamento`) REFERENCES `departamentos` (`codigo`),
  CONSTRAINT `proyectos_ibfk_3` FOREIGN KEY (`codigo_municipio`) REFERENCES `municipios` (`codigo`),
  CONSTRAINT `proyectos_ibfk_4` FOREIGN KEY (`codigo_carrera`) REFERENCES `carreras` (`codigo`),
  CONSTRAINT `proyectos_ibfk_5` FOREIGN KEY (`codigo_modalidad`) REFERENCES `modalidades` (`codigo_modalidad`),
  CONSTRAINT `proyectos_ibfk_6` FOREIGN KEY (`id_admin_aprobacion`) REFERENCES `usuarios` (`id_usuario`),
  CONSTRAINT `proyectos_ibfk_7` FOREIGN KEY (`codigo_estado`) REFERENCES `estados` (`codigo_estado`),
  CONSTRAINT `proyectos_ibfk_8` FOREIGN KEY (`id_solicitud_origen`) REFERENCES `solicitudes_proyectos` (`id_solicitud`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `proyectos`
--
--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `codigo` char(5) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nombre` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`codigo`),
  UNIQUE KEY `nombre` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES ('ADMIN','Administrador'),('COORD','Coordinador'),('EMP','Empresa'),('ESTUD','Estudiante'),('SUP','Supervisor');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rubros`
--

DROP TABLE IF EXISTS `rubros`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rubros` (
  `id_rubro` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id_rubro`),
  UNIQUE KEY `nombre` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rubros`
--

LOCK TABLES `rubros` WRITE;
/*!40000 ALTER TABLE `rubros` DISABLE KEYS */;
INSERT INTO `rubros` VALUES (1,'Aeronáutica y Espacial'),(2,'Agricultura'),(3,'Alimentos y Bebidas'),(4,'Artes y Cultura'),(5,'Automotriz'),(6,'Bienes Raíces'),(7,'Biotecnología'),(8,'Comercio'),(9,'Construcción'),(10,'Consultoría'),(11,'Deportes y Recreación'),(12,'Educación'),(13,'Electrónica'),(14,'Energía Renovable'),(15,'Gestión Ambiental'),(16,'Investigación y Desarrollo'),(17,'Manufactura'),(18,'Medios y Entretenimiento'),(19,'Minería'),(20,'Moda y Textiles'),(21,'Química'),(22,'Recursos Humanos'),(23,'Salud'),(24,'Seguridad y Vigilancia'),(25,'Seguros'),(26,'Servicios Financieros'),(27,'Tecnología de la Información'),(28,'Telecomunicaciones'),(29,'Transporte y Logística'),(30,'Turismo');
/*!40000 ALTER TABLE `rubros` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `solicitudes_proyectos`
--

DROP TABLE IF EXISTS `solicitudes_proyectos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `solicitudes_proyectos` (
  `id_solicitud` int NOT NULL AUTO_INCREMENT,
  `titulo` varchar(250) COLLATE utf8mb4_unicode_ci NOT NULL,
  `descripcion` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `requisitos` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `fecha_inicio` date DEFAULT NULL,
  `fecha_fin` date DEFAULT NULL,
  `duracion` int DEFAULT NULL,
  `max_estudiantes` int NOT NULL,
  `direccion_detallada` text COLLATE utf8mb4_unicode_ci,
  `fecha_revision` timestamp NULL DEFAULT NULL,
  `fecha_creacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `observaciones` text COLLATE utf8mb4_unicode_ci,
  `id_empresa` int NOT NULL,
  `codigo_departamento` char(2) COLLATE utf8mb4_unicode_ci NOT NULL,
  `codigo_municipio` char(4) COLLATE utf8mb4_unicode_ci NOT NULL,
  `codigo_carrera` char(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `codigo_modalidad` char(3) COLLATE utf8mb4_unicode_ci NOT NULL,
  `id_admin_revision` int DEFAULT NULL,
  `id_user_creacion` int NOT NULL,
  `codigo_estado` char(5) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id_solicitud`),
  UNIQUE KEY `unique_solicitud` (`titulo`,`id_empresa`),
  KEY `id_user_creacion` (`id_user_creacion`),
  KEY `idx_solicitud_empresa` (`id_empresa`),
  KEY `idx_solicitud_departamento` (`codigo_departamento`),
  KEY `idx_solicitud_municipio` (`codigo_municipio`),
  KEY `idx_solicitud_carrera` (`codigo_carrera`),
  KEY `idx_solicitud_modalidad` (`codigo_modalidad`),
  KEY `idx_solicitud_admin` (`id_admin_revision`),
  KEY `idx_solicitud_estado` (`codigo_estado`),
  KEY `idx_solicitud_fecha` (`fecha_creacion`),
  CONSTRAINT `solicitudes_proyectos_ibfk_1` FOREIGN KEY (`id_empresa`) REFERENCES `empresas` (`id_empresa`),
  CONSTRAINT `solicitudes_proyectos_ibfk_2` FOREIGN KEY (`codigo_departamento`) REFERENCES `departamentos` (`codigo`),
  CONSTRAINT `solicitudes_proyectos_ibfk_3` FOREIGN KEY (`codigo_municipio`) REFERENCES `municipios` (`codigo`),
  CONSTRAINT `solicitudes_proyectos_ibfk_4` FOREIGN KEY (`codigo_carrera`) REFERENCES `carreras` (`codigo`),
  CONSTRAINT `solicitudes_proyectos_ibfk_5` FOREIGN KEY (`codigo_modalidad`) REFERENCES `modalidades` (`codigo_modalidad`),
  CONSTRAINT `solicitudes_proyectos_ibfk_6` FOREIGN KEY (`id_admin_revision`) REFERENCES `usuarios` (`id_usuario`),
  CONSTRAINT `solicitudes_proyectos_ibfk_7` FOREIGN KEY (`id_user_creacion`) REFERENCES `usuarios` (`id_usuario`),
  CONSTRAINT `solicitudes_proyectos_ibfk_8` FOREIGN KEY (`codigo_estado`) REFERENCES `estados` (`codigo_estado`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id_usuario` int NOT NULL AUTO_INCREMENT,
  `nombres` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `apellidos` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `carnet` char(8) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `correo_institucional` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `correo_personal` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `telefono` varchar(12) COLLATE utf8mb4_unicode_ci NOT NULL,
  `username` varchar(15) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `codigo_rol` char(5) COLLATE utf8mb4_unicode_ci NOT NULL,
  `estado_activo` tinyint(1) NOT NULL DEFAULT '1',
  `id_depto_carrera` int DEFAULT NULL,
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `correo_institucional` (`correo_institucional`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `unique_user` (`username`,`correo_institucional`),
  KEY `id_depto_carrera` (`id_depto_carrera`),
  KEY `idx_usuario_carnet` (`carnet`),
  KEY `idx_usuario_correo` (`correo_institucional`),
  KEY `idx_usuario_username` (`username`),
  KEY `idx_usuario_rol` (`codigo_rol`),
  CONSTRAINT `usuarios_ibfk_1` FOREIGN KEY (`id_depto_carrera`) REFERENCES `departamentos_carreras` (`id_depto_carrera`),
  CONSTRAINT `usuarios_ibfk_2` FOREIGN KEY (`codigo_rol`) REFERENCES `roles` (`codigo`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'Admin','Sistema','','admin@ues.edu.sv','admin@correo.com','2484-0800','admin','$2a$10$cJeFmISGL8akwpitImYkJeIoMY1dWS6lLXBi8p.VnuxbOqlc2oz72','ADMIN',1,NULL),
(2,'Carlos Alberto','Martínez López','ML20001','ml20001@ues.edu.sv','carlos.martinez@correo.com','7600-1001','estudiante1','$2a$10$cJeFmISGL8akwpitImYkJeIoMY1dWS6lLXBi8p.VnuxbOqlc2oz72','ESTUD',1,1),
(3,'María Elena','Ramírez Torres','','coordinador@ues.edu.sv','maria.ramirez@correo.com','7600-1002','coordinador','$2a$10$cJeFmISGL8akwpitImYkJeIoMY1dWS6lLXBi8p.VnuxbOqlc2oz72','COORD',1,1),
(4,'José Roberto','Hernández Flores','','supervisor@ues.edu.sv','jose.hernandez@correo.com','7600-1003','supervisor','$2a$10$cJeFmISGL8akwpitImYkJeIoMY1dWS6lLXBi8p.VnuxbOqlc2oz72','SUP',1,1),
(5,'Ana Lucía','García Vega','','empresa@ues.edu.sv','ana.garcia@correo.com','7600-1004','empresa','$2a$10$cJeFmISGL8akwpitImYkJeIoMY1dWS6lLXBi8p.VnuxbOqlc2oz72','EMP',1,1),
(7,'Luis Fernando','Pérez Castillo','PC20002','pc20002@ues.edu.sv','luis.perez@correo.com','7600-1005','estudiante2','$2a$10$cJeFmISGL8akwpitImYkJeIoMY1dWS6lLXBi8p.VnuxbOqlc2oz72','ESTUD',1,1),
(8,'Andrea Sofía','Rivas Mendoza','RM20003','rm20003@ues.edu.sv','andrea.rivas@correo.com','7600-1006','estudiante3','$2a$10$cJeFmISGL8akwpitImYkJeIoMY1dWS6lLXBi8p.VnuxbOqlc2oz72','ESTUD',1,1);
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'rpups_ues_fmocc'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-02-27  9:49:10
