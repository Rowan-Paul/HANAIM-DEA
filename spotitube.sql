-- phpMyAdmin SQL Dump
-- version 5.0.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Mar 17, 2021 at 11:31 AM
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
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `owner` varchar(50) NOT NULL,
  PRIMARY KEY (`id`,`name`,`owner`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8mb4;

--
-- Truncate table before insert `playlists`
--

TRUNCATE TABLE `playlists`;
--
-- Dumping data for table `playlists`
--

INSERT INTO `playlists` (`id`, `name`, `owner`) VALUES
(1, 'Folklore era', 'rowan');

-- --------------------------------------------------------

--
-- Table structure for table `playlisttracks`
--

DROP TABLE IF EXISTS `playlisttracks`;
CREATE TABLE IF NOT EXISTS `playlisttracks` (
  `playlistid` int(11) NOT NULL,
  `trackid` int(11) NOT NULL,
  PRIMARY KEY (`playlistid`,`trackid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Truncate table before insert `playlisttracks`
--

TRUNCATE TABLE `playlisttracks`;
--
-- Dumping data for table `playlisttracks`
--

INSERT INTO `playlisttracks` (`playlistid`, `trackid`) VALUES
(1, 1),
(1, 2);

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
  `publicationDate` varchar(10) NOT NULL,
  `description` varchar(500) NOT NULL,
  PRIMARY KEY (`id`,`title`,`performer`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4;

--
-- Truncate table before insert `tracks`
--

TRUNCATE TABLE `tracks`;
--
-- Dumping data for table `tracks`
--

INSERT INTO `tracks` (`id`, `title`, `performer`, `duration`, `album`, `playcount`, `publicationDate`, `description`) VALUES
(1, 'the 1', 'Taylor Swift', 222, 'folklore', 0, '07-24-2020', '\"The 1\" (stylized in all lowercase) is a song recorded by American singer-songwriter Taylor Swift, for her eighth studio album, Folklore (2020), which was released on July 24, 2020, through Republic Records. The song was promoted to German contemporary hit radio on October 9, 2020, as the album\'s fourth single. As the opening track of the album, the song was written by Swift and Aaron Dessner, with production from the latter.\r\n\r\n'),
(2, 'Don\'t start now', 'Dua Lipa', 183, 'Future Nostalgia', 0, '10-31-2019', '\"Don\'t Start Now\" is a song by English singer Dua Lipa from her second studio album, Future Nostalgia (2020). Lipa wrote the song with Caroline Ailin, Emily Warren, and its producer Ian Kirkpatrick. The song was released for digital download and streaming by Warner Records on 31 October 2019 as the lead single from the album. A nu-disco song, it features a funk bassline, inspired by music by the Bee Gees, Daft Punk and Two Door Cinema Club. Elements used in the production include handclaps, a cr'),
(3, 'cardigan', 'Taylor Swift', 239, 'folklore', 0, '07-24-2020', '\"Cardigan\" (stylized in all lowercase) is a song recorded by American singer-songwriter Taylor Swift and the second track on her eighth studio album, Folklore (2020), surprise-released on July 24, 2020 through Republic Records. It impacted radio stations on July 27, 2020 as the album\'s lead single. Swift co-wrote the song with its producer Aaron Dessner. \"Cardigan\" is a slow-burning folk, soft rock and indie rock ballad with stripped-down instrumentals of tender piano, clopping drums and melanch'),
(4, 'the last great american dynasty', 'Taylor Swift', 243, 'folklore', 0, '07-24-2020', '\"The Last Great American Dynasty\" (stylized in all lowercase) is a song recorded by American singer-songwriter Taylor Swift. It is the third track on her eighth studio album, Folklore, which was released on July 24, 2020 through Republic Records. It was penned by Swift, inspired by American philanthropist Rebekah Harkness, one of the wealthiest women in United States history. Aaron Dessner composed and produced the song.'),
(5, 'coney island', 'Taylor Swift, The National', 275, 'evermore', 0, '12-11-2020', '\"Coney Island\" (stylized in all lowercase) is a song recorded by American singer-songwriter Taylor Swift, featuring American rock band the National. It is the ninth track on Swift\'s ninth studio album, Evermore (2020), released on December 11, 2020, through Republic Records. The song impacted US alternative radio on January 18, 2021, as the album\'s third single.');

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
