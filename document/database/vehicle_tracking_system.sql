-- phpMyAdmin SQL Dump
-- version 4.6.5.2
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th3 29, 2017 lúc 06:18 SA
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
  `address` varchar(255) DEFAULT NULL,
  `driver_status_type_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Đang đổ dữ liệu cho bảng `drivers`
--

INSERT INTO `drivers` (`driver_id`, `user_name`, `password`, `email`, `full_name`, `phone`, `birthday`, `sex`, `address`, `driver_status_type_id`) VALUES
(1, 'demo1', '123456', 'demo1@gmail.com', 'demo1', '0123456789', '2017-03-15', 'male', 'LAM DONG', 2),
(2, 'demo2', '0142538', 'demo2@gmail.com', 'demo2', '0969696969', '1995-11-20', 'fema', 'BINH THUAN', 2),
(3, 'demo3', '0146734', 'demo3@gmail.com', 'demo3', '0121212121', '1990-10-20', 'male', 'GIA LAI', 2),
(4, 'demo4', '003847', 'demo4@gmail.com', 'demo4', '0969634465', '1993-01-22', 'male', 'TPHCM', 2),
(5, 'demo5', '0009876', 'demo5@gmail.com', 'demo5', '01345674334', '1991-05-10', 'male', 'HA NOI', 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `driver_status`
--

CREATE TABLE `driver_status` (
  `driver_status_type_id` int(11) NOT NULL,
  `description` varchar(50) COLLATE utf8mb4_vietnamese_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;

--
-- Đang đổ dữ liệu cho bảng `driver_status`
--

INSERT INTO `driver_status` (`driver_status_type_id`, `description`) VALUES
(1, 'DA NGHI VIEC'),
(2, 'DANG LAM VIEC');

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
  `intend_start_time` datetime DEFAULT NULL,
  `intend_end_time` datetime DEFAULT NULL,
  `location_lat_start` double DEFAULT NULL,
  `location_long_start` double DEFAULT NULL,
  `location_lat_end` double DEFAULT NULL,
  `location_long_end` double DEFAULT NULL,
  `real_start_time` datetime DEFAULT NULL,
  `real_end_time` datetime DEFAULT NULL,
  `schedule_status_type_id` int(11) DEFAULT NULL,
  `device_id` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT;

--
-- Đang đổ dữ liệu cho bảng `schedule`
--

INSERT INTO `schedule` (`schedule_id`, `driver_id`, `vehicle_id`, `intend_start_time`, `intend_end_time`, `location_lat_start`, `location_long_start`, `location_lat_end`, `location_long_end`, `real_start_time`, `real_end_time`, `schedule_status_type_id`, `device_id`) VALUES
(1, 1, 3, '2017-03-19 08:00:00', '2017-03-19 22:00:00', 9.234667, 102.466743, 12.244567, 108.355529, NULL, NULL, 1, '1234'),
(2, 2, 2, '2017-03-19 13:00:00', '2017-03-20 09:00:00', 10.234668, 101.466745, 14.244357, 107.456532, NULL, NULL, 1, '5678'),
(3, 3, 1, '2017-03-20 08:00:00', '2017-03-21 06:00:00', 15.663214, 101.345231, 20.325752, 105.681227, NULL, NULL, 2, '4321'),
(4, 4, 5, '2017-03-22 17:00:00', '2017-03-23 16:00:00', 15.663214, 101.345231, 19.325756, 104.681678, NULL, NULL, 1, '8765'),
(5, 5, 4, '2017-03-22 10:00:00', '2017-03-23 11:00:00', 19.646843, 106.345577, 22.3267622, 109.689927, NULL, NULL, 3, 'abcd');

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
  `update_time` datetime DEFAULT NULL,
  `device_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Đang đổ dữ liệu cho bảng `schedule_active`
--

INSERT INTO `schedule_active` (`schedule_id`, `location_lat`, `location_long`, `fuel`, `speed`, `update_time`, `device_id`) VALUES
(1, 12.454356, 107.983723, 120343, 80, '2017-03-19 09:32:54', '1234'),
(2, 15.674321, 101.665478, 200343, 82, '2017-03-20 14:02:25', '5678'),
(3, 17.674321, 102.544212, 245221, 66, '2017-03-20 22:45:23', '4321'),
(4, 15.674321, 101.665478, 450353, 71, '2017-03-12 19:20:43', '8765'),
(5, 15.454356, 107.983723, 653421, 81, '2017-03-22 08:12:23', 'abcd');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `schedule_status`
--

