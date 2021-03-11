-- phpMyAdmin SQL Dump
-- version 5.0.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Mar 11, 2021 at 11:15 AM
-- Server version: 10.4.17-MariaDB
-- PHP Version: 8.0.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `spotitube`
--
CREATE DATABASE IF NOT EXISTS `spotitube` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `spotitube`;

-- --------------------------------------------------------

--
-- Table structure for table `playlists`
--

DROP TABLE IF EXISTS `playlists`;
CREATE TABLE IF NOT EXISTS `playlists` (
  `id` int(6) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `owner` varchar(50) NOT NULL,
  `tracks` varchar(10000) NOT NULL,
  PRIMARY KEY (`id`,`name`,`owner`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Truncate table before insert `playlists`
--

TRUNCATE TABLE `playlists`;
-- --------------------------------------------------------

--
-- Table structure for table `tokens`
--

DROP TABLE IF EXISTS `tokens`;
CREATE TABLE IF NOT EXISTS `tokens` (
  `token` varchar(38) NOT NULL,
  `user` varchar(50) NOT NULL,
  PRIMARY KEY (`token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Truncate table before insert `tokens`
--

TRUNCATE TABLE `tokens`;
--
-- Dumping data for table `tokens`
--

INSERT INTO `tokens` (`token`, `user`) VALUES
('0cd3f990-c297-47a9-a4e2-49d64a500203', 'rowan');

-- --------------------------------------------------------

--
-- Table structure for table `tracks`
--

DROP TABLE IF EXISTS `tracks`;
CREATE TABLE IF NOT EXISTS `tracks` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(50) NOT NULL,
  `performer` varchar(50) NOT NULL,
  `duration` int(4) NOT NULL,
  `album` varchar(50) NOT NULL,
  `playcount` int(11) NOT NULL,
  `publicationDate` date NOT NULL,
  `description` varchar(500) NOT NULL,
  PRIMARY KEY (`id`,`title`,`performer`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

--
-- Truncate table before insert `tracks`
--

TRUNCATE TABLE `tracks`;
--
-- Dumping data for table `tracks`
--

INSERT INTO `tracks` (`id`, `title`, `performer`, `duration`, `album`, `playcount`, `publicationDate`, `description`) VALUES
(1, 'the 1', 'Taylor Swift', 222, 'Folklore', 0, '2020-07-24', '\"The 1\" (stylized in all lowercase) is a song recorded by American singer-songwriter Taylor Swift, for her eighth studio album, Folklore (2020), which was released on July 24, 2020, through Republic Records. The song was promoted to German contemporary hit radio on October 9, 2020, as the album\'s fourth single. As the opening track of the album, the song was written by Swift and Aaron Dessner, with production from the latter.\r\n\r\n');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `user` varchar(50) NOT NULL,
  `password` varchar(200) NOT NULL,
  PRIMARY KEY (`user`,`password`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Truncate table before insert `users`
--

TRUNCATE TABLE `users`;
--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user`, `password`) VALUES
('rowan', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
