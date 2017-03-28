-- phpMyAdmin SQL Dump
-- version 4.6.5.2
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th3 27, 2017 lúc 03:44 CH
-- Phiên bản máy phục vụ: 10.1.21-MariaDB
-- Phiên bản PHP: 7.1.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `vehicle_tracking_system`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `drivers`
--

CREATE TABLE `drivers` (
  `driver_id` int(11) NOT NULL,
  `user_name` varchar(255) NOT NULL,
  `password` varchar(20) NOT NULL,
  `email` varchar(25) DEFAULT NULL,
  `full_name` varchar(25) DEFAULT NULL,
  `phone` varchar(11) DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `sex` varchar(4) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Đang đổ dữ liệu cho bảng `drivers`
--

INSERT INTO `drivers` (`driver_id`, `user_name`, `password`, `email`, `full_name`, `phone`, `birthday`, `sex`, `address`) VALUES
(1, 'demo1', '123456', 'demo1@gmail.com', 'demo1', '0123456789', '2017-03-15', 'male', 'LAM DONG'),
(2, 'demo2', '0142538', 'demo2@gmail.com', 'demo2', '0969696969', '1995-11-20', 'fema', 'BINH THUAN'),
(3, 'demo3', '0146734', 'demo3@gmail.com', 'demo3', '0121212121', '1990-10-20', 'male', 'GIA LAI'),
(4, 'demo4', '003847', 'demo4@gmail.com', 'demo4', '0969634465', '1993-01-22', 'male', 'HCM'),
(5, 'demo5', '0009876', 'demo5@gmail.com', 'demo5', '01345674334', '1991-05-10', 'male', 'HA NOI');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `history`
--

CREATE TABLE `history` (
  `schedule_id` int(11) NOT NULL,
  `total_time` float DEFAULT NULL,
  `total_fuel` float DEFAULT NULL,
  `total_km` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Đang đổ dữ liệu cho bảng `history`
--

INSERT INTO `history` (`schedule_id`, `total_time`, `total_fuel`, `total_km`) VALUES
(1, 50400, 72987, 500),
(2, 72000, 99987, 900),
(3, 79200, 80987, 1000),
(4, 82800, 92987, 1098),
(5, 90000, 100987, 1100);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `image_journey`
--

CREATE TABLE `image_journey` (
  `schedule_id` int(11) NOT NULL,
  `link_image` varchar(255) DEFAULT NULL,
  `time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Đang đổ dữ liệu cho bảng `image_journey`
--

INSERT INTO `image_journey` (`schedule_id`, `link_image`, `time`) VALUES
(1, 'http://shopanninh.com/uploads/tin-tuc/2016_03/hinh-anh-quay-tu-camera-giam-sat-hanh-trinh.jpg', '2017-03-19 09:34:00'),
(1, 'https://sc02.alicdn.com/kf/HTB1jMtQFVXXXXXTXVXXq6xXFXXXu/200665441/HTB1jMtQFVXXXXXTXVXXq6xXFXXXu.jpg', '2017-03-19 14:34:56'),
(2, 'http://i.dailymail.co.uk/i/pix/2015/01/10/248DCC1300000578-0-image-a-18_1420869445762.jpg', '2017-03-19 17:22:06'),
(2, 'https://i.ytimg.com/vi/Tqa_i2uWa-k/maxresdefault.jpg', '2017-03-19 18:54:06'),
(3, 'http://i.dailymail.co.uk/i/pix/2015/01/10/248DCC1300000578-0-image-a-18_1420869445762.jpg', '2017-03-20 14:22:06');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `schedule`
--

CREATE TABLE `schedule` (
  `schedule_id` int(11) NOT NULL,
  `driver_id` int(11) NOT NULL,
  `vehicle_id` int(11) NOT NULL,
  `time_start` datetime DEFAULT NULL,
  `time_end` datetime DEFAULT NULL,
  `location_lat_start` double DEFAULT NULL,
  `location_long_start` double DEFAULT NULL,
  `location_lat_end` double DEFAULT NULL,
  `location_long_end` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Đang đổ dữ liệu cho bảng `schedule`
--

INSERT INTO `schedule` (`schedule_id`, `driver_id`, `vehicle_id`, `time_start`, `time_end`, `location_lat_start`, `location_long_start`, `location_lat_end`, `location_long_end`) VALUES
(1, 1, 3, '2017-03-19 08:00:00', '2017-03-19 22:00:00', 9.234667, 102.466743, 12.244567, 108.355529),
(2, 2, 2, '2017-03-19 13:00:00', '2017-03-20 09:00:00', 10.234668, 101.466745, 14.244357, 107.456532),
(3, 3, 1, '2017-03-20 08:00:00', '2017-03-21 06:00:00', 15.663214, 101.345231, 20.325752, 105.681227),
(4, 4, 5, '2017-03-22 17:00:00', '2017-03-23 16:00:00', 15.663214, 101.345231, 19.325756, 104.681678),
(5, 5, 4, '2017-03-22 10:00:00', '2017-03-23 11:00:00', 19.646843, 106.345577, 22.3267622, 109.689927);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `schedule_active`
--

CREATE TABLE `schedule_active` (
  `schedule_id` int(11) NOT NULL,
  `location_lat` double DEFAULT NULL,
  `location_long` double DEFAULT NULL,
  `fuel` float DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Đang đổ dữ liệu cho bảng `schedule_active`
--

INSERT INTO `schedule_active` (`schedule_id`, `location_lat`, `location_long`, `fuel`, `speed`, `time`) VALUES
(1, 11.546327, 106.342111, 14484, 80, '2017-03-19 08:24:43'),
(1, 11.674321, 106.665478, 130343, 85, '2017-03-19 08:45:23'),
(2, 12.554212, 101.465542, 300343, 76, '2017-03-19 15:12:54'),
(2, 13.523233, 102.445472, 270343, 77, '2017-03-19 16:01:32'),
(3, 15.763212, 101.675433, 245655, 60, '2017-03-20 12:14:45');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `token`
--

CREATE TABLE `token` (
  `token_id` varchar(255) NOT NULL,
  `driver_id` int(11) NOT NULL,
  `time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Đang đổ dữ liệu cho bảng `token`
--

INSERT INTO `token` (`token_id`, `driver_id`, `time`) VALUES
('1', 5, '2017-03-17 13:23:13'),
('2', 4, '2017-03-18 10:01:56'),
('3', 2, '2017-03-18 12:02:45'),
('4', 3, '2017-03-19 07:02:45'),
('5', 4, '2017-03-19 11:02:45');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `vehicles`
--

CREATE TABLE `vehicles` (
  `vehicle_id` int(11) NOT NULL,
  `number_plate` varchar(20) NOT NULL,
  `vehicle_name` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Đang đổ dữ liệu cho bảng `vehicles`
--

INSERT INTO `vehicles` (`vehicle_id`, `number_plate`, `vehicle_name`, `type`) VALUES
(1, '51F 1245', 'Huyndai-91', 'Huyndai'),
(2, '86B2-0987', 'Huyndai-86', 'Huyndai'),
(3, '37F1-1234', 'Huyndai-02', 'Huyndai'),
(4, '49G3-6785', 'Huyndai-49', 'Huyndai'),
(5, '12B2-3454', 'Huyndai-12', 'Huyndai');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `warning`
--

CREATE TABLE `warning` (
  `warning_id` int(11) NOT NULL,
  `driver_id` int(11) DEFAULT NULL,
  `location_lat` double DEFAULT NULL,
  `location_long` double DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Đang đổ dữ liệu cho bảng `warning`
--

INSERT INTO `warning` (`warning_id`, `driver_id`, `location_lat`, `location_long`, `type`, `description`) VALUES
(1, 1, 13.123456, 108.245666, 'HONG DUONG', 'DUONG DANG THI CONG'),
(2, 3, 17.094653, 104.346578, 'HONG XE', 'NO BANH XE'),
(3, 2, 19.093451, 104.346578, 'KET XE', NULL),
(4, 5, 11.542356, 106.335563, 'CSGT', 'CSGT, HA TOC DO');

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `drivers`
--
ALTER TABLE `drivers`
  ADD PRIMARY KEY (`driver_id`);

--
-- Chỉ mục cho bảng `history`
--
ALTER TABLE `history`
  ADD KEY `schedule_id` (`schedule_id`);

--
-- Chỉ mục cho bảng `image_journey`
--
ALTER TABLE `image_journey`
  ADD KEY `schedule_id` (`schedule_id`);

--
-- Chỉ mục cho bảng `schedule`
--
ALTER TABLE `schedule`
  ADD PRIMARY KEY (`schedule_id`),
  ADD KEY `driver_id` (`driver_id`),
  ADD KEY `vehicle_id` (`vehicle_id`);

--
-- Chỉ mục cho bảng `schedule_active`
--
ALTER TABLE `schedule_active`
  ADD KEY `schedule_id` (`schedule_id`);

--
-- Chỉ mục cho bảng `vehicles`
--
ALTER TABLE `vehicles`
  ADD PRIMARY KEY (`vehicle_id`);

--
-- Chỉ mục cho bảng `warning`
--
ALTER TABLE `warning`
  ADD PRIMARY KEY (`warning_id`),
  ADD KEY `driver_id` (`driver_id`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `drivers`
--
ALTER TABLE `drivers`
  MODIFY `driver_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT cho bảng `vehicles`
--
ALTER TABLE `vehicles`
  MODIFY `vehicle_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT cho bảng `warning`
--
ALTER TABLE `warning`
  MODIFY `warning_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `history`
--
ALTER TABLE `history`
  ADD CONSTRAINT `history_ibfk_1` FOREIGN KEY (`schedule_id`) REFERENCES `schedule` (`schedule_id`);

--
-- Các ràng buộc cho bảng `image_journey`
--
ALTER TABLE `image_journey`
  ADD CONSTRAINT `image_journey_ibfk_1` FOREIGN KEY (`schedule_id`) REFERENCES `schedule` (`schedule_id`);

--
-- Các ràng buộc cho bảng `schedule`
--
ALTER TABLE `schedule`
  ADD CONSTRAINT `schedule_ibfk_1` FOREIGN KEY (`driver_id`) REFERENCES `drivers` (`driver_id`),
  ADD CONSTRAINT `schedule_ibfk_2` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicles` (`vehicle_id`);

--
-- Các ràng buộc cho bảng `schedule_active`
--
ALTER TABLE `schedule_active`
  ADD CONSTRAINT `schedule_active_ibfk_1` FOREIGN KEY (`schedule_id`) REFERENCES `schedule` (`schedule_id`);

--
-- Các ràng buộc cho bảng `warning`
--
ALTER TABLE `warning`
  ADD CONSTRAINT `warning_ibfk_1` FOREIGN KEY (`driver_id`) REFERENCES `drivers` (`driver_id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
