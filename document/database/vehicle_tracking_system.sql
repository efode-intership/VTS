-- phpMyAdmin SQL Dump
-- version 4.6.5.2
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th4 03, 2017 lúc 09:41 SA
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


-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `roles`
--

CREATE TABLE `roles` (
  `role_id` int(11) NOT NULL,
  `name` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `roles`
--


-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `role_user`
--

CREATE TABLE `role_user` (
  `user_id` int(11) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  `time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `role_user`
--


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

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `user`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `phone` varchar(11) DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `sex` varchar(6) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `user_status_type_id` int(11) DEFAULT NULL,
  `remember_token` varchar(100) DEFAULT NULL,
  `created_time` timestamp NULL,
  `updated_time` timestamp NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Đang đổ dữ liệu cho bảng `user`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `user_status`
--

CREATE TABLE `user_status` (
  `user_status_type_id` int(11) NOT NULL,
  `description` varchar(50) COLLATE utf8mb4_vietnamese_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;

--
-- Đang đổ dữ liệu cho bảng `user_status`
--


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


--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `image_journey`
--
ALTER TABLE `image_journey`
  ADD KEY `schedule_id` (`schedule_id`);

--
-- Chỉ mục cho bảng `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`role_id`);

--
-- Chỉ mục cho bảng `role_user`
--
ALTER TABLE `role_user`
  ADD KEY `user_id` (`user_id`),
  ADD KEY `role_id` (`role_id`);

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
-- Chỉ mục cho bảng `user`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD KEY `driver_status_type_id` (`user_status_type_id`);

--
-- Chỉ mục cho bảng `user_status`
--
ALTER TABLE `user_status`
  ADD PRIMARY KEY (`user_status_type_id`);

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
-- AUTO_INCREMENT cho bảng `user`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
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
-- Các ràng buộc cho bảng `image_journey`
--
ALTER TABLE `image_journey`
  ADD CONSTRAINT `image_journey_ibfk_1` FOREIGN KEY (`schedule_id`) REFERENCES `schedule` (`schedule_id`);

--
-- Các ràng buộc cho bảng `role_user`
--
ALTER TABLE `role_user`
  ADD CONSTRAINT `role_user_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `role_user_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`);

--
-- Các ràng buộc cho bảng `schedule`
--
ALTER TABLE `schedule`
  ADD CONSTRAINT `schedule_ibfk_1` FOREIGN KEY (`driver_id`) REFERENCES `users` (`id`),
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
-- Các ràng buộc cho bảng `user`
--
ALTER TABLE `users`
  ADD CONSTRAINT `user_ibfk_1` FOREIGN KEY (`user_status_type_id`) REFERENCES `user_status` (`user_status_type_id`);

--
-- Các ràng buộc cho bảng `vehicles`
--
ALTER TABLE `vehicles`
  ADD CONSTRAINT `vehicles_ibfk_1` FOREIGN KEY (`vehicle_status_type_id`) REFERENCES `vehicle_status` (`vehicle_status_type_id`);

--
-- Các ràng buộc cho bảng `warning`
--
ALTER TABLE `warning`
  ADD CONSTRAINT `warning_ibfk_1` FOREIGN KEY (`driver_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `warning_ibfk_2` FOREIGN KEY (`warning_type_id`) REFERENCES `warning_type` (`warning_type_id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