CREATE TABLE `schedule_status` (
  `schedule_status_type_id` int(11) NOT NULL,
  `description` varchar(50) COLLATE utf8mb4_vietnamese_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;

--
-- Đang đổ dữ liệu cho bảng `schedule_status`
--

INSERT INTO `schedule_status` (`schedule_status_type_id`, `description`) VALUES
(1, 'DANG CHAY'),
(2, 'DA HOAN THANH'),
(3, 'DA HUY');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `token`
--

CREATE TABLE `token` (
  `device_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `time` datetime DEFAULT NULL,
  `status` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `token`
--

INSERT INTO `token` (`device_id`, `time`, `status`) VALUES
('1234', '2017-03-26 08:00:00', NULL),
('4321', '2017-03-26 14:45:10', NULL),
('5678', '2017-03-26 09:05:23', NULL),
('8765', '2017-03-27 10:04:03', NULL),
('abcd', '2017-03-28 15:00:00', NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `vehicles`
--

CREATE TABLE `vehicles` (
  `vehicle_id` int(11) NOT NULL,
  `number_plate` varchar(20) NOT NULL,
  `vehicle_name` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `vehicle_status_type_id` int(11) DEFAULT NULL,
  `frame_number` varchar(50) DEFAULT NULL,
  `gross_ton` float DEFAULT NULL,
  `accreditation_date_start` date DEFAULT NULL,
  `accreditation_date_end` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Đang đổ dữ liệu cho bảng `vehicles`
--

INSERT INTO `vehicles` (`vehicle_id`, `number_plate`, `vehicle_name`, `type`, `vehicle_status_type_id`, `frame_number`, `gross_ton`, `accreditation_date_start`, `accreditation_date_end`) VALUES
(1, '51F 1245', 'Huyndai-91', 'Huyndai', 1, NULL, NULL, NULL, NULL),
(2, '86B2-0987', 'Huyndai-86', 'Huyndai', 2, NULL, NULL, NULL, NULL),
(3, '37F1-1234', 'Huyndai-02', 'Huyndai', 1, NULL, NULL, NULL, NULL),
(4, '49G3-6785', 'Huyndai-49', 'Huyndai', 2, NULL, NULL, NULL, NULL),
(5, '12B2-3454', 'Huyndai-12', 'Huyndai', 3, NULL, NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `vehicle_status`
--

CREATE TABLE `vehicle_status` (
  `vehicle_status_type_id` int(11) NOT NULL,
  `description` varchar(50) COLLATE utf8mb4_vietnamese_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;

--
-- Đang đổ dữ liệu cho bảng `vehicle_status`
--

INSERT INTO `vehicle_status` (`vehicle_status_type_id`, `description`) VALUES
(1, 'DANG HOAT DONG'),
(2, 'DANG RANH'),
(3, 'HONG XE');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `warning`
--

CREATE TABLE `warning` (
  `warning_id` int(11) NOT NULL,
  `driver_id` int(11) DEFAULT NULL,
  `location_lat` double DEFAULT NULL,
  `location_long` double DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `warning_type_id` int(11) DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Đang đổ dữ liệu cho bảng `warning`
--

INSERT INTO `warning` (`warning_id`, `driver_id`, `location_lat`, `location_long`, `description`, `warning_type_id`, `start_time`, `end_time`) VALUES
(1, 1, 13.123456, 108.245666, 'DUONG DANG THI CONG', NULL, '2017-03-19 11:22:16', '2017-03-19 11:30:20'),
(2, 3, 17.094653, 104.346578, 'NO BANH XE', NULL, '2017-03-19 14:30:00', '2017-03-19 14:43:34'),
(3, 2, 19.093451, 104.346578, NULL, NULL, '2017-03-20 09:20:33', '2017-03-20 10:00:00'),
(4, 5, 11.542356, 106.335563, 'CSGT, HA TOC DO', NULL, '2017-03-21 07:34:27', '2017-03-21 08:00:00');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `warning_type`
--

CREATE TABLE `warning_type` (
  `warning_type_id` int(11) NOT NULL,
  `type` varchar(25) COLLATE utf8mb4_vietnamese_ci NOT NULL,
  `description` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `defaut_time` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;

--
-- Đang đổ dữ liệu cho bảng `warning_type`
--

INSERT INTO `warning_type` (`warning_type_id`, `type`, `description`, `defaut_time`) VALUES
(1, 'KET XE', NULL, 1800),
(2, 'HONG DUONG', 'CONG TRUONG DANG THI CONG', 2000),
(3, 'CSGT', 'BAN TOC DO, CHAY CHAM LAI', 2000),
(4, 'HONG XE', 'XE HONG, NHO GIUP DO', 2000);

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `drivers`
--
ALTER TABLE `drivers`
  ADD PRIMARY KEY (`driver_id`),
  ADD KEY `driver_status_type_id` (`driver_status_type_id`);

--
-- Chỉ mục cho bảng `driver_status`
--
ALTER TABLE `driver_status`
  ADD PRIMARY KEY (`driver_status_type_id`);

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
  ADD KEY `vehicle_id` (`vehicle_id`),
  ADD KEY `schedule_ibfk_3` (`schedule_status_type_id`),
  ADD KEY `device_id` (`device_id`);

--
-- Chỉ mục cho bảng `schedule_active`
--
ALTER TABLE `schedule_active`
  ADD KEY `schedule_id` (`schedule_id`),
  ADD KEY `device_id` (`device_id`);

--
-- Chỉ mục cho bảng `schedule_status`
--
ALTER TABLE `schedule_status`
  ADD PRIMARY KEY (`schedule_status_type_id`);

--
-- Chỉ mục cho bảng `token`
--
ALTER TABLE `token`
  ADD PRIMARY KEY (`device_id`);

--
-- Chỉ mục cho bảng `vehicles`
--
ALTER TABLE `vehicles`
  ADD PRIMARY KEY (`vehicle_id`),
  ADD KEY `vehicle_status_type_id` (`vehicle_status_type_id`);

--
-- Chỉ mục cho bảng `vehicle_status`
--
ALTER TABLE `vehicle_status`
  ADD PRIMARY KEY (`vehicle_status_type_id`);

--
-- Chỉ mục cho bảng `warning`
--
ALTER TABLE `warning`
  ADD PRIMARY KEY (`warning_id`),
  ADD KEY `driver_id` (`driver_id`),
  ADD KEY `warning_type_id` (`warning_type_id`);

--
-- Chỉ mục cho bảng `warning_type`
--
ALTER TABLE `warning_type`
  ADD PRIMARY KEY (`warning_type_id`);

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
-- Các ràng buộc cho bảng `drivers`
--
ALTER TABLE `drivers`
  ADD CONSTRAINT `drivers_ibfk_1` FOREIGN KEY (`driver_status_type_id`) REFERENCES `driver_status` (`driver_status_type_id`);

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
  ADD CONSTRAINT `schedule_ibfk_2` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicles` (`vehicle_id`),
  ADD CONSTRAINT `schedule_ibfk_3` FOREIGN KEY (`schedule_status_type_id`) REFERENCES `schedule_status` (`schedule_status_type_id`),
  ADD CONSTRAINT `schedule_ibfk_4` FOREIGN KEY (`device_id`) REFERENCES `token` (`device_id`);

--
-- Các ràng buộc cho bảng `schedule_active`
--
ALTER TABLE `schedule_active`
  ADD CONSTRAINT `schedule_active_ibfk_1` FOREIGN KEY (`schedule_id`) REFERENCES `schedule` (`schedule_id`),
  ADD CONSTRAINT `schedule_active_ibfk_2` FOREIGN KEY (`device_id`) REFERENCES `token` (`device_id`);

--
-- Các ràng buộc cho bảng `vehicles`
--
ALTER TABLE `vehicles`
  ADD CONSTRAINT `vehicles_ibfk_1` FOREIGN KEY (`vehicle_status_type_id`) REFERENCES `vehicle_status` (`vehicle_status_type_id`);

--
-- Các ràng buộc cho bảng `warning`
--
ALTER TABLE `warning`
  ADD CONSTRAINT `warning_ibfk_1` FOREIGN KEY (`driver_id`) REFERENCES `drivers` (`driver_id`),
  ADD CONSTRAINT `warning_ibfk_2` FOREIGN KEY (`warning_type_id`) REFERENCES `warning_type` (`warning_type_id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
