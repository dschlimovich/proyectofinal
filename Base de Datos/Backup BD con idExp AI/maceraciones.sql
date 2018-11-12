-- phpMyAdmin SQL Dump
-- version 4.6.6deb5
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost:3306
-- Tiempo de generación: 12-11-2018 a las 17:07:50
-- Versión del servidor: 10.1.29-MariaDB-6+b1
-- Versión de PHP: 7.2.4-1+b2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `maceraciones`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Experimento`
--

CREATE TABLE `Experimento` (
  `id` int(11) NOT NULL,
  `fecha` datetime DEFAULT CURRENT_TIMESTAMP,
  `maceracion` int(11) DEFAULT NULL,
  `duracion_min` int(11) DEFAULT NULL,
  `intervaloMedicionTemp_seg` float DEFAULT NULL,
  `intervaloMedicionPH_seg` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `Experimento`
--

INSERT INTO `Experimento` (`id`, `fecha`, `maceracion`, `duracion_min`, `intervaloMedicionTemp_seg`, `intervaloMedicionPH_seg`) VALUES
(1, '2018-08-15 18:47:54', 1, 10, 30, 30),
(2, '2018-08-15 18:50:42', 1, 10, 30, 30),
(3, '2018-10-26 19:50:21', 4, 69, 60, 120),
(4, '2018-10-26 19:51:07', 4, 69, 60, 120),
(5, '2018-10-26 19:53:25', 4, 69, 60, 120),
(6, '2018-10-26 19:53:28', 4, 69, 60, 120),
(7, '2018-10-26 19:53:29', 4, 69, 60, 120),
(8, '2018-10-26 19:53:31', 4, 69, 60, 120),
(9, '2018-10-26 19:53:32', 4, 69, 60, 120),
(10, '2018-10-26 20:02:18', 4, 1, 15, 15),
(11, '2018-10-31 10:01:49', 4, 1, 15, 15),
(12, '2018-10-31 10:02:44', 4, 1, 15, 15),
(13, '2018-10-31 10:05:41', 4, 1, 15, 15),
(14, '2018-10-31 10:28:24', 4, 1, 15, 15),
(15, '2018-10-31 10:32:16', 4, 1, 15, 15),
(16, '2018-10-31 10:41:57', 4, 1, 15, 15),
(17, '2018-10-31 10:50:45', 4, 1, 15, 15),
(18, '2018-10-31 11:00:08', 4, 1, 15, 15),
(19, '2018-10-31 11:03:12', 4, 1, 15, 15),
(20, '2018-10-31 11:05:51', 4, 1, 15, 15),
(21, '2018-10-31 11:20:42', 4, 1, 15, 15),
(22, '2018-10-31 11:28:07', 4, 1, 15, 15),
(23, '2018-10-31 11:39:51', 4, 1, 15, 15),
(24, '2018-10-31 11:55:32', 4, 1, 15, 15),
(25, '2018-10-31 11:59:15', 4, 1, 15, 15),
(26, '2018-10-31 12:06:23', 4, 1, 15, 15),
(27, '2018-10-31 12:09:14', 4, 1, 15, 15),
(28, '2018-10-31 12:13:09', 4, 1, 15, 15),
(29, '2018-10-31 12:14:55', 4, 1, 15, 15),
(30, '2018-10-31 12:31:43', 4, 1, 15, 15),
(31, '2018-10-31 12:36:02', 4, 1, 15, 15);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Maceracion`
--

CREATE TABLE `Maceracion` (
  `id` int(11) NOT NULL,
  `nombre` varchar(190) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `Maceracion`
--

INSERT INTO `Maceracion` (`id`, `nombre`) VALUES
(3, ''),
(1, 'NEGROPUTO'),
(4, 'pepito');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `SensedValues`
--

CREATE TABLE `SensedValues` (
  `id` int(11) NOT NULL,
  `id_exp` int(11) DEFAULT NULL,
  `fechayhora` datetime DEFAULT CURRENT_TIMESTAMP,
  `temp1` float DEFAULT NULL,
  `temp2` float DEFAULT NULL,
  `temp3` float DEFAULT NULL,
  `temp4` float DEFAULT NULL,
  `temp5` float DEFAULT NULL,
  `tempPh` float DEFAULT NULL,
  `tempAmb` float DEFAULT NULL,
  `humity` float DEFAULT NULL,
  `pH` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `SensedValues`
--

INSERT INTO `SensedValues` (`id`, `id_exp`, `fechayhora`, `temp1`, `temp2`, `temp3`, `temp4`, `temp5`, `tempPh`, `tempAmb`, `humity`, `pH`) VALUES
(1, 2, '2018-08-15 18:54:05', 69, 69, 69, 69, 69, 69, 666, 100, 5.35),
(2, 2, '2018-08-15 18:59:26', 42, 42, 42, 42, 42, 42, 42, 80, 6),
(3, 2, '2018-08-15 19:00:51', 4, 4, 4, 4, 4, 4, 4, 70, 7),
(4, 1, '2018-08-16 11:43:06', -1000, -1000, -1000, -1000, -1000, -1000, -10000, -1, -2),
(5, 1, '2018-08-16 11:44:14', -1000, -1000, -1000, -1000, -1000, -1000, -10000, -1, -2),
(6, 2, '2018-08-16 16:52:13', -1000, -1000, -1000, -1000, -1000, -1000, -10000, -1, -2),
(7, 2, '2018-08-16 16:53:21', -1000, -1000, -1000, -1000, -1000, -1000, -10000, -1, -2),
(8, 31, '2018-10-31 12:36:04', 26.875, -1000, -1000, -1000, -1000, -1000, 27, 39, -2),
(9, 31, '2018-10-31 12:36:21', 26.875, -1000, -1000, -1000, -1000, -1000, 27, 40, -2),
(10, 31, '2018-10-31 12:36:40', 26.875, -1000, -1000, -1000, -1000, -1000, 27, 39, -2),
(11, 31, '2018-10-31 12:36:59', 26.875, -1000, -1000, -1000, -1000, -1000, 27, 39, -2);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `Experimento`
--
ALTER TABLE `Experimento`
  ADD PRIMARY KEY (`id`),
  ADD KEY `maceracion` (`maceracion`);

--
-- Indices de la tabla `Maceracion`
--
ALTER TABLE `Maceracion`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nombre` (`nombre`);

--
-- Indices de la tabla `SensedValues`
--
ALTER TABLE `SensedValues`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_exp` (`id_exp`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `Experimento`
--
ALTER TABLE `Experimento`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=32;
--
-- AUTO_INCREMENT de la tabla `Maceracion`
--
ALTER TABLE `Maceracion`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT de la tabla `SensedValues`
--
ALTER TABLE `SensedValues`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;
--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `Experimento`
--
ALTER TABLE `Experimento`
  ADD CONSTRAINT `Experimento_ibfk_1` FOREIGN KEY (`maceracion`) REFERENCES `Maceracion` (`id`);

--
-- Filtros para la tabla `SensedValues`
--
ALTER TABLE `SensedValues`
  ADD CONSTRAINT `SensedValues_ibfk_1` FOREIGN KEY (`id_exp`) REFERENCES `Experimento` (`id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
